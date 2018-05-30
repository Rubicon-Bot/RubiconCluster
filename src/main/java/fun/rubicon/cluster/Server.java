package fun.rubicon.cluster;

import fun.rubicon.cluster.bot.ServerBot;
import fun.rubicon.cluster.cluster.ClusterServer;
import fun.rubicon.cluster.cluster.command.ClusterCommandManager;
import fun.rubicon.cluster.event_impls.ClientConnectedEvent;
import fun.rubicon.cluster.util.Config;
import lombok.Getter;

import java.util.ArrayList;

public class Server {

    @Getter private static Server instance;
    @Getter private final Config config;
    @Getter private final ClusterCommandManager clusterCommandManager;
    @Getter private final ClusterServer clusterServer;
    @Getter private final ServerBot bot;

    public Server() {
        instance = this;

        // Init config
        config = new Config("server-config.json");
        setConfigDefaults();

        clusterCommandManager = new ClusterCommandManager();
        ClusterBuilder clusterBuilder = new ClusterBuilder(config.getInt("port"));

        // Add event listeners
        clusterBuilder.addListenerAdapter(clusterCommandManager);
        clusterBuilder.addListenerAdapter(new ClientConnectedEvent());

        try {
            clusterServer = clusterBuilder.build();
        } catch (Exception e) {
            throw new RuntimeException("Can't start server. Shutdown.");
        }

        bot = new ServerBot();
        bot.start();
    }

    public void shutdown() {
        clusterServer.shutdown();
    }

    private void setConfigDefaults() {
        config.setDefault("port", 13902);
        config.setDefault("bot_token", "myToken");
        config.setDefault("cmd_bot_start", "cd %s && sudo docker-compose up -d");
        config.setDefault("cmd_bot_stop", "cd %s && sudo docker-compose stop");
        config.setDefault("bot_credentials", new ArrayList<>());
        config.setDefault("whitelisted_users", new ArrayList<>());
    }
}
