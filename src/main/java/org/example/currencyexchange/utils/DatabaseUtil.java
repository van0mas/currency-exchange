package org.example.currencyexchange.utils;


import java.sql.*;

public class DatabaseUtil {

    private static final String DB_URL = "jdbc:sqlite:currency_exchange.db";

    public static void initDatabase() {
        System.out.println("Working directory: " + System.getProperty("user.dir"));

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {

            // Create Currencies table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Currencies (
                    ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    Code VARCHAR(3) NOT NULL UNIQUE,
                    FullName VARCHAR(100) NOT NULL,
                    Sign VARCHAR(5) NOT NULL
                )
                """);

            // Create ExchangeRates table (USD as base currency)
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS ExchangeRates (
                    ID INTEGER PRIMARY KEY AUTOINCREMENT,
                    BaseCurrencyId INTEGER NOT NULL,
                    TargetCurrencyId INTEGER NOT NULL,
                    Rate DECIMAL(6) NOT NULL,
                    FOREIGN KEY (BaseCurrencyId) REFERENCES Currencies(ID),
                    FOREIGN KEY (TargetCurrencyId) REFERENCES Currencies(ID),
                    UNIQUE (BaseCurrencyId, TargetCurrencyId)
                )
                """);

            // Check if Currencies table is empty
            if (isTableEmpty(conn, "Currencies")) {
                // Insert main currencies (USD will have ID=1)
                stmt.executeUpdate("""
                    INSERT INTO Currencies (Code, FullName, Sign) VALUES
                    ('USD', 'US Dollar', '$'),
                    ('EUR', 'Euro', '€'),
                    ('GBP', 'British Pound', '£'),
                    ('JPY', 'Japanese Yen', '¥'),
                    ('AUD', 'Australian Dollar', 'A$'),
                    ('CAD', 'Canadian Dollar', 'C$'),
                    ('RUB', 'Russian Ruble', '₽')
                    """);

                // Insert exchange rates (all pairs USD → other currencies)
                stmt.executeUpdate("""
                    INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES
                    (1, 2, 0.88),   -- USD → EUR
                    (1, 3, 0.75),   -- USD → GBP
                    (1, 4, 143.96), -- USD → JPY
                    (1, 5, 1.57),   -- USD → AUD
                    (1, 6, 1.39),   -- USD → CAD
                    (1, 7, 82)      -- USD → RUB
                    """);

                System.out.println("Main currencies and exchange rates added (USD as base currency)");
            }

        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
        }
    }

    private static boolean isTableEmpty(Connection conn, String tableName) throws SQLException {
        String query = "SELECT COUNT(*) FROM " + tableName;
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return rs.getInt(1) == 0;
        }
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL);
        conn.createStatement().execute("PRAGMA foreign_keys = ON");
        return conn;
    }
}
