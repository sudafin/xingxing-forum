package com.xingxingforum.utils;

import org.lionsoul.ip2region.xdb.Searcher;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SearchIPUtils {
    public static Map<String,String> getIP(String ip) throws IOException {
        String dbPath = "src/main/resources/ip2region.xdb";
        String region = "";
        // 1、从 dbPath 中预先加载 VectorIndex 缓存，并且把这个得到的数据作为全局变量，后续反复使用。
        byte[] vIndex;
        try {
            vIndex = Searcher.loadVectorIndexFromFile(dbPath);
        } catch (Exception e) {
            System.out.printf("failed to load vector index from `%s`: %s\n", dbPath, e);
            return null;
        }

        // 2、使用全局的 vIndex 创建带 VectorIndex 缓存的查询对象。
        Searcher searcher;
        try {
            searcher = Searcher.newWithVectorIndex(dbPath, vIndex);
        } catch (Exception e) {
            System.out.printf("failed to create vectorIndex cached searcher with `%s`: %s\n", dbPath, e);
            return null;
        }

        // 3、查询
        try {
            region = searcher.search(ip);
        } catch (Exception e) {
            System.out.printf("failed to search(%s): %s\n", ip, e);
        }
        // 4、关闭资源
        searcher.close();
        Map<String,String> ipMap = new HashMap<>();
        // 去除多余的 "0|"
        region = region.replace("0|", "");
        // 分割字符串
        String[] parts = region.split("\\|");
        if (parts.length >= 3) {
            ipMap.put("country", parts[0]);
            ipMap.put("province", parts[1]);
            ipMap.put("city", parts[2]);
        }
        return ipMap;
    }
}
