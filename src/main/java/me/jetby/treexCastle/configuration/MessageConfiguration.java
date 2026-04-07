package me.jetby.treexCastle.configuration;

import me.jetby.libb.Libb;
import me.jetby.treexCastle.TreexCastle;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.FileConfiguration;

public class MessageConfiguration {

    private final TreexCastle plugin;
    private final FileConfiguration messages;

    public MessageConfiguration(TreexCastle plugin) {
        this.plugin = plugin;
        this.messages = plugin.getFileConfiguration("messages.yml");
    }

    public Component getFormattedMessage(String path) {
        return TreexCastle.r(messages.getString(path));
    }

}
