package me.jetby.treexCastle.shulker;

import me.jetby.libb.action.ActionContext;
import me.jetby.libb.action.ActionExecute;
import me.jetby.libb.action.record.ActionBlock;
import me.jetby.treexCastle.TreexCastle;
import me.jetby.treexCastle.configuration.ItemsConfiguration;
import me.jetby.treexCastle.configuration.TypesConfiguration;
import me.jetby.treexCastle.model.FlyingDropParticle;
import me.jetby.treexCastle.model.Holo;
import me.jetby.treexCastle.model.Mask;
import me.jetby.treexCastle.util.HoloUtil;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public record ShulkerType(String id,
                          Material material,
                          int durability,
                          TypesConfiguration.LootDelivery lootDelivery,
                          String deliveryGui,
                          String lootAmount,
                          int removeAfter,
                          boolean isMask,
                          int takeCooldown,
                          Map<String, Mask> maskMap,
                          FlyingDropParticle dropParticle,
                          int spawnChance,
                          Holo holo,
                          Holo holoRemove,
                          List<ItemsConfiguration.ItemsData> items,
                          ActionBlock onSpawn,
                          ActionBlock onBreak,
                          ActionBlock onDespawn
) {
    public void spawn(Location location) {
        ShulkerInstance instance = ShulkerInstance.of(this, location);

        TreexCastle.INSTANCE.getLocations().acquire(location);

        if (holo.enable()) {
            createHolo(holo, instance, location);
        }

        location.getBlock().setType(material);
        if (onSpawn != null)
            ActionExecute.run(ActionContext.of(null, TreexCastle.INSTANCE).with(instance), onSpawn);
    }

    public void remove(TreexCastle plugin, ShulkerInstance instance) {
        try {
            instance.getLocation().getBlock().setType(Material.AIR);
            if (holo.enable())
                HoloUtil.remove(instance.getId().toString());
            plugin.getLocations().reset(instance.getLocation());
            ShulkerInstance.SHULKER_INSTANCE_LIST.remove(instance.getId());
        } catch (Exception ignored) {
        }
    }

    public void createHolo(Holo holo, ShulkerInstance instance, Location location) {
        if (!holo.enable()) return;
        HoloUtil.create(buildHoloLines(holo, instance), holoLocation(holo, location), instance.getId().toString());
    }
    public void updateHologram(Holo holo, ShulkerInstance instance) {
        if (!holo.enable()) {
            HoloUtil.remove(instance.getId().toString());
            return;
        }
        HoloUtil.update(buildHoloLines(holo, instance), holoLocation(holo, instance.getLocation()), instance.getId().toString());
    }

    private List<String> buildHoloLines(Holo holo, ShulkerInstance instance) {
        List<String> lines = new ArrayList<>(holo.lines());
        lines.replaceAll(s -> s.replace("{blocks_left}", String.valueOf(instance.getDurability())));
        lines.replaceAll(s -> s.replace("{time_left}", String.valueOf(instance.getRemoveAfter())));
        return lines;
    }

    public Location holoLocation(Holo holo, Location base) {
        return base.clone().add(holo.holoX(), holo.holoY(), holo.holoZ());
    }
}