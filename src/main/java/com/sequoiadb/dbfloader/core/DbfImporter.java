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

    private static LinkedBlockingDeque<List<BSONObject>> bsonListLinkedBlockingDeque = new LinkedBlockingDeque<>(CommonConfig.getConcurrency());

    private static ThreadFactory producerThreadFactory =
            new ThreadFactoryBuilder()
                    .setNameFormat("producer-pool-%d")
                    .build();

    private static ExecutorService producerPoolExecutor =
            new ThreadPoolExecutor(
                    CommonConfig.getConcurrency(),
                    CommonConfig.getConcurrency() + 10,
                    DatasourceConfig.getKeepAliveTimeout(),
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>(1024),
                    producerThreadFactory,
                    new ThreadPoolExecutor.AbortPolicy());

    private static ThreadFactory consumerThreadFactory =
            new ThreadFactoryBuilder()
                    .setNameFormat("consumer-pool-%d")
                    .build();

    private static ExecutorService consumerPoolExecutor =
            new ThreadPoolExecutor(
                    CommonConfig.getConcurrency(),
                    CommonConfig.getConcurrency() + 10,
                    DatasourceConfig.getKeepAliveTimeout(),
                    TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>(1024),
                    consumerThreadFactory,
                    new ThreadPoolExecutor.AbortPolicy());

    public static void importDBF() {

        DBFReader dbfReader = null;

        try {
            dbfReader = new DBFReader(new FileInputStream("C:\\Users\\14620\\Dropbox\\My PC (DESKTOP-HJS0QCH)\\Desktop\\test.dbf"));
            int fieldNum = dbfReader.getFieldCount();
            String[] fields = new String[fieldNum];
            for (int i = 0; i < fieldNum; i++) {
                DBFField dbfField = dbfReader.getField(i);
                fields[i] = dbfField.getName();
            }
            Object[] rowObjects;
            List<BSONObject> bsonObjectList = new ArrayList<>();
            while ((rowObjects = dbfReader.nextRecord()) != null) {
                BSONObject rowBSON = new BasicBSONObject();
                for (int j = 0; j < rowObjects.length; j++) {
                    rowBSON.put(fields[j], rowObjects[j]);
                }
                if (bsonObjectList.size() >= CommonConfig.getBulkSize()) {
                    List<BSONObject> bulkBSONList = bsonObjectList;
                    bsonListLinkedBlockingDeque.put(bulkBSONList);
                    consumerPoolExecutor.execute(
                            () -> {
                                try {
                                    SdbServer.bulkInsert(bsonListLinkedBlockingDeque.take());
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                    );
                    bsonObjectList = new ArrayList<>();
                }

                bsonObjectList.add(rowBSON);
            }
            List<BSONObject> remainBSONList = bsonObjectList;
            bsonListLinkedBlockingDeque.put(remainBSONList);
            consumerPoolExecutor.execute(
                    () -> {
                        try {
                            SdbServer.bulkInsert(bsonListLinkedBlockingDeque.take());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
            );
            consumerPoolExecutor.awaitTermination(60, TimeUnit.SECONDS);
        } catch (FileNotFoundException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            DBFUtils.close(dbfReader);
            consumerPoolExecutor.shutdown();
            consumerPoolExecutor.shutdownNow();
        }
    }
}
