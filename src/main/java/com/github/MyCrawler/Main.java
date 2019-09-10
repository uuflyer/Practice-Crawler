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

            //这里的作用是把链接池中使用过的链接删除掉，否则它会一直执行一个链接
            //为什么选择是从后面删除呢？因为ArrayList删除是会依次将每个元素位置往前移动
            //所以删除最后一个这样做更有效率一些
            String link = linkPool.remove(linkPool.size() - 1);

            //如果已处理池中不包含他就继续执行
            if (!processedLinks.contains(link)) {
                continue;
            }

            if (!link.contains("sina.cn") || link.contains("passport.sina.cn")) {
                //这里是我们不感兴趣的网站
                continue;
            } else {
                //在前端HTML语言中"//"表示当前页面的http或者https
                if (link.startsWith("//")) {
                    link = "https:" + link;
                }
                CloseableHttpClient httpclient = HttpClients.createDefault();
                HttpGet httpGet = new HttpGet("https://sina.cn");
                httpGet.addHeader("User-Agent", userAgent);

                try (CloseableHttpResponse response1 = httpclient.execute(httpGet)) {
                    System.out.println(link);
                    System.out.println(response1.getStatusLine());

                    HttpEntity entity1 = response1.getEntity();

                    String html = EntityUtils.toString(entity1);

                    //调用Jsoup进行对HTML字符串信息解析
                    Document doc = Jsoup.parse(html);

                    //使用Document对象查找a标签的所有信息
                    ArrayList<Element> links = doc.select("a");

                    for (Element aTag : links) {
                        linkPool.add(aTag.attr("href"));
                    }
                }
            }
        }
    }
}
