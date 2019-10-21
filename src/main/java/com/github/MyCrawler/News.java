package com.github.MyCrawler;

import java.time.Instant;

public class News {
    private Integer id;
    private String title;
    private String content;
    private String url;
    private Instant createdAt;
    private Instant updateAt;

    public News() {
    }

    public News(News old) {
        this.id = old.id;
        this.title = old.title;
        this.content = old.content;
        this.url = old.url;
        this.createdAt = old.createdAt;
        this.updateAt = old.updateAt;
    }

    public News(String title, String url, String content) {
        this.title = title;
        this.content = content;
        this.url = url;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Instant updateAt) {
        this.updateAt = updateAt;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


}
