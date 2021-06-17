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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
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
    
    public void MostrarTabla() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Producto");
        modelo.addColumn("Contenido");
        modelo.addColumn("Precio");
        modelo.addColumn("Categoria");
        modelo.addColumn("Stock");
        
        tabla_productos.setModel(modelo);
        
        String sql = "select * from productos order by categoria;";
        
        String prod[] = new String[5];
        
        try {
        Statement st = con.createStatement();
        ResultSet res = st.executeQuery(sql);
            while (res.next()) {
                prod[0] = res.getString(2);
                if (Double.parseDouble(res.getString(3))>3.0) {
                    prod[1] = res.getString(3)+" ml";
                } else {
                    prod[1] = res.getString(3)+" lts";
                }
                prod[2] = "$ "+res.getString(4)+" pesos";
                prod[3] = res.getString(5);
                prod[4] = res.getString(6);
                modelo.addRow(prod);
            }
            tabla_productos.setModel(modelo);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de Muestra "+e.getMessage());
        }
    }
    
    public DefaultComboBoxModel llenar() {
        DefaultComboBoxModel modelo = new DefaultComboBoxModel();
        String s ="select nombre_producto, categoria from productos order by nombre_producto;";
        modelo.addElement("Seleccionar");
        
        try {
          
          Statement st = con.createStatement();
          ResultSet res = st.executeQuery(s);
          while(res.next()) {
              modelo.addElement(res.getString(1)+", "+res.getString(2));
          }
          
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Error al llenar JCombox "+e.getMessage());
        }
        
        return modelo;
    }
    
    public void vender() throws SQLException {
        String nom;
        int id=0,a=0;
        nom = (String) prod.getSelectedItem();  
        String[] cat = nom.split(", ");
        nom = cat[0];
        String ca = cat[1];
        String sql = "Select stock from productos where nombre_producto='"+nom+"' and categoria='"+ca+"'";
        try {
        Statement st = con.createStatement();
        ResultSet res = st.executeQuery(sql);
            while (res.next()) {
                a = res.getInt(1)-Integer.parseInt(cantidad.getText());
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de Venta "+e.getMessage());
        }
        
        String sql2 = "Select idproductos from productos where nombre_producto='"+nom+"' and categoria='"+ca+"'";
        try {
        Statement st = con.createStatement();
        ResultSet res = st.executeQuery(sql2);
            while (res.next()) {
                id = res.getInt(1);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de Consulta ID "+e.getMessage());
        }
        
        String sql3 = "update productos set stock="+a+" where idproductos="+id+"";
        try {
        Statement st = con.createStatement();
        st.executeUpdate(sql3);
        JOptionPane.showConfirmDialog(null, "Confirmar Venta");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de UPDATE "+e.getMessage());
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
        datos_inventario = new javax.swing.JScrollPane();
        tabla_productos = new javax.swing.JTable();
        Reg_venta = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        barras = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btn_venta = new javax.swing.JButton();
        prod = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        cantidad = new javax.swing.JTextField();

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

        tabla_productos = new javax.swing.JTable() {
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        tabla_productos.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        tabla_productos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tabla_productos.setRowHeight(19);
        tabla_productos.setShowHorizontalLines(false);
        tabla_productos.getTableHeader().setResizingAllowed(false);
        tabla_productos.getTableHeader().setReorderingAllowed(false);
        datos_inventario.setViewportView(tabla_productos);

        jPanel1.add(datos_inventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 140, 710, 420));

        Reg_venta.setBackground(new java.awt.Color(24, 18, 30));
        Reg_venta.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel6.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Cantidad:");
        Reg_venta.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 140, -1, -1));

        barras.setFont(new java.awt.Font("Berlin Sans FB", 0, 10)); // NOI18N
        barras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barrasActionPerformed(evt);
            }
        });
        Reg_venta.add(barras, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 60, 190, 20));

        jLabel7.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Codigo de Barras");
        Reg_venta.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 60, -1, -1));

        btn_venta.setLabel("Vender");
        btn_venta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ventaActionPerformed(evt);
            }
        });
        Reg_venta.add(btn_venta, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 310, 130, 30));

        Reg_venta.add(prod, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 110, 190, -1));

        jLabel8.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Nombre:");
        Reg_venta.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 110, -1, -1));

        cantidad.setFont(new java.awt.Font("Berlin Sans FB", 0, 10)); // NOI18N
        cantidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cantidadActionPerformed(evt);
            }
        });
        Reg_venta.add(cantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 140, 190, 20));

        jPanel1.add(Reg_venta, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 140, 710, 420));

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
        b1_vent.setVisible(true); //VENTAAAAAAAASSSS
        b2_agprod.setVisible(false);
        b3_agprom.setVisible(false);
        b4_inv.setVisible(false);
        b5_cerrarc1.setVisible(false);
        Reg_venta.setVisible(true);
        datos_inventario.setVisible(false);
        prod.setModel(llenar());
        
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
        b4_inv.setVisible(true); //INVENTARIOOOOOOOO
        b2_agprod.setVisible(false);
        b3_agprom.setVisible(false);
        b1_vent.setVisible(false);
        b5_cerrarc1.setVisible(false);
        datos_inventario.setVisible(true);
        MostrarTabla();
        Reg_venta.setVisible(false);
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

    private void barrasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_barrasActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_barrasActionPerformed

    private void btn_ventaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ventaActionPerformed
        try {
            vender();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de UPDATE en funcion Vender del BTN "+e.getMessage());
        }
    }//GEN-LAST:event_btn_ventaActionPerformed

    private void cantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cantidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cantidadActionPerformed

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
    private javax.swing.JPanel Reg_venta;
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
    private javax.swing.JTextField barras;
    private javax.swing.JLabel bienvenido;
    private javax.swing.JButton btn_venta;
    private javax.swing.JTextField cantidad;
    private javax.swing.JScrollPane datos_inventario;
    private rojerusan.RSFotoCircle foto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JLabel name;
    private javax.swing.JComboBox<String> prod;
    private javax.swing.JTable tabla_productos;
    // End of variables declaration//GEN-END:variables

    private void setColor(JPanel b1) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void resetColor(JPanel[] jPanel, JPanel[] jPanel0) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private JComboBox<String> setModel(DefaultComboBoxModel llenar) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
