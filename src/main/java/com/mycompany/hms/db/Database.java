package com.mycompany.hms.db;

import com.mycompany.hms.config.AppConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public final class Database {

    private static final Logger log = LoggerFactory.getLogger(Database.class);
    private static volatile HikariDataSource ds;

    private Database() {}

    public static synchronized DataSource dataSource() {
        if (ds == null) {
            HikariConfig cfg = new HikariConfig();
            cfg.setJdbcUrl(AppConfig.get("db.url"));
            cfg.setUsername(AppConfig.get("db.user"));
            cfg.setPassword(AppConfig.get("db.password"));
            cfg.setMaximumPoolSize(AppConfig.getInt("db.pool.maxSize"));
            cfg.setMinimumIdle(AppConfig.getInt("db.pool.minIdle"));
            cfg.setConnectionTimeout(AppConfig.getInt("db.pool.connectionTimeoutMs"));
            cfg.setPoolName("hms-pool");
            ds = new HikariDataSource(cfg);
            log.info("Hikari pool initialised: max={} minIdle={}",
                    cfg.getMaximumPoolSize(), cfg.getMinimumIdle());
        }
        return ds;
    }

    public static Connection getConnection() throws SQLException {
        return dataSource().getConnection();
    }

    public static synchronized void shutdown() {
        if (ds != null) {
            ds.close();
            ds = null;
            log.info("Hikari pool closed");
        }
    }
}
