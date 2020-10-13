package org.springcat.carrycat.core.reader;

import org.springcat.carrycat.core.job.JobConf;
import org.springcat.carrycat.core.channel.BufferData;


/**
 * @Description DataMapping
 * @Author springCat
 * @Date 2020/10/12 12:45
 */
public interface DataMappingI{

    boolean init(JobConf jobConf);

    Object execute(BufferData bufferData);
}
