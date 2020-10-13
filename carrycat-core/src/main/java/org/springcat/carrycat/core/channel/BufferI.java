package org.springcat.carrycat.core.channel;

import java.util.Collection;

/**
 * @Description Channel
 * @Author springCat
 * @Date 2020/9/28 10:14
 */
public interface BufferI{

    void input(BufferData data);

    BufferData output();

    Collection<BufferData> outputBatch(int maxElements);

    boolean isEmpty();

    BufferData peek();
}
