package com.wolffy.hello.util;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.util.ASMifier;
import jdk.internal.org.objectweb.asm.util.Printer;
import jdk.internal.org.objectweb.asm.util.Textifier;
import jdk.internal.org.objectweb.asm.util.TraceClassVisitor;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * asm打印工具，当我们不知道一个class文件是由哪些asm代码实现的时候，可以利用此工具打印一下，不关你是直接copy还是对照着优化，都是非常好用的
 * <p>
 * lsieun大佬提供，代码简洁，功能强悍
 * <p>
 * 关于此工具，更多讲解，请移步lsieun大佬提供的网址 https://lsieun.github.io/java-asm-01/how-to-write-core-code.html
 * <p>
 * Created by wolffy on 2022/2/15.
 */
public class ASMPrint {

    public static void main(String[] args) throws IOException {
        // (1) 设置参数
        String className = "com.wolffy.hello.HelloTest";
        int parsingOptions = ClassReader.SKIP_FRAMES | ClassReader.SKIP_DEBUG;
        boolean asmCode = true;

        // (2) 打印结果
        Printer printer = asmCode ? new ASMifier() : new Textifier();
        PrintWriter printWriter = new PrintWriter(System.out, true);
        TraceClassVisitor traceClassVisitor = new TraceClassVisitor(null, printer, printWriter);
        new ClassReader(className).accept(traceClassVisitor, parsingOptions);
    }

}