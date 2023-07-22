package com.github.wyrdix.playeritemplaceholder;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;

import static com.github.wyrdix.playeritemplaceholder.PlayerItemPlaceholder.updateItemView;

public class PacketAdapter extends com.comphenix.protocol.events.PacketAdapter {

    /**
     * Used to define which packet should be listened to in order to change the item viewed by a player
     */
    private static final PacketType[] TYPES = new PacketType[]{PacketType.Play.Server.WINDOW_ITEMS, PacketType.Play.Server.SET_SLOT};

    public PacketAdapter(Plugin plugin) {
        super(plugin, ListenerPriority.LOWEST, TYPES);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        StructureModifier<ItemStack> itemModifier = event.getPacket().getItemModifier();
        if (itemModifier != null) {
            itemModifier.getValues().forEach(stack -> updateItemView(event.getPlayer(), stack));
        }

        StructureModifier<ItemStack[]> itemArrayModifier = event.getPacket().getItemArrayModifier();
        if (itemArrayModifier != null) {
            itemArrayModifier.getValues().forEach(array -> {
                for (ItemStack stack : array) {
                    updateItemView(event.getPlayer(), stack);
                }
            });
        }

        StructureModifier<List<ItemStack>> itemListModifier = event.getPacket().getItemListModifier();
        if (itemListModifier != null) {
            itemListModifier.getValues().forEach(list -> list.forEach(stack -> updateItemView(event.getPlayer(), stack)));
        }
    }
}
