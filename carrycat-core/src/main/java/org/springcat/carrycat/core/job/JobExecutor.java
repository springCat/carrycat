package org.springcat.carrycat.core.job;

import cn.hutool.core.lang.Validator;
import cn.hutool.cron.task.Task;
import org.springcat.carrycat.core.PluginI;
import org.springcat.carrycat.core.util.CarryCatUtil;

/**
 * @Description JobBuilder
 * @Author springCat
 * @Date 2020/10/12 12:36
 */
public class JobExecutor implements PluginI, Task {

    private JobConf jobConf;

    @Override
    public boolean init(JobConf jobConf) {
        Validator.validateNotNull(jobConf,"job init param jobConf can not null");
        jobConf.validate();
        this.jobConf = jobConf;
        return true;
    }

    @Override
    public void invoke() {
        Job job = CarryCatUtil.newInstance(jobConf.getJobClass());
        if(job.init(jobConf)){
            job.invoke();
        }
    }

    @Override
    public void execute() {
        invoke();
    }
}
