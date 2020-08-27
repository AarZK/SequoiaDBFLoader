package com.sequoiadb.dbfloader.db;

import com.sequoiadb.base.CollectionSpace;
import com.sequoiadb.base.DBCollection;
import com.sequoiadb.base.Sequoiadb;
import com.sequoiadb.dbfloader.config.CommonConfig;
import com.sequoiadb.exception.BaseException;
import org.bson.BSONObject;

import java.util.List;

/***
 * @Program: SequoiaDBFLoader
 * @Description: data options in sequoiadb
 * @Author: Lzk
 * @Create: 11:33 2020/08/20
 **/
public class SdbServer {

    public static void bulkInsert(List<BSONObject> bsonObjectList) {
        Sequoiadb sequoiadb = SdbManager.getSdb();
        try {
            CollectionSpace collectionSpace = sequoiadb.getCollectionSpace(CommonConfig.getCollectionSpace());
            DBCollection collection = collectionSpace.getCollection(CommonConfig.getCollection());
            collection.insert(bsonObjectList, CommonConfig.isSkipError());
        } catch (BaseException e) {
            e.printStackTrace();
        } finally {
            SdbManager.releaseSdb(sequoiadb);
        }
    }
}
