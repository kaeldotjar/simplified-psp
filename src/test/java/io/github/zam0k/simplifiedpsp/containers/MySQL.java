package io.github.zam0k.simplifiedpsp.containers;

import org.testcontainers.containers.MySQLContainer;

public class MySQL extends MySQLContainer<MySQL> {

    private static final MySQL MYSQL = new MySQL();

    private MySQL() {
        super("mysql:8.0.29");
    }

    public static MySQL getInstance() {
        return MYSQL;
    }

    @Override
    public void start() {
        super.start();
        System.setProperty("DB_URL", MYSQL.getJdbcUrl());
        System.setProperty("DB_USERNAME", MYSQL.getUsername());
        System.setProperty("DB_PASSWORD", MYSQL.getPassword());
    }

}
