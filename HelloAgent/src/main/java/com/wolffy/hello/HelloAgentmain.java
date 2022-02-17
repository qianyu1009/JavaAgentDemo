package com.wolffy.hello;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.instrument.Instrumentation;
import java.net.Socket;

/**
 * 后置处理，启动时无需加载，可在程序启动之后进行加载，JavaAgent.class - By 「Java者说」 -> https://www.jiweichengzhu.com/
 * Created by wolffy on 2022/2/15.
 */
public class HelloAgentmain {

    /**
     * agentmain()有两种写法，Instrumentation参数可以不传递，带有Instrumentation参数的方法优先级更高
     *
     * @param agentArgs 字符串参数，可以在启动的时候手动传入
     * @param inst      此参数由jvm传入，Instrumentation中包含了对class文件操作的一些api，可以让我们基于此来进行class文件的编辑
     */
    public static void agentmain(String agentArgs, Instrumentation inst) {
        System.out.println("Yes, I am a real Agent for agentmain Class.");

        try {
            // 连接socket服务端
            Socket socket = new Socket("127.0.0.1", 9876);

            // 打开输出流
            OutputStream os = socket.getOutputStream();

            // 格式化输出流，自带刷新
            PrintWriter pw = new PrintWriter(os, true);

            String project = System.getProperty("user.dir");
            pw.println("hay, i am project [" + project + "]");

            // 关闭IO
            pw.close();
            os.close();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // public static void agentmain(String agentArgs) {
    //     System.out.println("Yes, I am a real Agent Class.");
    // }

}
