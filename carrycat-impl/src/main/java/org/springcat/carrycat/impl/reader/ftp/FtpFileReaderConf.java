package org.springcat.carrycat.impl.reader.ftp;

import lombok.Data;

/**
 * @Description Conf
 * @Author springCat
 * @Date 2020/9/28 11:19
 */
@Data
public class FtpFileReaderConf{

    private String ftpIp;

    private int ftpPort;

    private String ftpUsername;

    private String ftpPassword;

    private String ftpCharset;

    private String ftpPath;

    private String ftpFilePattern;

    private String localTempPath;

    private String localBackupPath;

    private String ftpFileDelimiter;

}
