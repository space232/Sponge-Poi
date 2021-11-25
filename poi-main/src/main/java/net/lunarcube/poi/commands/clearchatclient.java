package net.lunarcube.poi.commands;

import net.lunarcube.poi.config;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public class clearchatclient implements CommandExecutor
{
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException
    {
        if(source instanceof Player)
        {
            Player player = (Player)source;

            int i = 151;
            for(int x = 0; x<i; x++)
            {
                player.sendMessage(Text.of(""));
            }

            // Returns the message from the configuration.
            player.sendMessage(Text.of(config.getInstance().getConfig().getNode("ClearChatClient").getString()));
        }

        return CommandResult.success();
    }
}
