package com.mycompany.hms.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class AppConfig {

    private static final String FILE = "application.properties";
    private static final Properties PROPS = load();

    private AppConfig() {}

    private static Properties load() {
        Properties p = new Properties();
        try (InputStream in = AppConfig.class.getClassLoader().getResourceAsStream(FILE)) {
            if (in == null) {
                throw new IllegalStateException(FILE + " not found on classpath");
            }
            p.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load " + FILE, e);
        }
        return p;
    }

    public static String get(String key) {
        String env = System.getenv(toEnv(key));
        if (env != null && !env.isBlank()) return env;
        String v = PROPS.getProperty(key);
        if (v == null) throw new IllegalStateException("Missing config key: " + key);
        return v;
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key).trim());
    }

    public static boolean getBoolean(String key) {
        return Boolean.parseBoolean(get(key).trim());
    }

    private static String toEnv(String key) {
        return key.toUpperCase().replace('.', '_');
    }
}
