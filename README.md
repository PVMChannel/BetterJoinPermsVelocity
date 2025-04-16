# BetterJoinPermsVelocity
A plugin to make permissions for joining different servers.

Feel free to open issues for any issues or suggestions.

## Installation
Just put the `.jar` file into the plugins folder, then restart your server and set up the config.yml, this is the default one and everything is explained in there:
```yaml
# WARNING!
# the plugin restricts any connections when the config is invalid!
# (with the reason "Configuration error")
# (and also when the config isn't loaded yet)

# if the plugin should restrict connection to every server
# servers that do not have a permission set up will require the permission "betterjoinpermsvelocity.*"
restrictAllServers: false

# list of servers to exclude from the restrictAllServers
# if restrictAllServers is false, this is useless
excludeServers:
  - lobby
  - survival

# list of permissions for each server
#
# this example makes it so that if you want to conenct to the server "adminserver"
# you will need the permission "betterjoinpermsvelocity.admin"
servers:
  adminserver: admin

# list of messages for each permission
# if no message is configured, no message will be displayed
# MiniMessage formatting, https://webui.advntr.dev/
messages:
  admin: "<red>You have to be an admin to join this server!</red>"
# you can set up a message for restrictAllServers like this:
#  "*": "<red>You have to be an admin to join this server!</red>"
```

## Contributing
This is my first open-source project so don't expect much, but I will be updating this to the newest version of velocity frequently. Feel free to contribute but I will probably miss your pull request because I don't check here often.
