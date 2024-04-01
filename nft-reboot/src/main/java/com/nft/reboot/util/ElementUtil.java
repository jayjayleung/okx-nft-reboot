package com.nft.reboot.util;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

/**
 * @Author: JayJay
 * @Date: 2021/7/30
 * @ClassName: ElementUtil
 * @Description:
 */
public class ElementUtil {

    public static WebElement findXpath(WebDriver driver, String xpath) {
        By by = By.xpath(xpath);
        waiting(driver, by);
        WebElement element = driver.findElement(by);
        return element;
    }

    public static boolean isExitXpath(WebDriver driver, String xpath) {
        try {

            By by = By.xpath(xpath);
            waiting(driver, by);
            return true;
        }catch (TimeoutException e){
            return false;
        }
    }

    public static WebElement findTagName(WebDriver driver, String tagName) {
        By by = By.tagName(tagName);
        waiting(driver, by);
        WebElement element = driver.findElement(by);
        return element;
    }

    public static boolean isExitTagName(WebDriver driver, String tagName) {
        try {

            By by = By.tagName(tagName);
            waiting(driver, by);
            return true;
        }catch (TimeoutException e){
            return false;
        }
    }


    public static WebElement findText(WebDriver driver, String text) {
        String xpath = "//*[text()='"+text+"']";
        By by = By.xpath(xpath);
        waiting(driver, by);
        WebElement element = driver.findElement(by);
        return element;
    }

    public static boolean isExitByText(WebDriver driver, String text) {
        try {

            String xpath = "//*[text()='"+text+"']";
            By by = By.xpath(xpath);
            waiting(driver, by);
            return true;
        }catch (TimeoutException e){
            return false;
        }
    }

    public static WebElement findLinkText(WebDriver driver, String linkText) {
        By by = By.linkText(linkText);
        waiting(driver, by);
        WebElement element = driver.findElement(by);
        return element;
    }

    public static boolean isExitLinkText(WebDriver driver, String linkText) {
        try {

            By by = By.linkText(linkText);
            waiting(driver, by);
            return true;
        }catch (TimeoutException e){
            return false;
        }
    }

    public static void waitingForfindElement(WebDriver driver) {
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
    }


    public static void waiting(WebDriver driver, By by) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 15);
        webDriverWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
    }

    public static void waiting5s(WebDriver driver, By by) {
        WebDriverWait webDriverWait = new WebDriverWait(driver, 5);
        webDriverWait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
    }
}
