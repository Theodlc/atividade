package com.template.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Centraliza somente a configuração e a abertura da conexão PostgreSQL. */
public final class Database {
    private static final String URL = "jdbc:postgresql://localhost:5432/bd_honkai";
    private static final String USUARIO = "postgres";
    private static final String SENHA = "postgres";

    private Database() {
    }

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, USUARIO, SENHA);
    }
}
