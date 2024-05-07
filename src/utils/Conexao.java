package utils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *
 * @author Otavio
 */
public class Conexao {
    private Connection conn = null;
    private PreparedStatement preStatement = null;
    
     public static Connection getConexao() {
        Conexao conexao = new Conexao();
        return conexao.getConnection();
    }

    private String filePath = "C:/sqlite/";
    private String dbFileName = "clientes";
    
    
    public Conexao() {
      
        File file = new File("C:\\sqlite");        
        if (!file.exists()) 
            file.mkdir();             
            
        
        String url = "jdbc:sqlite:" + filePath + dbFileName;
        
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        
        try {
            conn = DriverManager.getConnection(url);     
            //System.out.println("Connection Successful");
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
               
    }
    private Connection getConnection() {
        return conn;
    }
    
}