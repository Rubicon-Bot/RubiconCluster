package fun.rubicon.cluster.cluster.impl;

import fun.rubicon.cluster.cluster.ClusterServer;
import fun.rubicon.cluster.cluster.event.ClusterEventManager;
import fun.rubicon.cluster.cluster.event.ClusterListenerAdapter;
import fun.rubicon.cluster.cluster.events.ClusterConnectedEvent;
import fun.rubicon.cluster.cluster.events.ClusterDisconnectedEvent;
import fun.rubicon.cluster.cluster.events.ClusterMessageReceivedEvent;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class ClusterServerImpl implements ClusterServer {

    private final Logger logger = LoggerFactory.getLogger(ClusterServerImpl.class.getSimpleName());
    private final int port;
    @Getter
    private final List<Channel> channels;
    @Getter
    private final ClusterEventManager eventManager;

    private ChannelFuture closeFuture;
    private EventLoopGroup eventLoopGroup;

    public ClusterServerImpl(int port, List<ClusterListenerAdapter> listenerAdapters) {
        this.port = port;

        eventManager = new ClusterEventManager(listenerAdapters);
        channels = new ArrayList<>();
    }

    public void start() throws Exception {
        logger.info("Starting server...");

        ClusterServerHandler clusterServerHandler = new ClusterServerHandler() {
            @Override
            protected void addChannel(Channel channel) {
                channels.add(channel);
                eventManager.fire(new ClusterConnectedEvent(getInstance(), channel));
            }

            @Override
            protected void removeChannel(Channel channel) {
                channels.remove(channel);
                eventManager.fire(new ClusterDisconnectedEvent(getInstance(), channel));
            }

            @Override
            protected void receivedMessage(Channel channel, String s) {
                eventManager.fire(new ClusterMessageReceivedEvent(getInstance(), channel, s));
            }
        };
        EventLoopGroup group = new NioEventLoopGroup();
        eventLoopGroup = group;
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(group)
                .channel(NioServerSocketChannel.class)
                .localAddress(port)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(clusterServerHandler);
                    }
                });
        closeFuture = serverBootstrap.bind().sync();
        logger.info("Started Server.");
    }

    @Override
    public void send(String invoke, String message) {
        for (Channel channel : channels)
            channel.writeAndFlush(Unpooled.copiedBuffer(String.format("[%s] %s", invoke, message), CharsetUtil.UTF_8));
    }

    @Override
    public void send(Channel channel, String invoke, String message) {
        channel.writeAndFlush(Unpooled.copiedBuffer(String.format("[%s] %s", invoke, message), CharsetUtil.UTF_8));
    }

    @Override
    public void shutdown() {
        try {
            closeFuture.channel().closeFuture().sync();
            eventLoopGroup.shutdownGracefully().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private ClusterServer getInstance() {
        return this;
    }
}
