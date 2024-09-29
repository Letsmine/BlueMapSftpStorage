package eu.letsmine.bluemap.storage.sftp;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import de.bluecolored.bluemap.common.config.ConfigurationException;
import de.bluecolored.bluemap.common.config.storage.StorageConfig;
import de.bluecolored.bluemap.common.debug.DebugDump;
import de.bluecolored.bluemap.core.storage.Storage;
import de.bluecolored.bluemap.core.storage.compression.Compression;
import de.bluecolored.bluemap.core.util.Key;

import java.util.Map;
import java.util.HashMap;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@ConfigSerializable
public class SftpConfig extends StorageConfig implements ISftpConfiguration {

    private String host = "sftp://user@localhost:22";
    @DebugDump(exclude = true)
    private String password = "";
    @DebugDump(exclude = true)
    private Path knownHosts = Path.of("~/.ssh/known_hosts");
    private String compression = Compression.GZIP.getKey().getFormatted();
    private Map<String, String> config = new HashMap<String, String>();

    public Compression getCompression() throws ConfigurationException {
        return Compression.REGISTRY.get(Key.bluemap(compression));
    }

    @Override
    public Storage createStorage() throws ConfigurationException {
        return new SftpStorage(this, getCompression());
    }

    @Override
    public String getHost() {
        if (host == null || host.isEmpty()) return null;
        return host;
    }

    @Override
    public Optional<String> getPassword() {
        return Optional.ofNullable(password);
    }

    @Override
    public Optional<Path> getKnownHosts() {
        if (Files.exists(knownHosts))
            return Optional.of(knownHosts);
        return Optional.empty();
    }

    @Override
    public Map<String, String> getConfig() {
        return config;
    }

}
