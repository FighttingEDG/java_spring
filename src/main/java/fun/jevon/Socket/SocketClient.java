package fun.jevon.Socket;

import java.io.*;
import java.net.*;

public class SocketClient {
    public static void main(String[] args) {
        String host = "127.0.0.1"; // 连接到本机
        int port = 8080;           // 服务器端口

        try (Socket socket = new Socket(host, port)) {
            System.out.println("已连接服务器：" + host + ":" + port);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            // 发送一条消息给服务器
            out.println("你好，服务器！这是客户端发来的消息。");

            // 等待服务器回复
            String response = in.readLine();
            System.out.println("收到服务器回复: " + response);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
