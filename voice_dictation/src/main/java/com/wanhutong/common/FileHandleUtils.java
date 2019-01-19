package com.wanhutong.common;

import java.io.File;

/**
 * (C) Copyright 2017-now
 * All rights reserved.
 * <p>
 * 文件处理工具类
 *
 * @author DreamerCK
 * @date 2019-01-17 10:00
 **/
public class FileHandleUtils {

    /**
     * 创建StringBuilder对象
     *
     * @return StringBuilder对象
     */
    public static StringBuilder builder(String... strs) {
        final StringBuilder sb = new StringBuilder();
        for (String str : strs) {
            sb.append(str);
        }
        return sb;
    }

    public static String createTempFilePath(String childDicName, String fileName) {
        File parentFile = new File(builder(System.getProperty("java.io.tmpdir")).append(File.separator).append(childDicName).toString());
        if(!parentFile.exists()){
            //noinspection ResultOfMethodCallIgnored
            parentFile.mkdirs();
        }
        return builder(parentFile.getAbsolutePath()).append(File.separator).append(fileName).toString();
    }

    public static String getFileNameWithoutSuffix(String fileName){
        return fileName.substring(0, fileName.lastIndexOf("."));
    }
}
