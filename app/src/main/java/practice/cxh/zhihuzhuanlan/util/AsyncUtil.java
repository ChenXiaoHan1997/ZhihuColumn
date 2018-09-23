package practice.cxh.zhihuzhuanlan.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 2018/9/1 0001.
 */

public class AsyncUtil {

    private static final int CORE_SIZE = 4;

    private static ExecutorService sThreadPool = Executors.newFixedThreadPool(CORE_SIZE);

    public static void executeAsync(Runnable runnable) {
        sThreadPool.execute(runnable);
    }
}
