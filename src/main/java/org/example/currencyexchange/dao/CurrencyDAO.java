package org.example.currencyexchange.dao;

import org.example.currencyexchange.exceptions.AppException;
import org.example.currencyexchange.exceptions.DAOException;
import org.example.currencyexchange.exceptions.DuplicateCurrencyException;
import org.example.currencyexchange.model.CurrencyModel;
import org.example.currencyexchange.utils.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CurrencyDAO {

    public List<CurrencyModel> getCurrencies() throws AppException {
        String sql = "SELECT * FROM currencies";

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            List<CurrencyModel> currencies = new ArrayList<>();
            while (rs.next()) {
                currencies.add(new CurrencyModel(
                        rs.getInt("id"),
                        rs.getString("fullName"),
                        rs.getString("code"),
                        rs.getString("sign")
                ));
            }
            return currencies;

        } catch (SQLException e) {
            throw new DAOException("Error retrieving all currencies");
        }
    }

    public Optional<CurrencyModel> getCurrency(String code) throws AppException {
        String sql = "SELECT * FROM currencies WHERE code = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, code);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new CurrencyModel(
                            rs.getInt("id"),
                            rs.getString("fullName"),
                            rs.getString("code"),
                            rs.getString("sign")
                    ));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new DAOException("Error retrieving currency");
        }
    }

    public CurrencyModel save(CurrencyModel currency) throws AppException {
        String sql = "INSERT INTO currencies(code, fullName, sign) VALUES(?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, currency.getCode());
            stmt.setString(2, currency.getName());
            stmt.setString(3, currency.getSign());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    return new CurrencyModel(id, currency.getName(), currency.getCode(), currency.getSign());
                } else {
                    throw new DAOException("Failed to retrieve generated ID");
                }
            }

        } catch (SQLException e) {
            // Handle unique constraint violation for currency code
            if (e.getErrorCode() == 19 && e.getMessage().contains("UNIQUE constraint failed")) {
                throw new DuplicateCurrencyException(
                        "Currency with code " + currency.getCode() + " already exists"
                );
            }
            throw new DAOException("Error saving currency");
        }
    }
}
