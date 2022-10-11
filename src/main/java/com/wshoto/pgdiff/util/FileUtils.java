package com.wshoto.pgdiff.util;

import cn.hutool.core.io.FileUtil;
import cz.startnet.utils.pgdiff.Main;
import cz.startnet.utils.pgdiff.PgDiff;
import cz.startnet.utils.pgdiff.PgDiffArguments;

import java.io.*;
import java.util.List;

/**
 * @description:
 * @author: liwei
 * @date: 2022/10/10
 */
public class FileUtils {

    public static void copyNoExistFile(String srcDir, String destDir, String outDir) {
        List<String> srcFileList = FileUtil.listFileNames(srcDir);
        List<String> destFileList = FileUtil.listFileNames(destDir);
        destFileList.forEach(fileName -> {
            if (!srcFileList.contains(fileName)) {
                String outFile = outDir + File.separator + fileName;
                if (FileUtil.exist(outFile)) {
                    FileUtil.del(outFile);
                }
                FileUtil.copyFile(destDir + File.separator + fileName, outDir + File.separator + fileName);
            }
        });
    }

    public static void apgdiff(String srcDir, String destDir, String outDir) throws IOException {
        List<String> srcFileList = FileUtil.listFileNames(srcDir);
        List<String> destFileList = FileUtil.listFileNames(destDir);
        for (String fileName : destFileList) {
            if (srcFileList.contains(fileName)) {
                String[] args = new String[3];
                args[0] = "--ignore-start-with";
                args[1] = srcDir + File.separator + fileName;
                args[2] = destDir + File.separator + fileName;
                final PrintWriter writer = new PrintWriter(System.out, true);
                final PgDiffArguments arguments = new PgDiffArguments();

                if (arguments.parse(writer, args)) {
                    @SuppressWarnings("UseOfSystemOutOrSystemErr") final PrintWriter encodedWriter = new PrintWriter(
                            new FileWriter(outDir + File.separator + fileName, false));
                    PgDiff.createDiff(encodedWriter, arguments);
                    encodedWriter.close();
                }

                writer.close();
                try {
                    Main.main(args);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void removeLine(String outDir) {
        List<File> fileList = FileUtil.loopFiles(outDir);
        boolean flag = false;
        for (File file : fileList) {
            File tempFile = FileUtil.createTempFile();
            List<String> lines = FileUtil.readUtf8Lines(file);
            for(String line :lines){
                if(flag){
                    if(line.endsWith(";")){
                        flag = false;
                        continue;
                    }
                }
                if(line.startsWith("DROP TABLE")||line.startsWith("DROP EXTENSION")||line.startsWith("DROP SEQUENCE")||line.startsWith("DROP FUNCTION")){
                    if(!line.endsWith(";")){
                        //过滤标记
                        flag = true;
                    }
                    continue;
                }else{
                    FileUtil.appendUtf8String(line+"\r\n",tempFile);
                }
            }
            FileUtil.copy(tempFile,file,true);
            FileUtil.del(tempFile);
        }
    }
}
