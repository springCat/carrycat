package org.springcat.carrycat.core.writer;

import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import lombok.Data;
import lombok.SneakyThrows;
import org.springcat.carrycat.core.PluginI;
import org.springcat.carrycat.core.channel.BufferData;
import org.springcat.carrycat.core.channel.BufferI;
import org.springcat.carrycat.core.job.JobConf;
import org.springcat.carrycat.core.reader.DataMappingI;
import org.springcat.carrycat.core.util.CarryCatUtil;
import org.springcat.carrycat.core.util.ThreadUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description AbstractWriter
 * @Author springCat
 * @Date 2020/10/9 16:02
 */
@Data
public abstract class AbstractWriter implements PluginI {

    private Log LOGGER =  LogFactory.get(AbstractWriter.class);

    private BufferI buffer;

    private ExecutorService writerPool;

    private CountDownLatch countDownLatch;

    private AtomicInteger CURRENT_WRITER_NUM;

    /**
     * 默认无转换
     */
    private DataMappingI dataMapping;

    private JobConf jobConf;

    private WriterConf writerConf;

    private AtomicBoolean writerClose = new AtomicBoolean(false);


    public AbstractWriter(BufferI buffer) {
       this.buffer = buffer;

    }

    @SneakyThrows
    @Override
    public boolean init(JobConf jobConf){
        this.jobConf = jobConf;
        this.writerConf = jobConf.getGroupConfBean(WriterConf.class);

        this.dataMapping = CarryCatUtil.newInstance(writerConf.getMappingClass());
        if(!this.dataMapping.init(jobConf)){
            LOGGER.error("AbstractWriter init dataMapping error:{}",jobConf);
            return false;
        }

        this.countDownLatch = new CountDownLatch(writerConf.getWriterNum());
        this.CURRENT_WRITER_NUM = new AtomicInteger(0);
        this.writerPool = ThreadUtils.get(jobConf.getJobName()+"Pool",writerConf.getWriterPoolSize());
        LOGGER.info("writer start");
        return true;
    }

    @Override
    public void invoke(){
        for (int i = 0; i < writerConf.getWriterNum(); i++) {
            writerPool.submit(new WriteWorker(countDownLatch,getBuffer(),writerConf.getWriterBatchSize()));
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            LOGGER.error(e);
        }
        LOGGER.info("all WriteWorker finish");

        writerPool.shutdown();
        LOGGER.info("writer finish");
    }

    public abstract void batchInsert(Collection rawDataCollection);

    class WriteWorker implements Runnable {

        private CountDownLatch countDownLatch;

        private int batchSize;

        private BufferI buffer;

        public WriteWorker(CountDownLatch countDownLatch,BufferI buffer,int batchSize){
            this.batchSize = batchSize;
            this.countDownLatch = countDownLatch;
            this.buffer = buffer;
        }

        @Override
        public void run() {
            int currentWriterNum = CURRENT_WRITER_NUM.incrementAndGet();
            LOGGER.info("one writer start,current writer num:{}",currentWriterNum);
            try {
                while (true){
                    Collection<BufferData> inputData = buffer.outputBatch(batchSize);
                    if(inputData.isEmpty()){
                        if(writerClose.get()){
                            return;
                        }
                        ThreadUtil.sleep(20);
                        continue;
                    }

                    List list = new ArrayList(inputData.size());

                    transform:
                    for (BufferData bufferData : inputData) {
                        //读取到结束信息,设置结束信息,把剩余的数据入库就结束
                        if(bufferData == BufferData.END_FLAG){
                            writerClose.set(true);
                            break transform;
                        }
                        list.add(dataMapping.execute(bufferData));
                    }
                    batchInsert(list);
                }
            }finally {
                currentWriterNum = CURRENT_WRITER_NUM.decrementAndGet();
                LOGGER.info("one writer finish,current writer num:{}",currentWriterNum);
                countDownLatch.countDown();
            }
        }
    }
}
