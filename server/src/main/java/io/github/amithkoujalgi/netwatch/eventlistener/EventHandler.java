package io.github.amithkoujalgi.netwatch.eventlistener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@SuppressWarnings({"VulnerableCodeUsages", "CallToPrintStackTrace"})
@Slf4j
@Component
public class EventHandler extends ChannelInboundHandlerAdapter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (msg instanceof ByteBuf byteBuf) {
            String message = byteBuf.toString(CharsetUtil.UTF_8);
            log.info("Received message: " + message);
            try {
                Agent agent = objectMapper.readValue(message, Agent.class);
                AgentRegister.getInstance().updateAgent(agent);
            } catch (JsonProcessingException e) {
                log.error("Could not understand message from client: " + message);
            }
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
