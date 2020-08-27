package com.sequoiadb.dbfloader;

import com.sequoiadb.dbfloader.config.CommonConfig;
import com.sequoiadb.dbfloader.core.DbfImporter;
import com.sequoiadb.dbfloader.parser.ConfigParser;
import com.sequoiadb.dbfloader.db.SdbManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Program: SequoiaDBFLoader
 * @Description: test
 * @Author: Lzk
 * @Create: 14:57 2020/08/13
 **/
public class TestMain {

    private static final Logger logger = LoggerFactory.getLogger(TestMain.class);

    public static void main(String[] args) {
        ConfigParser.parseConfig("F:\\IdeaProjects\\SequoiaDBFLoader\\src\\main\\resources\\sequoiadbfloader.properties");
        CommonConfig.setCollectionSpace("pokemon");
        CommonConfig.setCollection("pokemon");
        SdbManager.initDataSource();

        DbfImporter.importDbf();
        SdbManager.closeDatasource();


    }
}
