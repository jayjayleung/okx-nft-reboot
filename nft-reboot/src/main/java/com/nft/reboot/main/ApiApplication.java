package com.nft.reboot.main;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ObjectUtil;
import com.nft.reboot.entity.TokenInfo;
import com.nft.reboot.entity.TokenSaleInfo;
import com.nft.reboot.util.ApiUtil;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author liangshijie
 * @Date 2024/4/2
 * @Description:
 */
public class ApiApplication {

    final static String ANSI_RESET = "\u001B[0m";
    final static String ANSI_RED = "\u001B[31m";
    final static String ANSI_GREEN = "\u001B[32m";
    final static String ANSI_YELLOW = "\u001B[33m";

    public void run(File tokenFile){
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
                        System.out.println(ANSI_YELLOW+"筛选到低于地板价:"+rank.getFloorPrice().toString()+"的nft，数量为"+collect.size()+ANSI_RESET);
                        for (TokenSaleInfo tokenSaleInfo : collect) {
                            System.out.println(ANSI_YELLOW+tokenSaleInfo.getName()+"价格:"+tokenSaleInfo.getSalePrice().toString()+ANSI_RESET);
                        }
                        new BowerApplicaion().run(collect);
                    }else{
                        System.out.println(ANSI_YELLOW+"没有筛选到低于地板价:"+rank.getFloorPrice().toString()+"的nft"+ANSI_RESET);
                    }

                }
            }
        }
    }
}
