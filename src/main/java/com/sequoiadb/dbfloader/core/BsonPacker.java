package com.sequoiadb.dbfloader.core;

import com.sequoiadb.dbfloader.config.CommonConfig;
import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import java.util.ArrayList;
import java.util.List;

/***
 * @Program: SequoiaDBFLoader
 * @Description: pack bson objects into a list
 * @Author: Lzk
 * @Create: 15:45 2020/08/27
 **/
public class BsonPacker {

    private List<BSONObject> bsonList = new ArrayList<>();
    private final int capacity = CommonConfig.getBulkSize();

    public void addBson(BSONObject bsonObject) {
        bsonList.add(bsonObject);
        if (bsonList.size() >= capacity) {
            try {
                DbfImporter.bsonLists.put(bsonList);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            bsonList.clear();
        }
    }

    public BSONObject rowToBson(String[] fields, Object[] rowObjects) {
        BSONObject rowBSON = new BasicBSONObject();
        for (int i = 0; i < rowObjects.length; i++) {
            rowBSON.put(fields[i], rowObjects[i]);
        }
        return rowBSON;
    }
}
