package fun.rubicon.cluster.cluster.event;

import fun.rubicon.cluster.cluster.ClusterServer;
import lombok.Getter;

public abstract class ClusterEvent {

    @Getter private final ClusterServer clusterServer;

    public ClusterEvent(ClusterServer clusterServer) {
        this.clusterServer = clusterServer;
    }
}
