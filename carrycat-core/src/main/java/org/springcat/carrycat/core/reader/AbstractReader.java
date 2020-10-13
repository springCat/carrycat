package org.springcat.carrycat.core.reader;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import lombok.Data;
import org.springcat.carrycat.core.PluginI;
import org.springcat.carrycat.core.channel.BufferData;
import org.springcat.carrycat.core.channel.BufferI;

/**
 * @Description ReaderI
 * @Author springCat
 * @Date 2020/10/9 15:36
 */
@Data
public abstract class AbstractReader implements PluginI {

    private Log LOGGER =  LogFactory.get();

    private BufferI buffer;

    public AbstractReader(BufferI buffer) {
        this.buffer = buffer;
    }

    @Override
    public void invoke() {
        getBuffer().input(BufferData.END_FLAG);
        LOGGER.info("close buffer input");
    }
}
