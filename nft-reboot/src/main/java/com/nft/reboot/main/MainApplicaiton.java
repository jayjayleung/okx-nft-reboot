package com.nft.reboot.main;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.RuntimeUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.Setting;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Scanner;

/**
 * @Author: jayjay
 * @Date: 2024/4/1
 * @ClassName: MainApplicaiton
 * @Description:
 */
public class MainApplicaiton {

    public static void main(String[] args) {
        Setting setting = new Setting("config.setting");
        String wallet = setting.get("wallet");
        String tokenPath = setting.get("tokenPath");
        // System.out.println(wallet);
        // System.out.println(tokenPath);
        File file = FileUtil.touch(tokenPath);
        // System.out.println(FileUtil.exist(file));
        // System.out.println(FileUtil.isEmpty(file));
        boolean flag = true;
        while (flag) {
            // clearConsole();
            int action = getAction();
            switch (action) {
                case 1:
                    System.out.println("配置文件路径为：" + wallet);
                    break;
                case 2:
                    System.out.print("输入token名称:");
                    String str = getStr();
                    FileUtil.appendUtf8Lines(CollUtil.toList(str), file);
                    break;
                case 3:
                    List<String> strings = FileUtil.readUtf8Lines(file);
                    System.out.println("当前token如下：");
                    strings.forEach(System.out::println);
                    break;
                case 0:
                    flag = false;
                    break;
            }
        }


    }

    public static int getAction(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入序号："); // 打印提示
        System.out.println("1：查看配置路径"); // 打印提示
        System.out.println("2：追加一个token"); // 打印提示
        System.out.println("3：列出所有token"); // 打印提示
        System.out.println("0：退出"); // 打印提示
        System.out.printf(":");
        int action = scanner.nextInt();
        return action;
    }


    public static String getStr(){
        Scanner scanner = new Scanner(System.in);
        String str = "";
        if(scanner.hasNextLine()) {
            str = scanner.nextLine();
            if (StrUtil.isBlank(str)) {
                System.out.print("不能为空，请重新输入：");
                getStr();
            }
        }
        return str;
    }

    public static void clearConsole() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }

}
