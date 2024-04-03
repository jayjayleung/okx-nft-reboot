package com.nft.reboot.util;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


/**
 * @Author liangshijie
 * @Date 2024/4/3
 * @Description:
 */
public class SeleniumUtil {
    // 检查元素是否存在
    public static boolean isElementPresent(WebDriver driver, By locator) {
        boolean exists = driver.findElements(locator).size() > 0;
        System.out.println("Element " + locator + " exists: " + exists);
        return exists;
    }

    // 等待元素出现
    public static WebElement waitForElement(WebDriver driver, By locator, int timeout) {
        WebDriverWait wait = new WebDriverWait(driver, timeout);
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (TimeoutException e) {
            System.out.println("Element " + locator + " was not found within timeout");
            return null;
        }
    }

    // 安全地点击元素
    public static void safeClick(WebDriver driver, By locator, int timeout) {
        WebElement element = waitForElement(driver, locator, timeout);
        if (element != null) {
            element.click();
            System.out.println("Clicked on element " + locator);
        } else {
            System.err.println("Element " + locator + " was not found, could not click");
        }
    }

    // 安全地输入文本
    public static void safeType(WebDriver driver, By locator, String text, int timeout) {
        WebElement element = waitForElement(driver, locator, timeout);
        if (element != null) {
            element.sendKeys(text);
            System.out.println("Typed text into element " + locator);
        } else {
            System.err.println("Element " + locator + " was not found, could not type text");
        }
    }
}

