package com.github.MyCrawler;

import java.sql.SQLException;

public interface CrawlerDao {

    String getNextLinkFromDataBaseAndDeleteIt() throws SQLException;

    boolean isLinkProcessed(String link) throws SQLException;

    void storeNewsIntoDataBase(String title, String link, String content) throws SQLException;

    void insertProcessedLink(String link) throws SQLException;

    void insertToBeProcessedLink(String href) throws SQLException;
}
