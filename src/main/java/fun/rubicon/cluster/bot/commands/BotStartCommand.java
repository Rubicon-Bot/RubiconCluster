package fun.rubicon.cluster.bot.commands;

import fun.rubicon.cluster.Server;
import fun.rubicon.cluster.bot.command.BotCommand;
import fun.rubicon.cluster.ssh.SSHBotCredentials;
import fun.rubicon.cluster.ssh.SSHController;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;

public class BotStartCommand extends BotCommand {

    /*
     * start container all
     * start container <host>
     * start bot all
     * start bot <host>
     */

    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    @Override
    public void execute(MessageReceivedEvent event, Message message, Guild guild, String content, String invoke, String[] args, String prefix, User author) {
        if (args.length < 2) {
            message.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.red).setTitle(":warning: Wrong usage!").setDescription("You must provide at least **two** argument.").build()).queue();
            return;
        }

        switch (args[0].toLowerCase()) {
            case "container":
                startContainers(event, args);
                return;
            case "bot":
                if(args[1].equalsIgnoreCase("all")) {

                }
            default:
                message.getTextChannel().sendMessage(new EmbedBuilder().setColor(Color.orange).setTitle(":warning: Wrong usage!").setDescription("Valid options are `container` and `bot`").build()).queue();
                return;
        }
    }

    private void startContainers(MessageReceivedEvent messageReceivedEvent, String[] args) {
        boolean all = args[1].equalsIgnoreCase("all");

        JSONArray array = Server.getInstance().getConfig().getArray("bot_credentials");

        if(all) {
            for(Object obj : array) {
                SSHBotCredentials credentials = SSHBotCredentials.parse((String) obj);
                try {
                    SSHController sshController = new SSHController(credentials.getHost(), credentials.getUser(), credentials.getPassword());
                    sshController.send(String.format(Server.getInstance().getConfig().getString("cmd_bot_start"), credentials.getPath()));
                    messageReceivedEvent.getTextChannel().sendMessage(new EmbedBuilder().setTitle(":green_heart: Starting Procedure").setDescription(String.format("Starting Container on `%s`...", credentials.getHost())).setColor(Color.green).build()).queue();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
