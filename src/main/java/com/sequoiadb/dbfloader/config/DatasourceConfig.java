package com.sequoiadb.dbfloader.config;

import com.sequoiadb.datasource.ConnectStrategy;

import java.util.Arrays;
import java.util.List;

/***
 * @Program: SequoiaDBFLoader
 * @Description: a class store configurations about sequoiadb datasource
 * @Author: Lzk
 * @Create: 15:37 2020/08/13
 **/
public class DatasourceConfig {
    private static int checkInterval = 60000;
    private static ConnectStrategy connectStrategy = ConnectStrategy.BALANCE;
    private static int deltaIncCount = 10;
    private static int keepAliveTimeout = 0;
    private static int maxCount = 500;
    private static int maxIdleCount = 10;
    private static List<String> preferedInstance = Arrays.asList("S");
    private static String preferedInstanceMode = "random";
    private static int sessionTimeout = -1;
    private static int syncCoordInterval = 60000;
    private static boolean validateConnection = false;

    public static int getCheckInterval() {
        return checkInterval;
    }

    public static void setCheckInterval(int checkInterval) {
        DatasourceConfig.checkInterval = checkInterval;
    }

    public static ConnectStrategy getConnectStrategy() {
        return connectStrategy;
    }

    public static void setConnectStrategy(ConnectStrategy connectStrategy) {
        DatasourceConfig.connectStrategy = connectStrategy;
    }

    public static int getDeltaIncCount() {
        return deltaIncCount;
    }

    public static void setDeltaIncCount(int deltaIncCount) {
        DatasourceConfig.deltaIncCount = deltaIncCount;
    }

    public static int getKeepAliveTimeout() {
        return keepAliveTimeout;
    }

    public static void setKeepAliveTimeout(int keepAliveTimeout) {
        DatasourceConfig.keepAliveTimeout = keepAliveTimeout;
    }

    public static int getMaxCount() {
        return maxCount;
    }

    public static void setMaxCount(int maxCount) {
        DatasourceConfig.maxCount = maxCount;
    }

    public static int getMaxIdleCount() {
        return maxIdleCount;
    }

    public static void setMaxIdleCount(int maxIdleCount) {
        DatasourceConfig.maxIdleCount = maxIdleCount;
    }

    public static List<String> getPreferedInstance() {
        return preferedInstance;
    }

    public static void setPreferedInstance(List<String> preferedInstance) {
        DatasourceConfig.preferedInstance = preferedInstance;
    }

    public static String getPreferedInstanceMode() {
        return preferedInstanceMode;
    }

    public static void setPreferedInstanceMode(String preferedInstanceMode) {
        DatasourceConfig.preferedInstanceMode = preferedInstanceMode;
    }

    public static int getSessionTimeout() {
        return sessionTimeout;
    }

    public static void setSessionTimeout(int sessionTimeout) {
        DatasourceConfig.sessionTimeout = sessionTimeout;
    }

    public static int getSyncCoordInterval() {
        return syncCoordInterval;
    }

    public static void setSyncCoordInterval(int syncCoordInterval) {
        DatasourceConfig.syncCoordInterval = syncCoordInterval;
    }

    public static boolean isValidateConnection() {
        return validateConnection;
    }

    public static void setValidateConnection(boolean validateConnection) {
        DatasourceConfig.validateConnection = validateConnection;
    }
}
