package org.springcat.carrycat.sample;

import cn.hutool.db.Entity;
import cn.hutool.script.ScriptUtil;
import cn.hutool.setting.Setting;
import cn.hutool.setting.SettingUtil;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.junit.Test;
import org.springcat.carrycat.core.ConfUtil;
import org.springcat.carrycat.core.JobExecutor;
import org.springcat.carrycat.core.job.CronTab;
import org.springcat.carrycat.core.job.JobConf;

import javax.script.Bindings;
import javax.script.SimpleBindings;
import java.io.File;
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


    @Test
    public void testJs(){

        Setting setting = SettingUtil.get("job" + File.separator + "hotRank");

        Entity entity = new Entity();
        Map<String, String> mapping = setting.getMap("mapping");
        entity.putAll(mapping);
        System.out.println(entity);

    }

}