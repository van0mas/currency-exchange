package org.example.currencyexchange.dao;

import org.example.currencyexchange.exceptions.AppException;
import org.example.currencyexchange.exceptions.DAOException;
import org.example.currencyexchange.exceptions.DuplicateCurrencyException;
import org.example.currencyexchange.exceptions.ExchangeRateNotFoundException;
import org.example.currencyexchange.model.CurrencyModel;
import org.example.currencyexchange.model.ExchangeRateModel;
import org.example.currencyexchange.utils.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExchangeRateDAO {

    public List<ExchangeRateModel> getExchangeRates() throws AppException {
        String sql = "SELECT " +
                "ExchangeRates.id, " +
                "ExchangeRates.baseCurrencyId, " +
                "ExchangeRates.targetCurrencyId, " +
                "ExchangeRates.rate, " +
                "base_currency.code baseCurrencyCode, " +
                "base_currency.fullName baseCurrencyName, " +
                "base_currency.sign baseCurrencySign, " +
                "target_currency.code targetCurrencyCode, " +
                "target_currency.fullName targetCurrencyName, " +
                "target_currency.sign targetCurrencySign " +
                "FROM ExchangeRates " +
                "JOIN Currencies base_currency ON ExchangeRates.baseCurrencyId = base_currency.id " +
                "JOIN Currencies target_currency ON ExchangeRates.targetCurrencyId = target_currency.id";

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            List<ExchangeRateModel> exchangeRates = new ArrayList<>();
            while (rs.next()) {
                CurrencyModel baseCurrency = new CurrencyModel(
                        rs.getInt("baseCurrencyId"),
                        rs.getString("baseCurrencyName"),
                        rs.getString("baseCurrencyCode"),
                        rs.getString("baseCurrencySign")
                );
                CurrencyModel targetCurrency = new CurrencyModel(
                        rs.getInt("targetCurrencyId"),
                        rs.getString("targetCurrencyName"),
                        rs.getString("targetCurrencyCode"),
                        rs.getString("targetCurrencySign")
                );

                exchangeRates.add(new ExchangeRateModel(
                        rs.getInt("id"),
                        baseCurrency,
                        targetCurrency,
                        rs.getBigDecimal("rate")
                ));
            }
            return exchangeRates;

        } catch (SQLException e) {
            throw new DAOException(e.getMessage());
        }
    }

    public Optional<ExchangeRateModel> getExchangeRate(CurrencyModel baseCurrency, CurrencyModel targetCurrency) throws AppException {
        String sql = "SELECT ExchangeRates.id, ExchangeRates.rate " +
                "FROM ExchangeRates " +
                "WHERE baseCurrencyId = ? AND targetCurrencyId = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, baseCurrency.getId());
            stmt.setInt(2, targetCurrency.getId());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new ExchangeRateModel(
                            rs.getInt("id"),
                            baseCurrency,
                            targetCurrency,
                            rs.getBigDecimal("rate")
                    ));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new DAOException("Error retrieving currency");
        }
    }

    public ExchangeRateModel save(ExchangeRateModel exchangeRate) throws AppException {
        String sql = "INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, exchangeRate.getBaseCurrency().getId());
            stmt.setInt(2, exchangeRate.getTargetCurrency().getId());
            stmt.setBigDecimal(3, exchangeRate.getRate());
            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    return new ExchangeRateModel(
                            id,
                            exchangeRate.getBaseCurrency(),
                            exchangeRate.getTargetCurrency(),
                            exchangeRate.getRate()
                    );
                } else {
                    throw new DAOException("Failed to retrieve generated ID");
                }
            }
        } catch (SQLException e) {
            if (e.getErrorCode() == 19 && e.getMessage().contains("UNIQUE constraint failed")) {
                throw new DuplicateCurrencyException("Exchange rate already exists");
            }
            throw new DAOException("Error saving exchange rate");
        }
    }

    public ExchangeRateModel update(ExchangeRateModel exchangeRate) throws AppException {
        String sql = "UPDATE ExchangeRates SET rate = ? WHERE baseCurrencyId = ? AND targetCurrencyId = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setBigDecimal(1, exchangeRate.getRate());
            stmt.setInt(2, exchangeRate.getBaseCurrency().getId());
            stmt.setInt(3, exchangeRate.getTargetCurrency().getId());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new ExchangeRateNotFoundException("Exchange rate not found for update");
            }

            return exchangeRate;

        } catch (SQLException e) {
            throw new DAOException("Error updating exchange rate");
        }
    }
}
