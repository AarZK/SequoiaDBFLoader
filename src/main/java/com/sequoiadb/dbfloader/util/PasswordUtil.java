package com.sequoiadb.dbfloader.util;

import com.sequoiadb.util.SdbDecrypt;
import org.apache.commons.cli.CommandLine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Properties;

/***
 * @Program: SequoiaDBFLoader
 * @Description: parse password
 * @Author: Lzk
 * @Create: 15:52 2020/08/14
 **/
public class PasswordUtil {

    private static final Logger logger = LoggerFactory.getLogger(PasswordUtil.class);
    private static SdbDecrypt sdbDecrypt = new SdbDecrypt();

    public static String getPassword(CommandLine commandLine) {
        /**
         * @Description: parse password from parameters
         * @Param: [commandLine]
         * @Return: java.lang.String
         * @Author: Li Zekun
         * @Create on 17:14 2020/8/18
         */
        String password = null;

        String usernameOption = "username";
        String passwordOption = "password";
        String tokenOption = "token";
        String cipherOption = "cipher";


        if (commandLine.hasOption(passwordOption)) {
            System.out.println("please input the password of " + commandLine.getOptionValue(usernameOption) + " :");
            password = new String(System.console().readPassword());
        } else {
            if (commandLine.hasOption(cipherOption)) {
                File ciperFile = new File(commandLine.getOptionValue(cipherOption));
                if (commandLine.hasOption(tokenOption)) {
                    password = sdbDecrypt.parseCipherFile(
                            commandLine.getOptionValue(usernameOption),
                            commandLine.getOptionValue(tokenOption),
                            ciperFile
                    ).getPasswd();
                } else {
                    password = sdbDecrypt.parseCipherFile(
                            commandLine.getOptionValue(usernameOption),
                            ciperFile
                    ).getPasswd();
                }
            } else {
                logger.error("failed to get password of " + commandLine.getOptionValue(usernameOption));
            }
        }
        return password;
    }

    public static String getPassword(Properties properties) {
        /**
         * @Description: parse password from configurations
         * @Param: [properties]
         * @Return: java.lang.String
         * @Author: Li Zekun
         * @Create on 11:20 2020/8/19
         */
        String password = null;
        String usernameConfig = "username";
        String passwordConfig = "password";
        String tokenConfig = "token";
        String cipherConfig = "cipherFilePath";

        if (properties.containsKey(passwordConfig) && properties.getProperty(passwordConfig).length() != 0) {
            if (properties.containsKey(tokenConfig) && properties.getProperty(tokenConfig).length() != 0) {
                password = sdbDecrypt.decryptPasswd(
                        properties.getProperty(passwordConfig),
                        properties.getProperty(tokenConfig)
                );
            } else {
                password = sdbDecrypt.decryptPasswd(
                        properties.getProperty(passwordConfig)
                );
            }
        } else {
            if (properties.containsKey(cipherConfig) && properties.getProperty(cipherConfig).length() != 0) {
                File ciperFile = new File(properties.getProperty(cipherConfig));
                if (properties.containsKey(tokenConfig) && properties.getProperty(tokenConfig).length() != 0) {
                    password = sdbDecrypt.parseCipherFile(
                            properties.getProperty(usernameConfig),
                            properties.getProperty(tokenConfig),
                            ciperFile
                    ).getPasswd();
                } else {
                    password = sdbDecrypt.parseCipherFile(
                            properties.getProperty(usernameConfig),
                            ciperFile
                    ).getPasswd();
                }
            } else {
                logger.error("failed to get password of user: " + properties.getProperty(usernameConfig));
            }
        }
        return password;
    }
}
