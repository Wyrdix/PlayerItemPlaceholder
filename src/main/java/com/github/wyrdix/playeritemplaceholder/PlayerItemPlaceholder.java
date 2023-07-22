package com.github.wyrdix.playeritemplaceholder;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class PlayerItemPlaceholder extends JavaPlugin {

    private ProtocolManager protocolManager;

    /**
     * Used to update the given item stack
     * Beware that no copy are made, so the given stack is modified
     *
     * @param player The placeholder with which {@link me.clip.placeholderapi.PlaceholderAPI#setPlaceholders(Player, String)} is going to be called
     * @param stack  The item on which lore and custom name are going to be updated
     */
    public static void updateItemView(Player player, ItemStack stack) {
        assert player != null;

        if (stack == null || stack.getType().isAir()) return;

        ItemMeta meta = stack.getItemMeta();
        if (meta == null) return;

        if (meta.hasLore()) {
            List<String> lore = meta.getLore();
            meta.setLore(Objects.requireNonNull(lore).stream()
                    .map(s -> PlaceholderAPI.setPlaceholders(player, s))
                    .collect(Collectors.toList()));
        }

        if (meta.hasDisplayName()) {
            String displayName = meta.getDisplayName();
            meta.setDisplayName(
                    PlaceholderAPI.setPlaceholders(player, displayName)
            );
        }
        stack.setItemMeta(meta);
    }

    public void onLoad() {
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable() {
        protocolManager.addPacketListener(new PacketAdapter(this));
    }
}
