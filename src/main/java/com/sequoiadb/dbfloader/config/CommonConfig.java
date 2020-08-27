package com.sequoiadb.dbfloader.config;

import com.sequoiadb.base.DBCollection;

import java.util.List;

/***
 * @Program: SequoiaDBFLoader
 * @Description: a class store common configurations
 * @Author: Lzk
 * @Create: 15:30 2020/08/13
 **/
public class CommonConfig {

    private static List<String> address;
    private static String username;
    private static String password;
    private static String collectionSpace;
    private static String collection;
    private static String filePath;
    private static int concurrency = 20;
    private static int bulkSize = 1000;
    private static boolean transaction = false;



    private static boolean allowDuplicate = false;

    private static int skipError = 0;
    private static boolean verbose = true;
    private static String characterSet = "gbk";

    public static List<String> getAddress() {
        return address;
    }

    public static void setAddress(List<String> address) {
        CommonConfig.address = address;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        CommonConfig.username = username;
    }

    public static String getPassword() {
        return password;
    }

    public static void setPassword(String password) {
        CommonConfig.password = password;
    }

    public static String getCollectionSpace() {
        return collectionSpace;
    }

    public static void setCollectionSpace(String collectionSpace) {
        CommonConfig.collectionSpace = collectionSpace;
    }

    public static String getCollection() {
        return collection;
    }

    public static void setCollection(String collection) {
        CommonConfig.collection = collection;
    }

    public static String getFilePath() {
        return filePath;
    }

    public static void setFilePath(String filePath) {
        CommonConfig.filePath = filePath;
    }

    public static int getConcurrency() {
        return concurrency;
    }

    public static void setConcurrency(int concurrency) {
        CommonConfig.concurrency = concurrency;
    }

    public static int getBulkSize() {
        return bulkSize;
    }

    public static void setBulkSize(int bulkSize) {
        CommonConfig.bulkSize = bulkSize;
    }

    public static boolean isTransaction() {
        return transaction;
    }

    public static void setTransaction(boolean transaction) {
        CommonConfig.transaction = transaction;
    }

    public static boolean isAllowDuplicate() {
        return allowDuplicate;
    }

    public static void setAllowDuplicate(boolean allowDuplicate) {
        CommonConfig.allowDuplicate = allowDuplicate;
    }

    public static int isSkipError() {
        return skipError;
    }

    public static void setSkipError(boolean skipError) {
        if (skipError) {
            CommonConfig.skipError = DBCollection.FLG_INSERT_CONTONDUP;
        }
    }

    public static boolean isVerbose() {
        return verbose;
    }

    public static void setVerbose(boolean verbose) {
        CommonConfig.verbose = verbose;
    }

    public static String getCharacterSet() {
        return characterSet;
    }

    public static void setCharacterSet(String characterSet) {
        CommonConfig.characterSet = characterSet;
    }
}
