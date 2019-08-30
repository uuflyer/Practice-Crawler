# 多线程练习：解决一个类的线程安全问题

我们有一个在多线程环境下不安全的[`Counter`](https://github.com/hcsp/fix-threadsafe-issue/blob/master/src/main/com/github/hcsp/multithread/Counter.java)类，请将它改写为线程安全的。

我们鼓励你使用多种方法：

- `synchronized`方法
- `java.util.concurrent.locks.Lock`
- `AtomicInteger`

祝你好运！

-----
注意！我们只允许你修改以下文件，对其他文件的修改会被拒绝：
- [src/main/com/github/hcsp/multithread/Counter.java](https://github.com/hcsp/fix-threadsafe-issue/blob/master/src/main/com/github/hcsp/multithread/Counter.java)
-----


完成题目有困难？不妨来看看[写代码啦的相应课程](https://xiedaimala.com/tasks/661cd7ab-7fea-47d0-8e11-555d6fca751d)吧！

回到[写代码啦的题目](https://xiedaimala.com/tasks/661cd7ab-7fea-47d0-8e11-555d6fca751d/quizzes/6c87ef57-7f06-4af2-9112-86dd27ff099d)，继续挑战！ 
