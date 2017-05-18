package com.streambright.lambda.sql;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataBase {

    private static Optional<String> encodePass(String pass) {
        try {
            return Optional.of(URLEncoder.encode(pass, "UTF-8"));
        } catch (UnsupportedEncodingException useex) {
            return Optional.empty();
        }
    }

    public static String getJdbcUrl(String host, String db, String user, String pass) {

        Optional<String> passEncodedOptional = encodePass(pass);
        String passEncoded = (passEncodedOptional.isPresent() ? passEncodedOptional.get() : "");

        return "jdbc:postgresql://" + host + ":5432" + "/" + db + "?user=" + user +
                "&password=" + passEncoded +
                "&ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory";

    }

    public static Optional<Connection> getConnection(String url) {
        try {
            return Optional.of(DriverManager.getConnection(url));
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public static Optional<Statement> getStatement(Connection dbConn) {
        try {
            return Optional.of(dbConn.createStatement());
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public static Optional<ResultSet> getResults(Statement smt, String query) {
        try {
            return Optional.of(smt.executeQuery(query));
        } catch (SQLException e) {
            return Optional.empty();
        }
    }

    public static void processRst(Optional<ResultSet> resultSetOptional) {
        Optional<List<String>> optionalRows = resultSetOptional.map(r -> {
            List<String> lst = new ArrayList<>();
            try {
                while (r.next()) {
                    lst.add(r.getString(1));
                }
            } catch (SQLException sqle) {
                lst.clear();
            }
            return lst;
        });

        if (optionalRows.isPresent()) {
            optionalRows.get().forEach(System.out::println);
        } else {
            System.err.println("Empty resultset");
        }
    }
}
