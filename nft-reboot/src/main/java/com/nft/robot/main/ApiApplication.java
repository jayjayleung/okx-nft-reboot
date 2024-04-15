package com.nft.robot.main;

import cn.hutool.cache.impl.TimedCache;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.setting.AbsSetting;
import cn.hutool.setting.Setting;
import com.nft.robot.cache.GlobalCacheManager;
import com.nft.robot.cache.SeetingManager;
import com.nft.robot.entity.TokenInfo;
import com.nft.robot.entity.TokenSaleInfo;
import com.nft.robot.util.ApiUtil;
import com.nft.robot.util.DirUtil;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author liangshijie
 * @Date 2024/4/2
 * @Description:
 */
public class ApiApplication {

    public void run(File tokenFile) {
        Setting setting = SeetingManager.getSetting();
        String proxyEnable = setting.getStr("proxyEnable");
        //是否代理 科学上网
        if ("1".equals(proxyEnable)) {
            //设置代理ip
            System.setProperty("proxyHost", setting.getStr("proxyIp"));
            System.setProperty("proxyPort", setting.getStr("proxyPort"));
        }
        List<String> tokens = FileUtil.readUtf8Lines(tokenFile);
        for (int i = 0; i < tokens.size(); i++) {
            String threshold = setting.getStrNotEmpty("threshold", AbsSetting.DEFAULT_GROUP, "0.5");
            String tokenStr = tokens.get(i);
            List<String> split = StrUtil.split(tokenStr, "-");
            String token = tokenStr;
            if(split.size()>=2){
                token = split.get(0);
                threshold = split.get(1);
            }
            System.out.println("开始查询" + token);
            //抓取指定代币接口,先从缓存取
            TokenInfo rank = GlobalCacheManager.get(token);
            if(ObjectUtil.isNull(rank)) {
                //从api重新获取地板价
                System.err.println("从api获取"+token+"的地板价");
                rank = ApiUtil.getRank(token);
                if (ObjectUtil.isNotNull(rank)) {
                    GlobalCacheManager.set(token, rank);
                }
            }
            if (ObjectUtil.isNotNull(rank)) {
                BigDecimal multiply = rank.getFloorPrice().multiply(new BigDecimal("1").subtract(new BigDecimal(threshold)));
                System.out.println("查询到对应token信息");
                System.out.println("id:" + rank.getId());
                System.out.println("name:" + rank.getName());
                System.out.println("currency:" + rank.getCurrency());
                System.out.println("floorPrice:" + rank.getFloorPrice().toString());
                System.out.println("threshold:" + threshold);
                System.out.println("buyPrice:" + multiply);
                System.out.println("开始查询" + token + "nft集合");
                //抓取代币合集，价格从低到高排序
                List<TokenSaleInfo> market = ApiUtil.getMarket(rank.getId());
                if (CollUtil.isNotEmpty(market)) {
                    System.out.println("查询到" + token + "集合，开始筛选低于地板价"+threshold+":" + multiply + "的nft");
                    List<TokenSaleInfo> collect = market.stream().filter(item -> item.getSalePrice().doubleValue() <= multiply.doubleValue()).collect(Collectors.toList());
                    //筛选到
                    if (CollUtil.isNotEmpty(collect)) {
                        System.out.println("筛选到低于地板价"+threshold+":" + multiply.toString() + "的nft，数量为" + collect.size());
                        for (TokenSaleInfo tokenSaleInfo : collect) {
                            System.out.println(tokenSaleInfo.getName() + "价格:" + multiply.toString());
                        }
                        //打开浏览器购买
                        new BowerApplicaion().run(collect);
                    } else {
                        System.out.println("没有筛选到低于地板价"+threshold+":" + multiply.toString() + "的nft");
                    }

                }
            }
        }
    }
}
