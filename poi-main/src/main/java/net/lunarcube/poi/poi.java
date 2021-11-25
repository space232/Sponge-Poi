package net.lunarcube.poi;

import com.google.inject.Inject;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.boss.BossBarColors;
import org.spongepowered.api.boss.BossBarOverlays;
import org.spongepowered.api.boss.ServerBossBar;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.entity.MoveEntityEvent;
import org.spongepowered.api.event.filter.Getter;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.io.File;
import java.net.URL;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import net.lunarcube.poi.commands.*;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;

@Plugin(
        id = "poisponge",
        name = "Poi",
        version = "1.0",
        description = "A plugin created by space for the LunarCube Network. Read the readme.md for more information.",
        authors = "space",
        url = "https://spce.moe"
)
public class poi {

    public static poi instance;

    @Inject
    private Logger logger;

    @Inject
    private Game game;

    // Poi Announcements Schedule
    private Task task;
    private int index = 0;

    @Inject
    @DefaultConfig(sharedRoot = true)
    private File configFile;

    @Inject
    @DefaultConfig(sharedRoot = true)
    ConfigurationLoader<CommentedConfigurationNode> configManager;

    // Server has started, time to get everything running now!
    @Listener
    public void onServerStart(GameStartedServerEvent event)
    {
        // Configuration
        config.getInstance().setup(configFile, configManager);

        // CommandSpec is used to assign our commands permissions and allow external executions.
        // This allows me to basically utilise the /commands folder.
        CommandSpec reload = CommandSpec.builder()
                .description(Text.of("Reload the configuration file"))
                .permission("poi.reload")
                .executor(new reload())
                .build();

        CommandSpec clearchatclient = CommandSpec.builder()
                .description(Text.of("Clear your text chat."))
                .permission("poi.clearchat.client")
                .executor(new clearchatclient())
                .build();

        CommandSpec clearchatserver = CommandSpec.builder()
                .description(Text.of("Clear everyone's text chat."))
                .permission("poi.clearchat.server")
                .executor(new clearchatserver())
                .build();

        CommandSpec discord = CommandSpec.builder()
                .description(Text.of("Displays a link to a Discord Server in chat."))
                .permission("poi.discord")
                .executor(new discord())
                .build();

        CommandSpec support = CommandSpec.builder()
                .description(Text.of("Displays a support link."))
                .permission("poi.support")
                .executor(new support())
                .build();

        CommandSpec forums = CommandSpec.builder()
                .description(Text.of("Displays a forums link."))
                .permission("poi.forums")
                .executor(new forums())
                .build();

        CommandSpec store = CommandSpec.builder()
                .description(Text.of("Displays a store link."))
                .permission("poi.store")
                .executor(new store())
                .build();

        CommandSpec cmd = CommandSpec.builder()
                .description(Text.of("Poi Commands"))
                .child(reload, "reload")
                .build();

        // Registers our prior commands to the server.
        Sponge.getCommandManager().register(this, cmd, "poi");

        Sponge.getCommandManager().register(this, discord, "discord");

        Sponge.getCommandManager().register(this, clearchatclient, "clearchat");
        Sponge.getCommandManager().register(this, clearchatserver, "clearchats");

        Sponge.getCommandManager().register(this, support, "support", "help");
        Sponge.getCommandManager().register(this, store, "buy", "store", "rank", "ranks");
        Sponge.getCommandManager().register(this, forums, "forums", "forum", "website");

        // Register Announcements
        initAnnouncements();
    }

    // Should the server need to be reloaded, include Poi to reload alongside it.
    public void onReload(GameReloadEvent event)
    {
        // Reload Configuration
        config.getInstance().loadConfig();
        // Reload Announcements
        initAnnouncements();
    }

    /*
    AntiBorder
        The All-In-One Solution to Degenerates trying to crash the server!
            'Basically prevents any losers from glitching out of the worldborders.'
    */
    @Listener
    public void BlockBypass(MoveEntityEvent event, @Getter("getTargetEntity") Player player) 
    {
        if (check(player)) 
        {
            player.sendMessage(Text.builder().append(toText(config.getInstance().getConfig().getNode("ABMessage").getString())).build());
            if (config.getInstance().getConfig().getNode("ABTeleportSpawn").getBoolean()) 
            {
                event.setToTransform(event.getFromTransform().setLocation(player.getWorld().getSpawnLocation()));
                Sponge.getScheduler().createTaskBuilder().delayTicks(2).execute(() -> event.setCancelled(true))
                        .submit(instance);
            }
        }
    }

    // Check required for AntiBorder
    private boolean check(Player player) {
        int playerX = (int) player.getLocation().getPosition().getX();
        int playerZ = (int) player.getLocation().getPosition().getZ();
        int centerX = (int) player.getWorld().getWorldBorder().getCenter().getX();
        int centerZ = (int) player.getWorld().getWorldBorder().getCenter().getZ();
        int diameter = (int) player.getWorld().getWorldBorder().getDiameter() / 2;
        double x = playerX - centerX;
        double z = playerZ - centerZ;
        return ((x > diameter || (-x) > diameter) || (z > diameter || (-z) > diameter));
    }

    /*
    BossBar
        This could also be used for other things, if I was not so lazy.
            'Assigns every player that joins a very irritating bar the top of their screen for important(ish) messages'
    */
    @Listener
    public void onPlayerJoin(final ClientConnectionEvent.Join event)
    {
        // Might need changing if Sponge does something like idk, assign 'player' to a fuckin citizens plugin or something.
        Player player = event.getTargetEntity();

        // Displays BossBar/Title Bar to the top of their screens, how definitely-not-so-annoying!
        ServerBossBar bossBar = ServerBossBar.builder()
                .name(Text.of(TextColors.WHITE, config.getInstance().getConfig().getNode("BossBarText").getString()))
                .percent(1f)
                .color(BossBarColors.PURPLE)
                .overlay(BossBarOverlays.PROGRESS)
                .build();
        bossBar.addPlayer(player);
    }

    // toText method, it just works, ok?
    public static Text toText(String str)
    {
        return TextSerializers.FORMATTING_CODE.deserialize(str);
    }

    /*
    Announcement Messages
        As if Boss/Title Bars were not annoying enough!
            'Displays configurable messages in the funny chat window to annoy your players.'
    */
    private void initAnnouncements(){
        logger.info("[POI] Reloading announcements");
        if (task != null)
        {
            task.cancel();
            logger.info("Announcements stopped");
        }

        // Pull from configuration
        int interval = config.getInstance().getConfig().getNode("configs","interval").getInt(60);
        int total = config.getInstance().getConfig().getNode("messages").getChildrenMap().keySet().size();
        String prefix = config.getInstance().getConfig().getNode("configs","prefix").getString();
        boolean rand = config.getInstance().getConfig().getNode("configs","random").getBoolean();

        task = game.getScheduler().createTaskBuilder().interval(interval, TimeUnit.SECONDS).execute(t -> {
            String indstr = String.valueOf(index);
            if (rand && total > 0)
            {
                indstr = String.valueOf(new Random().nextInt(total));
            }

            if (config.getInstance().getConfig().getNode("messages",indstr).hasMapChildren())
            {
                String msg = config.getInstance().getConfig().getNode("messages",indstr,"a-message").getString();
                int players = config.getInstance().getConfig().getNode("messages",indstr,"b-players-online").getInt();
                String perm = config.getInstance().getConfig().getNode("messages",indstr,"c-permission").getString();
                String hover = config.getInstance().getConfig().getNode("messages",indstr,"d-on-hover").getString();
                String cmd = config.getInstance().getConfig().getNode("messages",indstr,"f-click-cmd").getString();
                String url = config.getInstance().getConfig().getNode("messages",indstr,"g-click-url").getString();
                String scmd = config.getInstance().getConfig().getNode("messages",indstr,"e-suggest-cmd").getString();

                if (players != 0 && game.getServer().getOnlinePlayers().size() < players)
                {
                    return;
                }

                for (Player p:game.getServer().getOnlinePlayers())
                {
                    if (!perm.isEmpty() && !p.hasPermission(perm))
                    {
                        continue;
                    }

                    Text.Builder send = Text.builder();
                    msg = msg.replace("{player}", p.getName());
                    send.append(toText(prefix+msg));

                    if (!hover.isEmpty())
                    {
                        hover = hover.replace("{player}", p.getName());
                        send.onHover(TextActions.showText(toText(hover)));
                    }

                    if (!cmd.isEmpty())
                    {
                        cmd = cmd.replace("{player}", p.getName());
                        if (!cmd.startsWith("/")){
                            cmd = "/"+cmd;
                        }
                        send.onClick(TextActions.runCommand(cmd));
                    }

                    if (!url.isEmpty())
                    {
                        url = url.replace("{player}", p.getName());
                        try {
                            send.onClick(TextActions.openUrl(new URL(url)));
                        } catch (Exception ignored) {}
                    }

                    if (!scmd.isEmpty())
                    {
                        scmd = scmd.replace("{player}", p.getName());
                        if (!scmd.startsWith("/")){
                            scmd = "/"+scmd;
                        }

                        send.onClick(TextActions.suggestCommand(scmd));
                    }

                    p.sendMessage(send.build());
                }
            }
            if (index+1 >= total)
            {
                index = 0;
            } else {
                index++;
            }
        }).submit(this);

        logger.info("-> Task started");
    }

}
