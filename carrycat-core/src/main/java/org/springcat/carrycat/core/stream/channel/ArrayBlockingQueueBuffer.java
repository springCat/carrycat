package org.springcat.carrycat.core.stream.channel;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * @Description ArrayBlockingQueueBuffer
 * @Author springCat
 * @Date 2020/9/28 18:53
 */
public class ArrayBlockingQueueBuffer implements BufferI{

    private Log LOGGER = LogFactory.get(ArrayBlockingQueueBuffer.class);

    private BlockingQueue<BufferData> queue;


    public ArrayBlockingQueueBuffer() {
        this.queue  = new ArrayBlockingQueue<BufferData>(10000);;
    }

    @Override
    public void input(BufferData data) {
        queue.offer(data);
    }

    @Override
    public BufferData output() {
        try {
            return queue.poll(20L, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            LOGGER.error(e);
        }
        return null;
    }

    @Override
    public Collection<BufferData> outputBatch(int maxElements) {
        List<BufferData> dataList = new ArrayList<>(maxElements);
        queue.drainTo(dataList,maxElements);
        return dataList;
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty();
    }

    @Override
    public BufferData peek() {
        return queue.peek();
    }

}
