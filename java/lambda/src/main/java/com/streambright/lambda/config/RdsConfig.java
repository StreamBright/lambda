package com.streambright.lambda.config;

public class RdsConfig {

    private String dbtype;
    private String dbname;
    private String host;
    private String user;
    private String password;
    private String ssl;
    private String sslfactory;

    public RdsConfig(String dbtype, String dbname, String host, String user, String password, String ssl, String sslfactory) {
        this.dbtype = dbtype;
        this.dbname = dbname;
        this.host = host;
        this.user = user;
        this.password = password;
        this.ssl = ssl;
        this.sslfactory = sslfactory;
    }

    public String getDbtype() {
        return dbtype;
    }

    public void setDbtype(String dbtype) {
        this.dbtype = dbtype;
    }

    public String getDbname() {
        return dbname;
    }

    public void setDbname(String dbname) {
        this.dbname = dbname;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSsl() {
        return ssl;
    }

    public void setSsl(String ssl) {
        this.ssl = ssl;
    }

    public String getSslfactory() {
        return sslfactory;
    }

    public void setSslfactory(String sslfactory) {
        this.sslfactory = sslfactory;
    }
}
