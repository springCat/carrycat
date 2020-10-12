package org.springcat.carrycat.core.job;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import lombok.Data;
import lombok.SneakyThrows;
import org.springcat.carrycat.core.PluginI;
import org.springcat.carrycat.core.stream.channel.BufferData;
import org.springcat.carrycat.core.stream.channel.BufferI;
import org.springcat.carrycat.core.stream.AbstractReader;
import org.springcat.carrycat.core.stream.AbstractWriter;
import org.springcat.carrycat.core.util.CarryCatUtil;

/**
 * @Description JobA
 * @Author springCat
 * @Date 2020/9/28 10:15
 *
 * version:1
 *
 *               1                   N               N           N              N
 *      ftpfetch and download ->  reader + transformerBuffer -> transform -> writerBuffer ->   writeLine
 * 	                       file      rawData       Columns  List<Column>
 *
 * version:2
 *
 *     1 简化模型,降低复杂度,整合transform和 writer,如需提升性能只需要开多个job就好
 *     2 简化后, 1个job对应1个reader,1个buffer,N个writer+>transformer
 *     3 暂时实现只有1个buffer,下个版本实现N个buffer,以及路由策略
 *
 *               1                   N           1              N
 *       ftpfetch and download ->  reader  -> buffer -> transformer + writeLine
 *  	                       file            rawData                List<Column>
 *
 *
 */
@Data
public class Job implements PluginI {

    private Log LOGGER =  LogFactory.get(Job.class);

    private JobConf jobConf;

    private AbstractReader reader;

    private AbstractWriter writer;

    private BufferI buffer;

    @SneakyThrows
    @Override
    public boolean init(JobConf jobConf) {

        if(!jobConf.isJobSwitchOn()){
            LOGGER.info("Job switch off name:{}}",jobConf.getJobName());
            return false;
        }

        this.jobConf = jobConf;

        this.buffer = CarryCatUtil.newInstance(jobConf.getBufferClass());
        this.reader = CarryCatUtil.newInstance(jobConf.getReaderClass(),buffer);
        this.writer = CarryCatUtil.newInstance(jobConf.getWriterClass(),buffer);

        if(!reader.init(jobConf)){
            getBuffer().input(BufferData.END_FLAG);
            LOGGER.info("Job init reader error:{}",this.jobConf.getJobName());
            return false;
        }

        if(!writer.init(jobConf)){
            LOGGER.info("Job init writer error:{}",this.jobConf.getJobName());
            return false;
        }
        return true;
    }

    @Override
    public void invoke() {
        reader.invoke();
        writer.invoke();
        LOGGER.info("Job success execute name:{}}",jobConf.getJobName());
    }

}
