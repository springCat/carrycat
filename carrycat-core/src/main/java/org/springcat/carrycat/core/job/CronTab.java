package org.springcat.carrycat.core.job;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.thread.ThreadUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springcat.carrycat.core.ConfUtil;
import org.springcat.carrycat.core.JobExecutor;
import java.io.File;

/**
 * @Description crontab
 * @Author springCat
 * @Date 2020/10/10 15:10
 */
public class CronTab extends Thread{

    private Log LOGGER =  LogFactory.get(CronTab.class);

    @Override
    public void run() {

        CronUtil.setMatchSecond(true);
        File[] jobConfs = FileUtil.ls(ConfUtil.getJobConfPath());

        for (File jobConf : jobConfs) {

            if (jobConf.isDirectory()) {
                continue;
            }

            JobConf jobBean = ConfUtil.getJobBean(jobConf.getName());
            if (jobBean == null) {
                continue;
            }

            try {
                jobBean.validate();
                JobExecutor jobExecutor = new JobExecutor();
                if(jobExecutor.init(jobBean)){
                    CronUtil.schedule(jobBean.getJobCronExpression(), jobExecutor);
                }
            }catch (Exception e){
                LOGGER.error("job schedule error conf:{},error:{}",jobBean,e);
            }
        }
        CronUtil.start();
        while (true){}
    }

}
