package io.github.netgaze;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;

public class ChatClient {

    public static void main(String[] args) throws InterruptedException, URISyntaxException {
        Socket socket = IO.socket("http://localhost:" + 9291);
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                socket.emit("toServer", "connected");
                socket.send("test");
            }
        });
        socket.on("toClient", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("Client received : " + args[0]);
            }
        });
        socket.connect();
        while (!socket.connected())
            Thread.sleep(50);
        socket.send("another test");
        Thread.sleep(10000);
        socket.disconnect();
    }

}

