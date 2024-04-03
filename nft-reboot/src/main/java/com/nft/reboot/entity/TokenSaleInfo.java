package com.nft.reboot.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @Author liangshijie
 * @Date 2024/4/2
 * @Description:
 */
@Data
public class TokenSaleInfo {
    private Long id;
    private String name;
    private String projectName;
    private String contractAddress;
    private String collectionName;
    private String tokenId;
    private String currency;
    private Integer chain;
    private Integer project;

    private BigDecimal salePrice;
}
