package fun.jevon.Socket;

import java.io.*;
import java.net.*;

public class SocketServer {
    public static void main(String[] args) {
        int port = 8080; // 监听端口
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("服务器启动，正在监听端口 " + port + "...");
            // 客户端连接之前会一直监听，知道新的连接到来才执行后面的代码
            Socket clientSocket = serverSocket.accept(); // 等待客户端连接
            System.out.println("客户端已连接：" + clientSocket.getInetAddress());

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            // 读取客户端消息
            String message = in.readLine();
            System.out.println("收到客户端消息: " + message);

            // 回复客户端
            out.println("服务器已收到消息：" + message);

            clientSocket.close();
            System.out.println("连接已关闭。");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
