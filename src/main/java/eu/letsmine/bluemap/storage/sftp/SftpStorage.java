package eu.letsmine.bluemap.storage.sftp;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.github.robtimus.filesystems.sftp.SFTPEnvironment;
import com.github.robtimus.filesystems.sftp.SFTPFileSystemProvider;

import de.bluecolored.bluemap.core.storage.Storage;
import de.bluecolored.bluemap.core.storage.compression.Compression;
import de.bluecolored.bluemap.core.storage.file.FileMapStorage;

public class SftpStorage implements Storage {

    private FileSystem sshFileSystem = null;

    private final ISftpConfiguration config;
    private final Compression compression;
    private final LoadingCache<String, FileMapStorage> mapStorages;

    public SftpStorage(ISftpConfiguration config, Compression compression) {
        this.config = config;
        this.compression = compression;
        mapStorages = Caffeine.newBuilder().build(this::create);
    }

    private Path getRooPath() {
        return sshFileSystem.getPath(".").toAbsolutePath().normalize();
    }

    public FileMapStorage create(String mapId) {
        Path mapPath = getRooPath().resolve(mapId);
        return new FileMapStorage(mapPath, compression, false);
    }

    @Override
    public void initialize() throws IOException {
        SFTPEnvironment env = new SFTPEnvironment();
        config.getConfig().forEach(env::withConfig);
        if (config.getKnownHosts().isPresent()) {
            env.withKnownHosts(config.getKnownHosts().get().toFile());
        }
        if (config.getPassword().isPresent()) {
            env.withPassword(config.getPassword().get().toCharArray());
        }
        SFTPEnvironment.setDefault(env);

        try {
            SFTPFileSystemProvider provider = new SFTPFileSystemProvider();
            URI uri = URI.create(config.getHost());
            Path path = provider.getPath(uri);
            sshFileSystem = path.getFileSystem();
        } catch (Exception e) {
            throw new IOException("Failed to initialize SSH storage", e);
        }
    }

    @Override
    public FileMapStorage map(String mapId) {
        return mapStorages.get(mapId);
    }

    @Override
    public Stream<String> mapIds() throws IOException {
        return Files.list(getRooPath())
        .filter(Files::isDirectory)
        .map(Path::getFileName)
        .map(Path::toString);
    }

    @Override
    public boolean isClosed() {
        return sshFileSystem == null || !sshFileSystem.isOpen();
    }

    @Override
    public void close() throws IOException {
        if (sshFileSystem != null) {
            sshFileSystem.close();
            sshFileSystem = null;
        }
    }
}
