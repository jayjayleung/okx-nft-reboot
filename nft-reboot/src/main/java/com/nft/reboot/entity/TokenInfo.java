package com.nft.reboot.entity;

import lombok.Data;

/**
 * @Author liangshijie
 * @Date 2024/4/2
 * @Description:
 */
@Data
public class TokenInfo {

    private Integer id;
    private String name;
    private String currency;
    private Integer chain;
    private Double floorPrice;
}
