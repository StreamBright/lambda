package com.streambright.lambda;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.streambright.lambda.config.RdsConfig;
import com.streambright.lambda.sql.DataBase;
import org.boon.json.JsonFactory;
import org.boon.json.ObjectMapper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Optional;

import static com.streambright.lambda.env.Environment.getRdsConfig;
import static com.streambright.lambda.env.Environment.getRegion;
import static com.streambright.lambda.sql.DataBase.processRst;


public class DbmgmtJava implements RequestHandler<Object, Object> {


    private static void runTask() {

        // Get region
        System.out.println("Getting region...");
        String region = getRegion();
        System.out.println("Region is :: " + region);

        // Get rdsConfigJson
        System.out.println("Getting RDS config...");
        String rdsConfigJson = getRdsConfig(region);

        // Parse JSON to POJO
        ObjectMapper mapper = JsonFactory.create();
        RdsConfig rdsConfig = mapper.readValue(rdsConfigJson, RdsConfig.class);

        // Get jdbcUrl
        System.out.println("Getting JDBC url...");
        String jdbcUrl = DataBase.getJdbcUrl(
                rdsConfig.getHost(),
                rdsConfig.getDbname(),
                rdsConfig.getUser(),
                rdsConfig.getPassword()
        );

        // Get database connection
        System.out.println("Connecting to the database...");
        Optional<Connection> dbConn = DataBase.getConnection(jdbcUrl);

        // Queries

        String queryNow = "SELECT NOW();";
        String queryMv = "SELECT matviewname FROM pg_matviews;";

        if (dbConn.isPresent()) {
            System.out.println("Connected...");
            Statement smt = DataBase.getStatement(dbConn.get()).get();
            System.out.println("Executing query :: " + queryNow);
            Optional<ResultSet> resNow = DataBase.getResults(smt, queryNow);
            processRst(resNow);
            System.out.println("Executing query :: " + queryMv);
            Optional<ResultSet> resMv = DataBase.getResults(smt, queryMv);
            processRst(resMv);

            try {
                System.out.println("Closing database connection...");
                dbConn.get().close();
                System.out.println("Database connection has been closed");

            } catch (Exception e) {
                System.out.println("Error while closing database connection...");
            }
        }

    }

    public static void main(String[] args) {
        runTask();
    }

    @Override
    public String handleRequest(Object in, Context ctx) {
        runTask();
        return "ok";
    }
}
