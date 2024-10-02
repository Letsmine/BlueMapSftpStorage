package eu.letsmine.bluemap.storage.sftp;

import java.util.Map;
import java.nio.file.Path;
import java.util.Optional;

public interface ISftpConfiguration {

    public String getHost();

    public Map<String, String> getConfig();
    public Optional<String> getPassword();
    public Optional<Path> getKnownHosts();

    public int getPoolSize();
    public int getPoolInit();
    public int getPoolIdle();
    public int getPoolWait();
    
}
