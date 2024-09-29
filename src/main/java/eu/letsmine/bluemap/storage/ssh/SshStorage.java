package eu.letsmine.bluemap.storage.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.pastdev.jsch.DefaultSessionFactory;
import de.bluecolored.bluemap.core.storage.Storage;
import de.bluecolored.bluemap.core.storage.compression.Compression;
import de.bluecolored.bluemap.core.storage.file.FileMapStorage;

public class SshStorage implements Storage {

    private FileSystem sshFileSystem = null;

    private final ISshConfiguration config;
    private final Compression compression;
    private final LoadingCache<String, FileMapStorage> mapStorages;

    public SshStorage(ISshConfiguration config, Compression compression) {
        this.config = config;
        this.compression = compression;
        mapStorages = Caffeine.newBuilder().build(this::create);
    }

    private Path getRooPath() {
        return sshFileSystem.provider().getPath(URI.create(config.getRoot()));
    }

    public FileMapStorage create(String mapId) {
        var mapPath = getRooPath() .resolve(mapId);
        return new FileMapStorage(mapPath, compression, true);
    }

    @Override
    public void initialize() throws IOException {
        DefaultSessionFactory defaultSessionFactory = new DefaultSessionFactory(config.getUser(), config.getHost(), config.getPort());
        try {
            if (config.getKnownHosts().isPresent()) {
                InputStream is = Files.newInputStream(config.getKnownHosts().get());
                defaultSessionFactory.setKnownHosts(is);
            } else {
                // TODO Default known hosts?
            }

            if (config.getPrivateKeyPath().isPresent()) {
                // Private key is set in the configuration
                if (config.getPassword().isPresent()) {
                    // Password is set in the configuration
                    defaultSessionFactory.setIdentityFromPrivateKey(config.getPrivateKeyPath().get().toString(), config.getPassword().get());
                } else {
                    // Password is not set in the configuration
                    defaultSessionFactory.setIdentityFromPrivateKey(config.getPrivateKeyPath().get().toString());
                }
            } else if (config.getPassword().isPresent()) {
                defaultSessionFactory.setPassword(config.getPassword().get());
            } else {
                // No Private Key, no Password, this could be a unsecured connection. Do Nothing.
            }
            Map<String, Object> environment = new HashMap<String, Object>();
            environment.put( "defaultSessionFactory", defaultSessionFactory );
            URI uri = new URI( "ssh.unix", config.getRoot() , null );
            sshFileSystem = FileSystems.newFileSystem( uri, environment );
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
        return !sshFileSystem.isOpen();
    }

    @Override
    public void close() throws IOException {
        sshFileSystem.close();
    }
}
