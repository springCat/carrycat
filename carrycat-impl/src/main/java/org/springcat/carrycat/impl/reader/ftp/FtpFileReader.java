package org.springcat.carrycat.impl.reader.ftp;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.LineHandler;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import org.springcat.carrycat.core.stream.channel.BufferData;
import org.springcat.carrycat.core.job.JobConf;
import org.springcat.carrycat.core.stream.AbstractReader;
import org.springcat.carrycat.core.stream.channel.BufferI;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description Reader
 * @Author springCat
 * @Date 2020/9/28 10:12
 */
public class FtpFileReader extends AbstractReader {

    private Log LOGGER =  LogFactory.get(FtpFileReader.class);


    private FtpFileReaderConf ftpFileReaderConf;

    public FtpFileReader(BufferI buffer) {
        super(buffer);
    }

    private List<File> targetFiles;

    @Override
    public boolean init(JobConf jobConf) {
        this.ftpFileReaderConf = jobConf.getGroupConfBean(FtpFileReaderConf.class);
        this.targetFiles = fetchFtpFile();
        return !this.targetFiles.isEmpty();
    }

    @Override
    public void invoke() {
        for (File targetFile : targetFiles) {
            LOGGER.info("start parse file:{}", targetFile.getName());
            FileUtil.readLines(targetFile, Charset.forName(ftpFileReaderConf.getFtpCharset()), (LineHandler) line -> getBuffer().input(new BufferData(line)));
            LOGGER.info("end parse file:{}", targetFile.getName());
        }
        FtpFileUtil.moveBackup(targetFiles, ftpFileReaderConf);
        LOGGER.info("backup file :{}",targetFiles);
        LOGGER.info("reader end");
        super.invoke();
    }

    private List<File> fetchFtpFile() {
        List<File> targetFiles = new ArrayList<>();
        LOGGER.info("init ftp client");
        Ftp ftp = FtpFileUtil.initFtp(ftpFileReaderConf);

        List<String> targetFilenames = FtpFileUtil.scanTargetFiles(ftp, ftpFileReaderConf);
        LOGGER.info("remote targetFilenames:{}",targetFilenames);

        targetFiles = FtpFileUtil.downTargetFiles(ftp, targetFilenames,ftpFileReaderConf);
        LOGGER.info("download files success:{}",targetFilenames);

        FtpFileUtil.deleteFtpFile(ftp,targetFilenames,ftpFileReaderConf);
        LOGGER.info("deleteFtpFile :{}",targetFilenames);
        return targetFiles;
    }
}
