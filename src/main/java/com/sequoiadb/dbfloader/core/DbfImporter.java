package com.sequoiadb.dbfloader.core;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.linuxense.javadbf.DBFField;
import com.linuxense.javadbf.DBFReader;
import com.linuxense.javadbf.DBFUtils;
import com.sequoiadb.dbfloader.config.CommonConfig;
import com.sequoiadb.dbfloader.config.DatasourceConfig;
import com.sequoiadb.dbfloader.db.SdbServer;
import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/***
 * @Program: SequoiaDBFLoader
 * @Description: import dbf into sequoiadb
 * @Author: Lzk
 * @Create: 10:08 2020/08/20
 **/
public class DbfImporter {

    public static LinkedBlockingDeque<List<BSONObject>> bsonLists = new LinkedBlockingDeque<>(CommonConfig.getConcurrency() + 10);


    private static ThreadFactory producerFactory =
            new ThreadFactoryBuilder()
                    .setNameFormat("producer-pool-%d")
                    .build();

    private static ExecutorService producerExecutor =
            new ThreadPoolExecutor(
                    CommonConfig.getConcurrency(),
                    CommonConfig.getConcurrency() + 10,
                    DatasourceConfig.getKeepAliveTimeout(),
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(1024),
                    producerFactory,
                    new ThreadPoolExecutor.AbortPolicy());

    private static ThreadFactory consumerFactory =
            new ThreadFactoryBuilder()
                    .setNameFormat("consumer-pool-%d")
                    .build();

    private static ExecutorService consumerExecutor =
            new ThreadPoolExecutor(
                    CommonConfig.getConcurrency(),
                    CommonConfig.getConcurrency() + 10,
                    DatasourceConfig.getKeepAliveTimeout(),
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<>(1024),
                    consumerFactory,
                    new ThreadPoolExecutor.AbortPolicy());

    public static void readDbf(String dbfPath) {

        DBFReader dbfReader = null;

        try {
            dbfReader = new DBFReader(new FileInputStream(dbfPath));
            int fieldNum = dbfReader.getFieldCount();
            String[] fields = new String[fieldNum];
            for (int i = 0; i < fieldNum; i++) {
                DBFField dbfField = dbfReader.getField(i);
                fields[i] = dbfField.getName();
            }
            Object[] rowObjects;
            List<BSONObject> bsonObjectList = new ArrayList<>();
            while ((rowObjects = dbfReader.nextRecord()) != null) {
                BSONObject rowBson = new BasicBSONObject();
                for (int j = 0; j < rowObjects.length; j++) {
                    rowBson.put(fields[j], rowObjects[j]);
                }
                if (bsonObjectList.size() >= CommonConfig.getBulkSize()) {
                    List<BSONObject> bulkBsonList = bsonObjectList;
                    bsonLists.put(bulkBsonList);
                    consumerExecutor.execute(
                            () -> {
                                try {
                                    SdbServer.bulkInsert(bsonLists.take());
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                    );

                    bsonObjectList = new ArrayList<>();
                }

                bsonObjectList.add(rowBson);
            }
            List<BSONObject> remainBsonList = bsonObjectList;
            bsonLists.put(remainBsonList);
            consumerExecutor.execute(
                    () -> {
                        try {
                            SdbServer.bulkInsert(bsonLists.take());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
            );
            consumerExecutor.awaitTermination(60, TimeUnit.SECONDS);
        } catch (FileNotFoundException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            DBFUtils.close(dbfReader);
            consumerExecutor.shutdown();
            consumerExecutor.shutdownNow();
        }
    }
}
