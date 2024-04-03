package com.nft.reboot.main;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.setting.Setting;
import com.nft.reboot.entity.TokenSaleInfo;
import com.nft.reboot.util.ElementUtil;
import com.nft.reboot.util.SeleniumUtil;
import com.nft.reboot.util.WindowUtil;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.IOException;
import java.util.*;

/**
 * @Author liangshijie
 * @Date 2024/4/2
 * @Description:
 */
public class BowerApplicaion {

    public void initWallet(){

        Setting setting = new Setting("config.setting");
        String wallet = setting.get("wallet");
        System.setProperty("webdriver.chrome.driver", "D:\\chromedriver.exe");
    }

    public void run(List<TokenSaleInfo> list){
        WebDriver driver = null;
        try {
            Setting setting = new Setting("config.setting");
            String wallet = setting.get("wallet");
            System.setProperty("webdriver.chrome.driver", "D:\\chromedriver.exe");
            System.out.println("准备打开Chrome浏览器");
            ChromeOptions options = new ChromeOptions();
//            options.setHeadless(Boolean.TRUE);
            options.addArguments("--user-data-dir=D:/Chrome/default2/");
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
            driver = new ChromeDriver(options);
            System.out.println("Chrome浏览器已打开");
//        driver.manage().window().maximize();
            System.out.println("打开合集网页");
            System.out.println("网页已经打开,当前url:"+driver.getCurrentUrl());


            boolean flag = false;
            for (TokenSaleInfo tokenSaleInfo : list){
//            String url = new StringBuilder("https://www.okx.com/zh-hans/web3/marketplace/nft/collection/")
//                    .append(tokenSaleInfo.getCurrency().toLowerCase(Locale.ROOT))
//                    .append("/")
//                    .append(tokenSaleInfo.getCollectionName().toLowerCase(Locale.ROOT)).toString();

                StringBuilder url = new StringBuilder("https://www.okx.com/zh-hans/web3/marketplace/nft/asset/")
                        .append(tokenSaleInfo.getCurrency().toLowerCase(Locale.ROOT))
                        .append("/");
                if(!tokenSaleInfo.getCurrency().equalsIgnoreCase("btc") && !tokenSaleInfo.getCurrency().equalsIgnoreCase("sol")){
                    url.append(tokenSaleInfo.getContractAddress()).append("/");
                }
                url.append(tokenSaleInfo.getTokenId());
                System.out.println("当前url:"+driver.getCurrentUrl());
                if(!url.equals(driver.getCurrentUrl())) {
                    System.out.println("打开："+url);
                    driver.get(url.toString());
                }
                System.out.println("已打开编码："+tokenSaleInfo.getName()+"的NFT");
                System.out.println("开始购买...");
                WebElement buy = ElementUtil.findText(driver, "购买");
                if(buy ==null){
                    if(ElementUtil.exitByText(driver,"该铭文正在交易中，在交易确认之前无法交易或转移")){
                        System.err.println("该铭文正在交易中，在交易确认之前无法交易或转移,跳过");
                        continue;
                    }
                    System.out.println("找不到购买按钮，跳过");
                    continue;
                }
                buy.click();
                System.out.println("使用"+wallet+"钱包购买");

                if(!flag) {
                    ElementUtil.findText(driver, "欧易钱包").click();
                    boolean elementPresent = SeleniumUtil.isElementPresent(driver, By.xpath("//*[text()='立即安装']"));
                    if(elementPresent){
                        System.err.println("未安装钱包插件，请先安装");
                        return;
                    }
                    try {
                        ElementUtil.findXpath(driver, "//button/span[normalize-space()='连接钱包']").click();
                    }catch (NoSuchElementException e) {
                        if(elementPresent){
                            System.err.println("未安装钱包插件，请先安装");
                            return;
                        }
                    }

                    //等待
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    String nftHandle = driver.getWindowHandle();
                    Set<String> windowHandles = driver.getWindowHandles();
                    System.out.println(nftHandle);
                    System.out.println(windowHandles.size());
                    if (windowHandles.size() > 1) {
                        WindowUtil.swichToNextWindow(driver, windowHandles);
                        System.out.println(driver.getWindowHandle());
                        System.out.println(driver.getTitle());
                        if (ElementUtil.exitByText(driver, "解锁")) {
                            WebElement metaMaskPassWordEle = ElementUtil.findXpath(driver, "//input[@placeholder='请输入密码']");
                            metaMaskPassWordEle.sendKeys("a123478.");
                            WebElement lockUpEle = ElementUtil.findText(driver, "解锁");
//                ElementUtil.findXpath(driver,"//button/span[normalize-space()='解锁']").click();
                            lockUpEle.click();
                        }
                        System.out.println("----");
                        //等待
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        System.out.println(driver.getWindowHandle());
                        System.out.println(driver.getTitle());
                        if (driver.getWindowHandles().size() > 1) {
                            if (ElementUtil.isExitByText(driver, "连接")) {
                                ElementUtil.findText(driver, "连接").click();
                            }
                        }
                        WindowUtil.swichToWindow(driver, nftHandle);
                        flag = true;
                    }
                }
//            ElementUtil.findText(driver,"确定").click();
                ElementUtil.findText(driver,"取消").click();
                System.out.println("购买完成，继续下一个");
            }
        }finally {
            System.out.println("执行完成，退出浏览器");
            driver.quit();
        }

    }

    public void buy(WebDriver driver){
        //等待
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String nftHandle = driver.getWindowHandle();
        Set<String> windowHandles = driver.getWindowHandles();
        System.out.println(nftHandle);
        System.out.println(windowHandles.size());
        if (windowHandles.size() > 1) {
            WindowUtil.swichToNextWindow(driver, windowHandles);
            System.out.println(driver.getWindowHandle());
            System.out.println(driver.getTitle());
            if (ElementUtil.isExitByText(driver, "解锁")) {
                WebElement metaMaskPassWordEle = ElementUtil.findXpath(driver, "//input[@placeholder='请输入密码']");
                metaMaskPassWordEle.sendKeys("a123478.");
                WebElement lockUpEle = ElementUtil.findText(driver, "解锁");
//                ElementUtil.findXpath(driver,"//button/span[normalize-space()='解锁']").click();
                lockUpEle.click();
            }
            System.out.println("----");
            //等待
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(driver.getWindowHandle());
            System.out.println(driver.getTitle());
            if (driver.getWindowHandles().size() > 1) {
                if (ElementUtil.isExitByText(driver, "连接")) {
                    ElementUtil.findText(driver, "连接").click();
                }
            }
            WindowUtil.swichToWindow(driver, nftHandle);
        }
    }

//    public void dddd(){
//        //小狐狸钱包解锁等操作
//        String nftHandle = driver.getWindowHandle();
//        Set<String> windowHandles = driver.getWindowHandles();
//        System.out.println(nftHandle);
//        System.out.println(windowHandles.size());
//        WindowUtil.swichToNextWindow(driver,windowHandles);
//        System.out.println(driver.getWindowHandle());
//        System.out.println(driver.getTitle());
//            if(ElementUtil.isExitByText2(driver,"即将进入去中心化网络")) {
//        WebElement metaMaskPassWordEle = ElementUtil.findXpath(driver, "//*[@id=\"password\"]");
//        metaMaskPassWordEle.sendKeys("a123478.");
//        WebElement lockUpEle = ElementUtil.findXpath(driver, "//*[@id=\"app-content\"]/div/div[3]/div/div/button");
//        lockUpEle.click();
//            }
////            if(ElementUtil.isExitByText(driver,"下一步")) {
//        WebElement next = ElementUtil.findText(driver, "下一步");
//        next.click();
//        WebElement linkMetaMask = ElementUtil.findText(driver, "连接");
//        linkMetaMask.click();
////            }
//
//        WindowUtil.swichToWindow(driver,nftHandle);
//    }

    public static List<TokenSaleInfo> mockData(){
        List<TokenSaleInfo> list = new ArrayList<>();
        TokenSaleInfo tokenSaleInfo = new TokenSaleInfo();
        tokenSaleInfo.setCurrency("BTC");
        tokenSaleInfo.setCollectionName("ink-22");
        tokenSaleInfo.setChain(1);
        tokenSaleInfo.setProjectName("INK");
        tokenSaleInfo.setName("INK ＃6540 （＃66143305）");
        tokenSaleInfo.setTokenId("696978847241f88148b735ebd6fc31b372bc75d63a0dc880f19e13e8e893c9fei387");
        tokenSaleInfo.setProject(3291079);
        tokenSaleInfo.setId(27743127041228914L);
        tokenSaleInfo.setSalePrice(0.02999);
        tokenSaleInfo.setContractAddress("33U66KU5hi94szaebiTShvVTDGVsnZur7q");
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
        tokenSaleInfo2.setSalePrice(12.334);
        tokenSaleInfo2.setContractAddress("0xbc4ca0eda7647a8ab7c2061c2e118a18a936f13d");
//        list.add(tokenSaleInfo2);
        return list;
    }


    public static void main(String[] args) {
        new BowerApplicaion().run(mockData());
    }
    @Test
    public void closeChrome(){
        cleanChrome();
    }
    
    public static void cleanChrome(){
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
