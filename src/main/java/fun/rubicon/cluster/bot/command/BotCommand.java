package fun.rubicon.cluster.bot.command;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public abstract class BotCommand {

    public abstract void execute(MessageReceivedEvent event, Message message, Guild guild, String content, String invoke, String[] args, String prefix, User author);
}
