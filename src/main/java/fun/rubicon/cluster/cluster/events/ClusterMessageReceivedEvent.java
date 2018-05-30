package fun.rubicon.cluster.cluster.events;

import fun.rubicon.cluster.cluster.ClusterServer;
import fun.rubicon.cluster.cluster.event.ClusterEvent;
import io.netty.channel.Channel;
import lombok.Getter;

public class ClusterMessageReceivedEvent extends ClusterEvent implements ReplyableEvent {

    @Getter private final Channel channel;
    @Getter private final String message;

    public ClusterMessageReceivedEvent(ClusterServer clusterServer, Channel channel, String message) {
        super(clusterServer);
        this.channel = channel;
        this.message = message;
    }

    @Override
    public void reply(String invoke, String message) {
        getClusterServer().send(channel, invoke, message);
    }
}
