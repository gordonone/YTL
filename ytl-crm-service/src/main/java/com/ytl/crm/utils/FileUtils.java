package com.ytl.crm.utils;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class FileUtils {

    public static String fileBasePath() {

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String formattedDate = currentDate.format(formatter);

        // 指定要创建的目录路径
        String directoryPath = "/app/crm/wxmedia/" + formattedDate;

        // 创建File对象
        File directory = new File(directoryPath);

        // 如果目录不存在，就创建它
        if (!directory.exists()) {
            boolean result = directory.mkdirs();
            if (result) {
                System.out.println("目录创建成功：" + directoryPath);
            } else {
                System.out.println("目录创建失败：" + directoryPath);
            }
        } else {
            System.out.println("目录已存在：" + directoryPath);
        }

        return directoryPath;
    }
}
