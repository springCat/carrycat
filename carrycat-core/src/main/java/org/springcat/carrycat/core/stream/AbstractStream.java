package org.springcat.carrycat.core.stream;

import lombok.Data;
import org.springcat.carrycat.core.PluginI;
import org.springcat.carrycat.core.stream.channel.BufferI;

/**
 * @Description AbstractStream
 * @Author springCat
 * @Date 2020/10/12 10:57
 */
@Data
public abstract class AbstractStream implements PluginI {

    private BufferI buffer;

    public AbstractStream(BufferI buffer) {
        this.buffer = buffer;
    }
}
