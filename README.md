# VaultReloaded

**VaultReloaded** is a fork of [Vault](https://github.com/MilkBowl/Vault) by **ItsGabbooo**.

It is a Chat, Permissions & Economy API for Bukkit/Spigot/Paper servers that allows plugins to hook into these systems through a single, unified interface — without each plugin needing to support every permissions, chat or economy plugin individually.

## Supported Minecraft Versions

**Minecraft 1.8 – 1.26.2**

VaultReloaded is built with maximum compatibility in mind:

- `api-version: 1.13` in `plugin.yml` — ignored by servers older than 1.13, honored by 1.13+ servers
- Compiled as Java 8 bytecode — runs on older servers (Java 8) and on modern ones (Java 17+)
- No reflection into server internals — pure Bukkit API

## Installing

Installing VaultReloaded is as simple as copying the provided `VaultReloaded.jar` to your `<server>/plugins` directory and restarting the server. Everything else happens automatically — supported permission, chat and economy plugins are detected and hooked at startup.

## Commands

| Command | Description | Permission |
| --- | --- | --- |
| `/vaultreloaded-info` | Displays information about VaultReloaded (registered economy, permission and chat services) | `vaultreloaded.admin` |
| `/vaultreloaded-convert [economy1] [economy2]` | Converts all data from one economy into another | `vaultreloaded.admin` |

## Permissions

| Permission | Description | Default |
| --- | --- | --- |
| `vaultreloaded.admin` | Allows using the admin commands and receiving update notices | op |

## Building

VaultReloaded uses Maven:

```bash
mvn clean package
```

The resulting jar will be in `target/VaultReloaded-1.0.0.jar`.

See [CHANGELOG.md](CHANGELOG.md) for release notes.

## License

VaultReloaded is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.
