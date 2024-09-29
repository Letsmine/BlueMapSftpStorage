package eu.letsmine.bluemap;

import de.bluecolored.bluemap.common.config.storage.StorageConfig;
import de.bluecolored.bluemap.common.config.storage.StorageType;
import de.bluecolored.bluemap.core.util.Key;
import eu.letsmine.bluemap.storage.ssh.SshConfig;

public class LetsMineBlueMapAddOn implements Runnable {

    @Override
    public void run() {
        StorageType.REGISTRY.register(new SshStorageType());
    }

    class SshStorageType implements StorageType {

        // Copy ssh.conf to storage folder


        private final Key key = Key.parse("letsmine", "ssh");

        private final Class<? extends StorageConfig> configType = SshConfig.class;

        @Override
        public Key getKey() {
            return key;
        }

        @Override
        public Class<? extends StorageConfig> getConfigType() {
            return configType;
        }
    }
}
