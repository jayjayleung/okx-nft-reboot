package com.nft.robot.util;

import org.openqa.selenium.WebDriver;

import java.util.Set;

/**
 * @Author: JayJay
 * @Date: 2021/7/30
 * @ClassName: WindowUtil
 * @Description:
 */
public class WindowUtil {

    public static void swichToNextWindow(WebDriver driver, Set<String> windowHandles){
        if(windowHandles.size()>1){
            windowHandles.forEach(item->{
                if(!driver.getWindowHandle().equals(item)){
                    driver.switchTo().window(item);
                }
            });
        }
    }

    public static void swichToWindow(WebDriver driver, String windowHandle){

        driver.switchTo().window(windowHandle);
    }
}
