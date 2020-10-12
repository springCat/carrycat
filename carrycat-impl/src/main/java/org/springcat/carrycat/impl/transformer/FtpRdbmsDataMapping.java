package org.springcat.carrycat.impl.transformer;

import cn.hutool.core.date.DateUtil;
import cn.hutool.db.Entity;
import lombok.Data;
import org.springcat.carrycat.core.job.JobConf;
import org.springcat.carrycat.core.stream.DataMappingI;
import org.springcat.carrycat.core.stream.channel.BufferData;
import org.springcat.carrycat.impl.reader.ftp.FtpFileReaderConf;
import org.springcat.carrycat.impl.writer.rdbms.RdbmsWriterConf;

/**
 * @Description DataMapping
 * @Author springCat
 * @Date 2020/10/12 12:44
 */
@Data
public abstract class FtpRdbmsDataMapping implements DataMappingI {

    private JobConf jobConf;

    private FtpFileReaderConf ftpFileReaderConf;

    private RdbmsWriterConf rdbmsWriterConf;

    @Override
    public boolean init(JobConf jobConf) {
        this.jobConf = jobConf;
        this.ftpFileReaderConf = jobConf.getGroupConfBean(FtpFileReaderConf.class);
        this.rdbmsWriterConf = jobConf.getGroupConfBean(RdbmsWriterConf.class);;
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


    public abstract void mapping(String[] rawData,Entity entity);


}
