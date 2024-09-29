package eu.letsmine.bluemap;

import de.bluecolored.bluemap.common.config.storage.StorageConfig;
import de.bluecolored.bluemap.common.config.storage.StorageType;
import de.bluecolored.bluemap.core.util.Key;
import eu.letsmine.bluemap.storage.sftp.SftpConfig;

public class LetsMineBlueMapAddOn implements Runnable {

    @Override
    public void run() {
        StorageType.REGISTRY.register(new SftpStorageType());
    }

    class SftpStorageType implements StorageType {

        private final Key key = new Key("letsmine", "sftp");

        private final Class<? extends StorageConfig> configType = SftpConfig.class;

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
