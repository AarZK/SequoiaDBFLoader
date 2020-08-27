package com.sequoiadb.dbfloader.core;

import org.bson.BSONObject;
import org.bson.BasicBSONObject;

import java.util.ArrayList;
import java.util.List;

/***
 * @Program: SequoiaDBFLoader
 * @Description: read dbf records as BSON
 * @Author: Lzk
 * @Create: 10:13 2020/08/20
 **/
public class DbfConvertor implements Runnable{

    private List<BSONObject> bsonObjectList = new ArrayList<>();

    public DbfConvertor(String[] fields, Object[] rowObjects) {
        BSONObject rowBSON = new BasicBSONObject();
        for (int i = 0; i < rowObjects.length; i++) {
            rowBSON.put(fields[i], rowObjects[i]);
        }
        bsonObjectList.add(rowBSON);
    }

    @Override
    public void run() {

    }
}
