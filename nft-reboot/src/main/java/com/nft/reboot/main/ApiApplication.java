package com.nft.reboot.main;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.setting.Setting;
import com.nft.reboot.entity.TokenInfo;
import com.nft.reboot.entity.TokenSaleInfo;
import com.nft.reboot.util.ApiUtil;
import com.nft.reboot.util.DirUtil;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author liangshijie
 * @Date 2024/4/2
 * @Description:
 */
public class ApiApplication {

    final static String ANSI_RESET = "";
    final static String ANSI_RED = "";
    final static String ANSI_GREEN = "";
    final static String ANSI_YELLOW = "";

    public void run(File tokenFile){
        Setting setting = new Setting(DirUtil.getUserDir()+"config.setting");
        String proxyEnable = setting.getStr("proxyEnable");
        if("1".equals(proxyEnable)) {
            System.setProperty("proxyHost", setting.getStr("proxyIp"));
            System.setProperty("proxyPort", setting.getStr("proxyPort"));
        }
        List<String> tokens = FileUtil.readUtf8Lines(tokenFile);
        for (int i = 0; i < tokens.size(); i++) {
            System.out.println("开始查询"+tokens.get(i));
            TokenInfo rank = ApiUtil.getRank(tokens.get(i));
            if(ObjectUtil.isNotNull(rank)) {
                System.out.println("查询到对应token信息");
                System.out.println("id:"+rank.getId());
                System.out.println("name:"+rank.getName());
                System.out.println("currency:"+rank.getCurrency());
                System.out.println("floorPrice:"+rank.getFloorPrice().toString());
                System.out.println("开始查询"+tokens.get(i)+"nft集合");
                List<TokenSaleInfo> market = ApiUtil.getMarket(rank.getId());
                if(CollUtil.isNotEmpty(market)){
                    System.out.println("查询到"+tokens.get(i)+"集合，开始筛选低于地板价:"+rank.getFloorPrice().toString()+"的nft");
                    List<TokenSaleInfo> collect = market.stream().filter(item -> item.getSalePrice().doubleValue() < rank.getFloorPrice().doubleValue()).collect(Collectors.toList());
                    if(CollUtil.isNotEmpty(collect)){
                        System.err.println(ANSI_YELLOW+"筛选到低于地板价:"+rank.getFloorPrice().toString()+"的nft，数量为"+collect.size()+ANSI_RESET);
                        for (TokenSaleInfo tokenSaleInfo : collect) {
                            System.err.println(ANSI_YELLOW+tokenSaleInfo.getName()+"价格:"+tokenSaleInfo.getSalePrice().toString()+ANSI_RESET);
                        }
                        new BowerApplicaion().run(collect);
                    }else{
                        System.err.println(ANSI_YELLOW+"没有筛选到低于地板价:"+rank.getFloorPrice().toString()+"的nft"+ANSI_RESET);
                    }

                }
            }
        }
    }
}
