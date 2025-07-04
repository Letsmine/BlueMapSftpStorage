# BlueMap SFTP Storage AddOn

This addon provides SFTP storage support for BlueMap.

## Usage Instructions

1. **Download the AddOn**
   - Get the latest version of the addon from the [releases page](../../releases/latest).

2. **Install the AddOn**
   - Place the downloaded `BlueMapSshStorage-{version}.jar` file into the `packs` folder located next to your BlueMap configuration files, it should look like `BlueMap/packs/letsmine-bluemap-0.3.jar`.

3. **Configure SFTP**
   - Copy the [sftp.conf](./src/main/resources/sftp.conf) file to the `storage` folder located next to your BlueMap configuration files.
   - Fill in the necessary SFTP configuration details in the `sftp.conf` file.

4. **Update BlueMap Map Configuration**
   - Modify the storage setting in your map configuration files (`maps/*.conf`) to use the SFTP storage:
     ```json
     "storage": "sftp"
     ```

5. **Reload BlueMap**
   - Apply the changes by reloading BlueMap.

## Generate `known_hosts`
With the `ssh-keyscan` command you cann create/append your `known_hosts` file.
- `ssh-keyscan -H example.com >> known_hosts`
- `ssh-keyscan -H -p 23 u001.your-storagebox.de >> known_hosts`
You can now use that file in your `sftp.conf`

## Multiple Connections

1. Copy the [sftp.conf](./src/main/resources/sftp.conf) file to the `storage` folder, Name the file however you like, for example: `second-sftp.conf`
2. Modify the storage setting in your map configuration files (`maps/*.conf`) to use the new SFTP storage:
     ```json
     "storage": "second-sftp"
     ```
   The name of the `*.conf` File is used for the storage setting in the `maps/*.conf`.
