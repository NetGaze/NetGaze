package io.github.amithkoujalgi.netwatch.eventlistener;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;

@SuppressWarnings({"VulnerableCodeUsages", "CallToPrintStackTrace"})
@Slf4j
public class EventHandler extends ChannelInboundHandlerAdapter {

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) {
    if (msg instanceof ByteBuf byteBuf) {
      String message = byteBuf.toString(CharsetUtil.UTF_8);
      log.info("Received message: " + message);
      ctx.write(Unpooled.copiedBuffer("ok", StandardCharsets.UTF_8));
    } else {
      log.error("Received unexpected message type: " + msg.getClass().getName());
    }
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) {
    ctx.flush();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    cause.printStackTrace();
    ctx.close();
  }
}
