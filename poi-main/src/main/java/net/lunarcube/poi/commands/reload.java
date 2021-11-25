package net.lunarcube.poi.commands;

import net.lunarcube.poi.config;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

import static net.lunarcube.poi.poi.toText;

public class reload implements CommandExecutor
{
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException
    {
        // Configuration Reload
        config.getInstance().loadConfig();

        // Not sure if this works or not.
        // If broken, do /sponge plugins reload instead. Not recommended, but it works.
        poi.initAnnouncements();

        // Sends a message to notify the source (source being the player that executed the command).
        source.sendMessage(toText("\n&bPoi has been reloaded! uwu"));

        return CommandResult.success();
    }
}
