/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formularios;

import conexion.conexionSQL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
/**
 *
 * @author Compax
 */
public class sistema extends javax.swing.JFrame {
    conexionSQL cc = new conexionSQL();
    Connection con = cc.conexion();
    /**
     * Creates new form sistema
     */
    public sistema() {
        initComponents();
        this.setLocationRelativeTo(null);
        setNombre();
        this.setIconImage(new ImageIcon(getClass().getResource("/img/icon.png")).getImage());
        b1_vent.setVisible(false);
        b2_agprod.setVisible(false);
        b3_agprom.setVisible(false);
        b4_inv.setVisible(false);
        b5_cerrarc1.setVisible(false);
    }
    
    public void setNombre() {
        
        String n = usuarios.user.getText();
        String c = String.valueOf(usuarios.pass.getPassword());
        String SQL2 = "Select id from log where usuario='"+n+"' and contrasenia='"+c+"';";
        int b=0;
        String nom="";
        String ap="";
       
        try {
            Statement st = con.createStatement();
            ResultSet rs2 = st.executeQuery(SQL2);
            
            while (rs2.next()) {
                b = rs2.getInt(1);
            }  
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al recibir ID "+e);
        }
        
        String SQL3 = "Select nombre,apellido from empleados where idempleados="+b+";";
        try {
            Statement stt = con.createStatement();
            ResultSet rs3 = stt.executeQuery(SQL3);
            
            while (rs3.next()) {
                nom = rs3.getString(1);
                ap = rs3.getString(2);
            }
            nom = nom.concat(" "+ap);
            name.setText(nom);

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error al pasar Nombre"+e);
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        b4 = new javax.swing.JPanel();
        b4_inv = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        b1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        b1_vent = new javax.swing.JPanel();
        b2 = new javax.swing.JPanel();
        b2_agprod = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        b3 = new javax.swing.JPanel();
        b3_agprom = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        b5 = new javax.swing.JPanel();
        b5_cerrarc1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        name = new javax.swing.JLabel();
        bienvenido = new javax.swing.JLabel();
        foto = new rojerusan.RSFotoCircle();
        jPanel2 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(35, 50, 55));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(152, 75, 67));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        b4.setBackground(new java.awt.Color(30, 57, 42));
        b4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        b4_inv.setBackground(new java.awt.Color(130, 128, 129));
        b4_inv.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                b4_invMousePressed(evt);
            }
        });

        javax.swing.GroupLayout b4_invLayout = new javax.swing.GroupLayout(b4_inv);
        b4_inv.setLayout(b4_invLayout);
        b4_invLayout.setHorizontalGroup(
            b4_invLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        b4_invLayout.setVerticalGroup(
            b4_invLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        b4.add(b4_inv, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 50));

        jLabel1.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Inventario");
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel1MousePressed(evt);
            }
        });
        b4.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 130, 50));

        jPanel3.add(b4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 320, 150, 50));

        b1.setBackground(new java.awt.Color(30, 57, 42));
        b1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel2.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Ventas");
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel2MousePressed(evt);
            }
        });
        b1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 130, 50));

        b1_vent.setBackground(new java.awt.Color(130, 128, 129));

        javax.swing.GroupLayout b1_ventLayout = new javax.swing.GroupLayout(b1_vent);
        b1_vent.setLayout(b1_ventLayout);
        b1_ventLayout.setHorizontalGroup(
            b1_ventLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        b1_ventLayout.setVerticalGroup(
            b1_ventLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        b1.add(b1_vent, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jPanel3.add(b1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 140, 150, 50));

        b2.setBackground(new java.awt.Color(30, 57, 42));
        b2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        b2_agprod.setBackground(new java.awt.Color(130, 128, 129));

        javax.swing.GroupLayout b2_agprodLayout = new javax.swing.GroupLayout(b2_agprod);
        b2_agprod.setLayout(b2_agprodLayout);
        b2_agprodLayout.setHorizontalGroup(
            b2_agprodLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        b2_agprodLayout.setVerticalGroup(
            b2_agprodLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        b2.add(b2_agprod, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jLabel4.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Agregar Prod");
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel4MousePressed(evt);
            }
        });
        b2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 130, 50));

        jPanel3.add(b2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 150, 50));

        b3.setBackground(new java.awt.Color(30, 57, 42));
        b3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        b3_agprom.setBackground(new java.awt.Color(130, 128, 129));
        b3_agprom.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                b3_agpromMousePressed(evt);
            }
        });

        javax.swing.GroupLayout b3_agpromLayout = new javax.swing.GroupLayout(b3_agprom);
        b3_agprom.setLayout(b3_agpromLayout);
        b3_agpromLayout.setHorizontalGroup(
            b3_agpromLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        b3_agpromLayout.setVerticalGroup(
            b3_agpromLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        b3.add(b3_agprom, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        jLabel5.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Agregar Promo");
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel5MousePressed(evt);
            }
        });
        b3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 130, 50));

        jPanel3.add(b3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 260, 150, 50));

        b5.setBackground(new java.awt.Color(30, 57, 42));
        b5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        b5_cerrarc1.setBackground(new java.awt.Color(130, 128, 129));
        b5_cerrarc1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                b5_cerrarc1MousePressed(evt);
            }
        });

        javax.swing.GroupLayout b5_cerrarc1Layout = new javax.swing.GroupLayout(b5_cerrarc1);
        b5_cerrarc1.setLayout(b5_cerrarc1Layout);
        b5_cerrarc1Layout.setHorizontalGroup(
            b5_cerrarc1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        b5_cerrarc1Layout.setVerticalGroup(
            b5_cerrarc1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );

        b5.add(b5_cerrarc1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 50));

        jLabel3.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Cerrar Caja");
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel3MousePressed(evt);
            }
        });
        b5.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 130, 50));

        jPanel3.add(b5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 380, 150, 50));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 150, 560));

        jPanel4.setBackground(new java.awt.Color(24, 18, 30));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        name.setFont(new java.awt.Font("Berlin Sans FB", 0, 45)); // NOI18N
        name.setForeground(new java.awt.Color(255, 255, 255));
        jPanel4.add(name, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 34, 380, 50));

        bienvenido.setFont(new java.awt.Font("Berlin Sans FB", 0, 45)); // NOI18N
        bienvenido.setForeground(new java.awt.Color(255, 255, 255));
        bienvenido.setText("Bienvenido, ");
        jPanel4.add(bienvenido, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 34, -1, 50));

        foto.setImagenDefault(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_user_120px.png"))); // NOI18N
        jPanel4.add(foto, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 20, 100, 100));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 0, 710, 140));

        jPanel2.setBackground(new java.awt.Color(234, 198, 122));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 120, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 560, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 0, 120, 560));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 980, 560));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jLabel4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MousePressed
        b2_agprod.setVisible(true);
        b1_vent.setVisible(false);
        b3_agprom.setVisible(false);
        b4_inv.setVisible(false);
        b5_cerrarc1.setVisible(false);
    }//GEN-LAST:event_jLabel4MousePressed

    private void jLabel2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MousePressed
        b1_vent.setVisible(true);
        b2_agprod.setVisible(false);
        b3_agprom.setVisible(false);
        b4_inv.setVisible(false);
        b5_cerrarc1.setVisible(false);
    }//GEN-LAST:event_jLabel2MousePressed

    private void b3_agpromMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_b3_agpromMousePressed
        
    }//GEN-LAST:event_b3_agpromMousePressed

    private void b4_invMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_b4_invMousePressed
        
    }//GEN-LAST:event_b4_invMousePressed

    private void jLabel5MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MousePressed
        b3_agprom.setVisible(true);
        b2_agprod.setVisible(false);
        b1_vent.setVisible(false);
        b4_inv.setVisible(false);
        b5_cerrarc1.setVisible(false);
    }//GEN-LAST:event_jLabel5MousePressed

    private void jLabel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MousePressed
        b4_inv.setVisible(true);
        b2_agprod.setVisible(false);
        b3_agprom.setVisible(false);
        b1_vent.setVisible(false);
        b5_cerrarc1.setVisible(false);
    }//GEN-LAST:event_jLabel1MousePressed

    private void b5_cerrarc1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_b5_cerrarc1MousePressed
        
    }//GEN-LAST:event_b5_cerrarc1MousePressed

    private void jLabel3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MousePressed
        b5_cerrarc1.setVisible(true);
        b2_agprod.setVisible(false);
        b3_agprom.setVisible(false);
        b1_vent.setVisible(false);
        b4_inv.setVisible(false);
    }//GEN-LAST:event_jLabel3MousePressed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(sistema.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new sistema().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel b1;
    private javax.swing.JPanel b1_vent;
    private javax.swing.JPanel b2;
    private javax.swing.JPanel b2_agprod;
    private javax.swing.JPanel b3;
    private javax.swing.JPanel b3_agprom;
    private javax.swing.JPanel b4;
    private javax.swing.JPanel b4_inv;
    private javax.swing.JPanel b5;
    private javax.swing.JPanel b5_cerrarc1;
    private javax.swing.JLabel bienvenido;
    private rojerusan.RSFotoCircle foto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel name;
    // End of variables declaration//GEN-END:variables

    private void setColor(JPanel b1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void resetColor(JPanel[] jPanel, JPanel[] jPanel0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
