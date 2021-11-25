package net.lunarcube.poi;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

import java.io.File;
import java.io.IOException;

public class config
{
    private static config instance = new config();

    public static config getInstance()
    {
        return instance;
    }

    private ConfigurationLoader<CommentedConfigurationNode> configLoader;
    private CommentedConfigurationNode config;

    void setup (File configFile, ConfigurationLoader<CommentedConfigurationNode> configLoader)
    {
        this.configLoader = configLoader;

        // Begin configuration
        if(!configFile.exists())
        {
            try
            {
                configFile.createNewFile();
                loadConfig();

                // BossBar-Related
                config.getNode("BossBarText").setComment("The text to be displayed in the Boss/Title Bar").setValue("Welcome to the server!");

                // ClearChat-Related
                config.getNode("ClearChatServer").setComment("The message displayed when an administrator clears the chat.").setValue("Your chat was cleared by an Administrator.");
                config.getNode("ClearChatClient").setComment("The message displayed when a player clears their chat locally.").setValue("Your chat was cleared.");

                // Discord-Related
                config.getNode("DiscordMessage").setComment("The message displayed before the URL.").setValue("&6&lJoin us on Discord");
                config.getNode("DiscordLink").setComment("The link displayed to your Discord server.").setValue("https://spce.moe/discord");
                config.getNode("DiscordPrefix").setComment("The prefix for the Discord function.").setValue("&7> ");
                config.getNode("DiscordHover").setComment("The message displayed when you hover over the text chat embed.").setValue("&7Click to join us on Discord!");
                config.getNode("DiscordHeading").setComment("The heading... duh.").setValue("&l&bLunarCube");
                config.getNode("DiscordPadding").setComment("The padding... duh.").setValue("&7&m-");

                // AntiBorder-related
                config.getNode("ABMessage").setComment("The message displayed when a player tries to leave the worldborder.").setValue("Please do not try exploit out the border!");
                config.getNode("ABTeleportSpawn").setComment("Whether to teleport the offending player to spawn or not.").setValue(true);

                // Announcements-Related
                config.getNode("configs","prefix").setValue(config.getNode("configs","prefix").getString("&7[&aPoi&7]&r "));
                config.getNode("configs","interval").setValue(config.getNode("configs","interval").getInt(60));
                config.getNode("configs","random").setValue(config.getNode("configs","random").getBoolean(false));

                config.getNode("messages").setComment("Set your announcement messages here, Follow the example and add numbers for more messages. Also supports {player}, etc");
                if (!config.getNode("messages").hasMapChildren()){
                    config.getNode("messages","0","a-message").setComment("Message to send to the chat (supports colours too!)");
                    config.getNode("messages","0","a-message").setValue("&aThis is Poi at work! &6More info @ spce.moe/discord");

                    config.getNode("messages","0","b-players-online").setComment("Required players online to send the announcement message (set to 0 to disable)");
                    config.getNode("messages","0","b-players-online").setValue(0);

                    config.getNode("messages","0","c-permission").setComment("Permissions required for players to receive this specific message.");
                    config.getNode("messages","0","c-permission").setValue("");

                    config.getNode("messages","0","d-on-hover").setComment("What the player will see when hovered over chat embed.");
                    config.getNode("messages","0","d-on-hover").setValue("&7Looking closely, are we?");

                    config.getNode("messages","0","f-click-cmd").setComment("Run a command when the announced chat embed is clicked.");
                    config.getNode("messages","0","f-click-cmd").setValue("say {player} has just clicked on an announcement by Poi!");

                    config.getNode("messages","0","g-click-url").setComment("Open a url when the announced chat embed is clicked.");
                    config.getNode("messages","0","g-click-url").setValue("http://google.com");

                    config.getNode("messages","0","e-suggest-cmd").setComment("Suggests a command when the announced chat embed is clicked.");
                    config.getNode("messages","0","e-suggest-cmd").setValue("msg {player} Poi is OP!");
                }

                // End configuration

                saveConfig();

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            loadConfig();
        }
    }

    public CommentedConfigurationNode getConfig()
    {
        return config;
    }

    public void saveConfig()
    {
        try {
            configLoader.save(config);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadConfig()
    {
        try {
            config = configLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
