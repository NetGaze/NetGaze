package io.github.amithkoujalgi.netwatch.client;

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
import lombok.Setter;

@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NetWatchAgent extends Thread {

  private String agentName;
  private List<Connection> connections;
  private String serverHost;
  private int serverPort;

  public NetWatchAgent(String serverHost, int serverPort) {
    this.serverPort = serverPort;
    this.serverHost = serverHost;
  }

  public void run() {
    NioEventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap bootstrap = new Bootstrap().group(group).channel(NioSocketChannel.class)
          .handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
              ch.pipeline().addLast(new EventHandler(agentName, connections));
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

