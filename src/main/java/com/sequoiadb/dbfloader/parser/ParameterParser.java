package com.sequoiadb.dbfloader.parser;

import com.sequoiadb.dbfloader.config.CommonConfig;
import com.sequoiadb.dbfloader.util.PasswordUtil;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.util.Arrays;

/***
 * @Program: SequoiaDBFLoader
 * @Description: parse parameters from input command
 * @Author: Lzk
 * @Create: 16:09 2020/08/14
 **/
public class ParameterParser {

    private static final Logger logger = LoggerFactory.getLogger(ParameterParser.class);
    private static Options options = new Options();
    private static CommandLine commandLine = null;
    private static String helpMsg = null;

    public static void parseParameter(String[] param) {
        /**
         * @Description: parse parameters
         * @Param: [param]
         * @Return: void
         * @Author: Li Zekun
         * @Create on 16:22 2020/8/14
         */
        CommandLineParser commandLineParser = new DefaultParser();
        options.addOption("h", "help", false, "help of usage");
        options.addOption(Option.builder("c").longOpt("conf").required(false).hasArg().argName("config file").numberOfArgs(1).desc("the path of configuration file").build());
        options.addOption(Option.builder("a").longOpt("address").required(false).hasArg().argName("coord address").numberOfArgs(1).desc("address of coord (hostname:service,[...])").build());
        options.addOption(Option.builder("u").longOpt("username").required(false).hasArg().argName("sequoiadb username").numberOfArgs(1).desc("the name of user that used to access the sequoiadb").build());
        options.addOption(Option.builder("p").longOpt("password").required(false).hasArg(false).argName("sequoiadb password").desc("the password of current user").build());
        options.addOption(Option.builder("k").longOpt("token").required(false).hasArg().argName("token").numberOfArgs(1).desc("the token to decrypt password file").build());
        options.addOption(Option.builder("i").longOpt("cipher").required(false).hasArg().argName("cipher file").numberOfArgs(1).desc("the path of encrypt password file").build());
        options.addOption(Option.builder("s").longOpt("cs").required().hasArg().argName("collectionspace").numberOfArgs(1).desc("the name of target collectionspace").build());
        options.addOption(Option.builder("l").longOpt("cl").required().hasArg().argName("collection").numberOfArgs(1).desc("the name of target collection").build());
        options.addOption(Option.builder("f").longOpt("file").required().hasArg().argName("file path").numberOfArgs(1).desc("the path of file(s) waiting to be imported").build());
        options.addOption(Option.builder("o").longOpt("concur").required(false).hasArg().argName("concurrency number").numberOfArgs(1).desc("the number of import task thread(default:20)").build());
        options.addOption(Option.builder("b").longOpt("bulk").required(false).hasArg().argName("bulk size").numberOfArgs(1).desc("the number of records submitted in bulk(default:1000)").build());
        options.addOption(Option.builder("t").longOpt("trans").required(false).hasArg().argName("transaction").numberOfArgs(1).desc("whether the transaction is on(default:false)").build());
        options.addOption(Option.builder("d").longOpt("dupl").required(false).hasArg().argName("duplicate").numberOfArgs(1).desc("whether duplicate insert is allowed(default:false)").build());
        options.addOption(Option.builder("e").longOpt("error").required(false).hasArg().argName("skip error").numberOfArgs(1).desc("whether continue when an error occurred(default:false)").build());
        options.addOption(Option.builder("v").longOpt("verbose").required(false).hasArg().argName("verbose").numberOfArgs(1).desc("whether the import procedure is visible(default:true)").build());
        options.addOption(Option.builder("r").longOpt("char").required(false).hasArg().argName("character set").numberOfArgs(1).desc("the character set of dbf file(s)(default:gbk)").build());
        try {
            String helpOption = "-h";
            String helpLongOption = "--help";
            if (Arrays.asList(param).contains(helpOption) || Arrays.asList(param).contains(helpLongOption)) {
                System.out.println(getHelpMsg());
                System.exit(1);
            }
            commandLine = commandLineParser.parse(options, param);
            parseController();
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            System.out.println(getHelpMsg());
            System.exit(1);
        }
    }

    private static String getHelpMsg() {
        /**
         * @Description: get the help message
         * @Param: []
         * @Return: java.lang.String
         * @Author: Li Zekun
         * @Create on 16:30 2020/8/14
         */
        if (helpMsg == null) {
            HelpFormatter helpFormatter = new HelpFormatter();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            PrintWriter printWriter = new PrintWriter(byteArrayOutputStream);
            helpFormatter.printHelp(
                    printWriter,
                    HelpFormatter.DEFAULT_WIDTH,
                    "dbfloader",
                    "Description: \n this tool is used to import dbf files into SequoiaDB" +
                            "",
                    options,
                    HelpFormatter.DEFAULT_LEFT_PAD,
                    HelpFormatter.DEFAULT_DESC_PAD,
                    null,
                    true
            );
            printWriter.flush();
            helpMsg = new String(byteArrayOutputStream.toByteArray());
            printWriter.close();
        }
        return helpMsg;
    }

    public static void parseController() {
        /**
         * @Description: ensure that invoke correct function to init common configuration
         * @Param: []
         * @Return: void
         * @Author: Li Zekun
         * @Create on 15:06 2020/8/18
         */
        String configOption = "conf";
        String addressOption = "address";
        String usernameOption = "username";
        String csOption = "cs";
        String clOption = "cl";
        String fileOption = "file";
        String concurOption = "concur";
        String bulkOption = "bulk";
        String transOption = "trans";
        String duplOption = "dupl";
        String errorOption = "error";
        String charOption = "char";
        String verboseOption = "verbose";

        if (commandLine.hasOption(configOption)) {
            ConfigParser.parseConfig(commandLine.getOptionValue(configOption));
        }
        if (commandLine.hasOption(addressOption)) {
            CommonConfig.setAddress(Arrays.asList(commandLine.getOptionValue(addressOption).split(",")));
        }
        if (commandLine.hasOption(usernameOption)) {
            CommonConfig.setUsername(commandLine.getOptionValue(usernameOption));
        }

        CommonConfig.setPassword(PasswordUtil.getPassword(commandLine));
        CommonConfig.setCollectionSpace(commandLine.getOptionValue(csOption));
        CommonConfig.setCollection(commandLine.getOptionValue(clOption));
        CommonConfig.setFilePath(commandLine.getOptionValue(fileOption));

        if (commandLine.hasOption(concurOption)) {
            CommonConfig.setConcurrency(Integer.parseInt(commandLine.getOptionValue(concurOption)));
        }
        if (commandLine.hasOption(bulkOption)) {
            CommonConfig.setBulkSize(Integer.parseInt(commandLine.getOptionValue(bulkOption)));
        }
        if (commandLine.hasOption(transOption)) {
            CommonConfig.setTransaction(Boolean.parseBoolean(commandLine.getOptionValue(transOption)));
        }
        if (commandLine.hasOption(duplOption)) {
            CommonConfig.setAllowDuplicate(Boolean.parseBoolean(commandLine.getOptionValue(duplOption)));
        }
        if (commandLine.hasOption(errorOption)) {
            CommonConfig.setSkipError(Boolean.parseBoolean(commandLine.getOptionValue(errorOption)));
        }
        if (commandLine.hasOption(charOption)) {
            CommonConfig.setCharacterSet(commandLine.getOptionValue(charOption));
        }
    }
}
