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
                System.out.println("floorPrice:"+rank.getFloorPrice());
                System.out.println("开始查询"+tokens.get(i)+"nft集合");
                List<TokenSaleInfo> market = ApiUtil.getMarket(rank.getId());
                if(CollUtil.isNotEmpty(market)){
                    System.out.println("查询到"+tokens.get(i)+"集合，开始筛选低于地板价的nft");
                    List<TokenSaleInfo> collect = market.stream().filter(item -> item.getSalePrice() < rank.getFloorPrice()).collect(Collectors.toList());
                    if(CollUtil.isNotEmpty(collect)){
                        for (TokenSaleInfo tokenSaleInfo : collect) {
                            System.out.println(tokenSaleInfo.getName()+"价格:"+tokenSaleInfo.getSalePrice());
                        }
                        System.out.println("筛选到低于地板价的nft，数量为"+collect.size());
                        new BowerApplicaion().run(collect);
                    }

                }
            }
        }
    }
}
