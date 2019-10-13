package com.github.MyCrawler;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;


import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws IOException {
        final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36";
        //待处理的链接池
        List<String> linkPool = new ArrayList<>();
        //已经处理的链接池
        Set<String> processedLinks = new HashSet<>();
        linkPool.add("https://sina.cn");

        while (true) {
            if (linkPool.isEmpty()) {
                break;
            }
            //为什么选择是从后面删除呢？因为ArrayList删除是会依次将每个元素位置往前移动
            //所以删除最后一个这样做更有效率一些
            String link = linkPool.remove(linkPool.size() - 1);

            //如果已处理池中不包含就继续执行
            if (processedLinks.contains(link)) {
                continue;
            }

            if (isInterest(link)) {
                Document doc = httpGetAndParseHtml(link, userAgent);
                ArrayList<Element> links = doc.select("a");
                links.stream().map(aTag -> aTag.attr("href")).forEach(linkPool::add);

                storeIntoDataBaseIfIsNewsPage(doc);
            } else {
                //这里是我们不感兴趣的网站
                continue;
            }
            processedLinks.add(link);
        }
    }

    private static void storeIntoDataBaseIfIsNewsPage(Document doc) {
        ArrayList<Element> articleTags = doc.select("article");

        if (!articleTags.isEmpty()) {
            for (Element articleTag : articleTags) {
                String title = articleTags.get(0).child(0).text();
                System.out.println(title);
            }
        }
    }


    private static Document httpGetAndParseHtml(String link, String userAgent) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();

        //在前端HTML语言中"//"表示当前页面的http或者https
        if (link.startsWith("//")) {
            link = "https:" + link;
        }

        HttpGet httpGet = new HttpGet(link);
        httpGet.addHeader("User-Agent", userAgent);

        try (CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
            System.out.println(link);
            System.out.println(response1.getStatusLine());
            HttpEntity entity1 = response1.getEntity();

            String html = EntityUtils.toString(entity1);

            return Jsoup.parse(html);
        }
    }

    private static boolean isInterest(String link) {
        return (isNews(link) || isIndex(link)) && isNotLogPage(link);
    }

    private static boolean isNotLogPage(String link) {
        return !link.contains("passport.sina.cn");
    }

    private static boolean isIndex(String link) {
        return "https://sina.cn".equals(link);
    }

    private static boolean isNews(String link) {
        return link.contains("news.sina.cn");
    }
}

