package com.github.MyCrawler;

import java.sql.SQLException;

public interface CrawlerDao {

    String getNextLinkFromDataBaseAndDeleteIt() throws SQLException;

    boolean isLinkProcessed(String link) throws SQLException;

    String lodeLinkFromDataBase(String sql) throws SQLException;

    void storeNewsIntoDataBase(String title, String link, String content) throws SQLException;

    void upDateDataBase(String sql, String link) throws SQLException;
}
