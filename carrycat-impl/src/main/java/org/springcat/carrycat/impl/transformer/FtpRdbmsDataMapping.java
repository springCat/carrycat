package org.springcat.carrycat.impl.transformer;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.StrUtil;
import cn.hutool.db.Entity;
import lombok.Data;
import org.springcat.carrycat.core.job.JobConf;
import org.springcat.carrycat.core.reader.DataMappingI;
import org.springcat.carrycat.core.channel.BufferData;
import org.springcat.carrycat.impl.reader.ftp.FtpFileReaderConf;
import org.springcat.carrycat.impl.writer.rdbms.RdbmsWriterConf;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * @Description DataMapping
 * @Author springCat
 * @Date 2020/10/12 12:44
 */
@Data
public class FtpRdbmsDataMapping implements DataMappingI {

    private JobConf jobConf;

    private FtpFileReaderConf ftpFileReaderConf;

    private RdbmsWriterConf rdbmsWriterConf;

    private MappingEntity mappingEntity;

    @Override
    public boolean init(JobConf jobConf) {
        this.jobConf = jobConf;
        this.ftpFileReaderConf = jobConf.getGroupConfBean(FtpFileReaderConf.class);
        this.rdbmsWriterConf = jobConf.getGroupConfBean(RdbmsWriterConf.class);
        
        if(this.rdbmsWriterConf.getUseMappingConf()) {
            this.mappingEntity = new MappingEntity();
            return this.mappingEntity.init(jobConf);
        }
        return true;
    }

    @Override
    public Object execute(BufferData bufferData) {
        String line = (String)bufferData.getData();
        String[] rawData = line.split(ftpFileReaderConf.getFtpFileDelimiter());
        Entity entity = Entity.create(rdbmsWriterConf.getImportTable());
        mapping(rawData, entity);
        return entity;
    }

    public void mapping(String[] rawData,Entity entity){
        mappingEntity.mapping(rawData,entity);
    }
}
