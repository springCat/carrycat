package org.springcat.carrycat.core.job;

import cn.hutool.core.lang.Assert;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.setting.Setting;
import lombok.Data;
import org.springcat.carrycat.core.ConfUtil;
import org.springcat.carrycat.core.ConfI;
import org.springcat.carrycat.core.util.CarryCatUtil;

/**
 * @Description IConfig
 * @Author springCat
 * @Date 2020/4/30 11:53
 */
@Data
public class JobConf implements ConfI {

    private Log LOGGER =  LogFactory.get(JobConf.class);

    private String jobName;

    private boolean jobSwitchOn;

    private String jobCronExpression;

    private String jobClass;

    private String writerClass;

    private String readerClass;

    private String bufferClass;

    public void validate(){
        Assert.notBlank(jobName);
        Assert.notBlank(jobCronExpression);
        Assert.notBlank(jobClass);
        Assert.notBlank(writerClass);
        Assert.notBlank(readerClass);
        Assert.notBlank(bufferClass);
    }

    public Setting getJobConf(){
        return ConfUtil.getConf(jobName);
    }

    public <T> T getGroupConfBean(Class<T> pluginConfBean){
        String groupName = CarryCatUtil.getName(pluginConfBean);
        return getJobConf().getSetting(groupName).toBean(pluginConfBean);
    }
}