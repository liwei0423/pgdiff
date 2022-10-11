package com.wshoto.pgdiff;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.wshoto.pgdiff.util.FileUtils;

import java.io.IOException;

/**
 * @description:
 * @author: liwei
 * @date: 2022/10/10
 */
public class Main {
    private static final Log log = LogFactory.get();

    public static void main(String[] args) throws IOException {
        if (args == null || args.length == 0) {
            System.out.println("please add param: 448、456、output fileDir");
            return;
        } else {
            log.info("running...");
            long start = System.currentTimeMillis();
            String srcDir = args[0];
            String destDir = args[1];
            String outDir = args[2];
            log.info("copy no exist file...");
            FileUtils.copyNoExistFile(srcDir, destDir, outDir);
            log.info("apg diff...");
            FileUtils.apgdiff(srcDir, destDir, outDir);
            log.info("remove line...");
            FileUtils.removeLine(outDir);
            long end = System.currentTimeMillis();
            log.info("success, use time {}s",(end-start)/1000);
        }
    }
}
