package fun.rubicon.cluster.bot.command;

import fun.rubicon.cluster.bot.commands.BotStartCommand;
import fun.rubicon.cluster.bot.commands.BotStopCommand;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class BotCommandHandler extends ListenerAdapter {

    private final String prefix = "control!";

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if(event.getAuthor().getIdLong() != 227817074976751616L)
            return;
        if(!event.getChannel().getType().isGuild() || event.isWebhookMessage() || event.getAuthor().isBot() || event.getAuthor().isFake())
            return;
        Message message = event.getMessage();
        String content = event.getMessage().getContentDisplay();
        String[] rawArgs = content.split(" ");
        String invoke = rawArgs[0].replace(prefix, "");
        String[] args = new String[rawArgs.length - 1];
        System.arraycopy(rawArgs, 1, args, 0, args.length);

        if (!content.toLowerCase().startsWith(prefix))
            return;

        String inv = invoke.toLowerCase();
        switch (inv) {
            case "start":
                new BotStartCommand().execute(event, message, message.getGuild(), content, invoke, args, prefix, event.getAuthor());
                break;
            case "stop":
                new BotStopCommand().execute(event, message, message.getGuild(), content, invoke, args, prefix, event.getAuthor());
                break;
        }
    }
}
