package DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Model.Message;
import Util.ConnectionUtil;

/**
 * Data Access Object DAO for handling Message-realted database operations
 */


public class MessageDAO {
    // Inserts new Messages into the database
    public Message insertMessage(Message message) {
        String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, message.getPosted_by());
            pstmt.setString(2, message.getMessage_text());
            pstmt.setLong(3, message.getTime_posted_epoch());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                return null;
            }
            
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    int messageId = rs.getInt(1);
                    return new Message(messageId, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Retrieves all the Messages from the database

    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM Message";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                messages.add(new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }

    // Retrieves Messages by ID from the database

    public Message getMessageById(int messageId) {
        String sql = "SELECT * FROM Message WHERE message_id = ?";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, messageId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Deletes a Message from the database using the message_id

    public Message deleteMessage(int messageId) {
        Message message = getMessageById(messageId);
        if (message == null) {
            return null;
        }
        
        String sql = "DELETE FROM Message WHERE message_id = ?";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, messageId);
            int rowsDeleted = pstmt.executeUpdate();

            return (rowsDeleted > 0) ? message : null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Updates the text of existing message

    public Message updateMessage(int messageId, String newMessageText) {
        String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newMessageText);
            pstmt.setInt(2, messageId);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                return getMessageById(messageId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Retrieves all the Messages by a specific user

    // Uses sql query to select messages by user ID 

    public List<Message> getMessagesByUser(int accountId) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM Message WHERE posted_by = ?";
        try (Connection conn = ConnectionUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    messages.add(new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return messages;
    }
}
