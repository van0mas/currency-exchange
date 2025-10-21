package org.example.currencyexchange.utils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class DatabaseInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            Class.forName("org.sqlite.JDBC");
            DatabaseUtil.initDatabase();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to init DB", e);
        }
    }
}
