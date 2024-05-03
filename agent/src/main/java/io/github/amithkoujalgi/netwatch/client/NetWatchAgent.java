//package io.github.amithkoujalgi.netwatch.client;
//
//import io.github.amithkoujalgi.netwatch.Agent;
//import io.github.amithkoujalgi.netwatch.Connection;
//import io.netty.bootstrap.Bootstrap;
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioSocketChannel;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//import java.util.Date;
//import java.util.List;
//
//@SuppressWarnings("VulnerableCodeUsages")
//@AllArgsConstructor
//@NoArgsConstructor
//@Slf4j
//public class NetWatchAgent extends Thread {
//
//    private String serverHost;
//    private int serverPort;
//    private Agent agent;
//
//    public NetWatchAgent(String serverHost, int serverPort, String agentName, String agentHost, List<Connection> connections) {
//        this.serverPort = serverPort;
//        this.serverHost = serverHost;
//        this.agent = new Agent(agentName, agentHost, new Date(), connections);
//    }
//
//    public void run() {
//        NioEventLoopGroup group = new NioEventLoopGroup();
//        try {
//            Bootstrap bootstrap = new Bootstrap().group(group).channel(NioSocketChannel.class).handler(new ChannelInitializer<SocketChannel>() {
//                @Override
//                protected void initChannel(SocketChannel ch) {
//                    ch.pipeline().addLast(new EventHandler(agent));
//                }
//            });
//            ChannelFuture future = bootstrap.connect(serverHost, serverPort).sync();
//            future.channel().closeFuture().sync();
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        } catch (Exception e) {
//            if (e.getMessage().contains("Connection refused")) {
//                log.error(e.getMessage());
//            } else {
//                throw new RuntimeException(e);
//            }
//        } finally {
//            group.shutdownGracefully();
//        }
//    }
//}
//
