package com.wshoto.pgdiff;

import cn.hutool.core.io.FileUtil;
import com.wshoto.pgdiff.util.FileUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @description:
 * @author: liwei
 * @date: 2022/10/10
 */
public class Main {

    public static void main(String[] args) throws IOException {
        if (args == null || args.length == 0) {
            System.out.println("please add param: 448、456、output fileDir");
            return;
        } else {
            String srcDir = args[0];
            String destDir = args[1];
            String outDir = args[2];
            FileUtils.copyNoExistFile(srcDir, destDir, outDir);
            FileUtils.apgdiff(srcDir, destDir, outDir);
            FileUtils.removeLine(outDir);
        }
    }
}
