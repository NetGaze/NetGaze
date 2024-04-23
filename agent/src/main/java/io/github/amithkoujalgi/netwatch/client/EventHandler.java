package io.github.amithkoujalgi.netwatch.client;

import io.github.amithkoujalgi.netwatch.Connection;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("CallToPrintStackTrace")
public class EventHandler extends ChannelInboundHandlerAdapter {

  private final String agentName;
  private final List<Connection> connections;

  public EventHandler(String agentName, List<Connection> connections) {
    this.connections = connections;
    this.agentName = agentName;
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) {
    new Thread(new EventSender(agentName, connections, ctx)).start();
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    ByteBuf byteBuf = (ByteBuf) msg;
    log.info("Ack from server: " + byteBuf.toString(StandardCharsets.UTF_8));
    byteBuf.release();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }
}
