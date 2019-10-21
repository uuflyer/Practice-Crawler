package com.github.MyCrawler;


import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {
    public static void main(String[] args) {
        MyBatisDao dao = new MyBatisDao();


        ExecutorService threadPool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            threadPool.execute(new MyCrawler(dao));
        }
    }
}
