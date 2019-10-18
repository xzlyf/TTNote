package com.xz.ttnote.utils;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 文件增删改查工具类
 */
public class FileIOUtil {
    /**
     * 删除一个文件夹包括里面的文件
     * 递归
     *
     * @param path
     * @return false 删除失败 true成功
     */
    public static boolean deleteDir(File path) {

        if (!path.exists()) {
            return false;
        }

        File[] files = path.listFiles();

        for (File f : files) {

            if (f.isDirectory()) {
                //递归删除文件夹
                deleteDir(f);
            } else {
                f.delete();
            }
        }

        //删除最后一个文件夹
        path.delete();
        Log.d("xz", "deleteDir: "+path.getAbsolutePath());
        return true;

    }

    /**
     * 复制文件到新目录
     *
     * @param oldPath  文件旧地址
     * @param newPath  新地址
     * @param fileName 文件名
     * @return
     */
    public static boolean copyFile(String oldPath, String newPath, String fileName) {
        String[] split = oldPath.split(File.separator);
//        String filePath = noteCache + split[split.length - 1];
        //        Logger.w(filePath);
        //首先判断缓存目录是否存在
        File createdir = new File(newPath);
        if (!createdir.exists()) {
            createdir.mkdirs();
        }
        //复制图片到缓存目录
        try {
            FileInputStream inputStream = new FileInputStream(oldPath);
            FileOutputStream outputStream = new FileOutputStream(newPath + fileName);
            byte[] buffer = new byte[1024];
            int byteRead;
            while (-1 != (byteRead = inputStream.read(buffer))) {
                outputStream.write(buffer, 0, byteRead);
            }
            outputStream.flush();
            IOStreamUtil.close(inputStream, outputStream);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
