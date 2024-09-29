# LetsMine AddOn for BlueMap
A native addon for bluemap, adding SFTP-Storage support.

## Usage
- download the [BlueMapSshStorage-{version}.jar](./releases).
- place the jar file into the `addons` folder next to your bluemap config files
- Copy [sftp.conf](./scr/main/resources/sftp.conf) to `storage/sftp.conf` and fill the sftp files.
- change the `storage` in your map-config (`maps/*.conf`) to `"letsmine:sftp"`
- reload bluemap
