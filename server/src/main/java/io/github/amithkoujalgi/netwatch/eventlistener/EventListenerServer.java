//package io.github.amithkoujalgi.netwatch.eventlistener;
//
//import io.netty.bootstrap.ServerBootstrap;
//import io.netty.channel.ChannelFuture;
//import io.netty.channel.ChannelInitializer;
//import io.netty.channel.nio.NioEventLoopGroup;
//import io.netty.channel.socket.SocketChannel;
//import io.netty.channel.socket.nio.NioServerSocketChannel;
//import lombok.extern.slf4j.Slf4j;
//
//@SuppressWarnings("VulnerableCodeUsages")
//@Slf4j
//public class EventListenerServer implements Runnable {
//
//  private final int port;
//
//  public EventListenerServer(int port) {
//    this.port = port;
//  }
//
//  @Override
//  public void run() {
//    log.info("Starting event listener on port: {}", port);
//    NioEventLoopGroup bossGroup = new NioEventLoopGroup();
//    NioEventLoopGroup workerGroup = new NioEventLoopGroup();
//    try {
//      ServerBootstrap bootstrap = new ServerBootstrap().group(bossGroup, workerGroup)
//          .channel(NioServerSocketChannel.class)
//          .childHandler(new ChannelInitializer<SocketChannel>() {
//            @Override
//            protected void initChannel(SocketChannel ch) {
//              ch.pipeline().addLast(new EventHandler());
//            }
//          });
//      ChannelFuture future = bootstrap.bind(this.port).sync();
//      future.channel().closeFuture().sync();
//    } catch (InterruptedException e) {
//      throw new RuntimeException(e);
//    } finally {
//      workerGroup.shutdownGracefully();
//      bossGroup.shutdownGracefully();
//    }
//  }
//}
//
