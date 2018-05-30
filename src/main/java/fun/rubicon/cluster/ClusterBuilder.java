package fun.rubicon.cluster;

import fun.rubicon.cluster.cluster.ClusterServer;
import fun.rubicon.cluster.cluster.event.ClusterListenerAdapter;
import fun.rubicon.cluster.cluster.impl.ClusterServerImpl;

import java.util.ArrayList;
import java.util.List;

public class ClusterBuilder {

    private final int port;
    private final List<ClusterListenerAdapter> listenerAdapterList;

    public ClusterBuilder(int port) {
        this.port = port;

        listenerAdapterList = new ArrayList<>();
    }

    public void addListenerAdapter(ClusterListenerAdapter listenerAdapter) {
        listenerAdapterList.add(listenerAdapter);
    }

    public ClusterServer build() throws Exception {
        ClusterServerImpl clusterServer = new ClusterServerImpl(port, listenerAdapterList);
        clusterServer.start();
        return clusterServer;
    }
}
