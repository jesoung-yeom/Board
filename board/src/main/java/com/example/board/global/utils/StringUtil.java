package com.example.board.global.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.List;

public class StringUtil {
    public static String extractContent(String content) {
        Document doc = Jsoup.parse(content);
        Elements images = doc.select("img");

        for (int i = 0; i < images.size(); i++) {
            images.get(i).attr("src", "[image-" + i + "]");
        }

        return doc.toString();
    }

    public static String combineContent(String content, List<String> convertList) {
        Document doc = Jsoup.parse(content);
        Elements images = doc.select("img");

        for (int i = 0; i < images.size(); i++) {
            images.get(i).attr("src", convertList.get(i).toString());
        }

        return doc.toString();
    }

    public static String extractFileExtension(String target) {
        String[] result = target.split("[.]");

        return result[result.length - 1];
    }
}
