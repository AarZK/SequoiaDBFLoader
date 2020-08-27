package com.sequoiadb.dbfloader.db;

import com.sequoiadb.base.ConfigOptions;
import com.sequoiadb.base.Sequoiadb;
import com.sequoiadb.datasource.DatasourceOptions;
import com.sequoiadb.datasource.SequoiadbDatasource;
import com.sequoiadb.dbfloader.config.CommonConfig;
import com.sequoiadb.dbfloader.config.ConnectionConfig;
import com.sequoiadb.dbfloader.config.DatasourceConfig;
import com.sequoiadb.exception.BaseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Program: SequoiaDBFLoader
 * @Description: manage sequoiadb connection through a pool
 * @Author: Lzk
 * @Create: 11:34 2020/08/12
 **/
public class SdbManager {

    private static final Logger logger = LoggerFactory.getLogger(SdbManager.class);
    private static ConfigOptions configOptions = new ConfigOptions();
    private static DatasourceOptions datasourceOptions = new DatasourceOptions();
    private static SequoiadbDatasource sequoiadbDatasource = null;

    public static void initDataSource() {
        /**
         * @Description: init sequoiadb datasource(connection pool)
         * @Param: []
         * @Return: static void 
         * @Author: Li Zekun
         * @Create on 9:59 2020/8/18
         */
        List<String> address = CommonConfig.getAddress();
        String username = CommonConfig.getUsername();
        String password = CommonConfig.getPassword();
        initConfigOptions();
        initDatasourceOptions();
        sequoiadbDatasource = new SequoiadbDatasource(address, username, password, configOptions, datasourceOptions);
    }

    public static void initConfigOptions() {
        /**
         * @Description: init the connection configuration options
         * @Param: []
         * @Return: static void 
         * @Author: Li Zekun
         * @Create on 11:42 2020/8/18
         */
        try {
            configOptions.setConnectTimeout(ConnectionConfig.getConnectTimeoutMillis());
            configOptions.setMaxAutoConnectRetryTime(ConnectionConfig.getMaxRetryTimeMillis());
            configOptions.setSocketKeepAlive(ConnectionConfig.isSocketKeepAlive());
            configOptions.setSocketTimeout(ConnectionConfig.getSocketTimeoutMillis());
            configOptions.setUseNagle(ConnectionConfig.isUseNagle());
            configOptions.setUseSSL(ConnectionConfig.isUseSSL());
        } catch (Exception e) {
            logger.error("failed to init the configuration options of connections, cause:", e);
        }
    }

    public static void initDatasourceOptions() {
        /**
         * @Description: init the datasource configuration options
         * @Param: []
         * @Return: static void 
         * @Author: Li Zekun
         * @Create on 11:44 2020/8/18
         */
        try {
            datasourceOptions.setCheckInterval(DatasourceConfig.getCheckInterval());
            datasourceOptions.setConnectStrategy(DatasourceConfig.getConnectStrategy());
            datasourceOptions.setDeltaIncCount(DatasourceConfig.getDeltaIncCount());
            datasourceOptions.setKeepAliveTimeout(DatasourceConfig.getKeepAliveTimeout());
            datasourceOptions.setMaxCount(DatasourceConfig.getMaxCount());
            datasourceOptions.setMaxIdleCount(DatasourceConfig.getMaxIdleCount());
            datasourceOptions.setPreferedInstance(DatasourceConfig.getPreferedInstance());
            datasourceOptions.setPreferedInstanceMode(DatasourceConfig.getPreferedInstanceMode());
            datasourceOptions.setSessionTimeout(DatasourceConfig.getSessionTimeout());
            datasourceOptions.setSyncCoordInterval(DatasourceConfig.getSyncCoordInterval());
            datasourceOptions.setValidateConnection(DatasourceConfig.isValidateConnection());
        } catch (Exception e) {
            logger.error("failed to init the configuration options of datasource, cause:", e);
        }
    }

    public static synchronized Sequoiadb getSdb() {
        /**
         * @Description: get a sequoiadb connection
         * @Param: []
         * @Return: com.sequoiadb.base.Sequoiadb
         * @Author: Li Zekun
         * @Create on 14:04 2020/8/19
         */
        Sequoiadb sequoiadb = null;
        try {
            sequoiadb = sequoiadbDatasource.getConnection();
        } catch (BaseException e) {
            logger.error("failed to get sequoiadb connection from datasource, cause:", e);
        } catch (InterruptedException e) {
            logger.error("failed to get sequoiadb connection from datasource, cause:", e);
        }
        return sequoiadb;
    }

    public static void releaseSdb(Sequoiadb sequoiadb) {
        /**
         * @Description: release sequoiadb connection to datasource
         * @Param: [sequoiadb]
         * @Return: static void 
         * @Author: Li Zekun
         * @Create on 14:05 2020/8/19
         */
        try {
            sequoiadbDatasource.releaseConnection(sequoiadb);
        } catch (BaseException e) {
            logger.error("failed to release sequoiadb connection to datasource, cause:", e);
        }
    }

    public static void closeDatasource() {
        /**
         * @Description: close the sequoiadb datasource
         * @Param: []
         * @Return: static void 
         * @Author: Li Zekun
         * @Create on 14:07 2020/8/19
         */
        try {
            sequoiadbDatasource.close();
        } catch (Exception e) {
            logger.error("failed to close the sequoiadb datasource, cause:", e);
        }
    }
}
