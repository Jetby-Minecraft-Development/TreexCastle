package me.jetby.treexCastle.gui;

import me.jetby.libb.gui.AdvancedGui;
import me.jetby.libb.gui.item.ItemWrapper;
import me.jetby.treexCastle.TreexCastle;
import me.jetby.treexCastle.shulker.ShulkerType;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

import java.util.List;

import static me.jetby.treexCastle.TreexCastle.r;

public class MainGui extends AdvancedGui {
    public static final NamespacedKey CHANCE = new NamespacedKey("treexcastle", "chance");

    public MainGui(TreexCastle plugin) {

        super("TreexCastle", 54);

        lockEmptySlots(true);

        int slot = 0;
        for (String type : plugin.getTypes().getShulkers().keySet()) {
            ShulkerType shulker = plugin.getTypes().getShulkers().get(type);
            int finalSlot = slot;

            setItem(type, ItemWrapper.builder(shulker.material())
                    .displayName(r("<!i><#FB430A><bold>⭐</bold> <white>Шалкер: <yellow>" + type))
                    .lore(List.of(
                                    r("<!i><#FB430A><st>=                                   ="),
                                    r(""),
                                    r("<!i><#FB430A><b>▶</b> <white>Нажмите, чтобы настроить предметы"),
                                    r(""),
                                    r("<!i><#FB430A><st>=                                   =")
                            )
                    )
                    .slots(finalSlot)
                    .onClick(event -> {
                        event.setCancelled(true);

                        if (event.getWhoClicked() instanceof Player) {
                            new InvGui(plugin, player, shulker).open(player);
                        }
                    })
                    .build());
            slot++;
        }
    }
}
