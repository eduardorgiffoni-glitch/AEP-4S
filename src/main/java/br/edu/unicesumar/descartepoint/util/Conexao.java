package br.edu.unicesumar.descartepoint.util;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public final class Conexao {
    private Conexao() {}
    private static String env(String name,String fallback){String v=System.getenv(name);return v==null||v.isBlank()?fallback:v;}
    public static Connection abrir() throws SQLException {
        String url=env("DB_URL","jdbc:mysql://localhost:3306/descarte_point?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
        return DriverManager.getConnection(url,env("DB_USER","root"),env("DB_PASSWORD",""));
    }
}