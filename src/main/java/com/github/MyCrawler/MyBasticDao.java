package com.github.MyCrawler;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MyBasticDao implements CrawlerDao {
    private SqlSessionFactory sqlSessionFactory;

    public MyBasticDao() {
        try {
            String resource = "db/mybatis/config.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String getNextLinkFromDataBaseAndDeleteIt() {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            String link = session.selectOne("tql.MyMapper.selectNextAvailableLink");
            if (link != null) {
                session.delete("tql.MyMapper.deleteLink", link);
            }
            return link;
        }
    }

    @Override
    public boolean isLinkProcessed(String link) {
        try (SqlSession session = sqlSessionFactory.openSession()) {
            int count = session.selectOne("tql.MyMapper.countLink", link);
            return count != 0;
        }
    }


    @Override
    public void storeNewsIntoDataBase(String title, String link, String content) {
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            session.insert("tql.MyMapper.insertNews", new News(title, link, content));
        }

    }


    @Override
    public void insertProcessedLink(String link) {
        String tableName = "LINK_ALREADY_PROCESSED";
        inserLink(link, tableName);
    }


    @Override
    public void insertToBeProcessedLink(String href) {
        String tableName = "LINK_TO_BE_PROCESSED";
        inserLink(href, tableName);
    }

    private void inserLink(String link, String tableName) {
        Map<String, Object> param = new HashMap<>();
        param.put("tableName", tableName);
        param.put("link", link);
        try (SqlSession session = sqlSessionFactory.openSession(true)) {
            session.insert("tql.MyMapper.insertLink", param);
        }
    }
}
