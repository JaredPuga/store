/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class conexionSQL {
    Connection conectar = null;
    
    public Connection conexion() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            
            conectar = (Connection) DriverManager.getConnection("jdbc:mysql://localhost/login?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC","root","mrrobot2001");
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null,"Error de conexion "+ e.getMessage());
            System.out.println(e.getMessage());
        }
        
        return conectar;
    }
}
