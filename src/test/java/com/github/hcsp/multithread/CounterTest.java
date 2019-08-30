package com.github.hcsp.multithread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;

public class CounterTest {
    @RepeatedTest(10)
    public void test() throws ExecutionException, InterruptedException {
        ExecutorService threadPool = Executors.newFixedThreadPool(100);
        Counter counter = new Counter();

        List<Future<Void>> futures = new ArrayList<>();

        for (int i = 0; i < 100; ++i) {
            futures.add(
                    threadPool.submit(
                            () -> {
                                safeSleep();
                                counter.addAndGet(2);
                                counter.minusAndGet(2);
                                return null;
                            }));
        }

        for (Future future : futures) {
            future.get();
        }

        Assertions.assertEquals(0, counter.getValue());
    }

    private void safeSleep() {
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
