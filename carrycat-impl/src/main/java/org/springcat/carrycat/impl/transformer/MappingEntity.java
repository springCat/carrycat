package org.springcat.carrycat.impl.transformer;

import cn.hutool.db.Entity;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import lombok.Data;
import org.springcat.carrycat.core.job.JobConf;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Description MappingLine
 * @Author springCat
 * @Date 2020/10/13 12:58
 */
@Data
public class MappingEntity {

    private final static Log LOGGER =  LogFactory.get(MappingEntity.class);

    private List<MappingColumn> mappingColumns = new ArrayList<>();

    public boolean init(JobConf jobConf){
        Set<Map.Entry<String, String>> mappingLines = jobConf.getJobConf().entrySet("mapping");
        for (Map.Entry<String, String> mappingLine : mappingLines) {

            MappingColumn mappingColumn = new MappingColumn();
            if(!mappingColumn.init(mappingLine.getKey(),mappingLine.getValue())){
                LOGGER.error("MappingEntity init error columnName:{},tansformer:{}",mappingLine.getKey(),mappingLine.getValue());
                return false;
            }
            mappingColumns.add(mappingColumn);
        }
        return true;
    }

    public void mapping(String[] rawData,Entity entity){
        for (MappingColumn mappingColumn : mappingColumns) {
            Object value = mappingColumn.getValue(rawData);
            entity.put(mappingColumn.getName(),value);
        }
    }

}
