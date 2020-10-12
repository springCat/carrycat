package org.springcat.carrycat.core.stream.channel;

/**
 * @Description RawData
 * @Author springCat
 * @Date 2020/9/28 10:19
 */
public class BufferData {

    public final static BufferData END_FLAG = new BufferData(null);

    private Object data;

    public BufferData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

}
