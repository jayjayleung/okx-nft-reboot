package com.nft.robot.main;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.setting.AbsSetting;
import cn.hutool.setting.Setting;
import com.nft.robot.entity.TokenSaleInfo;
import com.nft.robot.util.DirUtil;
import com.nft.robot.util.ElementUtil;
import com.nft.robot.util.SeleniumUtil;
import com.nft.robot.util.WindowUtil;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 * @Author liangshijie
 * @Date 2024/4/2
 * @Description:
 */
public class BowerApplicaion {

    final static String ANSI_RESET = "";
    final static String ANSI_RED = "";
    final static String ANSI_GREEN = "";
    final static String ANSI_YELLOW = "";

    public void initWallet() {
        Setting setting = new Setting(DirUtil.getUserDir() + "config.setting");
        System.setProperty("webdriver.chrome.driver", setting.getStrNotEmpty("driverPath", AbsSetting.DEFAULT_GROUP, DirUtil.getUserDir() + "chromedriver.exe"));
        WebDriver driver = new ChromeDriver(getOptions());
        driver.manage().window().maximize();
        driver.get("https://chromewebstore.google.com/detail/%E6%AC%A7%E6%98%93-web3-%E9%92%B1%E5%8C%85/mcohilncbfahbmgdjkbpemcciiolgcge");
    }

    public static ChromeOptions getOptions() {
        Setting setting = new Setting(DirUtil.getUserDir() + "config.setting");
        // setting.autoLoad(true);
        ChromeOptions options = new ChromeOptions();

        //            options.setHeadless(Boolean.TRUE);
        options.addArguments("--user-data-dir=" + setting.getStrNotEmpty("chromeCachePath", AbsSetting.DEFAULT_GROUP, DirUtil.getUserDir() + "Chrome\\default\\"));
        // 浏览器窗口 最大化
        // options.addArguments("--start-maximized");

        // 忽略掉证书错误
        // /36.html
        // options.setExperimentalOption("excludeSwitches", "ignore-certificate-errors");
        // options.setExperimentalOption("excludeSwitches", "enable-automation");

        // 禁止显示：Chrome 正在受到自动软件的控制
        options.setExperimentalOption("excludeSwitches", CollectionUtil.toList("enable-automation", "ignore-certificate-errors"));
        // 禁止显示：请停用以开发者…
        options.setExperimentalOption("useAutomationExtension", false);
        options.setExperimentalOption("w3c", false);
        // 禁止显示：“保存密码”弹框
        Map<String, Object> prefsMap = new HashMap<>();
        prefsMap.put("credentials_enable_service", false);
        prefsMap.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefsMap);

        options.addArguments("lang=zh-CN,zh,zh-TW,en-US,en");
        //            options.addArguments("user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");
        // options.addArguments("disable-infobars");
        // options.addArguments("--no-sandbox");
        options.addArguments("disable-blink-features=AutomationControlled"); // 就是这一行告诉chrome去掉了webdriver痕迹
        options.addArguments("--ignore-certificate-errors"); // 忽略掉证书错误
        //            options.addArguments("--disable-extensions");//禁用扩展程序

        // 屏蔽 youtube、google等网站的资源
        options.addArguments("--host-resolver-rules=MAP www.youtube.com 127.0.0.1" +
                ",MAP www.google.com 127.0.0.1" +
                ",MAP cmacgm.matomo.cloud 127.0.0.1" +
                ",MAP ec.walkme.com 127.0.0.1" +
                ",MAP www.googletagmanager.com 127.0.0.1" +
                ",MAP www.freeprivacypolicy.com 127.0.0.1" +
                ",MAP www.googletagmanager.com 127.0.0.1" +
                ",MAP s.go-mpulse.net 127.0.0.1" +
                // ",MAP cdnjs.cloudflare.com 127.0.0.1" +
                ",MAP geolocation.onetrust.com 127.0.0.1"
        );
        //        options.addArguments("disable_gpu");
        //        options.addArguments("no-sandbox");//禁用沙盒
        //设置用户
        // options.addArguments("--profile-directory=Profile " + account.getUserName());
        return options;
    }

    public void run(List<TokenSaleInfo> list) {
        WebDriver driver = null;
        try {
            Setting setting = new Setting(DirUtil.getUserDir() + "config.setting");
            // setting.autoLoad(true);
            System.setProperty("webdriver.chrome.driver", setting.getStrNotEmpty("driverPath", AbsSetting.DEFAULT_GROUP, DirUtil.getUserDir() + "\\chromedriver.exe"));
            String wallet = setting.getStrNotEmpty("wallet", AbsSetting.DEFAULT_GROUP, "欧易钱包");
            System.out.println("准备打开Chrome浏览器");
            driver = new ChromeDriver(getOptions());
            //页面加载超时时间设置为 5s
            // driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
            System.out.println("Chrome浏览器已打开");
            driver.manage().window().maximize();
            System.out.println("打开合集网页");
            System.out.println("网页已经打开,当前url:" + driver.getCurrentUrl());


            boolean flag = false;
            for (int i = 0; i < list.size(); i++) {
                TokenSaleInfo tokenSaleInfo = list.get(i);
                //            String url = new StringBuilder("https://www.okx.com/zh-hans/web3/marketplace/nft/collection/")
                //                    .append(tokenSaleInfo.getCurrency().toLowerCase(Locale.ROOT))
                //                    .append("/")
                //                    .append(tokenSaleInfo.getCollectionName().toLowerCase(Locale.ROOT)).toString();

                StringBuilder url = new StringBuilder("https://www.okx.com/zh-hans/web3/marketplace/nft/asset/")
                        .append(tokenSaleInfo.getCurrency().toLowerCase(Locale.ROOT))
                        .append("/");
                if (!tokenSaleInfo.getCurrency().equalsIgnoreCase("btc") && !tokenSaleInfo.getCurrency().equalsIgnoreCase("sol")) {
                    url.append(tokenSaleInfo.getContractAddress()).append("/");
                }
                url.append(tokenSaleInfo.getTokenId());
                System.out.println("当前url:" + driver.getCurrentUrl());
                if (!url.equals(driver.getCurrentUrl())) {
                    System.out.println("打开：" + url);
                    driver.get(url.toString());
                    sleep(1000);
                }
                System.out.println("已打开编码：" + tokenSaleInfo.getName() + "的NFT");
                if (!ElementUtil.exitByText(driver, "购买")) {
                    System.err.println(ANSI_RED + "没有找到购买按钮，猜测已经购买，下一个" + ANSI_RESET);
                    continue;
                }
                System.out.println("开始购买...");
                WebElement buy = ElementUtil.findText(driver, "购买");
                if (buy == null) {
                    if (ElementUtil.exitByText(driver, "该铭文正在交易中，在交易确认之前无法交易或转移")) {
                        System.err.println("该铭文正在交易中，在交易确认之前无法交易或转移,跳过");
                        continue;
                    }
                    System.err.println("找不到购买按钮，跳过");
                    continue;
                }
                buy.click();
                System.out.println("使用" + wallet + "钱包购买");

                if (!flag) {
                    ElementUtil.findText(driver, "欧易钱包").click();
                    boolean elementPresent = SeleniumUtil.isElementPresent(driver, By.xpath("//*[text()='立即安装']"));
                    if (elementPresent) {
                        System.err.println(ANSI_RED + "未安装钱包插件，请先安装" + ANSI_RESET);
                        return;
                    }
                    try {
                        ElementUtil.findXpath(driver, "//button/span[normalize-space()='连接钱包']").click();
                    } catch (NoSuchElementException e) {
                        if (elementPresent) {
                            System.err.println(ANSI_RED + "未安装钱包插件，请先安装" + ANSI_RESET);
                            return;
                        }
                    }

                    //等待
                    sleep(1000);
                    String nftHandle = driver.getWindowHandle();
                    System.out.println(nftHandle);
                    int size = driver.getWindowHandles().size();
                    int wize = 0;
                    //阻塞
                    while (size < 2 && wize < 10) {
                        sleep(500);
                        size = driver.getWindowHandles().size();
                        wize++;
                    }
                    if (driver.getWindowHandles().size() > 1) {
                        WindowUtil.swichToNextWindow(driver, driver.getWindowHandles());
                        System.out.println(driver.getWindowHandle());
                        System.out.println(driver.getTitle());
                        if (ElementUtil.exitByText(driver, "解锁")) {
                            WebElement metaMaskPassWordEle = ElementUtil.findXpath(driver, "//input[@placeholder='请输入密码']");
                            metaMaskPassWordEle.sendKeys(setting.getStr("walletPassword"));
                            WebElement lockUpEle = ElementUtil.findText(driver, "解锁");
                            //                ElementUtil.findXpath(driver,"//button/span[normalize-space()='解锁']").click();
                            lockUpEle.click();
                        }
                        System.out.println("----");
                        //等待
                        sleep(1500);
                        if (driver.getWindowHandles().size() > 1) {
                            if (ElementUtil.exitByText(driver, "连接")) {
                                ElementUtil.findText(driver, "连接").click();
                            }
                        }
                        WindowUtil.swichToWindow(driver, nftHandle);
                        flag = true;
                    }
                }
                sleep(1000);
                if (ElementUtil.exitContainsByText(driver, "余额不足")) {
                    System.err.println(ANSI_RED + "余额不足" + ANSI_RESET);
                    ElementUtil.findText(driver, "取消").click();
                    continue;
                }
                if (ElementUtil.exitContainsByText(driver, "订单失效")) {
                    System.err.println(ANSI_RED + "该订单已被成交或取消，请选择其他订单进行交易" + ANSI_RESET);
                    ElementUtil.findText(driver, "继续交易").click();
                    continue;
                }
                if (ElementUtil.exitByText(driver, "风险提示")) {
                    ElementUtil.findText(driver, "确认").click();
                }
                sleep(1000);
                if (ElementUtil.exitByText(driver, "立即购买")) {
                    ElementUtil.findText(driver, "确认").click();
                    System.err.println(ANSI_YELLOW + "确认购买" + ANSI_RESET);
                    buy(driver);
                }
                // ElementUtil.findText(driver,"取消").click();
                if (i < list.size() - 1) {
                    System.out.println(ANSI_GREEN + "购买完成，继续下一个" + ANSI_RESET);
                } else {
                    System.out.println(ANSI_GREEN + "购买完成" + ANSI_RESET);
                }
            }
        } finally {
            System.out.println("执行完成，5秒后退出浏览器");
            sleep(5000);
            driver.quit();
        }

    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void buy(WebDriver driver) {
        Setting setting = new Setting(DirUtil.getUserDir() + "config.setting");
        System.out.println("打开钱包");
        String nftHandle = driver.getWindowHandle();
        System.out.println(nftHandle);
        int size = driver.getWindowHandles().size();
        int wize = 0;
        //阻塞
        while (size < 2 && wize < 10) {
            sleep(500);
            size = driver.getWindowHandles().size();
            wize++;
        }
        if (driver.getWindowHandles().size() > 1) {
            WindowUtil.swichToNextWindow(driver, driver.getWindowHandles());
            System.out.println(ANSI_YELLOW + "切换到钱包" + ANSI_RESET);
            System.out.println(driver.getWindowHandle());
            System.out.println(driver.getTitle());
            if (ElementUtil.exitByText(driver, "解锁")) {
                WebElement metaMaskPassWordEle = ElementUtil.findXpath(driver, "//input[@placeholder='请输入密码']");
                metaMaskPassWordEle.sendKeys(setting.getStr("walletPassword"));
                WebElement lockUpEle = ElementUtil.findText(driver, "解锁");
                //                ElementUtil.findXpath(driver,"//button/span[normalize-space()='解锁']").click();
                lockUpEle.click();
            }
            //等待
            sleep(1500);
            System.out.println(driver.getWindowHandle());
            System.out.println(driver.getTitle());
            if (driver.getWindowHandles().size() > 1) {
                if (ElementUtil.exitByText(driver, "连接")) {
                    ElementUtil.findText(driver, "连接").click();
                    sleep(1000);
                }
            }

            if (ElementUtil.exitByText(driver, "合约交互")) {
                System.out.println("当前是合约交互页面");
                ElementUtil.findText(driver, "确认").click();
                System.out.println(ANSI_RED + "确认合约交互" + ANSI_RESET);
            }
            sleep(600);
            WindowUtil.swichToWindow(driver, nftHandle);
            System.out.println("休息3秒");
            sleep(3000);
        }
    }


    public static List<TokenSaleInfo> mockData() {
        List<TokenSaleInfo> list = new ArrayList<>();
        TokenSaleInfo tokenSaleInfo = new TokenSaleInfo();
        tokenSaleInfo.setCurrency("SOL");
        tokenSaleInfo.setCollectionName("ink-22");
        tokenSaleInfo.setChain(1);
        tokenSaleInfo.setProjectName("INK");
        tokenSaleInfo.setName("INK ＃6540 （＃66143305）");
        tokenSaleInfo.setTokenId("APE74ztk9HN2bhvECSaZDME7tPskpd4LPDvrsUi68FEg");
        tokenSaleInfo.setProject(3291079);
        tokenSaleInfo.setId(27743127041228914L);
        tokenSaleInfo.setSalePrice(new BigDecimal(0.02999));
        tokenSaleInfo.setContractAddress("HTYzEokFYiXRi65kzfnXna1ZMYJ5q69JxFfjarviPpZz");
        list.add(tokenSaleInfo);
        TokenSaleInfo tokenSaleInfo2 = new TokenSaleInfo();
        tokenSaleInfo2.setCurrency("ETH");
        tokenSaleInfo2.setCollectionName("bored-ape-yacht-club");
        tokenSaleInfo2.setChain(1);
        tokenSaleInfo2.setProjectName("Bored Ape Yacht Club");
        tokenSaleInfo2.setTokenId("1456");
        tokenSaleInfo.setProject(205);
        tokenSaleInfo2.setName("＃9849");
        tokenSaleInfo2.setId(27743127041228914L);
        tokenSaleInfo2.setSalePrice(new BigDecimal(12.334));
        tokenSaleInfo2.setContractAddress("0xbc4ca0eda7647a8ab7c2061c2e118a18a936f13d");
        //        list.add(tokenSaleInfo2);
        return list;
    }

    public static void main(String[] args) {
        new BowerApplicaion().run(mockData());
    }


    @Test
    public void closeChrome() {
        cleanChrome();
    }

    public static void cleanChrome() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "taskkill /f /im chromedriver.exe").inheritIO().start().waitFor();
            } else {
                //                Runtime.getRuntime().exec("clear");
            }
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
