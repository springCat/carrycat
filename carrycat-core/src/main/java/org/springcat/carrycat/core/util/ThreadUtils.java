package org.springcat.carrycat.core.util;

import cn.hutool.core.thread.ThreadUtil;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

/**
 * @Description ThreadPool
 * @Author springCat
 * @Date 2020/9/28 18:32
 */

public class ThreadUtils {

    public static ExecutorService get(String name,int num){
        ThreadFactory threadFactory = ThreadUtil.newNamedThreadFactory(name, false);
        return Executors.newFixedThreadPool(num, threadFactory);
    }

}
