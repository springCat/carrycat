package org.springcat.carrycat.impl.reader.ftp;

import cn.hutool.core.io.FileUtil;
import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.ftp.FtpMode;
import lombok.experimental.UtilityClass;
import org.apache.commons.net.ftp.FTPFile;
import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description FtpFileUtil
 * @Author springCat
 * @Date 2020/9/28 15:16
 */
@UtilityClass
public class FtpFileUtil {

    public Ftp initFtp(FtpFileReaderConf conf){
        return new Ftp(conf.getFtpIp(),conf.getFtpPort(),conf.getFtpUsername(),conf.getFtpPassword(), Charset.forName(conf.getFtpCharset()), FtpMode.Passive);
    }

    public List<String> scanTargetFiles(Ftp ftp, FtpFileReaderConf conf){
        List<String> targetFiles = new ArrayList<String>();
        FTPFile[] ftpFiles = ftp.lsFiles(conf.getFtpPath());
        for (FTPFile ftpFile : ftpFiles) {
            //忽略目录
            if(ftpFile.isDirectory()){
                continue;
            }
            String ftpFileName = ftpFile.getName();
            if(ftpFileName.matches(conf.getFtpFilePattern())){
                targetFiles.add(ftpFileName);
            }
        }
        return targetFiles;
    }

    public List<File> downTargetFiles(Ftp ftp, List<String> targetFiles, FtpFileReaderConf conf){
        List<File> files = new ArrayList<File>();
        for (String targetFilename : targetFiles) {
            File file = FileUtil.newFile(conf.getLocalTempPath() + File.separator + targetFilename);
            if(file.exists()){
                file.delete();
            }
            ftp.download(conf.getFtpPath(),targetFilename,file);
            files.add(file);
        }
        return files;
    }

    public void deleteFtpFile(Ftp ftp, List<String> targetFiles, FtpFileReaderConf conf){
        for (String targetFile : targetFiles) {
            ftp.delFile(conf.getFtpPath() + "/" + targetFile);
        }
    }

    public void moveBackup(List<File> tempFiles,FtpFileReaderConf conf){
        for (File tempFile : tempFiles) {
            FileUtil.move(tempFile,FileUtil.file(conf.getLocalBackupPath() + File.separator + tempFile.getName()+"."+ System.currentTimeMillis()),true);
        }
    }

}
