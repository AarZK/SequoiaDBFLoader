package com.sequoiadb.dbfloader.parser;

import com.sequoiadb.datasource.ConnectStrategy;
import com.sequoiadb.dbfloader.config.CommonConfig;
import com.sequoiadb.dbfloader.config.ConnectionConfig;
import com.sequoiadb.dbfloader.config.DatasourceConfig;
import com.sequoiadb.dbfloader.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

/***
 * @Program: SequoiaDBFLoader
 * @Description: a util to parse configuration from config file
 * @Author: Lzk
 * @Create: 15:26 2020/08/13
 **/
public class ConfigParser {

    private static final Logger logger = LoggerFactory.getLogger(ConfigParser.class);
    private static Properties properties = new Properties();


    public static void parseConfig(String absoluteConfigPath) {
        /**
         * @Description: read config file as properties
         * @Param: [absoluteConfigPath]
         * @Return: void
         * @Author: Li Zekun
         * @Create on 15:57 2020/8/13
         */
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(absoluteConfigPath));
            properties.load(bufferedReader);
        } catch (FileNotFoundException e) {
            logger.error("not found config file [{}],cause:", absoluteConfigPath, e);
        } catch (IOException e) {
            logger.error("can not read config file [{}],cause:", absoluteConfigPath, e);
        }
        parseCommonConfig();
        parseConnectionConfig();
        parseDatasourceConfig();
    }

    public static void parseCommonConfig() {
        /**
         * @Description: generate common configurations
         * @Param: []
         * @Return: void
         * @Author: Li Zekun
         * @Create on 16:00 2020/8/13
         */
        try {
            CommonConfig.setAddress(Arrays.asList(properties.getProperty("address").split(",")));
            CommonConfig.setUsername(properties.getProperty("username"));
            CommonConfig.setPassword(PasswordUtil.getPassword(properties));
            CommonConfig.setConcurrency(Integer.parseInt(properties.getProperty("concurrency")));
            CommonConfig.setBulkSize(Integer.parseInt(properties.getProperty("bulkSize")));
            CommonConfig.setTransaction(Boolean.parseBoolean(properties.getProperty("transaction")));
            CommonConfig.setAllowDuplicate(Boolean.parseBoolean(properties.getProperty("allowDuplicate")));
            CommonConfig.setSkipError(Boolean.parseBoolean(properties.getProperty("skipError")));
            CommonConfig.setVerbose(Boolean.parseBoolean(properties.getProperty("verbose")));
            CommonConfig.setCharacterSet(properties.getProperty("characterSet"));
        } catch (NumberFormatException e) {
            logger.error("failed to parse configuration of common options, cause:", e);
        }
    }

    public static void parseDatasourceConfig() {
        /**
         * @Description: generate configurations for sequoiadb datasource
         * @Param: []
         * @Return: void
         * @Author: Li Zekun
         * @Create on 16:01 2020/8/13
         */
        try {
            DatasourceConfig.setCheckInterval(Integer.parseInt(properties.getProperty("checkInterval")));
            DatasourceConfig.setConnectStrategy(ConnectStrategy.valueOf(properties.getProperty("connectStrategy")));
            DatasourceConfig.setDeltaIncCount(Integer.parseInt(properties.getProperty("deltaIncCount")));
            DatasourceConfig.setKeepAliveTimeout(Integer.parseInt(properties.getProperty("keepAliveTimeout")));
            DatasourceConfig.setMaxCount(Integer.parseInt(properties.getProperty("maxCount")));
            DatasourceConfig.setMaxIdleCount(Integer.parseInt(properties.getProperty("maxIdleCount")));
            DatasourceConfig.setPreferedInstance(Arrays.asList(properties.getProperty("preferedInstance").split(",")));
            DatasourceConfig.setPreferedInstanceMode(properties.getProperty("preferedInstanceMode"));
            DatasourceConfig.setSessionTimeout(Integer.parseInt(properties.getProperty("sessionTimeout")));
            DatasourceConfig.setSyncCoordInterval(Integer.parseInt(properties.getProperty("syncCoordInterval")));
            DatasourceConfig.setValidateConnection(Boolean.parseBoolean(properties.getProperty("validateConnection")));
        } catch (IllegalArgumentException e) {
            logger.error("failed to parse configurations of datasource, cause:", e);
        }
    }

    public static void parseConnectionConfig() {
        /**
         * @Description: generate configurations for sequoiadb connections
         * @Param: []
         * @Return: void
         * @Author: Li Zekun
         * @Create on 16:45 2020/8/13
         */
        try {
            ConnectionConfig.setConnectTimeoutMillis(Integer.parseInt(properties.getProperty("connectTimeoutMillis")));
            ConnectionConfig.setMaxRetryTimeMillis(Integer.parseInt(properties.getProperty("maxRetryTimeMillis")));
            ConnectionConfig.setSocketKeepAlive(Boolean.parseBoolean(properties.getProperty("socketKeepAlive")));
            ConnectionConfig.setSocketTimeoutMillis(Integer.parseInt(properties.getProperty("socketTimeoutMillis")));
            ConnectionConfig.setUseNagle(Boolean.parseBoolean(properties.getProperty("useNagle")));
            ConnectionConfig.setUseSSL(Boolean.parseBoolean(properties.getProperty("useSSL")));
        } catch (NumberFormatException e) {
            logger.error("failed to parse configurations of connection, cause:", e);
        }
    }

}
