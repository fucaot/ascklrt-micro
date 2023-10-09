package com.ascklrt.infrastructure.jvm.classfile;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.OutputStream;

/**
 * @author goumang
 * @description
 * @date 2023/2/24 15:41
 */
public class AsClassLoader extends ClassLoader{

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {

        /*
         * 自定义类加载器需要去自定义找到class文件，根据传入的name读取到文件流
         *
         * 最终调用defineClass方法将byte数组转换成为class对象
         */

        try {
            // 构建到.class文件路径的inputstream输入流
            FileInputStream fis = new FileInputStream(name.replaceAll(".", "/"));

            // 构建outputstream，写入文件字节，最终获取字节数组
            ByteArrayOutputStream classFileOutputStream = new ByteArrayOutputStream();
            int b = 0;

            while ((b = fis.read()) != 0) {
                classFileOutputStream.write(b);
            }
            byte[] bytes = classFileOutputStream.toByteArray();

            // 另一种写法 不知是否可用
            // byte[] bytes = fis.readAllBytes();

            return defineClass(name, bytes, 0, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.findClass(name);
    }
}
