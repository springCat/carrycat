package org.springcat.carrycat.core;

import cn.hutool.core.util.StrUtil;
import org.springcat.carrycat.core.job.JobConf;

/**
 * @Description IPlugin
 * @Author springCat
 * @Date 2020/9/28 10:11
 */
public interface PluginI {

    boolean init(JobConf jobConf);

    void invoke();
}
