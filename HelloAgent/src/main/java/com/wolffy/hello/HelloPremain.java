package com.wolffy.hello;

import java.lang.instrument.Instrumentation;

/**
 * 预先处理，程序启动时优先加载，JavaAgent.class - By 「Java者说」 -> https://www.jiweichengzhu.com/
 * Created by wolffy on 2022/2/15.
 */
public class HelloPremain {

    /**
     * premain()有两种写法，Instrumentation参数可以不传递，带有Instrumentation参数的方法优先级更高
     *
     * @param agentArgs 字符串参数，可以在启动的时候手动传入
     * @param inst      此参数由jvm传入，Instrumentation中包含了对class文件操作的一些api，可以让我们基于此来进行class文件的编辑
     */
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("Yes, I am a real Agent for premain Class.");

        // 引入自定义的transformer
        inst.addTransformer(new HelloTransformer(), true);
    }

    // public static void premain(String agentArgs) {
    //     System.out.println("Yes, I am a real Agent Class.");
    // }

}
