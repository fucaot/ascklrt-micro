package com.ascklrt.common.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author goumang
 * @description
 * @date 2022/12/1 14:21
 */
public class DownloadUtil {

    public static InputStream download(String path) throws IOException {
        try {
            URL url = new URL(path);

            // 获取链接
            URLConnection con = url.openConnection();

            // 得到链接长度
            int contentLength = con.getContentLength();

            // 得到输入流
            return con.getInputStream();

            // inputStream.readAllBytes()

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
