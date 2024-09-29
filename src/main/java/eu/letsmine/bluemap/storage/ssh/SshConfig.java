package eu.letsmine.bluemap.storage.ssh;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import de.bluecolored.bluemap.common.config.ConfigurationException;
import de.bluecolored.bluemap.common.config.storage.StorageConfig;
import de.bluecolored.bluemap.common.debug.DebugDump;
import de.bluecolored.bluemap.core.storage.Storage;
import de.bluecolored.bluemap.core.storage.compression.Compression;
import de.bluecolored.bluemap.core.util.Key;

@ConfigSerializable
public class SshConfig extends StorageConfig implements ISshConfiguration {

    private String root = ".";
    private String host = "localhost";
    private int port = 22;
    private String user = "root";
    @DebugDump(exclude = true)
    private String password = "";
    @DebugDump(exclude = true)
    private Path privateKeyPath = Path.of("~/.ssh/id_rsa");
    @DebugDump(exclude = true)
    private Path knownHosts = Path.of("~/.ssh/known_hosts");
    private String compression = Compression.GZIP.getKey().getFormatted();

    public Compression getCompression() throws ConfigurationException {
        return Compression.REGISTRY.get(Key.bluemap(compression));
    }

    @Override
    public Storage createStorage() throws ConfigurationException {
        return new SshStorage(this, getCompression());
    }

    @Override
    public String getRoot() {
        return root;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public Optional<String> getPassword() {
        return Optional.ofNullable(password);
    }

    @Override
    public Optional<Path> getPrivateKeyPath() {
        if (Files.exists(privateKeyPath))
            return Optional.of(privateKeyPath);
        return Optional.empty();
    }

    @Override
    public Optional<Path> getKnownHosts() {
        if (Files.exists(knownHosts))
            return Optional.of(knownHosts);
        return Optional.empty();
    }

}
