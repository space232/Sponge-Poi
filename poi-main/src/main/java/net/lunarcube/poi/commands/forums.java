package net.lunarcube.poi.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;

import static net.lunarcube.poi.poi.toText;

public class forums implements CommandExecutor
{
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException
    {
        // Sends a message to notify the source (source being the player that executed the command).
        source.sendMessage(toText("&7You can find our website at &espce.moe"));

        return CommandResult.success();
    }
}
