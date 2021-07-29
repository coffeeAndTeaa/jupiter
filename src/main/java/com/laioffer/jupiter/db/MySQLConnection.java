package com.laioffer.jupiter.db;

import com.laioffer.jupiter.entity.Item;

import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class MySQLConnection {
    private final Connection conn;

    // create a connection to the MySQL database
    public MySQLConnection () throws MySQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(MySQLDBUtil.getMySQLAddress());
        } catch (Exception e) {
            e.printStackTrace();
            throw new MySQLException("failed to connect to the data base");
        }
    }

    public void close() {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    // Insert an item into database
    public void saveItem(Item item) throws MySQLException {
        if (conn == null) {
            System.err.println("DB connection failed");
            throw new MySQLException("failed to connect to the database");
        }
        String sql = "INSERT IGNORE INTO items VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, item.getId());
            statement.setString(2,item.getTitle());
            statement.setString(3,item.getUrl());
            statement.setString(4, item.getThumbnailUrl());
            statement.setString(5, item.getBroadcasterName());
            statement.setString(6, item.getGameId());
            statement.setString(7, item.getType().toString());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            throw new MySQLException("Failed to add item to the Database");
        }
    }

    // Insert a favorite item into the database
    public void setFavoriteItem(String userId, Item item) throws MySQLException {
        if (conn == null) {
            System.err.println("DB connection failed");
            throw new MySQLException("failed to connect to the database");
        }

        // need to make sure the item is added to the database first because the foreign key restriction on
        // item_id(favorite_records) -> id(items)
        saveItem(item);

        // using ? and PreparedStatement to prevent SQL injection.

        String sql = "INSERT IGNORE INTO favorite_records (user_id, item_id) VALUES (?, ?)";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, userId);
            statement.setString(2, item.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new MySQLException("failed to save favorite item into Database");
        }
    }

    public void unsetFavoriteItem(String userId, String itemId) throws MySQLException{
        if (conn == null) {
            System.err.println("DB connection failed");
            throw new MySQLException("failed to connect to the database");
        }

        String sql = "DELETE FROM favorite_records WHERE user_id = ? AND item_id = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1,userId);
            statement.setString(2,itemId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new MySQLException("failed to delete favorite_record");
        }
    }

    // get favorite item ids for the given user
    public Set<String> getFavoriteItemIds(String userId) throws MySQLException {
        if (conn == null) {
            System.err.println("DB connection failed");
            throw new MySQLException("failed to connect to the database");
        }
        Set<String> favoriteItems = new HashSet<>();
        String sql = "SELECT item_id FROM favorite_records WHERE user_id = ?";
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setString(1, userId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                String itemId = rs.getString("item_id");
                favoriteItems.add(itemId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new MySQLException("failed to get favorite item ids from database");
        }
        return favoriteItems;
    }



}
