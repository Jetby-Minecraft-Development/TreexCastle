package me.jetby.treexCastle.gui;

import me.jetby.libb.Keys;
import me.jetby.libb.gui.parser.ParseUtil;
import me.jetby.libb.gui.parser.ParsedGui;
import me.jetby.libb.util.Randomizer;
import me.jetby.treexCastle.shulker.ShulkerInstance;
import me.jetby.treexCastle.shulker.ShulkerManager;
import me.jetby.treexCastle.shulker.ShulkerType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class LootGui extends ParsedGui {

    public LootGui(@NotNull Player viewer, @NotNull FileConfiguration config, JavaPlugin plugin, ShulkerInstance instance) {
        super(viewer, config, plugin);

        lockEmptySlots(false);

        List<Integer> lootSlots = ParseUtil.parseSlots(config.getStringList("loot-slots"));

        if (instance.getSharedLootInventory() != null) {
            viewer.openInventory(instance.getSharedLootInventory());
            return;
        }

        Map<Integer, ItemStack> slotToOriginal = new HashMap<>();
        ShulkerType shulker = instance.getType();

        for (ItemStack item : instance.getLoot()) {
            if (lootSlots.isEmpty()) break;
            int slot = Randomizer.rand(lootSlots);
            lootSlots.remove(Integer.valueOf(slot));

            if (shulker.isMask() && !shulker.maskMap().isEmpty()) {
                slotToOriginal.put(slot, item);
                getInventory().setItem(slot, ShulkerManager.applyMask(shulker, item));
            } else {
                getInventory().setItem(slot, item);
            }
        }


        instance.setSharedLootInventory(getInventory());

        Consumer<InventoryClickEvent> onClick = onClick();
        onClick(event -> {
            if (onClick != null) onClick.accept(event);
            event.setCancelled(false);

            int clickedSlot = event.getSlot();
            if (slotToOriginal.containsKey(clickedSlot)) {
                ItemStack original = slotToOriginal.get(clickedSlot).clone();
                ItemMeta meta = original.getItemMeta();
                if (meta != null) {
                    meta.getPersistentDataContainer().remove(Keys.GUI_ITEM);
                    original.setItemMeta(meta);
                }
                event.getInventory().setItem(clickedSlot, original);
                slotToOriginal.remove(clickedSlot);
            }
        });

        onClose(event -> {
            boolean empty = true;
            for (ItemStack i : instance.getSharedLootInventory().getContents()) {
                if (i != null) {
                    empty = false;
                    break;
                }
            }
            if (empty) instance.setSharedLootInventory(null);
        });

        open(viewer);
    }

}

