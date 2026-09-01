# Changelog

All notable changes to this project are documented in this file.

## [Unreleased]

### Changed
- Update checker now queries the GitHub Releases of `itsgabbooo/VaultReloaded` instead of the original Vault CurseForge project — it no longer reports Vault's versions as updates for VaultReloaded

## [1.0.0] - 2026-09-01

Initial release of **VaultReloaded**, a fork of [Vault](https://github.com/MilkBowl/Vault) by **ItsGabbooo**.

### Added
- Full rebrand: plugin renamed from Vault to **VaultReloaded**
- New main class: `eu.gabbooo.vaultreloaded.VaultReloaded` (previously `net.milkbowl.vault.Vault`)
- All classes and packages moved from `net.milkbowl.vault.*` to `eu.gabbooo.vaultreloaded.*`
- API classes (Chat, Economy, Permission, EconomyResponse, AbstractEconomy) bundled directly into the project — no external VaultAPI dependency
- bStats relocated under `eu.gabbooo.vaultreloaded.metrics`

### Changed
- Renamed commands: `/vaultreloaded-info`, `/vaultreloaded-convert` (previously `/vault-info`, `/vault-convert`)
- Renamed permissions: `vaultreloaded.admin` (previously `vault.admin`)
- Updated update-check permission: `vaultreloaded.update` (previously `vault.update`)
- New English README

### Compatibility
- Supports **Minecraft 1.8 – 1.26.2**
- `api-version: 1.13` — ignored by servers older than 1.13, honored by 1.13+ servers
- Compiled as Java 8 bytecode — runs on older servers (Java 8) and modern ones (Java 17+)
- Pure Bukkit API, no reflection into server internals

### Breaking changes
- The API is no longer exposed under `net.milkbowl.vault.*`. Plugins looking up the old Vault API package will not detect VaultReloaded and must be recompiled against `eu.gabbooo.vaultreloaded.*`
- Old command names and permission nodes no longer work — update any scripts, groups or permissions files accordingly
