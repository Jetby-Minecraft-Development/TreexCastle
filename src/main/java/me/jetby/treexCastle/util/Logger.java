package me.jetby.treexCastle.util;

import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;

@UtilityClass
public class Logger {

    private Component c(String message) {
        return LegacyComponentSerializer.legacySection().deserialize(message);
    }

    public void warn(String message) {
        Bukkit.getConsoleSender().sendMessage(c("§e[TreexCastle] " + message));
    }

    public void info(String message) {
        Bukkit.getConsoleSender().sendMessage(c("§a[TreexCastle] §f" + message));
    }

    public void success(String message) {
        Bukkit.getConsoleSender().sendMessage(c("§a[TreexCastle] §a" + message));
    }

    public void error(String message) {
        Bukkit.getConsoleSender().sendMessage(c("§c[TreexCastle] " + message));
    }

    public void msg(String message) {
        Bukkit.getConsoleSender().sendMessage(c("§6[TreexCastle] §f" + message.replace("&", "§")));
    }
}