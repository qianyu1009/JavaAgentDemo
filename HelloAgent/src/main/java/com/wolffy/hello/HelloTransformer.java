package com.wolffy.hello;

import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.MethodVisitor;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import static jdk.internal.org.objectweb.asm.Opcodes.*;

/**
 * Transformer.class - By 「Java者说」 -> https://www.jiweichengzhu.com/
 * Created by wolffy on 2022/2/15.
 */
public class HelloTransformer implements ClassFileTransformer {

    /**
     * 转换提供的类文件并返回一个新的替换类文件
     *
     * @param loader              要转换的类的定义加载器，如果引导加载器可能为null
     * @param className           Java 虚拟机规范中定义的完全限定类和接口名称的内部形式的类名称。例如， "java/util/List" 。
     * @param classBeingRedefined 如果这是由重新定义或重新转换触发的，则该类被重新定义或重新转换；如果这是一个类加载， null
     * @param protectionDomain    被定义或重新定义的类的保护域
     * @param classfileBuffer     类文件格式的输入字节缓冲区 - 不得修改
     * @return 转换后的字节码数组
     * @throws IllegalClassFormatException class文件格式异常
     */
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {

        // 加入了自定义的transformer之后，所有需要加载的、但是还没有加载的类，当它们每一个要加载的时候，就需要通过transform方法
        // 所以需要加一个判断，只处理AgentTest类
        if (className.equals("com/wolffy/hello/test/AgentTest")) {
            System.out.println("<----------------- agent加载生效，开始更改class字节码 ----------------->");
            return dumpAgentTest();
        }

        // 其他AgentTest之外的类，我们直接返回null，就代表没有任何修改，还是用它原来的class
        // return new byte[0];
        return null;
    }

    /**
     * 更改AgentTest的字节码，将system.out打印的内容给替换掉
     * <p>
     * 设计到asm更改字节码的知识点，网络上很少有一个完整的知识体系，偶尔找到两篇文章也还都是抄来抄去，这里给大家提供一个学习的地址：https://lsieun.github.io/java/asm/index.html
     *
     * @return 更改之后的class文件字节流
     */
    private static byte[] dumpAgentTest() {
        ClassWriter cw = new ClassWriter(0);
        MethodVisitor mv;

        cw.visit(52, ACC_PUBLIC + ACC_SUPER, "com/wolffy/hello/test/AgentTest", null, "java/lang/Object", null);

        {
            mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(1, 1);
            mv.visitEnd();
        }

        {
            mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null);
            mv.visitCode();
            mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("hello world - https://www.jiweichengzhu.com/");
            mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(2, 1);
            mv.visitEnd();
        }

        cw.visitEnd();

        return cw.toByteArray();
    }

}
