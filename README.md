# VaultReloaded

[![Build Status](https://app.travis-ci.com/itsgabbooo/VaultReloaded.svg?branch=master)](https://app.travis-ci.com/itsgabbooo/VaultReloaded)

**VaultReloaded** is a fork of [Vault](https://github.com/MilkBowl/Vault) by **ItsGabbooo**.

Repository: [github.com/itsgabbooo/VaultReloaded](https://github.com/itsgabbooo/VaultReloaded)

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

## Placeholders (PlaceholderAPI)

When [PlaceholderAPI](https://github.com/PlaceholderAPI/PlaceholderAPI) is installed, VaultReloaded registers its own placeholder expansion automatically — no extra expansion download needed. It provides the same placeholders as the classic Vault expansion:

- `%vaultreloaded_...%` — the new identifier (**recommended**)
- `%vault_...%` — registered for backwards compatibility, so existing chat plugins and scoreboards keep working (skipped if another vault expansion is already active)

### Economy

| Placeholder | Description |
| --- | --- |
| `%vaultreloaded_eco_balance%` | Player's balance (with economy's decimal points, min. 2) |
| `%vaultreloaded_eco_balance_fixed%` | Balance without decimals |
| `%vaultreloaded_eco_balance_commas%` | Balance with thousands separators |
| `%vaultreloaded_eco_balance_formatted%` | Short formatted balance (`1200` → `1.2K`) |
| `%vaultreloaded_eco_balance_<n>dp%` | Balance with exactly `n` decimal points |

### Permissions & Chat

| Placeholder | Description |
| --- | --- |
| `%vaultreloaded_rank%` / `%vaultreloaded_group%` | Player's primary group |
| `%vaultreloaded_rank_capital%` / `%vaultreloaded_group_capital%` | Primary group, capitalized |
| `%vaultreloaded_ranks%` / `%vaultreloaded_groups%` | Comma-separated list of the player's groups |
| `%vaultreloaded_prefix%` / `%vaultreloaded_suffix%` | Player's prefix / suffix |
| `%vaultreloaded_rankprefix%` / `%vaultreloaded_ranksuffix%` | Prefix / suffix of the primary group |
| `%vaultreloaded_rankprefix_<n>%` / `%vaultreloaded_ranksuffix_<n>%` | Prefix / suffix of the n-th group |
| `%vaultreloaded_hasgroup_<group>%` | Whether the player is in the given group |
| `%vaultreloaded_inprimarygroup_<group>%` | Whether the given group is the player's primary group |

All of the above work with `vaultreloaded_` replaced by `vault_` as well.

## Building

VaultReloaded uses Maven:

```bash
mvn clean package
```

The resulting jar will be in `target/VaultReloaded-1.1.0.jar`.

See [CHANGELOG.md](CHANGELOG.md) for release notes.

## License

VaultReloaded is free software: you can redistribute it and/or modify
it under the terms of the GNU Lesser General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.
