package org.springcat.carrycat.impl.writer.rdbms;

import lombok.Data;
/**
 * @Description DbWriterConf
 * @Author springCat
 * @Date 2020/10/9 16:16
 */
@Data
public class RdbmsWriterConf{

    private String url;

    private String user;

    private String pass;

    private Boolean showSql = true;

    private Boolean formatSql = false;

    private Boolean showParams = true;

    private String sqlLevel ="debug";

    private String importTable;

    private Boolean cleanTableBeforeImport = false;

    private Boolean useMappingConf = true;


}
