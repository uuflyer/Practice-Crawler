package com.github.MyCrawler;

import com.sun.xml.internal.bind.v2.model.annotation.RuntimeAnnotationReader;
import org.apache.http.HttpHost;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Elasticsearch {
    public static void main(String[] args) throws IOException {
        SqlSessionFactory sqlSessionFactory;

        try {
            String resource = "db/mybatis/config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        List<News> newsFromMysql = getNewsFromMysql(sqlSessionFactory);
        for (int i = 0; i < 10; i++) {
            new Thread(() -> writeToElasticsearch(newsFromMysql)).start();
        }

    }

    private static void writeToElasticsearch(List<News> newsFromMysql) {
        try (RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
                new HttpHost("localhost", 9200, "http")))) {
            for (int i = 0; i < 10; i++) {

                for (News news : newsFromMysql) {
                    IndexRequest request = new IndexRequest("news");
                    Map<String, Object> data = new HashMap<>();
                    data.put("title", news.getTitle());
                    data.put("content", news.getContent());
                    data.put("url", news.getUrl());
                    data.put("createdAt", news.getCreatedAt());
                    data.put("UpdateAt", news.getUpdateAt());

                    request.source(data, XContentType.JSON);
                    IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
                    System.out.println(indexResponse.status().getStatus());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<News> getNewsFromMysql(SqlSessionFactory sqlSessionFactory) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            return sqlSession.selectList("tql.MockMapper.selectNews");
        }
    }
}
