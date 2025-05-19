package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.*;

// DAO class for account related database operations

public class AccountDAO {

    // Insert New Account
    public Account insertAccount(Account account) {
        String sql = "INSERT INTO Account (username, password) VALUES (?, ?)";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            
            int affectedRows = ps.executeUpdate();
            if (affectedRows == 0) {
                return null;
            }
            
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    int accountId = rs.getInt(1);
                    return new Account(accountId, account.getUsername(), account.getPassword());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Retrieves an account from the database by username

    public Account getAccountByUsername(String username) {
        String sql = "SELECT * FROM Account WHERE username = ?";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Account(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

     // Retrieves an account from the database by ID
    public Account getAccountById(int accountId) {
        String sql = "SELECT * FROM Account WHERE account_id = ?";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Account(
                        rs.getInt("account_id"),
                        rs.getString("username"),
                        rs.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}