package com.wolffy.hello;

import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.AttachNotSupportedException;
import com.sun.tools.attach.VirtualMachine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Scanner;

/**
 * Main.class - By 「Java者说」 -> https://www.jiweichengzhu.com/
 * Created by wolffy on 2022/2/15.
 */
public class HelloMain {

    public static void main(String[] args) {
        System.out.println("Hello, I am a JavaAgent demo, created by https://www.jiweichengzhu.com/\n");

        try {
            // 创建socket服务器
            ServerSocket server = new ServerSocket(9876);
            System.out.println("启动Socket Server完毕，开始监听9876端口\n");

            // 选择java应用程序的pid
            Scanner scanner = new Scanner(System.in);
            System.out.print("请输入PID: ");

            String pid = scanner.next();
            System.out.println();

            // 模拟arthas选择pid动作 + 加载agent
            simulationAndLoad(pid);
            System.out.println("为PID=" + pid + "的应用程序加载agent完毕\n");

            // 接收socket客户端链接，这里会阻塞，直到有客户端连接上来
            Socket client = server.accept();

            // 获取客户端远程地址信息
            SocketAddress address = client.getRemoteSocketAddress();
            System.out.println("客户端[" + address + "]已连接\n");

            String msg = receiveMsg(client);
            System.out.println("客户端[" + address + "]说: " + msg + "\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 模拟arthas选择pid动作 + 加载agent
     *
     * @param pid 进程ID
     */
    private static void simulationAndLoad(String pid) {
        try {
            // 连接jvm，并利用相关的api找到HelloTest工程运行时的进程id，也就是PID
            VirtualMachine vm = VirtualMachine.attach(pid);
            // 加载agent，大家注意使用自己的路径
            vm.loadAgent("D:\\workspace_idea\\HelloAgent\\target\\HelloAgent.jar");
            // 脱离jvm
            vm.detach();
        } catch (AttachNotSupportedException | IOException | AgentLoadException | AgentInitializationException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接收客户端消息，只做演示使用，所以只使用一次就行了
     *
     * @param socket 客户端链接
     * @return 客户端发来的消息
     * @throws IOException IO异常
     */
    private static String receiveMsg(Socket socket) throws IOException {
        // 打开客户端的输入流
        InputStream is = socket.getInputStream();
        // 创建字节流到字符流的桥接
        InputStreamReader isr = new InputStreamReader(is);
        // 借助缓存流来进行缓冲文本读取
        BufferedReader br = new BufferedReader(isr);
        // 读取一个文本行
        String msg = br.readLine();

        // 关闭IO
        br.close();
        isr.close();
        is.close();

        return msg;
    }

}
