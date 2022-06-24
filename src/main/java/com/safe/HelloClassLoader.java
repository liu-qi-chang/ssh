package com.safe;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HelloClassLoader
{
    public static void main( String[] args ) throws Exception
    {
        // 示例
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(4);
        executor.schedule(() -> System.out.println("延迟10秒执行"), 10, TimeUnit.SECONDS);
    }
}
