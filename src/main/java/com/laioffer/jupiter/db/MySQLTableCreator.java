package com.laioffer.jupiter.db;

import java.sql.Connection;
import java.sql.DriverManager;

public class MySQLTableCreator {
    public static void main (String[] args) {
        try {
            System.out.println("Connecting to " + MySQLDBUtil.getMySQLAddress());
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
            Connection conn = DriverManager.getConnection(MySQLDBUtil.getMySQLAddress());

            if (conn == null) {
                return;
            }

            conn.close();
            System.out.println("Import done successfully");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
