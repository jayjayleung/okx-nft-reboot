package com.nft.reboot.main;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.AbsSetting;
import cn.hutool.setting.Setting;
import com.nft.reboot.util.DirUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * @Author: jayjay
 * @Date: 2024/4/1
 * @ClassName: MainApplicaiton
 * @Description:
 */
public class MainApplicaiton {

    // final static String ANSI_RESET = "\u001B[0m";
    // final static String ANSI_RED = "\u001B[31m";
    // final static String ANSI_GREEN = "\u001B[32m";
    // final static String ANSI_YELLOW = "\u001B[33m";
    final static String ANSI_RESET = "";
    final static String ANSI_RED = "";
    final static String ANSI_GREEN = "";
    final static String ANSI_YELLOW = "";

    public static void main(String[] args) {
        Setting setting = new Setting(DirUtil.getUserDir() + "config.setting");
        // setting.autoLoad(true);
        // System.out.println(DirUtil.getUserDir() + "config.setting");
        String tokenPath = setting.getStrNotEmpty("tokenPath", AbsSetting.DEFAULT_GROUP, DirUtil.getUserDir() + "tokens.txt");
        // System.out.println(wallet);
        // System.out.println(tokenPath);
        File file = FileUtil.touch(tokenPath);
        // System.out.println(FileUtil.exist(file));
        // System.out.println(FileUtil.isEmpty(file));
        boolean flag = true;
        while (flag) {
            // clearConsole();
            String action = getAction();
            switch (action) {
                case "1":
                    System.out.println(ANSI_YELLOW);
                    System.out.println("nft文件路径为：" + tokenPath);
                    System.out.println("驱动路径为：" + setting.getStrNotEmpty("driverPath", AbsSetting.DEFAULT_GROUP, DirUtil.getUserDir() + "chromedriver.exe"));
                    System.out.println("谷歌浏览器缓存路径：" + setting.getStrNotEmpty("chromeCachePath", AbsSetting.DEFAULT_GROUP, DirUtil.getUserDir() + "Chrome\\default\\"));
                    System.out.println("执行间隔(分钟)：" + setting.getStr("time"));
                    System.out.println("代理开关：" + setting.getInt("proxyEnable"));
                    System.out.println("代理IP：" + setting.getStr("proxyIp"));
                    System.out.println("代理端口：" + setting.getStr("proxyPort"));
                    System.out.println(ANSI_RESET);
                    break;

                case "2":
                    System.out.print("输入token名称(请输入正确名称全称，否则会忽略):");
                    String str = getStr();
                    FileUtil.appendUtf8Lines(CollUtil.toList(str), file);
                    break;
                case "3":
                    List<String> strings = FileUtil.readUtf8Lines(file);
                    if (CollUtil.isNotEmpty(strings)) {
                        System.out.println("当前token如下：");
                        strings.forEach(System.out::println);
                    } else {
                        System.out.println("当前没有token");
                    }
                    break;
                case "4":
                    new BowerApplicaion().initWallet();
                    break;
                case "5":
                    flag = false;
                    boolean run = true;
                    Long time = setting.getLong("time", 3L);
                    while (run) {
                        System.out.println("开始执行...间隔" + time + "分钟");
                        new ApiApplication().run(file);
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println("执行完成，休息" + time + "分钟");
                        try {
                            Thread.sleep(time * 60 * 1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                case "9":
                    boolean flag2 = true;
                    while (flag2) {
                        System.out.println(ANSI_GREEN + "========================================");
                        System.out.println("========================================" + ANSI_RESET);
                        System.out.println("请输入序号："); // 打印提示
                        System.out.println("1：修改nft文件路"); // 打印提示
                        System.out.println("2：修改驱动路径"); // 打印提示
                        System.out.println("3：修改谷歌浏览器缓存路径"); // 打印提示
                        System.out.println("4：修改执行间隔"); // 打印提示
                        System.out.println("0：返回"); // 打印提示
                        System.out.println(ANSI_GREEN + "========================================");
                        System.out.println("========================================" + ANSI_RESET);
                        System.out.printf(":");
                        Scanner scanner = new Scanner(System.in);
                        String num = scanner.nextLine();
                        switch (num) {
                            case "1":
                                System.out.println("请输入文件路径(注意路径分隔符是\"/\")：");
                                scanner = new Scanner(System.in);
                                String tokenPathStr = scanner.nextLine();
                                if (StrUtil.isBlank(tokenPathStr)) {
                                    System.out.println("路径不能为空");
                                } else {
                                    setting.set("tokenPath", tokenPathStr);
                                }
                                break;
                            case "2":
                                System.out.println("请输入文件路径(注意路径分隔符是\"/\")：");
                                scanner = new Scanner(System.in);
                                String driverPath = scanner.nextLine();
                                if (StrUtil.isBlank(driverPath)) {
                                    System.out.println("路径不能为空");
                                } else {
                                    setting.set("driverPath", driverPath);
                                }
                                break;
                            case "3":
                                System.out.println("请输入文件路径(注意路径分隔符是\"/\")：");
                                scanner = new Scanner(System.in);
                                String chromeCachePath = scanner.nextLine();
                                if (StrUtil.isBlank(chromeCachePath)) {
                                    System.out.println("路径不能为空");
                                } else {
                                    setting.set("chromeCachePath", chromeCachePath);
                                }
                                break;
                            case "4":
                                System.out.println("请输入间隔分钟数(建议不要小于3分钟)：");
                                scanner = new Scanner(System.in);
                                String time2 = scanner.nextLine();
                                if (StrUtil.isBlank(time2)) {
                                    System.out.println("分钟数不能为空");
                                } else {
                                    if (!NumberUtil.isNumber(time2)) {
                                        System.out.println("请输入数字！");
                                    } else {
                                        if (Double.valueOf(time2) <= 0) {
                                            System.out.println("请输入数大于0的数字！");
                                        } else {
                                            setting.set("time", time2);
                                        }
                                    }
                                }
                                break;
                            case "0":
                                flag2 = false;
                                break;
                            default:
                                break;
                        }
                    }

                    break;
                case "0":
                    flag = false;
                    System.exit(0);
                    break;
                default:
                    break;
            }
        }


    }

    public static String getAction() {
        Scanner scanner = new Scanner(System.in);
        System.out.printf(ANSI_GREEN);
        System.out.println("===========================================================================");
        System.out.println("===========================================================================");
        System.out.printf(ANSI_RESET);
        System.out.println(ANSI_RED + "*请按照config.setting设置好对应的参数带*是必填项" + ANSI_RESET);
        System.out.println(ANSI_RED + "*注意：浏览器驱动一定要下载和当前谷歌浏览器对应版本号的驱动，" + ANSI_RESET);
        System.out.println(ANSI_RED + "*查看版本号方式：" + ANSI_RESET);
        System.out.println(ANSI_RED + "*  1.浏览器地址栏输入：chrome://version/" + ANSI_RESET);
        System.out.println(ANSI_RED + "*  2.点击浏览器右上角三个点，设置->关于关于 Chrome 即可查看" + ANSI_RESET);
        System.out.println(ANSI_RED + "*驱动下载地址为(需要科学上网)：https://chromedriver.com/download" + ANSI_RESET);
        System.out.println(ANSI_RED + "*注意：token文件路径是一行一个token，输入后请按回车再输入下一行，否则视为同一个token" + ANSI_RESET);
        System.out.println("请输入序号："); // 打印提示
        System.out.println("1：查看配置"); // 打印提示
        System.out.println("2：追加一个token"); // 打印提示
        System.out.println("3：列出所有token"); // 打印提示
        System.out.println("4：初始化钱包"); // 打印提示
        System.out.println("5：开始监控"); // 打印提示
        // System.out.println("9：修改配置"); // 打印提示
        System.out.println("0：退出"); // 打印提示
        System.out.printf(ANSI_GREEN);
        System.out.println("===========================================================================");
        System.out.println("===========================================================================");
        System.out.printf(ANSI_RESET);
        System.out.printf(":");
        String action = scanner.nextLine();
        return action;
    }


    public static String getStr() {
        Scanner scanner = new Scanner(System.in);
        String str = "";
        if (scanner.hasNextLine()) {
            str = scanner.nextLine();
            if (StrUtil.isBlank(str)) {
                System.err.print("不能为空，请重新输入：");
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
