package fun.rubicon.cluster.event_impls;

import fun.rubicon.cluster.cluster.event.ClusterListenerAdapter;
import fun.rubicon.cluster.cluster.events.ClusterConnectedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientConnectedEvent extends ClusterListenerAdapter {

    private Logger logger = LoggerFactory.getLogger(getClass().getSimpleName());

    @Override
    public void onConnected(ClusterConnectedEvent clusterConnectedEvent) {
        logger.info("New Connection: {}", clusterConnectedEvent.getChannel().remoteAddress());
        clusterConnectedEvent.reply("heartbeat", "");
    }
}
