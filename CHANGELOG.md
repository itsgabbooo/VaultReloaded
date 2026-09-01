# Changelog

All notable changes to this project are documented in this file.

## [Unreleased]

## [1.1.0] - 2026-09-01

### Added
- Built-in **PlaceholderAPI expansion**: the classic Vault placeholders are now provided natively, without needing to install the Vault expansion from the PlaceholderAPI eCloud
  - Registered under both identifiers: `%vaultreloaded_...%` (new) and `%vault_...%` (backwards compatibility, skipped when another vault expansion is already active)
  - Economy: `%vaultreloaded_eco_balance%`, `..._balance_fixed%`, `..._balance_commas%`, `..._balance_formatted%`, `..._balance_<n>dp%`
  - Permissions & Chat: `..._rank%`, `..._group%`, `..._ranks%`, `..._groups%`, `..._prefix%`, `..._suffix%`, `..._rankprefix%`, `..._ranksuffix%`, `..._rankprefix_<n>%`, `..._hasgroup_<group>%`, `..._inprimarygroup_<group>%`

### Fixed
- **Update checker**: it was querying the original Vault project on CurseForge and wrongly reported `Stable Version: 1.7.3 is out! You are still running version: 1.0.0` on every start — it now checks the GitHub Releases of `itsgabbooo/VaultReloaded` and only reports real VaultReloaded versions
- **Placeholder parsing**: when no permission/chat provider is registered (e.g. only the built-in SuperPerms backup), some placeholders (`rank`, `prefix`, ...) could throw `UnsupportedOperationException` and abort the whole placeholder resolution — requests are now exception-safe and return an empty value instead of crashing

### Tasks & Async
- The update check runs as a **scheduled async task** (every 30 minutes, after server start) — it never blocks the main thread or server startup
- bStats metrics are submitted **in the background** by the bundled single-file `Metrics` class, using its own scheduled executor
- The PlaceholderAPI expansion **re-resolves the registered providers on every request** on the calling thread, so economy/chat/permission plugins loaded after VaultReloaded are picked up without a server restart

### Dependencies
- Added `PlaceholderAPI` as a compile-time provided dependency (`plugin.yml` soft-depend) — the plugin still loads and works on servers without PlaceholderAPI

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
