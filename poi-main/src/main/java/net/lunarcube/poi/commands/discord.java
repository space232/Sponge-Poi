package net.lunarcube.poi.commands;

import net.lunarcube.poi.config;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import static net.lunarcube.poi.poi.toText;

public class discord implements CommandExecutor
{
    public CommandResult execute(CommandSource source, CommandContext args) throws CommandException
    {
        // This is just getting the messages from the configuration.
        String message = config.getInstance().getConfig().getNode("DiscordMessage").getString();
        String link = config.getInstance().getConfig().getNode("DiscordLink").getString();
        String prefix = config.getInstance().getConfig().getNode("DiscordPrefix").getString();
        String hover = config.getInstance().getConfig().getNode("DiscordHover").getString();
        String heading = config.getInstance().getConfig().getNode("DiscordHeading").getString();
        String padding = config.getInstance().getConfig().getNode("DiscordPadding").getString();

        // Build the Pagination to send to the source (source being the player that executed the command).
        ArrayList<Text> contents = new ArrayList<>();
        try {
            contents.add(
                    Text.builder()
                            .append(Text.builder()
                                    .append(toText("\n" + prefix + message))
                                    .build())
                            .onHover(TextActions.showText(toText(hover)))
                            .onClick(TextActions.openUrl(new URL(link)))
                            .build());
            contents.add(
                    Text.builder()
                            .append(toText(prefix + "&a" + link + "\n"))
                            .onHover(TextActions.showText(toText(hover)))
                            .onClick(TextActions.openUrl(new URL(link)))
                            .build());
        } catch (MalformedURLException ignored) {
            contents.add(
                    Text.builder()
                            .append(Text.builder()
                                    .append(toText("\n" + prefix + message))
                                    .build())
                            .onHover(TextActions.showText(toText("&cURL is broken, check config")))
                            .build());
            contents.add(
                    Text.builder()
                            .append(toText(prefix + "&a" + link + "\n"))
                            .onHover(TextActions.showText(toText("&cURL is broken, check config")))
                            .build());
        }

        PaginationList.builder()
                .title(toText(heading))
                .padding(toText(padding))
                .contents(contents)
                .build()
                .sendTo(source);

        return CommandResult.success();
    }
}
