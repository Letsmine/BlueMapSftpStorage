package eu.letsmine.bluemap.storage.ssh;

import java.nio.file.Path;
import java.util.Optional;

public interface ISshConfiguration {

    public String getRoot();
    public String getHost();
    public int getPort();
    public String getUser();
    public Optional<String> getPassword();
    public Optional<Path> getPrivateKeyPath();
    public Optional<Path> getKnownHosts();
    
}
