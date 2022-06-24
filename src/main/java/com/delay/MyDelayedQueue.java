package com.delay;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MyDelayedQueue {
    public static void main(String[] args) throws InterruptedException {
        test();
    }
    private static void test() throws InterruptedException {
        DelayQueue<DelayedTask> queue = new DelayQueue<>();
        producer(queue);
        log.info("start take task from queue");
        //循环从队列中取数据
        while(!queue.isEmpty()) {
            //只有到期的数据才能取的出来，否则就阻塞等待
            log.info(String.valueOf(queue.take()));
        }

    }
    private static void producer(DelayQueue<DelayedTask> queue) {
        for (int i = 0; i < 10; i++) {
            queue.put(new DelayedTask((10 - i) * 1000, "delayed taask"+i));
        }
    }

    public static void info(Object object) {
        System.out.println(new Date() + " [" + Thread.currentThread().getName() + "] " + object);
    }

    private static class DelayedTask implements Delayed {
        private long delay;
        private long expire;
        private String msg;

        public DelayedTask(long delay, String msg) {
            this.delay = delay;
            this.msg = msg;
            this.expire = System.currentTimeMillis() + delay;
        }

        /**
         * 用于延迟队列内部进行排序，将最先到期的放在队首，保证take出来的是到期的那个
         */
        @Override
        public int compareTo(Delayed o) {
            return (int)(this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
        }

        /**
         * 指定到期时间计算规则
         */
        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(this.expire - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
        }

        @Override
        public String toString() {
            return "DelayedTask [delay=" + delay + ", expire=" + expire + ", msg=" + msg + "]";
        }
    }
}
