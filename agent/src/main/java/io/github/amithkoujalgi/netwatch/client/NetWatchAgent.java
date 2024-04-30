package io.github.amithkoujalgi.netwatch.client;

import io.github.amithkoujalgi.netwatch.Agent;
import io.github.amithkoujalgi.netwatch.Connection;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("VulnerableCodeUsages")
@AllArgsConstructor
@NoArgsConstructor
public class NetWatchAgent extends Thread {

  private String serverHost;
  private int serverPort;
  private Agent agent;

  public NetWatchAgent(String serverHost, int serverPort, String agentName, String agentHost,
      List<Connection> connections) {
    this.serverPort = serverPort;
    this.serverHost = serverHost;
    this.agent = new Agent(agentName, agentHost, connections);
  }

  public void run() {
    NioEventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap bootstrap = new Bootstrap().group(group).channel(NioSocketChannel.class)
          .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
              ch.pipeline().addLast(new EventHandler(agent));
            }
          });
      ChannelFuture future = bootstrap.connect(serverHost, serverPort).sync();
      future.channel().closeFuture().sync();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    } finally {
      group.shutdownGracefully();
    }
  }
}

