package org.springcat.carrycat.core.stream;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import lombok.Data;
import org.springcat.carrycat.core.stream.channel.BufferData;
import org.springcat.carrycat.core.stream.channel.BufferI;

/**
 * @Description ReaderI
 * @Author springCat
 * @Date 2020/10/9 15:36
 */
@Data
public abstract class AbstractReader extends AbstractStream {

    private Log LOGGER =  LogFactory.get();

    public AbstractReader(BufferI buffer) {
        super(buffer);
    }

    @Override
    public void invoke() {
        getBuffer().input(BufferData.END_FLAG);
        LOGGER.info("close buffer input");
    }
}
