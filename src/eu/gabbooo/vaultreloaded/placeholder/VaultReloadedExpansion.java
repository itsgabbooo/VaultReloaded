/* This file is part of VaultReloaded.

    VaultReloaded is free software: you can redistribute it and/or modify
    it under the terms of the GNU Lesser General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    VaultReloaded is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Lesser General Public License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with VaultReloaded.  If not, see <http://www.gnu.org/licenses/>.
 */
package eu.gabbooo.vaultreloaded.placeholder;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.Optional;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import eu.gabbooo.vaultreloaded.chat.Chat;
import eu.gabbooo.vaultreloaded.economy.Economy;
import eu.gabbooo.vaultreloaded.permission.Permission;
import me.clip.placeholderapi.PlaceholderAPIPlugin;
import me.clip.placeholderapi.expansion.Cacheable;
import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * Built-in PlaceholderAPI expansion for VaultReloaded.
 * <p>
 * Mirrors the official Vault expansion (PlaceholderAPI/Vault-Expansion) so that
 * <code>%vaultreloaded_...%</code> behaves exactly like the well known
 * <code>%vault_...%</code> placeholders. An instance with identifier
 * <code>vault</code> is also registered for backwards compatibility, unless
 * another <code>vault</code> expansion is already active.
 */
public class VaultReloadedExpansion extends PlaceholderExpansion implements Cacheable, Configurable {

    private static final Pattern BALANCE_DECIMAL_POINTS_PATTERN = Pattern.compile("balance_(\\d+)dp");
    private static final DecimalFormat COMMAS_FORMAT = new DecimalFormat("#,###");
    private static final DecimalFormat FIXED_FORMAT = new DecimalFormat("#");
    private static final Map<Integer, DecimalFormat> DECIMAL_FORMATS_CACHE = new HashMap<Integer, DecimalFormat>();

    private final String identifier;
    private final NavigableMap<Long, String> suffixes = new TreeMap<Long, String>();

    private Economy economy;
    private Permission permission;
    private Chat chat;

    public VaultReloadedExpansion(String identifier) {
        this.identifier = identifier;
        suffixes.put(1000L, getString("formatting.thousands", "K"));
        suffixes.put(1000000L, getString("formatting.millions", "M"));
        suffixes.put(1000000000L, getString("formatting.billions", "B"));
        suffixes.put(1000000000000L, getString("formatting.trillions", "T"));
        suffixes.put(1000000000000000L, getString("formatting.quadrillions", "Q"));
    }

    @Override
    public String getIdentifier() {
        return identifier;
    }

    @Override
    public String getAuthor() {
        return "ItsGabbooo";
    }

    @Override
    public String getVersion() {
        return "1.1.0";
    }

    @Override
    public String getRequiredPlugin() {
        return null;
    }

    @Override
    public void clear() {
        economy = null;
        permission = null;
        chat = null;
    }

    @Override
    public Map<String, Object> getDefaults() {
        Map<String, Object> defaults = new LinkedHashMap<String, Object>();
        defaults.put("formatting.thousands", "K");
        defaults.put("formatting.millions", "M");
        defaults.put("formatting.billions", "B");
        defaults.put("formatting.trillions", "T");
        defaults.put("formatting.quadrillions", "Q");
        return defaults;
    }

    @Override
    public boolean canRegister() {
        // Always register: providers may (soft)depend on third party plugins that
        // load after us, so availability is re-checked on every request instead.
        return true;
    }

    private void refresh() {
        economy = getService(Economy.class);
        permission = getService(Permission.class);
        chat = getService(Chat.class);
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (player == null) {
            return "";
        }

        try {
            return onRequestInternal(player, params);
        } catch (Throwable t) {
            // A placeholder must never break placeholder parsing: some providers
            // (e.g. the SuperPerms backup) throw when no permission plugin is active.
            return "";
        }
    }

    private String onRequestInternal(OfflinePlayer player, String params) {
        refresh();

        if (economy != null && params.startsWith("eco_")) {
            return onEconomyRequest(player, params.substring("eco_".length()));
        }

        return permission != null ? onPermissionRequest(player, params) : null;
    }

    // ------------------------------------------------ Economy

    private double getBalance(OfflinePlayer player) {
        return economy.getBalance(player);
    }

    private String onEconomyRequest(OfflinePlayer player, String params) {
        final double balance = getBalance(player);

        if (params.startsWith("balance_")) {
            final Matcher matcher = BALANCE_DECIMAL_POINTS_PATTERN.matcher(params);
            if (matcher.find()) {
                try {
                    int points = Integer.parseInt(matcher.group(1));
                    return setDecimalPoints(balance, points);
                } catch (NumberFormatException e) {
                    return matcher.group(1) + " is not a valid number";
                }
            }
        }

        if ("balance".equals(params)) {
            return setDecimalPoints(balance, Math.max(2, economy.fractionalDigits()));
        }
        if ("balance_fixed".equals(params)) {
            return FIXED_FORMAT.format(balance);
        }
        if ("balance_formatted".equals(params)) {
            return formatBalance((long) balance);
        }
        if ("balance_commas".equals(params)) {
            return COMMAS_FORMAT.format(balance);
        }
        return null;
    }

    private String setDecimalPoints(double balance, int points) {
        DecimalFormat cachedFormat = DECIMAL_FORMATS_CACHE.get(points);
        if (cachedFormat != null) {
            return cachedFormat.format(balance);
        }
        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getIntegerInstance();
        decimalFormat.setMaximumFractionDigits(points);
        decimalFormat.setGroupingUsed(false);
        DECIMAL_FORMATS_CACHE.put(points, decimalFormat);
        return decimalFormat.format(balance);
    }

    /**
     * Format a balance, e.g. 1200 becomes 1.2K.
     */
    private String formatBalance(long balance) {
        // Long.MIN_VALUE == -Long.MIN_VALUE, so we need an adjustment here
        if (balance == Long.MIN_VALUE) {
            return formatBalance(Long.MIN_VALUE + 1);
        }
        if (balance < 0) {
            return "-" + formatBalance(-balance);
        }
        if (balance < 1000) {
            return Long.toString(balance);
        }

        final Map.Entry<Long, String> e = suffixes.floorEntry(balance);
        final Long divideBy = e.getKey();
        final String suffix = e.getValue();

        long truncated = balance / (divideBy / 10); // the number part of the output times 10
        boolean hasDecimal = truncated < 100 && (truncated / 10d) != (truncated / 10);
        return hasDecimal ? (truncated / 10d) + suffix : (truncated / 10) + suffix;
    }

    // ------------------------------------------------ Permission & Chat

    private String onPermissionRequest(OfflinePlayer player, String params) {
        if ((params.startsWith("rankprefix_") || params.startsWith("groupprefix_"))
                || (params.startsWith("ranksuffix_") || params.startsWith("groupsuffix_"))) {
            final String[] parts = params.split("_", 2);
            final Integer index = parseInt(parts[1]);
            if (index == null || index < 1) {
                return "Invalid number " + parts[1];
            }
            return getGroupMeta(player, index, parts[0].contains("prefix"));
        }

        if (params.startsWith("hasgroup_")) {
            final String group = params.substring("hasgroup_".length());
            return bool(permission.playerInGroup(null, player, group));
        }

        if (params.startsWith("inprimarygroup_")) {
            final String group = params.substring("inprimarygroup_".length());
            final String primary = permission.getPrimaryGroup(null, player);
            return bool(primary != null && primary.equals(group));
        }

        switch (params) {
            case "group":
            case "rank":
                return primaryGroup(player).orElse("");
            case "group_capital":
            case "rank_capital":
                return primaryGroup(player).map(this::capitalize).orElse("");
            case "groups":
            case "ranks":
                return String.join(", ", getPlayerGroups(player));
            case "groups_capital":
            case "ranks_capital":
                return Arrays.stream(getPlayerGroups(player))
                        .map(this::capitalize)
                        .collect(Collectors.joining(", "));
            case "prefix":
                return getPlayerMeta(player, true).orElse("");
            case "suffix":
                return getPlayerMeta(player, false).orElse("");
            case "groupprefix":
            case "rankprefix":
                return primaryGroup(player)
                        .map(group -> getGroupMeta(group, true).orElse(""))
                        .orElse("");
            case "groupsuffix":
            case "ranksuffix":
                return primaryGroup(player)
                        .map(group -> getGroupMeta(group, false).orElse(""))
                        .orElse("");
            default:
                return null;
        }
    }

    private Optional<String> primaryGroup(OfflinePlayer player) {
        return Optional.ofNullable(permission.getPrimaryGroup(null, player));
    }

    private String[] getPlayerGroups(OfflinePlayer player) {
        final String[] groups = permission.getPlayerGroups(null, player);
        return groups == null ? new String[0] : groups;
    }

    private Optional<String> getGroupMeta(String group, boolean isPrefix) {
        if (group == null || group.isEmpty()) {
            return Optional.empty();
        }
        final String meta = isPrefix ? chat.getGroupPrefix((String) null, group) : chat.getGroupSuffix((String) null, group);
        return Optional.ofNullable(meta);
    }

    private Optional<String> getPlayerMeta(OfflinePlayer player, boolean isPrefix) {
        if (chat == null) {
            return Optional.empty();
        }
        final String meta = isPrefix ? chat.getPlayerPrefix(null, player) : chat.getPlayerSuffix(null, player);
        return Optional.ofNullable(meta);
    }

    /**
     * Returns the meta (prefix/suffix) of the <code>startIndex</code>-th group of the player
     * (1-based), falling back to the following groups when no meta is set.
     */
    private String getGroupMeta(OfflinePlayer player, int startIndex, boolean isPrefix) {
        final String[] groups = getPlayerGroups(player);
        if (startIndex > groups.length) {
            return "";
        }
        for (int i = startIndex - 1; i < groups.length; i++) {
            final Optional<String> meta = getGroupMeta(groups[i], isPrefix);
            if (meta.isPresent()) {
                return meta.get();
            }
        }
        return "";
    }

    private String capitalize(String string) {
        if (string == null || string.isEmpty()) {
            return string == null ? "" : string;
        }
        return Character.toUpperCase(string.charAt(0)) + string.substring(1).toLowerCase();
    }

    private String bool(boolean bool) {
        return bool ? PlaceholderAPIPlugin.booleanTrue() : PlaceholderAPIPlugin.booleanFalse();
    }

    private Integer parseInt(String value) {
        try {
            return Integer.valueOf(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private <T> T getService(Class<T> cls) {
        RegisteredServiceProvider<T> rsp = Bukkit.getServer().getServicesManager().getRegistration(cls);
        return rsp != null ? rsp.getProvider() : null;
    }
}