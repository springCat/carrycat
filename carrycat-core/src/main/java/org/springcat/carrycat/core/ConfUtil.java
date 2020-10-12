package org.springcat.carrycat.core;

import cn.hutool.setting.Setting;
import cn.hutool.setting.SettingUtil;
import lombok.Getter;
import lombok.experimental.UtilityClass;
import org.springcat.carrycat.core.job.JobConf;

import java.io.File;


/**
 * @Description SysConfE
 * @Author springCat
 * @Date 2020/10/10 10:59
 */
@UtilityClass
public class ConfUtil {

    public static final String DEFAULT_JOB_PATH = "carryCat.setting";

    private static String jobConfPath = SettingUtil.get(DEFAULT_JOB_PATH).get("jobConfPath");

    public String getJobConfPath() {
        return jobConfPath;
    }

    public Setting getConf(String jobName){
        return SettingUtil.get(jobConfPath + File.separator + jobName);
    }

    public JobConf getJobBean(String jobName){
        Setting conf = getConf(jobName);
        if(conf != null) {
            JobConf jobConf =conf.toBean(JobConf.class);
            jobConf.setJobName(jobName);
            return jobConf;
        }
        return null;
    }
}
