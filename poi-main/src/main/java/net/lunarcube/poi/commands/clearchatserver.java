package net.lunarcube.poi.commands;

import net.lunarcube.poi.config;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class clearchatserver implements CommandExecutor
{
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException
    {
        // This will get every currently online player.
        for (Player onlineplayer : Sponge.getServer().getOnlinePlayers())
        {
            // Send enough amount of i to clear every players chat.
            for (int i = 1 ; i<151 ; i++)
            {
                onlineplayer.sendMessage(Text.of(""));
            }

            // Returns the message from the configuration.
            onlineplayer.sendMessage(Text.of(config.getInstance().getConfig().getNode("ClearChatServer").getString()));
        }

        return CommandResult.success();
    }
}
