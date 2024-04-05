package com.nft.robot.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.hutool.setting.Setting;
import com.nft.robot.entity.TokenInfo;
import com.nft.robot.entity.TokenSaleInfo;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author liangshijie
 * @Date 2024/4/2
 * @Description:
 */
public class ApiUtil {
    public static void main(String[] args) {

        Setting setting = new Setting(DirUtil.getUserDir()+"config.setting");
        String proxyEnable = setting.getStr("proxyEnable");
        if("1".equals(proxyEnable)) {
            System.setProperty("proxyHost", setting.getStr("proxyIp"));
            System.setProperty("proxyPort", setting.getStr("proxyPort"));
        }
        TokenInfo rank = getRank("INK");
        System.out.println(JSONUtil.toJsonStr(rank));
        List<TokenSaleInfo> market = getMarket(rank.getId());
        System.out.println(JSONUtil.toJsonStr(market));
    }

    public static TokenInfo getRank(String nameLike){
        Map<String, Object> rankParam = getRankParam();
        rankParam.put("nameLike",nameLike);
        String url = "https://www.okx.com/priapi/v1/nft/project-rank";
        HttpResponse res = HttpRequest.get(url)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36 Edg/124.0.0.0")
                .header("Host", "www.okx.com")
                .form(rankParam).form("t",System.currentTimeMillis()).execute();
        if(res.getStatus() != 200){
            return null;
        }
        String body = res.body();
        JSONObject json = toJSON(body);
        if(JSONUtil.isNull(json)){
            return null;
        }
        Integer code = json.getInt("code");
        if(code != 0){
            return null;
        }
        JSONObject data = json.getJSONObject("data");
        if(JSONUtil.isNull(data)){
            return null;
        }
        JSONArray list = data.getJSONArray("list");
        if(JSONUtil.isNull(list)){
            return null;
        }
        Optional<TokenInfo> any = list.stream().map(item -> {
            JSONObject itemJson = (JSONObject) item;
            TokenInfo tokenInfo = new TokenInfo();
            tokenInfo.setName(itemJson.getStr("name"));
            tokenInfo.setId(itemJson.getInt("id"));
            tokenInfo.setChain(itemJson.getInt("chain"));
            tokenInfo.setCurrency(itemJson.getStr("currency"));
            JSONObject floorNativePrice = itemJson.getJSONObject("floorNativePrice");
            tokenInfo.setFloorPrice(new BigDecimal(floorNativePrice.getStr("price")));
            return tokenInfo;
        }).filter(item -> item.getName().equals(nameLike)).findAny();

        return any.isPresent()?any.get():null;
    }

    public static List<TokenSaleInfo> getMarket(int id){
        String url = "https://www.okx.com/priapi/v1/nft/secondary/market";
        HttpResponse res = HttpRequest.post(url)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36 Edg/124.0.0.0")
                .header("Host", "www.okx.com")
                .form(getMarketParam())
                .body(getMarketBody(id).toString()).execute();
        if(res.getStatus() != 200){
            return null;
        }
        String body = res.body();
        JSONObject json = toJSON(body);
        if(JSONUtil.isNull(json)){
            return null;
        }
        Integer code = json.getInt("code");
        if(code != 0){
            return null;
        }
        JSONObject data = json.getJSONObject("data");
        if(JSONUtil.isNull(data)){
            return null;
        }
        JSONArray list = data.getJSONArray("list");
        if(JSONUtil.isNull(list)){
            return null;
        }
        List<TokenSaleInfo> collect = list.stream().map(item -> {
            JSONObject itemJson = (JSONObject) item;
            TokenSaleInfo tokenInfo = new TokenSaleInfo();
            tokenInfo.setProjectName(itemJson.getStr("projectName"));
            tokenInfo.setName(itemJson.getStr("name"));
            tokenInfo.setContractAddress(itemJson.getStr("contractAddress"));
            tokenInfo.setCollectionName(itemJson.getStr("collectionName"));
            tokenInfo.setProject(itemJson.getInt("project"));
            tokenInfo.setTokenId(itemJson.getStr("tokenId"));
            tokenInfo.setId(itemJson.getLong("id"));
            tokenInfo.setChain(itemJson.getInt("chain"));
            JSONObject sale = itemJson.getJSONObject("sale");
            tokenInfo.setCurrency(sale.getStr("currency"));
            tokenInfo.setSalePrice(new BigDecimal(sale.getDouble("price")));
            return tokenInfo;
        }).collect(Collectors.toList());
        return collect;
    }

    public static Map<String,Object> getRankParam(){
        Map<String, Object> form = new HashMap<>();
        form.put("chain","");
        form.put("walletAddress","");
        form.put("currency","");
        form.put("timeType",1);
        form.put("page",1);
        form.put("pageSize",100);
        form.put("sortBy","");
        form.put("collectionType","");
        form.put("projectIds","");
        form.put("noWash",1);
        form.put("nameLike","");
        form.put("t", System.currentTimeMillis());
        return form;
    }

    public static Map<String,Object> getMarketParam(){
        Map<String, Object> form = new HashMap<>();
        form.put("t", System.currentTimeMillis());
        return form;
    }

    public static JSONObject getMarketBody(Integer id){
        JSONObject json = JSONUtil.parseObj("{\"sources\":[],\"stateIn\":[],\"nameSearch\":\"\",\"sortBy\":\"makeOrderUsd\",\"ownerAddressIn\":[],\"contentTypes\":[],\"pageNum\":1,\"pageSize\":24,\"cursor\":\"\"}");
        json.set("projectIn", new Integer[]{id});
        return json;
    }


    public static JSONObject toJSON(String str){
        if(StrUtil.isBlank(str)){
            return null;
        }
        if(!JSONUtil.isTypeJSON(str)){
            return null;
        }
        return JSONUtil.parseObj(str);
    }



}
