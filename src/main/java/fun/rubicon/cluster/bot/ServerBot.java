package fun.rubicon.cluster.bot;

import fun.rubicon.cluster.Server;
import fun.rubicon.cluster.bot.command.BotCommandHandler;
import lombok.Getter;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;

public class ServerBot {

    private final Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());
    @Getter private JDA jda;

    public void start() {
        logger.info("Starting Bot...");
        try {
            jda = new JDABuilder(AccountType.BOT)
                    .setToken(Server.getInstance().getConfig().getString("bot_token"))
                    .setGame(Game.watching("Shards \uD83D\uDE08"))
                    .setAudioEnabled(false)
                    .addEventListener(new BotCommandHandler())
                    .buildBlocking();
            logger.info("Started Bot.");
        } catch (LoginException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
