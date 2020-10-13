package org.springcat.carrycat.core.writer;

import cn.hutool.core.lang.Assert;
import lombok.Data;
import org.springcat.carrycat.core.ConfI;

/**
 * @Description WriterConf
 * @Author springCat
 * @Date 2020/10/12 12:13
 */
@Data
public class WriterConf implements ConfI {

    private String mappingClass;

    private int writerNum = 1;

    private int writerBatchSize = 5;

    private int writerPoolSize = 1;

    @Override
    public void validate() {
        Assert.notBlank(mappingClass);
        Assert.isTrue(writerNum > 0);
        Assert.isTrue(writerBatchSize > 0);
        Assert.isTrue(writerPoolSize > 0);
    }
}
