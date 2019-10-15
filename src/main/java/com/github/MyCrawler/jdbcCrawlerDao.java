package com.github.MyCrawler;

import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class jdbcCrawlerDao implements CrawlerDao {
    private final String UserName = "root";
    private final String Password = "root";
    Connection connection;

    public jdbcCrawlerDao() {
        try {
            this.connection = DriverManager.getConnection("jdbc:h2:file:E:/Software/My-Crawler/news", UserName, Password);
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }


    public String getNextLinkFromDataBaseAndDeleteIt() throws SQLException {
        String link = lodeLinkFromDataBase("select link from LINK_TO_BE_PROCESSED limit 1");
        if (link != null) {
            upDateDataBase("delete from LINK_TO_BE_PROCESSED where link = ?", link);
            return link;
        }
        return null;
    }

    public boolean isLinkProcessed(String link) throws SQLException {
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement("select link from LINK_TO_BE_PROCESSED where link = ?")) {
            statement.setString(1, link);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return true;
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
        return false;
    }

    public String lodeLinkFromDataBase(String sql) throws SQLException {
        ResultSet resultSet = null;
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                return resultSet.getString(1);
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
        }
        return null;
    }

    public void storeNewsIntoDataBase(String title, String link, String content) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("insert into NEWS (TITLE, URL, CONTENT, CREATED_AT, UPDATE_AT) values ( ?,?,?,now(),now() ) ")) {
            statement.setString(1, title);
            statement.setString(2, link);
            statement.setString(3, content);
            statement.executeUpdate();
        }
    }

    public void upDateDataBase(String sql, String link) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, link);
            statement.executeUpdate();
        }
    }
}
