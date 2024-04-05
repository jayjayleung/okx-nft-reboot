package com.nft.robot.util;

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
public class SeleniumUtils {
    private WebDriver driver;
    private WebDriverWait wait;
//    private Logger logger = Logger.getLogger(SeleniumUtils.class.getName());

    public SeleniumUtils(WebDriver driver, int timeout) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, timeout);
    }

    // 检查元素是否存在
    public boolean isElementPresent(By locator) {
        boolean exists = driver.findElements(locator).size() > 0;
        System.out.println("Element " + locator + " exists: " + exists);
        return exists;
    }

    // 等待元素出现
    public WebElement waitForElement(By locator) {
        try {
            return wait.until(ExpectedConditions.presenceOfElementLocated(locator));
        } catch (TimeoutException e) {
            System.err.println("Element " + locator + " was not found within timeout");
            return null;
        }
    }

    // 安全地点击元素
    public void safeClick(By locator) {
        WebElement element = waitForElement(locator);
        if (element != null) {
            element.click();
            System.out.println("Clicked on element " + locator);
        } else {
            System.err.println("Element " + locator + " was not found, could not click");
        }
    }

    // 安全地输入文本
    public void safeType(By locator, String text) {
        WebElement element = waitForElement(locator);
        if (element != null) {
            element.sendKeys(text);
            System.out.println("Typed text into element " + locator);
        } else {
            System.err.println("Element " + locator + " was not found, could not type text");
        }
    }
}

