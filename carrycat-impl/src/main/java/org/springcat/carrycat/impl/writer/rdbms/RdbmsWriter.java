package org.springcat.carrycat.impl.writer.rdbms;

import cn.hutool.db.Db;
import cn.hutool.db.DbUtil;
import cn.hutool.db.Entity;
import cn.hutool.db.ds.DSFactory;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import cn.hutool.setting.Setting;
import org.springcat.carrycat.core.job.JobConf;
import org.springcat.carrycat.core.stream.AbstractWriter;
import org.springcat.carrycat.core.stream.channel.BufferData;
import org.springcat.carrycat.core.stream.channel.BufferI;

import java.sql.SQLException;
import java.util.Collection;

/**
 * @Description WriterA
 * @Author springCat
 * @Date 2020/9/28 10:13
 */
public class RdbmsWriter extends AbstractWriter {

    private Log LOGGER =  LogFactory.get(RdbmsWriter.class);

    private Db db;

    private RdbmsWriterConf rdbmsWriterConf;

    public RdbmsWriter(BufferI buffer) {
        super(buffer);
    }

    public boolean init(JobConf jobConf){
        super.init(jobConf);
        this.rdbmsWriterConf = jobConf.getGroupConfBean(RdbmsWriterConf.class);
        this.initDb(rdbmsWriterConf);
        return true;
    }

    public Db initDb(RdbmsWriterConf rdbmsWriterConf){
        Setting setting = new Setting();
        setting.put("url",rdbmsWriterConf.getUrl());
        setting.put("pass",rdbmsWriterConf.getPass());
        setting.put("user",rdbmsWriterConf.getUser());
        setting.put("showSql",rdbmsWriterConf.getShowSql().toString());
        setting.put("formatSql",rdbmsWriterConf.getFormatSql().toString());
        setting.put("showParams",rdbmsWriterConf.getShowParams().toString());
        setting.put("sqlLevel",rdbmsWriterConf.getSqlLevel());
        return DbUtil.use(DSFactory.create(setting).getDataSource());
    }

    public void cleanTableBeforeImport(){
        if(rdbmsWriterConf.getCleanTableBeforeImport()){
            try {
                //做全表清理
                db.del(Entity.create(rdbmsWriterConf.getImportTable()).set("1","1"));
            } catch (SQLException e) {
                LOGGER.error(e);
            }
        }
    }

    @Override
    public void invoke(){
       cleanTableBeforeImport();
       super.invoke();
    }

    @Override
    public void batchInsert(Collection list) {
        try {
            db.insert(list);
        } catch (Exception e) {
            LOGGER.error(e);
        }
    }

}
