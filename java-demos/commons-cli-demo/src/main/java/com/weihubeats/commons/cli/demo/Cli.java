package com.weihubeats.commons.cli.demo;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

/**
 * @author : wh
 * @date : 2023/2/23 17:59
 * @description:
 */
public class Cli {

    public static void main(String[] args) throws ParseException {
        //定义
        Options options = new Options();
        // false表示不是强制要求的启动参数
        options.addOption("h",false,"list help");
        // true表示需要启动参数必须有这个
        options.addOption("t",true,"set time on system");
  
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options,args);

        //查询交互
        //你的程序应当写在这里，从这里启动
        if (cmd.hasOption("h")){
            String formatstr = "CLI  cli  test";
            HelpFormatter hf = new HelpFormatter();
            hf.printHelp(formatstr, "", options, "");
            return;
        }

        if (cmd.hasOption("t")){
            System.out.printf("system time has setted  %s ",cmd.getOptionValue("t"));
            return;
        }

        System.out.println("error");
    }
    
}
