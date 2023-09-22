package com.valley.yueojcodesandbox.docker;

import com.github.dockerjava.api.async.ResultCallback;
import com.github.dockerjava.api.model.Statistics;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;

/**
 * DOcker容器内存收集器
 */
@Slf4j
public class MemoryCollector implements ResultCallback<Statistics> {
    private Long initMemory = 0L;
    private Long maxMemory = 0L;

    /**
     * 返回当前的内存用量
     * @return
     */
    public Long getMemoryUsage() {
        long usage = maxMemory - initMemory;
        log.info(String.format("初始内存：%d, 最大内存：%d", initMemory, maxMemory));
        return usage;
    }

    @Override
    public void onStart(Closeable closeable) {
    }
    @Override
    public void onNext(Statistics statistics) {
        Long memory = statistics.getMemoryStats().getUsage();
        if (memory == null) {
            return;
        }
        if (initMemory == 0) {
            initMemory = memory;
        }
        if (memory > maxMemory) {
            maxMemory = memory;
        }
    }
    @Override
    public void onError(Throwable throwable) {
    }
    @Override
    public void onComplete() {
    }
    @Override
    public void close() {
    }
}
