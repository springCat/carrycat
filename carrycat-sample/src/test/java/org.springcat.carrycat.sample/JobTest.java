package org.springcat.carrycat.sample;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Entity;
import cn.hutool.setting.Setting;
import cn.hutool.setting.SettingUtil;
import org.junit.Test;
import org.springcat.carrycat.core.ConfUtil;
import org.springcat.carrycat.core.job.JobExecutor;
import org.springcat.carrycat.core.job.CronTab;
import org.springcat.carrycat.core.job.JobConf;

import java.io.File;
import java.lang.reflect.Type;
import java.sql.Struct;
import java.util.Map;

/**
 * @Description JobTest
 * @Author springCat
 * @Date 2020/10/9 10:09
 */
public class JobTest {

    @Test
    public void testHotRank(){
        JobConf hotRank = ConfUtil.getJobBean("hotRank");
        JobExecutor jobExecutor = new JobExecutor();
        if(jobExecutor.init(hotRank)) {
            jobExecutor.invoke();
        }
    }

    @Test
    public void testCronTab(){
        new CronTab().run();
    }

}