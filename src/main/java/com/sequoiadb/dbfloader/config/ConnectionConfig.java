package com.sequoiadb.dbfloader.config;

/***
 * @Program: SequoiaDBFLoader
 * @Description: a class store configurations about sequoiadb connection
 * @Author: Lzk
 * @Create: 16:37 2020/08/13
 **/
public class ConnectionConfig {

    private static int connectTimeoutMillis = 10000;
    private static int maxRetryTimeMillis = 150000;
    private static boolean socketKeepAlive = true;
    private static int socketTimeoutMillis = 0;
    private static boolean useNagle = false;
    private static boolean useSSL = false;

    public static int getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    public static void setConnectTimeoutMillis(int connectTimeoutMillis) {
        ConnectionConfig.connectTimeoutMillis = connectTimeoutMillis;
    }

    public static int getMaxRetryTimeMillis() {
        return maxRetryTimeMillis;
    }

    public static void setMaxRetryTimeMillis(int maxRetryTimeMillis) {
        ConnectionConfig.maxRetryTimeMillis = maxRetryTimeMillis;
    }

    public static boolean isSocketKeepAlive() {
        return socketKeepAlive;
    }

    public static void setSocketKeepAlive(boolean socketKeepAlive) {
        ConnectionConfig.socketKeepAlive = socketKeepAlive;
    }

    public static int getSocketTimeoutMillis() {
        return socketTimeoutMillis;
    }

    public static void setSocketTimeoutMillis(int socketTimeoutMillis) {
        ConnectionConfig.socketTimeoutMillis = socketTimeoutMillis;
    }

    public static boolean isUseNagle() {
        return useNagle;
    }

    public static void setUseNagle(boolean useNagle) {
        ConnectionConfig.useNagle = useNagle;
    }

    public static boolean isUseSSL() {
        return useSSL;
    }

    public static void setUseSSL(boolean useSSL) {
        ConnectionConfig.useSSL = useSSL;
    }
}
