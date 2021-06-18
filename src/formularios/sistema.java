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
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import java.util.Date;
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
        
        
        Reg_venta.setVisible(false);
        datos_inventario.setVisible(false);
        Agregar_prod.setVisible(false);
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
        modelo.addColumn("Codigo de Barras");
        modelo.addColumn("Contenido");
        modelo.addColumn("Precio");
        modelo.addColumn("Categoria");
        modelo.addColumn("Stock");
        
        tabla_productos.setModel(modelo);
        
        String sql = "select * from productos order by categoria;";
        
        String prod[] = new String[6];
        
        try {
        Statement st = con.createStatement();
        ResultSet res = st.executeQuery(sql);
            while (res.next()) {
                prod[0] = res.getString(3);
                prod[1] = res.getString(2);
                if (Double.parseDouble(res.getString(4))>3.0) {
                    prod[2] = res.getString(4)+" ml";
                } else {
                    prod[2] = res.getString(4)+" lts";
                }
                prod[3] = "$ "+res.getString(5)+" pesos";
                prod[4] = res.getString(6);
                prod[5] = res.getString(7);
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
    
    public void actualizarSuma() {
        double tventas=0;
        String sql8 = "Select sum(total) from ventas;";
        try {
          Statement st = con.createStatement();
          ResultSet res = st.executeQuery(sql8);
          while(res.next()) {
              if(res.getString(1)==null) {
                  caja.setText("0");
              } else {
                tventas = Double.parseDouble(res.getString(1));
              }
          }          
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error obtener suma total ventas"+e.getMessage());
        }
        
        String vv = Double.toString(tventas);
        caja.setText(vv);
        
    }
    
    
    public void vender() throws SQLException {
        String nom;
        int id=0,a=0;
        nom = (String) prod.getSelectedItem();  
        String[] cat = nom.split(", ");
        nom = cat[0];
        String ca = cat[1];
        String sql = "Select stock from productos where nombre_producto='"+nom+"' and categoria='"+ca+"';"; //Se actualiza el stock
        try {
        Statement st = con.createStatement();
        ResultSet res = st.executeQuery(sql);
            while (res.next()) {
                a = res.getInt(1)-Integer.parseInt(cantidad.getText());
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de Venta "+e.getMessage());
        }
        
        String sql2 = "Select idproductos from productos where nombre_producto='"+nom+"' and categoria='"+ca+"';"; //Para obtener el id
        try {
        Statement st = con.createStatement();
        ResultSet res = st.executeQuery(sql2);
            while (res.next()) {
                id = res.getInt(1);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de Consulta ID "+e.getMessage());
        }
        
        String sql3 = "update productos set stock="+a+" where idproductos="+id+";"; //Se actualiza el stock
        try {
        Statement st = con.createStatement();
        st.executeUpdate(sql3);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de UPDATE "+e.getMessage());
        }
        
        int maxv=0;
        double pre = 0,tot = 0;
        String sql5 = "Select precio from productos where idproductos="+id+";"; //Se retorna el precio del producto
        try {
          Statement st = con.createStatement();
          ResultSet res = st.executeQuery(sql5);
          while(res.next()) {
              pre =Double.parseDouble(res.getString(1));
          }
          
          tot = pre*(Double.parseDouble(cantidad.getText()));
          
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error hacer operacion total"+e.getMessage());
        }
        
        ActualizarVentas();
        
        String sql6 = "Select max(idventa) from ventas;";
        try {
          Statement st = con.createStatement();
          ResultSet res = st.executeQuery(sql6);
          while(res.next()) {
              maxv = Integer.parseInt(res.getString(1));
          }          
        } catch (NumberFormatException | SQLException e) {
            JOptionPane.showMessageDialog(null, "Error obtener maxidventas id"+e.getMessage());
        }
        
        String sql7 = "update ventas set total="+tot+" where idventa="+maxv+";";
        try {
        Statement st = con.createStatement();
        st.executeUpdate(sql7);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de UPDATE para total"+e.getMessage());
        }
        
        double s = Double.parseDouble(caja.getText());
        String aa = Double.toString(s+(Double.parseDouble(caja.getText())));
        caja.setText(aa);
        
        actualizarSuma();
    }
    
    
    
    public void AgregarProducto() {
        double pr = Double.parseDouble(precio.getText());
        int sto = Integer.parseInt(stock.getText());
        String sql = "insert into productos (nombre_producto, contenido, precio, categoria, stock) VALUES ('"+nombre.getText()+"', '"+contenido.getText()+"', '"+pr+"', '"+categoria.getText()+"', '"+sto+"');";
        try {
        Statement st = con.createStatement();
        st.executeUpdate(sql);
        JOptionPane.showMessageDialog(null, "Producto Agregado Correctamente");
        nombre.setText(null);
        contenido.setText(null);
        precio.setText(null);
        categoria.setText(null);
        stock.setText(null);
        
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error agregando producto "+e.getMessage());
        }
        
        
    }
    
    
    public void MostrarTablaVentas() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Venta No");
        modelo.addColumn("Producto");
        modelo.addColumn("Categoria");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Total");
        
        
        tabla_ventas.setModel(modelo);
        
        String sql = "SELECT idventa, producto,categoria,cantidad,total FROM ventas Order by idventa;";
        
        String vent[] = new String[5];
        
        try {
        Statement st = con.createStatement();
        ResultSet res = st.executeQuery(sql);
            while (res.next()) {
                vent[0] = res.getString(1);//idvente
                vent[1] = res.getString(2);//producto
                vent[2] = res.getString(3);//categoria
                vent[3] = res.getString(4);//cantidad
                vent[4] = "$ "+res.getString(5)+" "; //total
                modelo.addRow(vent);
            }
            tabla_productos.setModel(modelo);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de Muestra "+e.getMessage());
        }
    }
    
    public String getFecha() {
        Date fecha = new Date();
        SimpleDateFormat fech = new SimpleDateFormat("dd/MM/YYYY");
        return fech.format(fecha);
    }
    
    public void Buscar() {
        String cod = barras.getText();
        int ee=0;
        
        int tam = prod.getItemCount();
        String pro[] = new String[tam];
        String nombree[] = new String[tam];
        String categ[] = new String[tam];
        for (int i = 1; i < tam; i++) {
            pro[i-1] = (String) prod.getItemAt(i);
            String[] div = pro[i-1].split(", ");
            nombree[i-1] = div[0];
            categ[i-1] = div[1];
        }
       String sql = "SELECT nombre_producto, categoria, precio FROM productos WHERE codigobarras="+cod+";";
        try {
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery(sql);
   
            while (res.next()) {
                String n = res.getString(1);
                String c = res.getString(2);
                pre.setText("$ "+res.getString(3));
                for (int j = 0; j < tam-1; j++) { //Recorrer los nombres y las categorias por separado
                    if (nombree[j].equals(n) && categ[j].equals(c)) {
                        ee = j;
                    }
                } 
            }
            if (ee==0) {
                prod.setSelectedIndex(0);
            } else {
                prod.setSelectedIndex(ee+1);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error, el producto no existe ");
        }  
    }
    
    public void ActualizarVentas() {
        if (prod.getSelectedIndex()!=0) {
           String n = (String)prod.getSelectedItem();
           String[] div = n.split(", ");
           String np = div[0];
           String cp = div[1];
           String prp ="";
           for (int i=0;i<pre.getText().length();i++) {
               if(Character.isDigit(pre.getText().charAt(i))) {
                   prp+=pre.getText().charAt(i);
               }
           }

           String sql = "insert into ventas (producto, categoria, cantidad, total) VALUES ('"+np+"', '"+cp+"', "+cantidad.getText()+", "+prp+");";
           try {
               Statement st = con.createStatement();
               st.executeUpdate(sql);
               barras.setText(null);
               pre.setText(null);
               cantidad.setText(null);
               prod.setSelectedIndex(0);
               JOptionPane.showMessageDialog(null, "Venta agregada Correctamente");
           } catch (SQLException e) {
               JOptionPane.showMessageDialog(null, "Error agregando venta "+e.getMessage());
           }
           
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
        barras = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btn_venta = new javax.swing.JButton();
        prod = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        cantidad = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla_ventas = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        labelfecha = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        pre = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        caja = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        Agregar_prod = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        nombre = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel10 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        contenido = new javax.swing.JTextField();
        jSeparator3 = new javax.swing.JSeparator();
        precio = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        categoria = new javax.swing.JTextField();
        mlolts = new javax.swing.JLabel();
        stock = new javax.swing.JTextField();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel13 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();

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

        barras.setBackground(new java.awt.Color(24, 18, 30));
        barras.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        barras.setForeground(new java.awt.Color(255, 255, 255));
        barras.setBorder(null);
        barras.setOpaque(false);
        barras.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barrasActionPerformed(evt);
            }
        });
        barras.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                barrasKeyTyped(evt);
            }
        });
        Reg_venta.add(barras, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 40, 130, 20));

        jLabel7.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Codigo de Barras:");
        Reg_venta.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 140, -1));

        btn_venta.setLabel("Vender");
        btn_venta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_ventaActionPerformed(evt);
            }
        });
        Reg_venta.add(btn_venta, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 240, 130, 30));

        prod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prodActionPerformed(evt);
            }
        });
        Reg_venta.add(prod, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 90, 170, -1));

        jLabel8.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Nombre:");
        Reg_venta.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 90, -1, -1));

        cantidad.setBackground(new java.awt.Color(24, 18, 30));
        cantidad.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        cantidad.setForeground(new java.awt.Color(255, 255, 255));
        cantidad.setBorder(null);
        cantidad.setOpaque(false);
        cantidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cantidadActionPerformed(evt);
            }
        });
        Reg_venta.add(cantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 130, 50, 20));

        jScrollPane1.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N

        tabla_ventas = new javax.swing.JTable() {
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        tabla_ventas.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        tabla_ventas.getTableHeader().setReorderingAllowed(false);
        tabla_ventas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tabla_ventas.setRowHeight(19);
        tabla_ventas.setRowSelectionAllowed(false);
        tabla_ventas.setShowHorizontalLines(false);
        jScrollPane1.setViewportView(tabla_ventas);

        Reg_venta.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 80, 370, 340));

        jPanel5.setBackground(new java.awt.Color(2, 200, 167));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        labelfecha.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        labelfecha.setForeground(new java.awt.Color(255, 255, 255));
        jPanel5.add(labelfecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 20, 160, 40));

        jLabel17.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setText("Ventas del dia");
        jPanel5.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 20, 160, 40));

        Reg_venta.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 0, 370, 80));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search.png"))); // NOI18N
        jButton2.setBorderPainted(false);
        jButton2.setContentAreaFilled(false);
        jButton2.setLabel("");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        Reg_venta.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 30, 50, 40));

        jLabel12.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Cantidad:");
        Reg_venta.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 130, -1, -1));

        pre.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        pre.setForeground(new java.awt.Color(255, 255, 255));
        Reg_venta.add(pre, new org.netbeans.lib.awtextra.AbsoluteConstraints(152, 170, 40, -1));

        jLabel19.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Precio:");
        Reg_venta.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 170, 60, -1));
        Reg_venta.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, 130, 10));
        Reg_venta.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 150, 50, 10));

        caja.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        caja.setForeground(new java.awt.Color(255, 255, 255));
        caja.setText("0");
        Reg_venta.add(caja, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 350, 100, 30));

        jLabel18.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Caja:");
        Reg_venta.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 350, 50, 30));

        jPanel1.add(Reg_venta, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 140, 710, 420));

        Agregar_prod.setBackground(new java.awt.Color(68, 132, 206));
        Agregar_prod.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Categoría");
        Agregar_prod.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 230, 90, -1));

        nombre.setBackground(new java.awt.Color(68, 132, 206));
        nombre.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        nombre.setForeground(new java.awt.Color(255, 255, 255));
        nombre.setBorder(null);
        nombre.setOpaque(false);
        Agregar_prod.add(nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 110, 240, 20));
        Agregar_prod.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 130, 240, 20));

        jLabel10.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Nombre del producto ");
        Agregar_prod.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(68, 110, 170, -1));
        Agregar_prod.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 170, 110, 10));

        contenido.setBackground(new java.awt.Color(68, 132, 206));
        contenido.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        contenido.setForeground(new java.awt.Color(255, 255, 255));
        contenido.setBorder(null);
        contenido.setOpaque(false);
        contenido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                contenidoKeyTyped(evt);
            }
        });
        Agregar_prod.add(contenido, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 150, 60, 20));
        Agregar_prod.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 210, 110, 20));

        precio.setBackground(new java.awt.Color(68, 132, 206));
        precio.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        precio.setForeground(new java.awt.Color(255, 255, 255));
        precio.setBorder(null);
        precio.setOpaque(false);
        precio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                precioActionPerformed(evt);
            }
        });
        precio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                precioKeyTyped(evt);
            }
        });
        Agregar_prod.add(precio, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 190, 100, 20));

        jLabel11.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Contenido");
        Agregar_prod.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 150, 90, -1));
        Agregar_prod.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 210, 110, 20));
        Agregar_prod.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 250, 110, 20));

        categoria.setBackground(new java.awt.Color(68, 132, 206));
        categoria.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        categoria.setForeground(new java.awt.Color(255, 255, 255));
        categoria.setBorder(null);
        categoria.setOpaque(false);
        Agregar_prod.add(categoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 230, 110, 20));

        if (contenido.getText().equals("1.02") || contenido.getText().equals("2.5")) {
            mlolts.setText("lts");
        } else {
            mlolts.setText("ml");
        }
        mlolts.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        mlolts.setForeground(new java.awt.Color(255, 255, 255));
        mlolts.setText("ml/lts");
        Agregar_prod.add(mlolts, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 150, 60, 20));

        stock.setBackground(new java.awt.Color(68, 132, 206));
        stock.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        stock.setForeground(new java.awt.Color(255, 255, 255));
        stock.setBorder(null);
        stock.setOpaque(false);
        Agregar_prod.add(stock, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 270, 110, 20));
        Agregar_prod.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 290, 110, 20));

        jLabel13.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Stock");
        Agregar_prod.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 270, 60, -1));

        jButton1.setBackground(new java.awt.Color(241, 159, 77));
        jButton1.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jButton1.setForeground(new java.awt.Color(255, 255, 255));
        jButton1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        jButton1.setLabel("Agregar Producto");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        Agregar_prod.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 320, 200, 40));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/beer.png"))); // NOI18N
        Agregar_prod.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 300, 60, 70));

        jLabel15.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Precio");
        Agregar_prod.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 190, 60, -1));

        jLabel16.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("$");
        Agregar_prod.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 190, 30, -1));

        jPanel1.add(Agregar_prod, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 140, 710, 420));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 980, 560));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        
    }//GEN-LAST:event_jLabel4MouseClicked

    private void jLabel4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MousePressed
        b2_agprod.setVisible(true); //AGREGAR PRODUCTOOOOOOOO
        b1_vent.setVisible(false);
        b3_agprom.setVisible(false);
        b4_inv.setVisible(false);
        b5_cerrarc1.setVisible(false);
        
        
        datos_inventario.setVisible(false);
        Reg_venta.setVisible(false);
        Agregar_prod.setVisible(true);
    }//GEN-LAST:event_jLabel4MousePressed

    private void jLabel2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MousePressed
        b1_vent.setVisible(true); //VENTAAAAAAAASSSS
        b2_agprod.setVisible(false);
        b3_agprom.setVisible(false);
        b4_inv.setVisible(false);
        b5_cerrarc1.setVisible(false);
        
        
        
        datos_inventario.setVisible(false);
        Agregar_prod.setVisible(false);
        Reg_venta.setVisible(true);
        prod.setModel(llenar());
        MostrarTablaVentas();
        actualizarSuma();
        labelfecha.setText(getFecha());
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
        
        
        Reg_venta.setVisible(false);
        Agregar_prod.setVisible(false);
        datos_inventario.setVisible(true);
        MostrarTabla();
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
        
    }//GEN-LAST:event_barrasActionPerformed

    private void btn_ventaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ventaActionPerformed
        try {
            if (cantidad.getText().isEmpty() || prod.getSelectedItem().equals("Seleccionar")) {
                JOptionPane.showMessageDialog(null, "Campos vacios, por favor llena todos los campos");
            } else {
                vender();
                ActualizarVentas();
                MostrarTablaVentas();
                JOptionPane.showMessageDialog(null, "Venta Exitosa");
                
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de UPDATE en funcion Vender del BTN "+e.getMessage());
        }
    }//GEN-LAST:event_btn_ventaActionPerformed

    private void cantidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cantidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cantidadActionPerformed

    private void precioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_precioActionPerformed
        
    }//GEN-LAST:event_precioActionPerformed

    private void precioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_precioKeyTyped
        char validar = evt.getKeyChar();
        
        if (Character.isLetter(validar)) {
            getToolkit().beep();
            evt.consume();
            
            JOptionPane.showMessageDialog(null, "Solo numeros para el precio");
        }
    }//GEN-LAST:event_precioKeyTyped

    private void contenidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_contenidoKeyTyped
        char validar = evt.getKeyChar();
        
        if (Character.isLetter(validar)) {
            getToolkit().beep();
            evt.consume();
            JOptionPane.showMessageDialog(null, "Solo numeros para el precio");
        }
    }//GEN-LAST:event_contenidoKeyTyped

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        if (nombre.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Nombre vacio, por favor ingresa uno");
        } else if (contenido.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Contenido vacio, por favor ingresa uno");
        } else if (precio.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Precio vacio, por favor ingresa uno");
        } else if (categoria.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Categoria vacia, por favor ingresa uno");
        } else if (stock.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Stock vacio, por favor ingresa uno");
        } else {
            AgregarProducto();
        }

        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Buscar();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void barrasKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_barrasKeyTyped
        char validar = evt.getKeyChar();
        
        if (Character.isLetter(validar)) {
            getToolkit().beep();
            evt.consume();
            
            JOptionPane.showMessageDialog(null, "Solo numeros para el precio");
        }
    }//GEN-LAST:event_barrasKeyTyped

    private void prodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_prodActionPerformed
       String nom = (String) prod.getSelectedItem();
       if (prod.getSelectedIndex()!=0) {
           String[] div = nom.split(", ");
           String np = div[0];
           String cp = div[1];
           barras.setText(null);
           pre.setText(null);
           String sql = "SELECT codigobarras, precio FROM productos WHERE nombre_producto='"+np+"' and categoria='"+cp+"';";
           try {
               Statement st = con.createStatement();
               ResultSet res = st.executeQuery(sql);
   
               while (res.next()) {
                   String n = res.getString(1);
                   String c = res.getString(2);
                   barras.setText(n);
                   pre.setText("$ "+c);
               }
           } catch (SQLException e) {
           JOptionPane.showMessageDialog(null, "Error, el producto no existe ");
        }
       }
       
       
    }//GEN-LAST:event_prodActionPerformed

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
    private javax.swing.JPanel Agregar_prod;
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
    private javax.swing.JLabel caja;
    private javax.swing.JTextField cantidad;
    private javax.swing.JTextField categoria;
    private javax.swing.JTextField contenido;
    private javax.swing.JScrollPane datos_inventario;
    private rojerusan.RSFotoCircle foto;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JLabel labelfecha;
    private javax.swing.JLabel mlolts;
    private javax.swing.JLabel name;
    private javax.swing.JTextField nombre;
    private javax.swing.JLabel pre;
    private javax.swing.JTextField precio;
    private javax.swing.JComboBox<String> prod;
    private javax.swing.JTextField stock;
    private javax.swing.JTable tabla_productos;
    private javax.swing.JTable tabla_ventas;
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
