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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class MyCrawler implements Runnable {

    private CrawlerDao dao;

    public MyCrawler(CrawlerDao dao) {
        this.dao = dao;
    }


    @Override
    public void run() {
        final String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.100 Safari/537.36";
        try {
            String link;
            while ((link = dao.getNextLinkFromDataBaseAndDeleteIt()) != null) {

                if (!dao.isLinkProcessed(link)) {
                    if (isInterest(link)) {
                        Document doc = httpGetAndParseHtml(link, userAgent);

                        parseHtmlFromPageAndStoreIntoDataBase(doc);

                        storeIntoDataBaseIfIsNewsPage(doc, link);

                        dao.insertProcessedLink(link);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private void parseHtmlFromPageAndStoreIntoDataBase(Document doc) throws SQLException {
        ArrayList<Element> links = doc.select("a");
        for (Element aTag : links) {
            String href = aTag.attr("href");
            //在前端HTML语言中"//"表示当前页面的http或者https
            if (href.startsWith("//")) {
                href = "https:" + href;
            }
            if (!href.toLowerCase().startsWith("javascript")) {
                dao.insertToBeProcessedLink(href);
            }
        }
    }


    private void storeIntoDataBaseIfIsNewsPage(Document doc, String link) throws SQLException {
        ArrayList<Element> articleTags = doc.select("article");

        if (!articleTags.isEmpty()) {
            for (Element articleTag : articleTags) {
                String title = articleTags.get(0).child(0).text();
                String content = articleTag.select("p").stream().map(Element::text).collect(Collectors.joining("/n"));

                dao.storeNewsIntoDataBase(title, link, content);
                System.out.println(title);

            }
        }
    }


    private static Document httpGetAndParseHtml(String link, String userAgent) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();


        HttpGet httpGet = new HttpGet(link);
        httpGet.addHeader("User-Agent", userAgent);

        try (CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
            System.out.println(link);
            HttpEntity entity1 = response1.getEntity();

            String html = EntityUtils.toString(entity1);

            return Jsoup.parse(html);
        }
    }

    private static boolean isInterest(String link) {
        return (isNews(link) || isIndex(link)) && isNotLogPage(link);
    }

    private static boolean isNotLogPage(String link) {
        return !(link.contains("passport.sina.cn") || link.contains("hotnews.sina.cn"));
    }

    private static boolean isIndex(String link) {
        return "https://sina.cn".equals(link);
    }

    private static boolean isNews(String link) {
        return link.contains("news.sina.cn");
    }
}

