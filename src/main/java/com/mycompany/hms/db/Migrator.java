package com.mycompany.hms.db;

import com.mycompany.hms.config.AppConfig;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Migrator {

    private static final Logger log = LoggerFactory.getLogger(Migrator.class);

    private Migrator() {}

    public static void migrate() {
        if (!AppConfig.getBoolean("flyway.enabled")) {
            log.info("Flyway disabled");
            return;
        }
        Flyway flyway = Flyway.configure()
                .dataSource(Database.dataSource())
                .baselineOnMigrate(AppConfig.getBoolean("flyway.baselineOnMigrate"))
                .locations("classpath:db/migration")
                .load();
        var result = flyway.migrate();
        log.info("Flyway: schema={} migrationsExecuted={} initialSchemaVersion={}",
                result.schemaName, result.migrationsExecuted, result.initialSchemaVersion);
    }
}
