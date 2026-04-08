package me.jetby.treexCastle.gui;

import me.jetby.libb.gui.AdvancedGui;
import me.jetby.libb.gui.item.ItemWrapper;
import me.jetby.treexCastle.TreexCastle;
import me.jetby.treexCastle.configuration.ItemsConfiguration;
import me.jetby.treexCastle.shulker.ShulkerType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static me.jetby.treexCastle.TreexCastle.r;

public class InvGui extends AdvancedGui {


    public InvGui(TreexCastle plugin, Player player, ShulkerType shulker) {
        super(plugin.getFormattedMessage("gui.inv.title"), 54);

        lockEmptySlots(true);
        String id = shulker.id();

        Set<String> invs = new HashSet<>();
        List<ItemsConfiguration.ItemsData> itemsData = plugin.getItems().getData().get(id);
        if (itemsData != null) {
            for (ItemsConfiguration.ItemsData iData : itemsData) {
                invs.add(iData.inv());
            }
        }

        int slot = 0;
        for (String inv : invs) {
            if (slot + 1 > 53) break;
            int currentSlot = slot;
            setItem(inv + "_" + slot, ItemWrapper.builder(Material.CHEST)
                    .slots(currentSlot)
                    .displayName(plugin.getFormattedMessage("gui.inv.inv_button.display_name", "{inv}", inv))
                    .lore(plugin.getFormattedMessageList("gui.inv.inv_button.lore"))
                    .onClick(event -> {
                        event.setCancelled(true);

                        switch (event.getClick()) {
                            case LEFT -> {
                                new ItemGui(player, inv, id, plugin).open(player);
                            }
                            case RIGHT -> {
                                new ChanceGui(player, id, inv, plugin).open(player);
                            }
                        }
                    })
                    .build());
            slot++;
        }

        setItem("add_button", ItemWrapper.builder(Material.EMERALD)
                .slots(53)
                .displayName(plugin.getFormattedMessage("gui.inv.add_button.display_name"))
                .onClick(event -> {
                    event.setCancelled(true);
                    String newInvName = "inv_" + System.currentTimeMillis();
                    ItemStack dirt = new ItemStack(Material.DIRT);
                    plugin.getItems().saveItem(id, newInvName, dirt, 0, 100);

                    InvGui newGui = new InvGui(plugin, player, shulker);
                    newGui.open(player);
                })
                .build());

    }
}
