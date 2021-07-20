/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formularios;

import conexion.conexionSQL;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import java.util.Date;

//PDF
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.HeadlessException;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.sun.glass.events.KeyEvent;
import java.awt.Color;
import java.awt.image.BufferedImage;
        
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.table.TableColumnModel;



/**
 *
 * @author Compax
 */
public class sistema extends javax.swing.JFrame {
    conexionSQL cc = new conexionSQL();
    Connection con = cc.conexion();
    String cerveza,productos,pagos,depo,cerveza2;
    java.awt.Font f = new java.awt.Font("Berlin Sans FB", Font.NORMAL, 14);
    
    public sistema() {
        initComponents();
        this.setLocationRelativeTo(null);
        setNombre();
        this.setIconImage(new ImageIcon(getClass().getResource("/img/icon.png")).getImage());
        
        cerveza="categoria='Mega' OR categoria='Promo' OR categoria='Medias' OR categoria='Six' OR categoria='Familiar' OR categoria='Cuartitas' OR categoria='Doce' OR categoria='Caja' OR categoria='Bote' OR categoria='Latones' OR categoria='710'";
        cerveza2="categoria='Mega' OR categoria='Medias' OR categoria='Familiar' OR categoria='Cuartitas' OR categoria='Bote' OR categoria='Latones' OR categoria='710'";
        productos = "categoria='Bebidas' OR categoria='Botanas' OR categoria='Cigarro' OR categoria='Hielo'";
        pagos = "categoria='Deposito Dev' OR categoria='Pago'";
        depo = "categoria='Deposito'";
        
        b1_vent.setVisible(false);
        b2_agprod.setVisible(false);
        b3_agprom.setVisible(false);
        b4_inv.setVisible(false);
        b5_cerrarc1.setVisible(false);
        
        
        Reg_venta.setVisible(false);
        datos_inventarioo.setVisible(false);
        Agregar_prod.setVisible(false);
        cerrar_caja.setVisible(false);
        
        
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
            Icon iconoo = new ImageIcon(getClass().getResource("/img/error_ic.png"));
           JOptionPane.showMessageDialog(null, "Error al recibir ID "+e, "Error de ID", JOptionPane.PLAIN_MESSAGE, iconoo);
        }
        
        
        ImageIcon ii = null;
        InputStream is = null;
        String SQL3 = "Select nombre,apellido,genero,foto from empleados where idempleados="+b+";";
        try {
            Statement stt = con.createStatement();
            ResultSet rs3 = stt.executeQuery(SQL3);
            
            while (rs3.next()) {
                nom = rs3.getString(1);
                ap = rs3.getString(2);
                if (rs3.getString(3).equals("F")) { //Femenino o Masculino
                    bienvenido.setText("Bienvenida, ");
                }
                is = rs3.getBinaryStream(4);
                System.out.println(is +"<-- foto");
                if (is==null && rs3.getString(3).equals("M")) {
                    Icon def = new ImageIcon(getClass().getResource("/img/us1.png")); //default
                    foto.setImagenDefault(def);
                } else if (is==null && rs3.getString(3).equals("F")){
                    Icon def = new ImageIcon(getClass().getResource("/img/us2.png")); //default
                    foto.setImagenDefault(def);
                } else {
                    BufferedImage bi = ImageIO.read(is);
                    ii = new ImageIcon(bi);
                    foto.setImagenDefault(ii);
                }
            }
            nom = nom.concat(" "+ap);
            name.setText(nom);

        } catch (SQLException e) {
            Icon iconoo = new ImageIcon(getClass().getResource("/img/error_ic.png"));
           JOptionPane.showMessageDialog(null, "Error al pasar Nombre "+e, "Error Nombre", JOptionPane.PLAIN_MESSAGE, iconoo);
        } catch (IOException ex) {
            Logger.getLogger(sistema.class.getName()).log(Level.SEVERE, null, ex);
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
        
        tabla_cerv_inv.setModel(modelo);
        
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
            tabla_cerv_inv.setModel(modelo);
        } catch (SQLException e) {
            Icon iconoo = new ImageIcon(getClass().getResource("/img/error_db.png"));
            JOptionPane.showMessageDialog(null, "Error de Muestra de DB "+e, "Error de muestra", JOptionPane.PLAIN_MESSAGE, iconoo);
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
        } catch (SQLException e) {
            Icon iconoo = new ImageIcon(getClass().getResource("/img/list.png"));
           JOptionPane.showMessageDialog(null, "Error al llenar JCombox", "Error JCombox", JOptionPane.PLAIN_MESSAGE, iconoo);
        }        
        return modelo;
    }
    
    public void vender() throws SQLException {
        String nom;
        int a=0;
        double tot=0;
        nom = (String) prod.getSelectedItem();  
        String[] cat = nom.split(", ");
        nom = cat[0];
        String ca = cat[1];
        String barr = barras.getText();
        String sql = "Select stock, categoria from productos where codigobarras="+barr+" and nombre_producto='"+nom+"' and categoria='"+ca+"';"; //Se actualiza el stock
        try {
        Statement st = con.createStatement();
        ResultSet res = st.executeQuery(sql);
            while (res.next()) {
                System.out.println(res.getString(2) + " <-- mega o promo");
                if (res.getString(2).equals("Promo")) {
                    a = res.getInt(1)-(Integer.parseInt(cantidad.getText())*2);
                    System.out.println(a +"<-- a en promo");
                } if (res.getString(2).equals("Six")) {
                    a = res.getInt(1)-(Integer.parseInt(cantidad.getText())*6);
                    System.out.println(a +"<-- a en six");
                } if (res.getString(2).equals("Doce")) {
                    a = res.getInt(1)-(Integer.parseInt(cantidad.getText())*12);
                    System.out.println(a +"<-- a en Doce");
                } if (res.getString(2).equals("Caja")) {
                    a = res.getInt(1)-(Integer.parseInt(cantidad.getText())*24);
                    System.out.println(a +"<-- a en Caja");
                } if (!"Promo".equals(res.getString(2)) && !"Six".equals(res.getString(2)) && !"Doce".equals(res.getString(2)) && !"Caja".equals(res.getString(2))) {
                    a = res.getInt(1)-Integer.parseInt(cantidad.getText());
                    System.out.println(a +"<-- a en normal");
                }   
            }
        } catch (SQLException e) {
            Icon iconoo = new ImageIcon(getClass().getResource("/img/sell_error.png"));
            JOptionPane.showMessageDialog(null, "Error de venta en Stock", "Error de Stock", JOptionPane.PLAIN_MESSAGE, iconoo);
        }
        
        
        String sql3 = "update productos set stock="+a+" where codigobarras="+barr+";"; //Se actualiza el stock
        try {
        Statement st = con.createStatement();
        st.executeUpdate(sql3);
        } catch (SQLException e) {
            Icon iconoo = new ImageIcon(getClass().getResource("/img/error_update.png"));
            JOptionPane.showMessageDialog(null, "Error de UPDATE en stock "+e, "Error de UPDATE", JOptionPane.PLAIN_MESSAGE, iconoo);
        }
        
        //Para hacer la operacion del calculo de precio final
        
        ActualizarVentas();
    
    }
    
    public void CajaTot() {
        String sql2 = "Select sum(total) from ventas;";
            String ven ="";
            try {
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(sql2);
                while(res.next()) {
                    ven = res.getString(1);
                }          
            } catch (NumberFormatException | SQLException e) {
                Icon iconoo = new ImageIcon(getClass().getResource("/img/sell_error.png"));
                JOptionPane.showMessageDialog(null, "Erro al obtener la suma total ventas en la impresion "+e, "Error de suma", JOptionPane.PLAIN_MESSAGE, iconoo);
            }
            
            caja2.setText(ven);
    }
    
    public void AgregarProducto() {
        double pr = Double.parseDouble(precio.getText());
        int sto = Integer.parseInt(stock.getText());
        String sql = "insert into productos (codigobarras, nombre_producto, contenido, precio, categoria, stock) VALUES ("+barras_nuevo.getText()+",'"+nombre.getText()+"', '"+contenido.getText()+"', '"+pr+"', '"+categoria.getText()+"', '"+sto+"');";
        try {
        Statement st = con.createStatement();
        st.executeUpdate(sql);
        Icon iconoo = new ImageIcon(getClass().getResource("/img/paloma.png"));
        JOptionPane.showMessageDialog(null, "Producto Agregado Correctamente", "Agregando Producto", JOptionPane.PLAIN_MESSAGE, iconoo);
        nombre.setText(null);
        barras_nuevo.setText(null);
        contenido.setText(null);
        precio.setText(null);
        categoria.setText(null);
        stock.setText(null);
        
        } catch (SQLException e) {
            Icon iconoo = new ImageIcon(getClass().getResource("/img/invalid.png"));
            JOptionPane.showMessageDialog(null, "Error agregando producto "+e, "Error en productos", JOptionPane.PLAIN_MESSAGE, iconoo);
        }
        
        
    }
    
    public void MostrarTablaProductos() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("No");
        modelo.addColumn("Producto");
        modelo.addColumn("Categoria");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Total");
        tabla_prod.setModel(modelo);  
        
        String sql = "SELECT idventa, producto,categoria,cantidad,total FROM ventas WHERE "+productos+" Order by idventa;";
        String vent[] = new String[5];
        int i=0;
        try {
        Statement st = con.createStatement();
        ResultSet res = st.executeQuery(sql);
            while (res.next()) {
                i++;
                vent[0] = Integer.toString(i);//idvente
                vent[1] = res.getString(2);//producto
                vent[2] = res.getString(3);//categoria
                vent[3] = res.getString(4);//cantidad
                vent[4] = "$ "+res.getString(5)+" "; //total
                modelo.addRow(vent);
            }
            tabla_prod.setModel(modelo);
        } catch (SQLException e) {
            Icon iconoo = new ImageIcon(getClass().getResource("/img/error_db.png"));
            JOptionPane.showMessageDialog(null, "Error de Muestra DB1 "+e, "Error de DB", JOptionPane.PLAIN_MESSAGE, iconoo);
        }
        tabla_prod.getTableHeader().setFont(f);
        TableColumnModel columnModel = tabla_prod.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(60);
        columnModel.getColumn(1).setPreferredWidth(250);
        columnModel.getColumn(2).setPreferredWidth(150);
        columnModel.getColumn(3).setPreferredWidth(50);
        columnModel.getColumn(4).setPreferredWidth(150);
        
        tabla_prod.setColumnModel(columnModel);
    }
    
    public void MostrarTablaProductos_inv() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("No");
        modelo.addColumn("Codigo de Barras");
        modelo.addColumn("Producto");
        modelo.addColumn("Categoria");
        modelo.addColumn("Stock");
        tabla_pro_inv.setModel(modelo);  
        
        String sql = "SELECT idproductos,codigobarras,nombre_producto,categoria,stock FROM productos WHERE "+productos+" Order by nombre_producto;";
        String vent[] = new String[5];
        int i=0;
        try {
        Statement st = con.createStatement();
        ResultSet res = st.executeQuery(sql);
            while (res.next()) {
                i++;
                vent[0] = Integer.toString(i);//idvente
                vent[1] = res.getString(2);//producto
                vent[2] = res.getString(3);//categoria
                vent[3] = res.getString(4);//cantidad
                vent[4] = res.getString(5)+" pzs"; //total
                modelo.addRow(vent);
            }
            tabla_pro_inv.setModel(modelo);
        } catch (SQLException e) {
            Icon iconoo = new ImageIcon(getClass().getResource("/img/error_db.png"));
            JOptionPane.showMessageDialog(null, "Error de Muestra DB1 "+e, "Error de DB", JOptionPane.PLAIN_MESSAGE, iconoo);
        }
        tabla_pro_inv.getTableHeader().setFont(f);
        TableColumnModel columnModel = tabla_pro_inv.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(60);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(250);
        columnModel.getColumn(3).setPreferredWidth(100);
        columnModel.getColumn(4).setPreferredWidth(100);
        
        tabla_pro_inv.setColumnModel(columnModel);
    }
    
    public void MostrarTablaCerveza() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("No");
        modelo.addColumn("Producto");
        modelo.addColumn("Categoria");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Total");
        
        tabla_cerv.setModel(modelo);  
        
        String sql = "SELECT idventa, producto,categoria,cantidad,total FROM ventas WHERE "+cerveza+" Order by idventa;";
        String vent[] = new String[5];
        int i=0;
        try {
        Statement st = con.createStatement();
        ResultSet res = st.executeQuery(sql);
            while (res.next()) {
                i++;
                vent[0] = Integer.toString(i);//idvente
                vent[1] = res.getString(2);//producto
                vent[2] = res.getString(3);//categoria
                vent[3] = res.getString(4);//cantidad
                vent[4] = "$ "+res.getString(5)+" "; //total
                modelo.addRow(vent);
            }
            tabla_cerv.setModel(modelo);
        } catch (SQLException e) {
            Icon iconoo = new ImageIcon(getClass().getResource("/img/error_db.png"));
            JOptionPane.showMessageDialog(null, "Error de Muestra DB1 "+e, "Error de DB", JOptionPane.PLAIN_MESSAGE, iconoo);
        }
        
        tabla_cerv.getTableHeader().setFont(f);
        TableColumnModel columnModel = tabla_cerv.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(60);
        columnModel.getColumn(1).setPreferredWidth(250);
        columnModel.getColumn(2).setPreferredWidth(150);
        columnModel.getColumn(3).setPreferredWidth(50);
        columnModel.getColumn(4).setPreferredWidth(150);
        
        tabla_cerv.setColumnModel(columnModel);
    }
    
    public void MostrarTablaCerveza_inv() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("No");
        modelo.addColumn("Codigo de Barras");
        modelo.addColumn("Producto");
        modelo.addColumn("Categoria");
        modelo.addColumn("Stock");
        
        tabla_cerv_inv.setModel(modelo);  
        
        String sql = "SELECT idproductos,codigobarras,nombre_producto,categoria,stock FROM productos WHERE "+cerveza2+" Order by categoria DESC;";
        String vent[] = new String[5];
        int i=0;
        try {
        Statement st = con.createStatement();
        ResultSet res = st.executeQuery(sql);
            while (res.next()) {
                i++;
                vent[0] = Integer.toString(i);//idproductos
                vent[1] = res.getString(2);//codigobarras
                vent[2] = res.getString(3);//Nombre
                vent[3] = res.getString(4);//cantegoria
                vent[4] = res.getString(5)+" pzs"; //stock
                modelo.addRow(vent);
            }
            tabla_cerv_inv.setModel(modelo);
        } catch (SQLException e) {
            Icon iconoo = new ImageIcon(getClass().getResource("/img/error_db.png"));
            JOptionPane.showMessageDialog(null, "Error de Muestra DB1 "+e, "Error de DB", JOptionPane.PLAIN_MESSAGE, iconoo);
        }
        tabla_cerv_inv.getTableHeader().setFont(f);
        TableColumnModel columnModel = tabla_cerv_inv.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(60);
        columnModel.getColumn(1).setPreferredWidth(150);
        columnModel.getColumn(2).setPreferredWidth(250);
        columnModel.getColumn(3).setPreferredWidth(100);
        columnModel.getColumn(4).setPreferredWidth(100);
        
        tabla_cerv_inv.setColumnModel(columnModel);
    }
    
    public void MostrarTablaDepositos() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("No");
        modelo.addColumn("Nombre");
        modelo.addColumn("Total");
        
        tabla_depositos.setModel(modelo);  
        
        String sql = "SELECT producto,total FROM ventas WHERE "+depo+" or "+pagos+" Order by idventa;";
        String vent[] = new String[3];
        int i=0;
        try {
        Statement st = con.createStatement();
        ResultSet res = st.executeQuery(sql);
            while (res.next()) {
                i++;
                vent[0] = Integer.toString(i);//idvente
                vent[1] = res.getString(1);//nombre
                vent[2] = "$ "+res.getString(2)+" "; //total
                modelo.addRow(vent);
            }
            tabla_depositos.setModel(modelo);
        } catch (SQLException e) {
            Icon iconoo = new ImageIcon(getClass().getResource("/img/error_db.png"));
            JOptionPane.showMessageDialog(null, "Error de Muestra DB1 "+e, "Error de DB", JOptionPane.PLAIN_MESSAGE, iconoo);
        }
        
        tabla_depositos.getTableHeader().setFont(f);
        TableColumnModel columnModel = tabla_depositos.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(60);
        columnModel.getColumn(1).setPreferredWidth(350);
        columnModel.getColumn(2).setPreferredWidth(150);
        
        tabla_depositos.setColumnModel(columnModel);
    }
    
    public String getFecha() {
        Date fechaa = new Date();
        SimpleDateFormat fech = new SimpleDateFormat("dd/MM/YYYY");
        return fech.format(fechaa);
    }
    
    public String getFecha2() {
        Date fechaa = new Date();
        SimpleDateFormat fech = new SimpleDateFormat("dd-MM-YYYY");
        return fech.format(fechaa);
        
    }
    
    public void Buscar() {
        String cod = barras.getText();
        int ee=0;
        int indx=0;
        int tam = prod.getItemCount();
        String pro[] = new String[tam];
        String nombree[] = new String[tam];
        String categ[] = new String[tam];
        String preec="";
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
                preec = res.getString(3);
                for (int j = 0; j < tam-1; j++) { //Recorrer los nombres y las categorias por separado
                    if (nombree[j].equals(n) && categ[j].equals(c)) {
                        ee = 1;
                        indx = j;
                    }
                } 
            }
            if (ee==0) {
                prod.setSelectedIndex(0);
                Icon iconoo = new ImageIcon(getClass().getResource("/img/pro_no.png"));
                JOptionPane.showMessageDialog(null, "Error, el producto no existe ", "Error producto inexistente", JOptionPane.PLAIN_MESSAGE, iconoo);
                pre.setText(null);
            } else {
                prod.setSelectedIndex(indx+1);
                pre.setText(preec);
            }
        } catch (SQLException e) {
            Icon iconoo = new ImageIcon(getClass().getResource("/img/pro_no.png"));
            JOptionPane.showMessageDialog(null, "Error, el producto no existe "+e, "Error producto inexistente", JOptionPane.PLAIN_MESSAGE, iconoo);
        }  
    }
    
    public void Buscar2() {
        String cod = barras_existente.getText();
        int ee=0;
        int indx = 0;
        int tam = combx_prod.getItemCount();
        String pro[] = new String[tam];
        String nombree[] = new String[tam];
        String categ[] = new String[tam];
        for (int i = 1; i < tam; i++) {
            pro[i-1] = (String) combx_prod.getItemAt(i);
            String[] div = pro[i-1].split(", ");
            nombree[i-1] = div[0];
            categ[i-1] = div[1];
        }
       String sql = "SELECT nombre_producto, categoria, precio, stock FROM productos WHERE codigobarras="+cod+";";
       String prec="",sto="";
        try {
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery(sql);
   
            while (res.next()) {
                String n = res.getString(1);
                String c = res.getString(2);
                prec = res.getString(3);
                sto = res.getString(4);
                for (int j = 0; j < tam-1; j++) { //Recorrer los nombres y las categorias por separado
                    if (nombree[j].equals(n) && categ[j].equals(c)) {
                        ee = 1;
                        indx = j;
                    }
                } 
            }
            if (ee==0) {
                combx_prod.setSelectedIndex(0);
                Icon iconoo = new ImageIcon(getClass().getResource("/img/pro_no.png"));
                JOptionPane.showMessageDialog(null, "Error, el producto no existe 1", "Error producto inexistente", JOptionPane.PLAIN_MESSAGE, iconoo);
                precio_existente.setText(null);
                stock_existente.setText(null);
            } else {
                combx_prod.setSelectedIndex(indx+1);
                precio_existente.setText(prec);
                stock_existente.setText(sto);
            }
        } catch (SQLException e) {
            Icon iconoo = new ImageIcon(getClass().getResource("/img/pro_no.png"));
            JOptionPane.showMessageDialog(null, "Error, el producto no existe 1"+e, "Error producto inexistente", JOptionPane.PLAIN_MESSAGE, iconoo);
        }  
    }
    
    public void ActualizarVentas() {
        if (prod.getSelectedIndex()!=0) {
           String n = (String)prod.getSelectedItem();
           String[] div = n.split(", ");
           String np = div[0];
           String cp = div[1];

           double tot = Double.parseDouble(pre.getText())*Double.parseDouble(cantidad.getText());
           String sql = "insert into ventas (producto, categoria, cantidad, total) VALUES ('"+np+"', '"+cp+"', "+cantidad.getText()+", "+tot+");";
           try {
               Statement st = con.createStatement();
               st.executeUpdate(sql);
               barras.setText(null);
               pre.setText(null);
               cantidad.setText(null);
               prod.setSelectedIndex(0);
               precio.setText(null);
               Icon iconoo = new ImageIcon(getClass().getResource("/img/ok.png"));
               JOptionPane.showMessageDialog(null, "Venta agregada Correctamente ", "Venta exitosa", JOptionPane.PLAIN_MESSAGE, iconoo);
           } catch (SQLException e) {
               Icon iconoo = new ImageIcon(getClass().getResource("/img/invalid.png"));
                JOptionPane.showMessageDialog(null, "Error agredando venta", "Error en venta", JOptionPane.PLAIN_MESSAGE, iconoo);
           }
           
        }
    }
    
    public void MostrarTablaVentas2() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("No");
        modelo.addColumn("Producto");
        modelo.addColumn("Categoria");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Total");
        
        
        tabla_caja.setModel(modelo);
        
        String sql = "SELECT idventa, producto,categoria,cantidad,total FROM ventas WHERE "+cerveza+" Order by idventa;";
        
        String vent[] = new String[5];
        int i=0;
        try {
        Statement st = con.createStatement();
        ResultSet res = st.executeQuery(sql);
            while (res.next()) {
                i++;
                vent[0] = Integer.toString(i);//idvente
                vent[1] = res.getString(2);//producto
                vent[2] = res.getString(3);//categoria
                vent[3] = res.getString(4);//cantidad
                vent[4] = "$ "+res.getString(5)+" "; //total
                modelo.addRow(vent);
            }
            tabla_caja.setModel(modelo);
        } catch (SQLException e) {
            Icon iconoo = new ImageIcon(getClass().getResource("/img/error_db.png"));
            JOptionPane.showMessageDialog(null, "Error de muestra db", "Error en db", JOptionPane.PLAIN_MESSAGE, iconoo);
        }
        
        tabla_caja.getTableHeader().setFont(f);
        TableColumnModel columnModel = tabla_caja.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(60);
        columnModel.getColumn(1).setPreferredWidth(250);
        columnModel.getColumn(2).setPreferredWidth(150);
        columnModel.getColumn(3).setPreferredWidth(100);
        columnModel.getColumn(4).setPreferredWidth(100);
        
        tabla_caja.setColumnModel(columnModel);
    }
    
    public void MostrarTablaProductos2() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("No");
        modelo.addColumn("Producto");
        modelo.addColumn("Categoria");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Total");
        tabla_prod_caja.setModel(modelo);  
        
        String sql = "SELECT idventa, producto,categoria,cantidad,total FROM ventas WHERE "+productos+" Order by idventa;";
        String vent[] = new String[5];
        int i=0;
        try {
        Statement st = con.createStatement();
        ResultSet res = st.executeQuery(sql);
            while (res.next()) {
                i++;
                vent[0] = Integer.toString(i);//idvente
                vent[1] = res.getString(2);//producto
                vent[2] = res.getString(3);//categoria
                vent[3] = res.getString(4);//cantidad
                vent[4] = "$ "+res.getString(5)+" "; //total
                modelo.addRow(vent);
            }
            tabla_prod_caja.setModel(modelo);
        } catch (SQLException e) {
            Icon iconoo = new ImageIcon(getClass().getResource("/img/error_db.png"));
            JOptionPane.showMessageDialog(null, "Error de Muestra DB1 "+e, "Error de DB", JOptionPane.PLAIN_MESSAGE, iconoo);
        }
        tabla_prod_caja.getTableHeader().setFont(f);
        TableColumnModel columnModel = tabla_prod_caja.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(60);
        columnModel.getColumn(1).setPreferredWidth(250);
        columnModel.getColumn(2).setPreferredWidth(150);
        columnModel.getColumn(3).setPreferredWidth(100);
        columnModel.getColumn(4).setPreferredWidth(100);
        
        tabla_prod_caja.setColumnModel(columnModel);
    }
    
    public void MostrarTablaDepositos2() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("No");
        modelo.addColumn("Categoria");
        modelo.addColumn("Total");
        
        tabla_dep_caja.setModel(modelo);  
        
        String sql = "SELECT producto,total FROM ventas WHERE "+depo+" or "+pagos+" Order by idventa;";
        String vent[] = new String[3];
        int i=0;
        try {
        Statement st = con.createStatement();
        ResultSet res = st.executeQuery(sql);
            while (res.next()) {
                i++;
                vent[0] = Integer.toString(i);//idvente
                vent[1] = res.getString(1);//categoria
                vent[2] = "$ "+res.getString(2)+" "; //total
                modelo.addRow(vent);
            }
            tabla_dep_caja.setModel(modelo);
        } catch (SQLException e) {
            Icon iconoo = new ImageIcon(getClass().getResource("/img/error_db.png"));
            JOptionPane.showMessageDialog(null, "Error de Muestra DB1 "+e, "Error de DB", JOptionPane.PLAIN_MESSAGE, iconoo);
        }
        
        tabla_dep_caja.getTableHeader().setFont(f);
        TableColumnModel columnModel = tabla_dep_caja.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(60);
        columnModel.getColumn(1).setPreferredWidth(250);
        columnModel.getColumn(2).setPreferredWidth(150);
        
        tabla_dep_caja.setColumnModel(columnModel);
    }
    
    public void ImprimirCaja(JPanel pnl) {
        PrinterJob printerjob = PrinterJob.getPrinterJob();
        printerjob.setJobName("Imprimir Venta");
        printerjob.setPrintable(new Printable() {
            @Override
            public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
                if (pageIndex > 0) {
                    return Printable.NO_SUCH_PAGE;
                }
                
                Graphics2D graphics2d = (Graphics2D) graphics;
                graphics2d.translate (pageFormat.getImageableX()*2, pageFormat.getImageableY()*2);
                
                graphics2d.scale(0.5, 0.5);
                
                pnl.paint(graphics2d);
                
                return Printable.PAGE_EXISTS;
            }
        });
        
        boolean returningResult = printerjob.printDialog();
        
        if (returningResult) {
            try {
                printerjob.print();
            } catch (PrinterException printerException ) {
                Icon iconoo = new ImageIcon(getClass().getResource("/img/error_print.png"));
                JOptionPane.showMessageDialog(null, "Error de impresion "+printerException.getMessage(), "Error al imprimir", JOptionPane.PLAIN_MESSAGE, iconoo);
            }
        }
    }
    
    public String SumaCerveza() {
        //Suma Cerveza
            String sql21 = "SELECT sum(total) FROM ventas where "+cerveza+" ORDER BY idventa;";
            String ven ="";
            try {
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(sql21);
                while(res.next()) {
                    ven = res.getString(1);
                }          
            } catch (NumberFormatException | SQLException e) {
                Icon iconoo = new ImageIcon(getClass().getResource("/img/error_print"));
                JOptionPane.showMessageDialog(null, "Error al obtener suma total cervezas en la impresion "+e, "Error suma impresion", JOptionPane.PLAIN_MESSAGE, iconoo);
            }
            return ven;
    }
    
    public String SumaProductos() {
        //Suma Productos
            String sql22 = "SELECT sum(total) FROM ventas where "+productos+" ORDER BY idventa;";
            String proo ="";
            try {
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(sql22);
                while(res.next()) {
                    proo = res.getString(1);
                }          
            } catch (NumberFormatException | SQLException e) {
                Icon iconoo = new ImageIcon(getClass().getResource("/img/error_print"));
                JOptionPane.showMessageDialog(null, "Error al obtener suma total productos en la impresion "+e, "Error suma impresion", JOptionPane.PLAIN_MESSAGE, iconoo);
            }
        return proo;
    }
    
    public String SumaPagosYDep() {
        //Suma Pagos
        String sql27 = "SELECT sum(total) FROM ventas where "+pagos+" OR "+depo+" ORDER BY idventa;";
        String pag ="";
        try {
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery(sql27);
            while(res.next()) {
                pag = res.getString(1);
            }          
        } catch (NumberFormatException | SQLException e) {
            Icon iconoo = new ImageIcon(getClass().getResource("/img/error_print"));
            JOptionPane.showMessageDialog(null, "Error al obtener suma total pagos en la impresion "+e, "Error suma impresion", JOptionPane.PLAIN_MESSAGE, iconoo);
        }
        return pag;
    }
    
    public void Imprimir2(String name) {
        Document documento = new Document();
        
        try {
            String ruta = System.getProperty("user.home"); //para la ruta principal
            PdfWriter.getInstance(documento, new FileOutputStream(ruta + "/Desktop/Reportes/"+name)); //Cambiar nombre del reporte
            Image header = Image.getInstance(getClass().getResource("/img/model.png"));
            header.scaleToFit(500, 1000);
            header.setAlignment(Chunk.ALIGN_CENTER);
            
            Paragraph parrafo = new Paragraph();
            parrafo.setAlignment(Paragraph.ALIGN_LEFT);
            parrafo.setFont(FontFactory.getFont(FontFactory.COURIER_BOLD, 14, Font.NORMAL));
            parrafo.add("Reporte del día "+getFecha()+"\n\n");
            documento.open();
            documento.add(header);
            documento.add(parrafo);
            
            PdfPTable tabla = new PdfPTable(5);
            PdfPTable tabla_Prod = new PdfPTable(5);
            PdfPTable tabla_pagos = new PdfPTable(5);
            PdfPTable tabla_dep = new PdfPTable(5);
            
            
            tabla.setWidthPercentage(100);
            tabla_Prod.setWidthPercentage(100);
            tabla_pagos.setWidthPercentage(100);
            tabla_dep.setWidthPercentage(100);
            
            Font fff = FontFactory.getFont(FontFactory.COURIER);
            fff.setColor(BaseColor.WHITE);
            Font f2 = FontFactory.getFont(FontFactory.COURIER);
            f2.setColor(BaseColor.WHITE);
            
            //Colores
            BaseColor head = new BaseColor(33,47,61);
            BaseColor tab1 = new BaseColor(97,106,107);
            BaseColor tab2 = new BaseColor(66,73,73);
            
            PdfPCell venta = new PdfPCell(new Phrase("Venta no",fff));
            venta.setBackgroundColor(head);
            venta.setHorizontalAlignment(Element.ALIGN_CENTER);
            venta.setVerticalAlignment(Element.ALIGN_CENTER);
            venta.setFixedHeight(50);
            venta.setPaddingTop(15);
            venta.setBorderWidth(0);
            
            PdfPCell producto = new PdfPCell(new Phrase("Producto",fff));
            producto.setBackgroundColor(head);
            producto.setHorizontalAlignment(Element.ALIGN_LEFT);
            producto.setVerticalAlignment(Element.ALIGN_CENTER);
            producto.setFixedHeight(50);
            producto.setPaddingTop(15);
            producto.setBorderWidth(0);
            
            PdfPCell categoriaa = new PdfPCell(new Phrase("Categoria",fff));
            categoriaa.setBackgroundColor(head);
            categoriaa.setHorizontalAlignment(Element.ALIGN_LEFT);
            categoriaa.setVerticalAlignment(Element.ALIGN_CENTER);
            categoriaa.setFixedHeight(50);
            categoriaa.setPaddingTop(15);
            categoriaa.setBorderWidth(0);
            
            PdfPCell cantidadd = new PdfPCell(new Phrase("Cantidad",fff));
            cantidadd.setBackgroundColor(head);
            cantidadd.setHorizontalAlignment(Element.ALIGN_LEFT);
            cantidadd.setVerticalAlignment(Element.ALIGN_CENTER);
            cantidadd.setFixedHeight(50);
            cantidadd.setPaddingTop(15);
            cantidadd.setBorderWidth(0);
            
            PdfPCell total = new PdfPCell(new Phrase("Total",fff));
            total.setBackgroundColor(head);
            total.setHorizontalAlignment(Element.ALIGN_LEFT);
            total.setVerticalAlignment(Element.ALIGN_CENTER);
            total.setFixedHeight(50);
            total.setPaddingTop(15);
            total.setBorderWidth(0);
            
            //TABLA VENTAS CERVEZA
            
            tabla.addCell(venta);
            tabla.addCell(producto);
            tabla.addCell(categoriaa);
            tabla.addCell(cantidadd);
            tabla.addCell(total);
            
            //TABLA PRODUCTOS
            
            tabla_Prod.addCell(venta);
            tabla_Prod.addCell(producto);
            tabla_Prod.addCell(categoriaa);
            tabla_Prod.addCell(cantidadd);
            tabla_Prod.addCell(total);
            
            //TABLA PAGOS
            
            tabla_pagos.addCell(venta);
            tabla_pagos.addCell(producto);
            tabla_pagos.addCell(categoriaa);
            tabla_pagos.addCell(cantidadd);
            tabla_pagos.addCell(total);
            
            //TABLA DEPOSITOS
            
            tabla_dep.addCell(venta);
            tabla_dep.addCell(producto);
            tabla_dep.addCell(categoriaa);
            tabla_dep.addCell(cantidadd);
            tabla_dep.addCell(total);
            
            
            
            //TABLA CERVEZAS
            String sql="SELECT idventa, producto, categoria, cantidad, total FROM ventas where "+cerveza+" ORDER BY idventa;";
            try {
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(sql);
                int id_v = 0;
                if (res.next()) {
                    do {
                        id_v++;
                        PdfPCell idd = new PdfPCell(new Phrase(Integer.toString(id_v),f2)); //IDVENTA
                        if (id_v%2==0) {
                            idd.setBackgroundColor(tab1);
                        } else {
                            idd.setBackgroundColor(tab2);
                        }
                        idd.setHorizontalAlignment(Element.ALIGN_CENTER);
                        idd.setBorderWidth(0);
                        tabla.addCell(idd);
                        
                        PdfPCell proo = new PdfPCell(new Phrase(res.getString(2),f2)); //NOMBRE PRODUCTO
                        if (id_v%2==0) {
                            proo.setBackgroundColor(tab1);
                        } else {
                            proo.setBackgroundColor(tab2);
                        }
                        proo.setHorizontalAlignment(Element.ALIGN_LEFT);
                        proo.setBorderWidth(0);
                        tabla.addCell(proo);
                        
                        PdfPCell cat = new PdfPCell(new Phrase(res.getString(3),f2)); //CATEGORIA
                        if (id_v%2==0) {
                            cat.setBackgroundColor(tab1);
                        } else {
                            cat.setBackgroundColor(tab2);
                        }
                        cat.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cat.setBorderWidth(0);
                        tabla.addCell(cat);
                        
                        PdfPCell cant = new PdfPCell(new Phrase(res.getString(4),f2)); //CANTIDAD
                        if (id_v%2==0) {
                            cant.setBackgroundColor(tab1);
                        } else {
                            cant.setBackgroundColor(tab2);
                        }
                        cant.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cant.setBorderWidth(0);
                        tabla.addCell(cant);
                        
                        PdfPCell prr = new PdfPCell(new Phrase("$ "+res.getString(5),f2)); //PRECIO
                        if (id_v%2==0) {
                            prr.setBackgroundColor(tab1);
                        } else {
                            prr.setBackgroundColor(tab2);
                        }
                        prr.setHorizontalAlignment(Element.ALIGN_LEFT);
                        prr.setBorderWidth(0);
                        tabla.addCell(prr);
                    } while (res.next());
                    documento.add(tabla);
                }
       
            } catch (SQLException e) {
                Icon iconoo = new ImageIcon(getClass().getResource("/img/pdf.png"));
                JOptionPane.showMessageDialog(null, "Error generando PDF ", "Error PDF", JOptionPane.PLAIN_MESSAGE, iconoo);
            }
            
            //TABLA PRODUCTOS
            
            String sql2="SELECT * FROM ventas where "+productos+" ORDER BY idventa;";
            try {
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(sql2);
                int id_v = 0;
                if (res.next()) {
                    do {
                        id_v++;
                        PdfPCell idd = new PdfPCell(new Phrase(Integer.toString(id_v),f2)); //IDVENTA
                        if (id_v%2==0) {
                            idd.setBackgroundColor(tab1);
                        } else {
                            idd.setBackgroundColor(tab2);
                        }
                        idd.setHorizontalAlignment(Element.ALIGN_CENTER);
                        idd.setBorderWidth(0);
                        tabla_Prod.addCell(idd);
                        
                        PdfPCell proo = new PdfPCell(new Phrase(res.getString(2),f2)); //NOMBRE PRODUCTO
                        if (id_v%2==0) {
                            proo.setBackgroundColor(tab1);
                        } else {
                            proo.setBackgroundColor(tab2);
                        }
                        proo.setHorizontalAlignment(Element.ALIGN_LEFT);
                        proo.setBorderWidth(0);
                        tabla_Prod.addCell(proo);
                        
                        PdfPCell cat = new PdfPCell(new Phrase(res.getString(3),f2)); //CATEGORIA
                        if (id_v%2==0) {
                            cat.setBackgroundColor(tab1);
                        } else {
                            cat.setBackgroundColor(tab2);
                        }
                        cat.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cat.setBorderWidth(0);
                        tabla_Prod.addCell(cat);
                        
                        PdfPCell cant = new PdfPCell(new Phrase(res.getString(4),f2)); //CANTIDAD
                        if (id_v%2==0) {
                            cant.setBackgroundColor(tab1);
                        } else {
                            cant.setBackgroundColor(tab2);
                        }
                        cant.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cant.setBorderWidth(0);
                        tabla_Prod.addCell(cant);
                        
                        PdfPCell prr = new PdfPCell(new Phrase("$ "+res.getString(5),f2)); //PRECIO
                        if (id_v%2==0) {
                            prr.setBackgroundColor(tab1);
                        } else {
                            prr.setBackgroundColor(tab2);
                        }
                        prr.setHorizontalAlignment(Element.ALIGN_LEFT);
                        prr.setBorderWidth(0);
                        tabla_Prod.addCell(prr);
                    } while (res.next());
                }
       
            } catch (SQLException e) {
                Icon iconoo = new ImageIcon(getClass().getResource("/img/pdf.png"));
                JOptionPane.showMessageDialog(null, "Error generando PDF ", "Error PDF", JOptionPane.PLAIN_MESSAGE, iconoo);
            }
            
            //TABLA PAGOS
            
            String sql25="SELECT * FROM ventas where "+pagos+" ORDER BY idventa;";
            try {
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(sql25);
                int id_v = 0;
                if (res.next()) {
                    do {
                        id_v++;
                        PdfPCell idd = new PdfPCell(new Phrase(Integer.toString(id_v),f2)); //IDVENTA
                        if (id_v%2==0) {
                            idd.setBackgroundColor(tab1);
                        } else {
                            idd.setBackgroundColor(tab2);
                        }
                        idd.setHorizontalAlignment(Element.ALIGN_CENTER);
                        idd.setBorderWidth(0);
                        tabla_pagos.addCell(idd);
                        
                        PdfPCell proo = new PdfPCell(new Phrase(res.getString(2),f2)); //NOMBRE PRODUCTO
                        if (id_v%2==0) {
                            proo.setBackgroundColor(tab1);
                        } else {
                            proo.setBackgroundColor(tab2);
                        }
                        proo.setHorizontalAlignment(Element.ALIGN_LEFT);
                        proo.setBorderWidth(0);
                        tabla_pagos.addCell(proo);
                        
                        PdfPCell cat = new PdfPCell(new Phrase(res.getString(3),f2)); //CATEGORIA
                        if (id_v%2==0) {
                            cat.setBackgroundColor(tab1);
                        } else {
                            cat.setBackgroundColor(tab2);
                        }
                        cat.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cat.setBorderWidth(0);
                        tabla_pagos.addCell(cat);
                        
                        PdfPCell cant = new PdfPCell(new Phrase(res.getString(4),f2)); //CANTIDAD
                        if (id_v%2==0) {
                            cant.setBackgroundColor(tab1);
                        } else {
                            cant.setBackgroundColor(tab2);
                        }
                        cant.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cant.setBorderWidth(0);
                        tabla_pagos.addCell(cant);
                        
                        PdfPCell prr = new PdfPCell(new Phrase("$ "+res.getString(5),f2)); //PRECIO
                        if (id_v%2==0) {
                            prr.setBackgroundColor(tab1);
                        } else {
                            prr.setBackgroundColor(tab2);
                        }
                        prr.setHorizontalAlignment(Element.ALIGN_LEFT);
                        prr.setBorderWidth(0);
                        tabla_pagos.addCell(prr);
                    } while (res.next());
                }
       
            } catch (SQLException e) {
                Icon iconoo = new ImageIcon(getClass().getResource("/img/pdf.png"));
                JOptionPane.showMessageDialog(null, "Error generando PDF ", "Error PDF", JOptionPane.PLAIN_MESSAGE, iconoo);
            }
            
            //TABLA DEPOSITOS
            
            String sql26="SELECT * FROM ventas where "+depo+" ORDER BY idventa;";
            try {
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(sql26);
                int id_v = 0;
                if (res.next()) {
                    do {
                        id_v++;
                        PdfPCell idd = new PdfPCell(new Phrase(Integer.toString(id_v),f2)); //IDVENTA
                        if (id_v%2==0) {
                            idd.setBackgroundColor(tab1);
                        } else {
                            idd.setBackgroundColor(tab2);
                        }
                        idd.setHorizontalAlignment(Element.ALIGN_CENTER);
                        idd.setBorderWidth(0);
                        tabla_dep.addCell(idd);
                        
                        PdfPCell proo = new PdfPCell(new Phrase(res.getString(2),f2)); //NOMBRE PRODUCTO
                        if (id_v%2==0) {
                            proo.setBackgroundColor(tab1);
                        } else {
                            proo.setBackgroundColor(tab2);
                        }
                        proo.setHorizontalAlignment(Element.ALIGN_LEFT);
                        proo.setBorderWidth(0);
                        tabla_dep.addCell(proo);
                        
                        PdfPCell cat = new PdfPCell(new Phrase(res.getString(3),f2)); //CATEGORIA
                        if (id_v%2==0) {
                            cat.setBackgroundColor(tab1);
                        } else {
                            cat.setBackgroundColor(tab2);
                        }
                        cat.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cat.setBorderWidth(0);
                        tabla_dep.addCell(cat);
                        
                        PdfPCell cant = new PdfPCell(new Phrase(res.getString(4),f2)); //CANTIDAD
                        if (id_v%2==0) {
                            cant.setBackgroundColor(tab1);
                        } else {
                            cant.setBackgroundColor(tab2);
                        }
                        cant.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cant.setBorderWidth(0);
                        tabla_dep.addCell(cant);
                        
                        PdfPCell prr = new PdfPCell(new Phrase("$ "+res.getString(5),f2)); //PRECIO
                        if (id_v%2==0) {
                            prr.setBackgroundColor(tab1);
                        } else {
                            prr.setBackgroundColor(tab2);
                        }
                        prr.setHorizontalAlignment(Element.ALIGN_LEFT);
                        prr.setBorderWidth(0);
                        tabla_dep.addCell(prr);
                    } while (res.next());
                }
       
            } catch (SQLException e) {
                Icon iconoo = new ImageIcon(getClass().getResource("/img/pdf.png"));
                JOptionPane.showMessageDialog(null, "Error generando PDF ", "Error PDF", JOptionPane.PLAIN_MESSAGE, iconoo);
            }
            
            String ven =SumaCerveza();
            String proo = SumaProductos();            
            //Suma Pagos
            String sql27 = "SELECT sum(total) FROM ventas where "+pagos+" ORDER BY idventa;";
            String pag ="";
            try {
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(sql27);
                while(res.next()) {
                    pag = res.getString(1);
                }          
            } catch (NumberFormatException | SQLException e) {
                Icon iconoo = new ImageIcon(getClass().getResource("/img/error_print"));
                JOptionPane.showMessageDialog(null, "Error al obtener suma total pagos en la impresion "+e, "Error suma impresion", JOptionPane.PLAIN_MESSAGE, iconoo);
            }
            
            //Suma Depositos
            String sql28 = "SELECT sum(total) FROM ventas where "+depo+" ORDER BY idventa;";
            String depp ="";
            try {
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(sql28);
                while(res.next()) {
                    depp = res.getString(1);
                }          
            } catch (NumberFormatException | SQLException e) {
                Icon iconoo = new ImageIcon(getClass().getResource("/img/error_print"));
                JOptionPane.showMessageDialog(null, "Error al obtener suma total depositos en la impresion "+e, "Error suma impresion", JOptionPane.PLAIN_MESSAGE, iconoo);
            }
            
            //Suma Total
            String sql29 = "SELECT sum(total) FROM ventas;";
            String toot ="";
            try {
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(sql29);
                while(res.next()) {
                    toot = res.getString(1);
                }          
            } catch (NumberFormatException | SQLException e) {
                Icon iconoo = new ImageIcon(getClass().getResource("/img/error_print"));
                JOptionPane.showMessageDialog(null, "Error al obtener suma total en la impresion "+e, "Error suma impresion", JOptionPane.PLAIN_MESSAGE, iconoo);
            }
            
            Paragraph parrafo2 = new Paragraph();
            parrafo2.setAlignment(Paragraph.ALIGN_LEFT);
            parrafo2.setFont(FontFactory.getFont(FontFactory.COURIER_BOLD, 14, Font.NORMAL));
            parrafo2.add("Total Cerveza: $ "+ven+"\n\n\n");
            
            Paragraph parrafo21 = new Paragraph();
            parrafo21.setAlignment(Paragraph.ALIGN_LEFT);
            parrafo21.setFont(FontFactory.getFont(FontFactory.COURIER_BOLD, 14, Font.NORMAL));
            parrafo21.add("Total Productos: $ "+proo+"\n\n\n");
            
            Paragraph parrafo23 = new Paragraph();
            parrafo23.setAlignment(Paragraph.ALIGN_LEFT);
            parrafo23.setFont(FontFactory.getFont(FontFactory.COURIER_BOLD, 14, Font.NORMAL));
            parrafo23.add("Total Pagos: $ "+pag+"\n\n\n");
            
            Paragraph parrafo24 = new Paragraph();
            parrafo24.setAlignment(Paragraph.ALIGN_LEFT);
            parrafo24.setFont(FontFactory.getFont(FontFactory.COURIER_BOLD, 14, Font.NORMAL));
            parrafo24.add("Total Depositos: $ "+depp+"\n\n\n");

            Paragraph parrafo25 = new Paragraph();
            parrafo25.setAlignment(Paragraph.ALIGN_LEFT);
            parrafo25.setFont(FontFactory.getFont(FontFactory.COURIER_BOLD, 18, Font.UNDERLINE));
            parrafo25.add("Venta Total: $ "+toot);
            
            documento.add(parrafo2);
            Paragraph parrafo3 = new Paragraph();
            parrafo3.setAlignment(Paragraph.ALIGN_LEFT);
            parrafo3.setFont(FontFactory.getFont(FontFactory.COURIER_BOLD, 14, Font.NORMAL));
            parrafo3.add("\n\n\nNota: \n");
            Paragraph parrafo4 = new Paragraph();
            parrafo4.setAlignment(Paragraph.ALIGN_LEFT);
            parrafo4.setFont(FontFactory.getFont(FontFactory.COURIER, 17, Font.NORMAL));
            parrafo4.add(texto_nota.getText());
            
            documento.add(tabla_Prod);
            documento.add(parrafo21);
            documento.add(tabla_pagos);
            documento.add(parrafo23);
            documento.add(tabla_dep);
            documento.add(parrafo24);
            documento.add(parrafo25);
            documento.add(parrafo3);
            documento.add(parrafo4);
            documento.close();
            Icon iconoo = new ImageIcon(getClass().getResource("/img/ok.png"));
            JOptionPane.showMessageDialog(null, "Reporte generado Exitosamente", "Generando Reporte", JOptionPane.PLAIN_MESSAGE, iconoo);
        } catch (DocumentException | HeadlessException | FileNotFoundException ee) {
            JOptionPane.showMessageDialog(null, "Error generando PDF 2e"+ee.getMessage());
        } catch (IOException img) {
            JOptionPane.showMessageDialog(null, "Error al cargar imagen "+img.getMessage());
        }
}
    
    public void Imprimir3(String name) {
        Document documento = new Document();
        
        try {
            String ruta = System.getProperty("user.home"); //para la ruta principal
            PdfWriter.getInstance(documento, new FileOutputStream(ruta + "/Desktop/Inventario/"+name)); //Cambiar nombre del reporte
            Image header = Image.getInstance(getClass().getResource("/img/model.png"));
            header.scaleToFit(500, 1000);
            header.setAlignment(Chunk.ALIGN_CENTER);
            
            Paragraph parrafo = new Paragraph();
            parrafo.setAlignment(Paragraph.ALIGN_LEFT);
            parrafo.setFont(FontFactory.getFont(FontFactory.COURIER_BOLD, 14, Font.NORMAL));
            parrafo.add("Inventario del día "+getFecha()+"\n\n");
            documento.open();
            documento.add(header);
            documento.add(parrafo);
            
            PdfPTable tabla = new PdfPTable(4);
                        
            tabla.setWidthPercentage(100);
            
            Font f = FontFactory.getFont(FontFactory.COURIER);
            f.setColor(BaseColor.WHITE);
            Font f2 = FontFactory.getFont(FontFactory.COURIER);
            f2.setColor(BaseColor.WHITE);
            
            //Colores
            BaseColor head = new BaseColor(33,47,61);
            BaseColor tab1 = new BaseColor(97,106,107);
            BaseColor tab2 = new BaseColor(66,73,73);
            BaseColor error = new BaseColor(176,0,32); //Error
            PdfPCell venta = new PdfPCell(new Phrase("ID Prod",f));
            venta.setBackgroundColor(head);
            venta.setHorizontalAlignment(Element.ALIGN_CENTER);
            venta.setVerticalAlignment(Element.ALIGN_CENTER);
            venta.setFixedHeight(50);
            venta.setPaddingTop(15);
            venta.setBorderWidth(0);
            
            PdfPCell producto = new PdfPCell(new Phrase("Nombre",f));
            producto.setBackgroundColor(head);
            producto.setHorizontalAlignment(Element.ALIGN_LEFT);
            producto.setVerticalAlignment(Element.ALIGN_CENTER);
            producto.setFixedHeight(50);
            producto.setPaddingTop(15);
            producto.setBorderWidth(0);
            
            PdfPCell cajaa = new PdfPCell(new Phrase("Cajas",f));
            cajaa.setBackgroundColor(head);
            cajaa.setHorizontalAlignment(Element.ALIGN_CENTER);
            cajaa.setVerticalAlignment(Element.ALIGN_CENTER);
            cajaa.setFixedHeight(50);
            cajaa.setPaddingTop(15);
            cajaa.setBorderWidth(0);
            
            PdfPCell unidd = new PdfPCell(new Phrase("Unidad",f));
            unidd.setBackgroundColor(head);
            unidd.setHorizontalAlignment(Element.ALIGN_CENTER);
            unidd.setVerticalAlignment(Element.ALIGN_CENTER);
            unidd.setFixedHeight(50);
            unidd.setPaddingTop(15);
            unidd.setBorderWidth(0);
            
            //TABLA INVENTARIO
            
            tabla.addCell(venta);
            tabla.addCell(producto);
            tabla.addCell(cajaa);
            tabla.addCell(unidd);
            
            //TABLA INVENTARIO
            String sql="SELECT categoria, stock, nombre_producto FROM productos WHERE "+cerveza2+" Order by categoria DESC;";
            double ca=0, un=0,ca2=0,ca3=0;
            String ccaj="",unn="";
            NumberFormat nf = new DecimalFormat("##.###");
            try {
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(sql);
                int id_v = 0;
                if (res.next()) {
                    do {
                        if (res.getString(1).equals("Mega") || res.getString(1).equals("Familiar") || res.getString(1).equals("710")) {
                            ca = Double.parseDouble(res.getString(2));
                            ca2 = Double.parseDouble(res.getString(2));
                            ca3 = Double.parseDouble(res.getString(2));
                            ca2 = ca/12;
                            un = ca2%1;
                            ca = ca2-un;
                            ccaj = nf.format(ca);
                            if (un>0) {
                                un = ca3-(ca*12);
                                unn = nf.format(un);
                            }
                        }
                        if (res.getString(1).equals("Latones") || res.getString(1).equals("Bote") || res.getString(1).equals("Cuartitas") || res.getString(1).equals("Medias")) {
                            ca = Double.parseDouble(res.getString(2));
                            ca2 = Double.parseDouble(res.getString(2));
                            ca3 = Double.parseDouble(res.getString(2));
                            ca2 = ca/24;
                            un = ca2%1;
                            ca = ca2-un;
                            ccaj = nf.format(ca);
                            if (un>0) {
                                un = ca3-(ca*24);
                                unn = nf.format(un);
                            }
                        }
                        id_v++;
                        PdfPCell idd = new PdfPCell(new Phrase(Integer.toString(id_v),f2)); //IDPRODUCTOS
                        if (id_v%2==0) {
                            idd.setBackgroundColor(tab1);
                        } else {
                            idd.setBackgroundColor(tab2);
                        }
                        idd.setHorizontalAlignment(Element.ALIGN_CENTER);
                        idd.setBorderWidth(0);
                        tabla.addCell(idd);

                        PdfPCell cat = new PdfPCell(new Phrase(res.getString(3),f2)); //NOMBRE_PRODUCTO
                        if (id_v%2==0) {
                            cat.setBackgroundColor(tab1);
                        } else {
                            cat.setBackgroundColor(tab2);
                        }
                        cat.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cat.setBorderWidth(0);
                        tabla.addCell(cat);
                        
                        PdfPCell cajas = new PdfPCell(new Phrase(ccaj,f2)); //Cajas
                        if (id_v%2==0) {
                            cajas.setBackgroundColor(tab1);
                        } else {
                            cajas.setBackgroundColor(tab2);
                        }
                        cajas.setHorizontalAlignment(Element.ALIGN_CENTER);
                        cajas.setBorderWidth(0);
                        tabla.addCell(cajas);
                        
                        PdfPCell unidad = new PdfPCell(new Phrase(unn,f2)); //Unidad
                        if (id_v%2==0) {
                            unidad.setBackgroundColor(tab1);
                        } else {
                            unidad.setBackgroundColor(tab2);
                        }
                        unidad.setHorizontalAlignment(Element.ALIGN_CENTER);
                        unidad.setBorderWidth(0);
                        tabla.addCell(unidad);
                    } while (res.next());
                    documento.add(tabla);
                }
       
            } catch (SQLException e) {
                Icon iconoo = new ImageIcon(getClass().getResource("/img/pdf.png"));
                JOptionPane.showMessageDialog(null, "Error generando PDF Inventario "+e, "Error PDF", JOptionPane.PLAIN_MESSAGE, iconoo);
            }
            

            documento.close();
            Icon iconoo = new ImageIcon(getClass().getResource("/img/ok.png"));
            JOptionPane.showMessageDialog(null, "Inventario Generado Exitosamente", "Generando Inventario", JOptionPane.PLAIN_MESSAGE, iconoo);
        } catch (DocumentException | HeadlessException | FileNotFoundException ee) {
            JOptionPane.showMessageDialog(null, "Error generando PDF 2e"+ee.getMessage());
        } catch (IOException img) {
            JOptionPane.showMessageDialog(null, "Error al cargar imagen "+img.getMessage());
        }
    }
    
    public void Reset() {
        String sql = "TRUNCATE TABLE ventas;";
           try {
               Statement st = con.createStatement();
               st.execute(sql);
               Icon iconoo = new ImageIcon(getClass().getResource("/img/escoba.png"));
               JOptionPane.showMessageDialog(null, "Las ventas se han limpiado ", "Limpiando Ventas", JOptionPane.PLAIN_MESSAGE, iconoo);
           } catch (SQLException e) {
               JOptionPane.showMessageDialog(null, "Error limpiando ventas"+e.getMessage());
           }
        String sql2 = "ALTER TABLE ventas auto_increment = 0;";
           try {
               Statement st = con.createStatement();
               st.execute(sql2);
           } catch (SQLException e) {
               JOptionPane.showMessageDialog(null, "Error autoincrement"+e.getMessage());
           }
    }
    
    public void Existente() {
        String nom = (String) combx_prod.getSelectedItem();
       if (combx_prod.getSelectedIndex()!=0) {
           String[] div = nom.split(", ");
           String np = div[0];
           String cp = div[1];
           //contenido_existente.setText(null);
           //precio_existente.setText(null);
           String sql = "Select codigobarras, contenido, precio, categoria, stock from productos where nombre_producto='"+np+"' and categoria='"+cp+"';";
           try {
               Statement st = con.createStatement();
               ResultSet res = st.executeQuery(sql);
   
               while (res.next()) {
                   String cb = res.getString(1);
                   String p = res.getString(3);
                   String s = res.getString(5);
                   barras_existente.setText(cb);
                   precio_existente.setText(p); //precio
                   stock_existente.setText(s);
               }
           } catch (SQLException e) {
               Icon iconoo = new ImageIcon(getClass().getResource("/img/list.png"));
               JOptionPane.showMessageDialog(null, "Error, el producto no existe en JCombox, no se puede agregar productos", "Error JCombox", JOptionPane.PLAIN_MESSAGE, iconoo);
           }
       }
    }
    
    public void AgregarExistente() {
        String codigbarras=barras_existente.getText();
        String stockk = stock_existente.getText();
        String sql = "update productos set stock="+stockk+" where codigobarras="+codigbarras+";"; //Se actualiza el stock
        try {
        Statement st = con.createStatement();
        st.executeUpdate(sql);
        Icon iconoo = new ImageIcon(getClass().getResource("/img/ok.png"));
        JOptionPane.showMessageDialog(null, "Productos agregados al stock correctamente", "Stock Actualizado", JOptionPane.PLAIN_MESSAGE, iconoo);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de UPDATE en agregar productos, stock "+e.getMessage());
        }
    }
    
    public void ActualizarPrecio() {
        String nom = (String) combx_prod.getSelectedItem();  
        String[] cat = nom.split(", ");
        nom = cat[0];
        String ca = cat[1];
        String prec = precio_existente.getText();
        String sql = "update productos set precio="+prec+" where nombre_producto='"+nom+"' and categoria='"+ca+"';"; //Se actualiza el stock
        try {
        Statement st = con.createStatement();
        st.executeUpdate(sql);
        Icon iconoo = new ImageIcon(getClass().getResource("/img/ok.png"));
        JOptionPane.showMessageDialog(null, "Precio actualizado correctamente", "Precio Actualizado", JOptionPane.PLAIN_MESSAGE, iconoo);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de UPDATE en actualizar Precio "+e.getMessage());
        }
    }
    
    public boolean Stock_Existe(){
        boolean exist=false;
        String nom;
        nom = (String) prod.getSelectedItem();  
        String[] cat = nom.split(", ");
        nom = cat[0];
        String ca = cat[1];
        String barr = barras.getText();
        String sql = "Select stock, categoria from productos where codigobarras="+barr+" and nombre_producto='"+nom+"' and categoria='"+ca+"';";
        try {
            Statement st = con.createStatement();
            ResultSet res = st.executeQuery(sql);
            while (res.next()) {
                //Productos
                int caa = Integer.parseInt(cantidad.getText());

                if (res.getString(2).equals("Bebidas") && (caa>res.getInt(1))) { //Este está bien, hacer los otros así
                    Icon iconoo = new ImageIcon(getClass().getResource("/img/sell_error.png"));
                    JOptionPane.showMessageDialog(null, "Bebidas Insuficientes", "Falta Producto", JOptionPane.PLAIN_MESSAGE, iconoo);
                    exist=true;
                }
                if (res.getString(2).equals("Botanas") && (caa>res.getInt(1))) { //ya
                    Icon iconoo = new ImageIcon(getClass().getResource("/img/sell_error.png"));
                    JOptionPane.showMessageDialog(null, "Botanas Insuficientes", "Falta Producto", JOptionPane.PLAIN_MESSAGE, iconoo);
                    exist=true;
                }
                if (res.getString(2).equals("Cigarro") && (caa>res.getInt(1))) { //ya
                    Icon iconoo = new ImageIcon(getClass().getResource("/img/sell_error.png"));
                    JOptionPane.showMessageDialog(null, "Cigarros Insuficientes", "Falta Producto", JOptionPane.PLAIN_MESSAGE, iconoo);
                    exist=true;
                }
                
                //Cervezas
                if (res.getString(2).equals("Mega") && (caa>res.getInt(1))) { //ya
                    Icon iconoo = new ImageIcon(getClass().getResource("/img/sell_error.png"));
                    JOptionPane.showMessageDialog(null, "Mega Insuficiente", "Falta Cerveza", JOptionPane.PLAIN_MESSAGE, iconoo);
                    exist=true;
                }
                if (res.getString(2).equals("710") && (caa>res.getInt(1))) { //ya
                    Icon iconoo = new ImageIcon(getClass().getResource("/img/sell_error.png"));
                    JOptionPane.showMessageDialog(null, "710 Insuficiente", "Falta Cerveza", JOptionPane.PLAIN_MESSAGE, iconoo);
                    exist=true;
                }
                if (res.getString(2).equals("Medias") && (caa>res.getInt(1))) {  //ya
                    Icon iconoo = new ImageIcon(getClass().getResource("/img/sell_error.png"));
                    JOptionPane.showMessageDialog(null, "Medias Insuficientes", "Falta Cerveza", JOptionPane.PLAIN_MESSAGE, iconoo);
                    exist=true;
                }
                if (res.getString(2).equals("Familiar") && (caa>res.getInt(1))) { //ya
                    Icon iconoo = new ImageIcon(getClass().getResource("/img/sell_error.png"));
                    JOptionPane.showMessageDialog(null, "Familiar Insuficiente", "Falta Cerveza", JOptionPane.PLAIN_MESSAGE, iconoo);
                    exist=true;
                }
                if (res.getString(2).equals("Cuartitas") && (caa>res.getInt(1))) { //ya
                    Icon iconoo = new ImageIcon(getClass().getResource("/img/sell_error.png"));
                    JOptionPane.showMessageDialog(null, "Cuartitas Insuficientes", "Falta Cerveza", JOptionPane.PLAIN_MESSAGE, iconoo);
                    exist=true;
                }
                if (res.getString(2).equals("Bote") && (caa>res.getInt(1))) { //ya
                    Icon iconoo = new ImageIcon(getClass().getResource("/img/sell_error.png"));
                    JOptionPane.showMessageDialog(null, "Bote Insuficientes", "Falta Cerveza", JOptionPane.PLAIN_MESSAGE, iconoo);
                    exist=true;
                }
                if (res.getString(2).equals("Latones") && (caa>res.getInt(1))) { //ya
                    Icon iconoo = new ImageIcon(getClass().getResource("/img/sell_error.png"));
                    JOptionPane.showMessageDialog(null, "Latones Insuficientes", "Falta Cerveza", JOptionPane.PLAIN_MESSAGE, iconoo);
                    exist=true;
                }
                
                
                if (res.getString(2).equals("Promo") && (caa*2>res.getInt(1))) { //ya
                    Icon iconoo = new ImageIcon(getClass().getResource("/img/sell_error.png"));
                    JOptionPane.showMessageDialog(null, "No hay suficiente cerveza para aplicar Promo", "Falta Cerveza", JOptionPane.PLAIN_MESSAGE, iconoo);
                    exist=true;
                }
                if (res.getString(2).equals("Six") && (caa*6>res.getInt(1))) { //ya
                    Icon iconoo = new ImageIcon(getClass().getResource("/img/sell_error.png"));
                    JOptionPane.showMessageDialog(null, "No hay suficiente cerveza para aplicar Six", "Falta Cerveza", JOptionPane.PLAIN_MESSAGE, iconoo);
                    exist=true;
                }
                if (res.getString(2).equals("Doce") && (caa*12>res.getInt(1))) { //ya
                    Icon iconoo = new ImageIcon(getClass().getResource("/img/sell_error.png"));
                    JOptionPane.showMessageDialog(null, "No hay suficiente cerveza para aplicar Doce Pack", "Falta Cerveza", JOptionPane.PLAIN_MESSAGE, iconoo);
                    exist=true;
                }
                if (res.getString(2).equals("Caja") && (caa*24>res.getInt(1))) { //ya
                    Icon iconoo = new ImageIcon(getClass().getResource("/img/sell_error.png"));
                    JOptionPane.showMessageDialog(null, "No hay suficiente cerveza para aplicar Carton", "Falta Cerveza", JOptionPane.PLAIN_MESSAGE, iconoo);
                    exist=true;
                }
                
               }
        } catch (SQLException e) {
            Icon iconoo = new ImageIcon(getClass().getResource("/img/list.png"));
            JOptionPane.showMessageDialog(null, "Error, en stock existente", "Error falta de productos", JOptionPane.PLAIN_MESSAGE, iconoo);
        }
        return exist;
    }
    
    public void EliminarVenta() {
        int a = tabla_cerv.getSelectedRow();
        System.out.println("Datos : "+tabla_cerv.getValueAt(a, 1));
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
        jLabel37 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        name = new javax.swing.JLabel();
        bienvenido = new javax.swing.JLabel();
        foto = new rojerusan.RSFotoCircle();
        jLabel35 = new javax.swing.JLabel();
        datos_inventarioo = new javax.swing.JPanel();
        datos_inventario = new javax.swing.JScrollPane();
        tabla_cerv_inv = new javax.swing.JTable();
        jButton4 = new javax.swing.JButton();
        jScrollPane6 = new javax.swing.JScrollPane();
        tabla_pro_inv = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jLabel41 = new javax.swing.JLabel();
        jLabel42 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        Reg_venta = new javax.swing.JPanel();
        barras = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        prod = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        cantidad = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla_cerv = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        tabla_prod = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        tabla_depositos = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        search = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        pre = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        caja2 = new javax.swing.JLabel();
        jLabel38 = new javax.swing.JLabel();
        btn_ventaaa = new javax.swing.JPanel();
        btn_ventaa = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
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
        AgregarProducto = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jSeparator10 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        combx_prod = new javax.swing.JComboBox<>();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jSeparator11 = new javax.swing.JSeparator();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        precio_existente = new javax.swing.JTextField();
        jSeparator12 = new javax.swing.JSeparator();
        jSeparator13 = new javax.swing.JSeparator();
        stock_existente = new javax.swing.JTextField();
        ActualizarPrecio_existente = new javax.swing.JButton();
        barras_existente = new javax.swing.JTextField();
        AgregarProducto_existente = new javax.swing.JButton();
        jLabel34 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jSeparator14 = new javax.swing.JSeparator();
        barras_nuevo = new javax.swing.JTextField();
        search1 = new javax.swing.JButton();
        cerrar_caja = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabla_caja = new javax.swing.JTable();
        jScrollPane7 = new javax.swing.JScrollPane();
        tabla_prod_caja = new javax.swing.JTable();
        jScrollPane8 = new javax.swing.JScrollPane();
        tabla_dep_caja = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        texto_nota = new javax.swing.JTextArea();
        jButton5 = new javax.swing.JButton();
        fecha = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        cvz = new javax.swing.JLabel();
        prd = new javax.swing.JLabel();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        dep = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(10, 34, 64));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(10, 34, 64));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        b4.setBackground(new java.awt.Color(248, 184, 48));
        b4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        b4_inv.setBackground(new java.awt.Color(24, 33, 46));

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

        jPanel3.add(b4, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 260, 150, 50));

        b1.setBackground(new java.awt.Color(248, 184, 48));
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

        b1_vent.setBackground(new java.awt.Color(24, 33, 46));

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

        b2.setBackground(new java.awt.Color(248, 184, 48));
        b2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        b2_agprod.setBackground(new java.awt.Color(24, 33, 46));

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
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel4MousePressed(evt);
            }
        });
        b2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 130, 50));

        jPanel3.add(b2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 200, 150, 50));

        b3.setBackground(new java.awt.Color(248, 184, 48));
        b3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        b3_agprom.setBackground(new java.awt.Color(24, 33, 46));

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

        jPanel3.add(b3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 380, 150, 50));

        b5.setBackground(new java.awt.Color(248, 184, 48));
        b5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        b5_cerrarc1.setBackground(new java.awt.Color(24, 33, 46));
        b5_cerrarc1.setForeground(new java.awt.Color(10, 34, 64));

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

        jPanel3.add(b5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 320, 150, 50));

        jLabel37.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/vaso.png"))); // NOI18N
        jPanel3.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 100, 100));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 150, 620));

        jPanel4.setBackground(new java.awt.Color(10, 34, 64));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        name.setFont(new java.awt.Font("Berlin Sans FB", 0, 45)); // NOI18N
        name.setForeground(new java.awt.Color(255, 255, 255));
        jPanel4.add(name, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 40, 380, 50));

        bienvenido.setFont(new java.awt.Font("Berlin Sans FB", 0, 45)); // NOI18N
        bienvenido.setForeground(new java.awt.Color(255, 255, 255));
        bienvenido.setText("Bienvenido, ");
        jPanel4.add(bienvenido, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 40, -1, 50));

        foto.setImagenDefault(new javax.swing.ImageIcon(getClass().getResource("/img/icons8_user_120px.png"))); // NOI18N
        jPanel4.add(foto, new org.netbeans.lib.awtextra.AbsoluteConstraints(535, 20, 100, 100));

        jLabel35.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/modelorama.png"))); // NOI18N
        jPanel4.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(700, 0, 240, 140));

        jPanel1.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 0, 1000, 140));

        datos_inventarioo.setBackground(new java.awt.Color(35, 76, 119));
        datos_inventarioo.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tabla_cerv_inv = new javax.swing.JTable() {
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        tabla_cerv_inv.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        tabla_cerv_inv.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        tabla_cerv_inv.setRowHeight(19);
        tabla_cerv_inv.setShowHorizontalLines(false);
        tabla_cerv_inv.getTableHeader().setResizingAllowed(false);
        tabla_cerv_inv.getTableHeader().setReorderingAllowed(false);
        datos_inventario.setViewportView(tabla_cerv_inv);

        datos_inventarioo.add(datos_inventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 100, 540, 320));

        jButton4.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/print.png"))); // NOI18N
        jButton4.setContentAreaFilled(false);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        datos_inventarioo.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 420, 70, 60));

        tabla_pro_inv = new javax.swing.JTable() {
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        tabla_pro_inv.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        tabla_pro_inv.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tabla_pro_inv.setRowHeight(20);
        tabla_pro_inv.getTableHeader().setResizingAllowed(false);
        tabla_pro_inv.getTableHeader().setReorderingAllowed(false);
        jScrollPane6.setViewportView(tabla_pro_inv);

        datos_inventarioo.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 100, 540, 320));

        jPanel6.setBackground(new java.awt.Color(24, 33, 46));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel41.setFont(new java.awt.Font("Berlin Sans FB", 0, 36)); // NOI18N
        jLabel41.setForeground(new java.awt.Color(255, 255, 255));
        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel41.setText("PRODUCTOS");
        jPanel6.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 0, 540, 100));

        jLabel42.setFont(new java.awt.Font("Berlin Sans FB", 0, 36)); // NOI18N
        jLabel42.setForeground(new java.awt.Color(255, 255, 255));
        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel42.setText("CERVEZA");
        jPanel6.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 540, 100));

        jLabel36.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/cerv1.png"))); // NOI18N
        jPanel6.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 5, 80, 90));

        jLabel40.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/prod1.png"))); // NOI18N
        jPanel6.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 5, -1, 90));

        datos_inventarioo.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 100));

        jPanel1.add(datos_inventarioo, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 140, 1100, 480));

        Reg_venta.setBackground(new java.awt.Color(35, 76, 119));
        Reg_venta.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        barras.setBackground(new java.awt.Color(35, 76, 119));
        barras.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        barras.setForeground(new java.awt.Color(255, 255, 255));
        barras.setBorder(null);
        barras.setOpaque(false);
        barras.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                barrasKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                barrasKeyTyped(evt);
            }
        });
        Reg_venta.add(barras, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 20, 170, 20));

        jLabel7.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Codigo:");
        Reg_venta.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 70, -1));

        prod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                prodActionPerformed(evt);
            }
        });
        Reg_venta.add(prod, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 70, 210, -1));

        jLabel8.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Nombre:");
        Reg_venta.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 70, 80, -1));

        cantidad.setBackground(new java.awt.Color(35, 76, 119));
        cantidad.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        cantidad.setForeground(new java.awt.Color(255, 255, 255));
        cantidad.setBorder(null);
        cantidad.setOpaque(false);
        cantidad.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cantidadKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cantidadKeyTyped(evt);
            }
        });
        Reg_venta.add(cantidad, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 110, 80, 20));

        tabla_cerv = new javax.swing.JTable() {
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        tabla_cerv.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        tabla_cerv.getTableHeader().setReorderingAllowed(false);
        tabla_cerv.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tabla_cerv.setGridColor(new java.awt.Color(187, 223, 215));
        tabla_cerv.setRowHeight(20);
        tabla_cerv.setShowHorizontalLines(false);
        tabla_cerv.getTableHeader().setResizingAllowed(false);
        tabla_cerv.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tabla_cerv);

        Reg_venta.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 50, 390, 430));

        jScrollPane4.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N

        tabla_prod = new javax.swing.JTable() {
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        tabla_prod.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        tabla_prod.getTableHeader().setReorderingAllowed(false);
        tabla_prod.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tabla_prod.setRowHeight(20);
        tabla_prod.setShowHorizontalLines(false);
        tabla_prod.getTableHeader().setResizingAllowed(false);
        tabla_prod.getTableHeader().setReorderingAllowed(false);
        jScrollPane4.setViewportView(tabla_prod);

        Reg_venta.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 50, 390, 430));

        tabla_depositos = new javax.swing.JTable() {
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        tabla_depositos.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        tabla_depositos.getTableHeader().setReorderingAllowed(false);
        tabla_depositos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tabla_depositos.setRowHeight(20);
        tabla_depositos.getTableHeader().setResizingAllowed(false);
        tabla_depositos.getTableHeader().setReorderingAllowed(false);
        jScrollPane5.setViewportView(tabla_depositos);

        Reg_venta.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 310, 300, 170));

        jPanel5.setBackground(new java.awt.Color(24, 33, 46));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel21.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("Cerveza");
        jPanel5.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 390, 50));

        jLabel22.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("Productos");
        jPanel5.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 0, 390, 50));

        Reg_venta.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 0, 790, 50));

        search.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search.png"))); // NOI18N
        search.setBorderPainted(false);
        search.setContentAreaFilled(false);
        search.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchActionPerformed(evt);
            }
        });
        Reg_venta.add(search, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 10, 50, 40));

        jLabel12.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Cantidad:");
        Reg_venta.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 80, -1));

        pre.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        pre.setForeground(new java.awt.Color(255, 255, 255));
        Reg_venta.add(pre, new org.netbeans.lib.awtextra.AbsoluteConstraints(103, 150, 50, 20));
        Reg_venta.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 40, 170, 10));

        jLabel18.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/caja.png"))); // NOI18N
        jLabel18.setText(":");
        Reg_venta.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 170, 80, 50));

        jLabel20.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Precio:");
        Reg_venta.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 150, 80, -1));
        Reg_venta.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 130, 80, 10));

        caja2.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        caja2.setForeground(new java.awt.Color(255, 255, 255));
        caja2.setText("0");
        Reg_venta.add(caja2, new org.netbeans.lib.awtextra.AbsoluteConstraints(103, 182, 60, 30));

        jLabel38.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(255, 255, 255));
        jLabel38.setText("$");
        Reg_venta.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 182, -1, 30));

        btn_ventaaa.setBackground(new java.awt.Color(248, 184, 48));
        btn_ventaaa.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        btn_ventaaa.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                btn_ventaaaMouseMoved(evt);
            }
        });
        btn_ventaaa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn_ventaaaMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btn_ventaaaMousePressed(evt);
            }
        });

        btn_ventaa.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        btn_ventaa.setForeground(new java.awt.Color(10, 34, 64));
        btn_ventaa.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        btn_ventaa.setText("Vender");

        javax.swing.GroupLayout btn_ventaaaLayout = new javax.swing.GroupLayout(btn_ventaaa);
        btn_ventaaa.setLayout(btn_ventaaaLayout);
        btn_ventaaaLayout.setHorizontalGroup(
            btn_ventaaaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(btn_ventaaaLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(btn_ventaa)
                .addContainerGap(32, Short.MAX_VALUE))
        );
        btn_ventaaaLayout.setVerticalGroup(
            btn_ventaaaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, btn_ventaaaLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btn_ventaa, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        Reg_venta.add(btn_ventaaa, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 190, 120, 30));

        jPanel2.setBackground(new java.awt.Color(24, 33, 46));

        jLabel17.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 255, 255));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Pagos y Depositos");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel17, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        Reg_venta.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 260, 300, 50));

        jLabel39.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setText("$");
        Reg_venta.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 150, -1, -1));

        jPanel1.add(Reg_venta, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 140, 1100, 480));

        Agregar_prod.setBackground(new java.awt.Color(68, 132, 206));
        Agregar_prod.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Categoría");
        Agregar_prod.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 290, 90, -1));

        nombre.setBackground(new java.awt.Color(68, 132, 206));
        nombre.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        nombre.setForeground(new java.awt.Color(255, 255, 255));
        nombre.setBorder(null);
        nombre.setOpaque(false);
        Agregar_prod.add(nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 130, 160, 20));
        Agregar_prod.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 150, 160, 20));

        jLabel10.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Nombre del producto ");
        Agregar_prod.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 140, 170, -1));
        Agregar_prod.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 190, 160, 10));

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
        Agregar_prod.add(contenido, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 210, 110, 20));
        Agregar_prod.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 270, 160, 20));

        precio.setBackground(new java.awt.Color(68, 132, 206));
        precio.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        precio.setForeground(new java.awt.Color(255, 255, 255));
        precio.setBorder(null);
        precio.setOpaque(false);
        precio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                precioKeyTyped(evt);
            }
        });
        Agregar_prod.add(precio, new org.netbeans.lib.awtextra.AbsoluteConstraints(820, 250, 150, 20));

        jLabel11.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Codigo de barras");
        Agregar_prod.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 170, 130, -1));
        Agregar_prod.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 270, 110, 20));
        Agregar_prod.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 310, 160, 20));

        categoria.setBackground(new java.awt.Color(68, 132, 206));
        categoria.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        categoria.setForeground(new java.awt.Color(255, 255, 255));
        categoria.setBorder(null);
        categoria.setOpaque(false);
        Agregar_prod.add(categoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 290, 160, 20));

        if (contenido.getText().equals("1.02") || contenido.getText().equals("2.5")) {
            mlolts.setText("lts");
        } else {
            mlolts.setText("ml");
        }
        mlolts.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        mlolts.setForeground(new java.awt.Color(255, 255, 255));
        mlolts.setText("ml/lts");
        Agregar_prod.add(mlolts, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 210, 40, 20));

        stock.setBackground(new java.awt.Color(68, 132, 206));
        stock.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        stock.setForeground(new java.awt.Color(255, 255, 255));
        stock.setBorder(null);
        stock.setOpaque(false);
        Agregar_prod.add(stock, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 330, 160, 20));
        Agregar_prod.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 350, 160, 20));

        jLabel13.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Stock");
        Agregar_prod.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 330, 60, -1));

        AgregarProducto.setBackground(new java.awt.Color(241, 159, 77));
        AgregarProducto.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        AgregarProducto.setForeground(new java.awt.Color(255, 255, 255));
        AgregarProducto.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        AgregarProducto.setLabel("Agregar Producto");
        AgregarProducto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AgregarProductoActionPerformed(evt);
            }
        });
        Agregar_prod.add(AgregarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(770, 380, 200, 40));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/beer.png"))); // NOI18N
        Agregar_prod.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 360, 60, 70));

        jLabel15.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Precio");
        Agregar_prod.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 250, 60, -1));

        jLabel16.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("$");
        Agregar_prod.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 250, 30, -1));

        jSeparator10.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator10.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jSeparator10.setPreferredSize(new java.awt.Dimension(50, 20));
        Agregar_prod.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 0, 30, 480));

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Berlin Sans FB", 0, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Existente");
        Agregar_prod.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 550, 90));

        jLabel23.setBackground(new java.awt.Color(255, 255, 255));
        jLabel23.setFont(new java.awt.Font("Berlin Sans FB", 0, 36)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("Nuevo");
        Agregar_prod.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 0, 550, 90));

        combx_prod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combx_prodActionPerformed(evt);
            }
        });
        Agregar_prod.add(combx_prod, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 140, 210, -1));

        jLabel25.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Nombre del producto ");
        Agregar_prod.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 130, 170, -1));

        jLabel26.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Contenido");
        Agregar_prod.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 210, 90, -1));
        Agregar_prod.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 230, 160, 10));

        jLabel27.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Precio");
        Agregar_prod.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 250, 60, -1));

        jLabel28.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Categoría");
        Agregar_prod.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 290, 90, -1));

        jLabel29.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Stock");
        Agregar_prod.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 330, 60, -1));

        jLabel30.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("Precio");
        Agregar_prod.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 220, 50, -1));

        jLabel32.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Stock");
        Agregar_prod.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 310, 50, -1));

        precio_existente.setBackground(new java.awt.Color(68, 132, 206));
        precio_existente.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        precio_existente.setForeground(new java.awt.Color(255, 255, 255));
        precio_existente.setBorder(null);
        precio_existente.setOpaque(false);
        precio_existente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                precio_existenteKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                precio_existenteKeyTyped(evt);
            }
        });
        Agregar_prod.add(precio_existente, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 220, 110, 20));
        Agregar_prod.add(jSeparator12, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 330, 110, 20));
        Agregar_prod.add(jSeparator13, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 240, 110, 20));

        stock_existente.setBackground(new java.awt.Color(68, 132, 206));
        stock_existente.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        stock_existente.setForeground(new java.awt.Color(255, 255, 255));
        stock_existente.setBorder(null);
        stock_existente.setOpaque(false);
        stock_existente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                stock_existenteKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                stock_existenteKeyTyped(evt);
            }
        });
        Agregar_prod.add(stock_existente, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 310, 110, 20));

        ActualizarPrecio_existente.setBackground(new java.awt.Color(241, 159, 77));
        ActualizarPrecio_existente.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        ActualizarPrecio_existente.setForeground(new java.awt.Color(255, 255, 255));
        ActualizarPrecio_existente.setText("Actualizar Precio");
        ActualizarPrecio_existente.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        ActualizarPrecio_existente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ActualizarPrecio_existenteActionPerformed(evt);
            }
        });
        Agregar_prod.add(ActualizarPrecio_existente, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 260, 170, -1));

        barras_existente.setBackground(new java.awt.Color(68, 132, 206));
        barras_existente.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        barras_existente.setForeground(new java.awt.Color(255, 255, 255));
        barras_existente.setBorder(null);
        barras_existente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                barras_existenteKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                barras_existenteKeyTyped(evt);
            }
        });
        Agregar_prod.add(barras_existente, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 180, 150, 20));

        AgregarProducto_existente.setBackground(new java.awt.Color(241, 159, 77));
        AgregarProducto_existente.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        AgregarProducto_existente.setForeground(new java.awt.Color(255, 255, 255));
        AgregarProducto_existente.setText("Actualizar Stock");
        AgregarProducto_existente.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.LOWERED));
        AgregarProducto_existente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AgregarProducto_existenteActionPerformed(evt);
            }
        });
        Agregar_prod.add(AgregarProducto_existente, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 360, 170, -1));

        jLabel34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/money.png"))); // NOI18N
        Agregar_prod.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 250, 70, 50));

        jLabel31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/stock.png"))); // NOI18N
        Agregar_prod.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 340, 60, -1));

        jLabel33.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("Codigo de barras");
        Agregar_prod.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 180, 130, -1));
        Agregar_prod.add(jSeparator14, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 200, 150, 10));

        barras_nuevo.setBackground(new java.awt.Color(68, 132, 206));
        barras_nuevo.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        barras_nuevo.setForeground(new java.awt.Color(255, 255, 255));
        barras_nuevo.setBorder(null);
        Agregar_prod.add(barras_nuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 170, 160, -1));

        search1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/search.png"))); // NOI18N
        search1.setBorderPainted(false);
        search1.setContentAreaFilled(false);
        search1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                search1ActionPerformed(evt);
            }
        });
        Agregar_prod.add(search1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 170, 50, 40));

        jPanel1.add(Agregar_prod, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 140, 1100, 480));

        cerrar_caja.setBackground(new java.awt.Color(35, 76, 119));
        cerrar_caja.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane3.setBorder(null);

        tabla_caja = new javax.swing.JTable() {
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        tabla_caja.getTableHeader().setReorderingAllowed(false);
        tabla_caja.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        tabla_caja.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tabla_caja.setRowHeight(20);
        tabla_caja.setShowHorizontalLines(false);
        tabla_caja.getTableHeader().setResizingAllowed(false);
        tabla_caja.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(tabla_caja);

        cerrar_caja.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 50, 450, 310));

        tabla_prod_caja = new javax.swing.JTable() {
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        tabla_prod_caja.getTableHeader().setReorderingAllowed(false);
        tabla_prod_caja.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        tabla_prod_caja.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tabla_prod_caja.setRowHeight(20);
        tabla_prod_caja.getTableHeader().setResizingAllowed(false);
        tabla_prod_caja.getTableHeader().setReorderingAllowed(false);
        jScrollPane7.setViewportView(tabla_prod_caja);

        cerrar_caja.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 50, 420, 310));

        tabla_dep_caja = new javax.swing.JTable() {
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        tabla_dep_caja.getTableHeader().setReorderingAllowed(false);
        tabla_dep_caja.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        tabla_dep_caja.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        tabla_dep_caja.setRowHeight(20);
        tabla_dep_caja.getTableHeader().setResizingAllowed(false);
        tabla_dep_caja.getTableHeader().setReorderingAllowed(false);
        jScrollPane8.setViewportView(tabla_dep_caja);

        cerrar_caja.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 50, 210, 310));

        jPanel9.setBackground(new java.awt.Color(10, 34, 64));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        cerrar_caja.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 710, -1));

        jButton3.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/print.png"))); // NOI18N
        jButton3.setContentAreaFilled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        cerrar_caja.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 420, 70, 60));

        jLabel24.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Nota:");
        cerrar_caja.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 400, -1, -1));

        jScrollPane2.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N

        texto_nota.setColumns(20);
        texto_nota.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        texto_nota.setRows(5);
        jScrollPane2.setViewportView(texto_nota);

        cerrar_caja.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 407, 410, 70));

        jButton5.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/escoba.png"))); // NOI18N
        jButton5.setContentAreaFilled(false);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        cerrar_caja.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 420, 70, 60));

        fecha.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        fecha.setForeground(new java.awt.Color(255, 255, 255));
        cerrar_caja.add(fecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 0, 150, 50));

        jLabel19.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Ventas del dia");
        cerrar_caja.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(800, 0, 160, 50));

        jLabel43.setFont(new java.awt.Font("Berlin Sans FB", 0, 20)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(255, 255, 255));
        jLabel43.setText("Total Cerveza:");
        cerrar_caja.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 360, 130, 30));

        jLabel44.setFont(new java.awt.Font("Berlin Sans FB", 0, 20)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(255, 255, 255));
        jLabel44.setText("$");
        cerrar_caja.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 360, 10, 30));

        cvz.setFont(new java.awt.Font("Berlin Sans FB", 0, 20)); // NOI18N
        cvz.setForeground(new java.awt.Color(255, 255, 255));
        cerrar_caja.add(cvz, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 360, 70, 30));

        prd.setFont(new java.awt.Font("Berlin Sans FB", 0, 20)); // NOI18N
        prd.setForeground(new java.awt.Color(255, 255, 255));
        cerrar_caja.add(prd, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 360, 70, 30));

        jLabel47.setFont(new java.awt.Font("Berlin Sans FB", 0, 20)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(255, 255, 255));
        jLabel47.setText("$");
        cerrar_caja.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(797, 360, 20, 30));

        jLabel48.setFont(new java.awt.Font("Berlin Sans FB", 0, 20)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(255, 255, 255));
        jLabel48.setText("Total Productos:");
        cerrar_caja.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(657, 360, 140, 30));

        dep.setFont(new java.awt.Font("Berlin Sans FB", 0, 20)); // NOI18N
        dep.setForeground(new java.awt.Color(255, 255, 255));
        cerrar_caja.add(dep, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 360, 70, 30));

        jLabel50.setFont(new java.awt.Font("Berlin Sans FB", 0, 20)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(255, 255, 255));
        jLabel50.setText("$");
        cerrar_caja.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(1017, 360, 10, 30));

        jLabel51.setFont(new java.awt.Font("Berlin Sans FB", 0, 20)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(255, 255, 255));
        jLabel51.setText("Total Deposito:");
        cerrar_caja.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 360, 150, 30));

        jPanel1.add(cerrar_caja, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 140, 1100, 480));

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/min.png"))); // NOI18N
        jButton1.setContentAreaFilled(false);
        jButton1.setName(""); // NOI18N
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(1160, 0, 50, 40));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/close.png"))); // NOI18N
        jButton2.setContentAreaFilled(false);
        jButton2.setName(""); // NOI18N
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(1220, 0, 30, 40));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1250, 620));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MousePressed
        b2_agprod.setVisible(true); //AGREGAR PRODUCTOOOOOOOO
        b1_vent.setVisible(false);
        b3_agprom.setVisible(false);
        b4_inv.setVisible(false);
        b5_cerrarc1.setVisible(false);
        
        
        datos_inventarioo.setVisible(false);
        Reg_venta.setVisible(false);
        cerrar_caja.setVisible(false);
        Agregar_prod.setVisible(true);
        
        combx_prod.setModel(llenar());
    }//GEN-LAST:event_jLabel4MousePressed

    private void jLabel2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MousePressed
        b1_vent.setVisible(true); //VENTAAAAAAAASSSS
        b2_agprod.setVisible(false);
        b3_agprom.setVisible(false);
        b4_inv.setVisible(false);
        b5_cerrarc1.setVisible(false);
        
        
        
        datos_inventarioo.setVisible(false);
        cerrar_caja.setVisible(false);
        Agregar_prod.setVisible(false);
        Reg_venta.setVisible(true);
        prod.setModel(llenar());
        MostrarTablaProductos();
        MostrarTablaCerveza();
        MostrarTablaDepositos();
        CajaTot();
    }//GEN-LAST:event_jLabel2MousePressed

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
        cerrar_caja.setVisible(false);
        datos_inventarioo.setVisible(true);
        
        MostrarTablaCerveza_inv();
        MostrarTablaProductos_inv();
    }//GEN-LAST:event_jLabel1MousePressed

    private void jLabel3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MousePressed
        b5_cerrarc1.setVisible(true); //CERRAR CAJAAAAAAA
        b2_agprod.setVisible(false);
        b3_agprom.setVisible(false);
        b1_vent.setVisible(false);
        b4_inv.setVisible(false);
        
        Reg_venta.setVisible(false);
        Agregar_prod.setVisible(false);
        datos_inventarioo.setVisible(false);
        cerrar_caja.setVisible(true);
        MostrarTablaVentas2(); //Cerveza
        MostrarTablaProductos2(); //Prod
        MostrarTablaDepositos2(); //Dep y pagos
        fecha.setText(getFecha());
        cvz.setText(SumaCerveza());
        prd.setText(SumaProductos());
        dep.setText(SumaPagosYDep());
    }//GEN-LAST:event_jLabel3MousePressed

    private void precioKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_precioKeyTyped
        char validar = evt.getKeyChar();
        
        if (Character.isLetter(validar)) {
            getToolkit().beep();
            evt.consume();
            Icon iconoo = new ImageIcon(getClass().getResource("/img/invalid.png"));
            JOptionPane.showMessageDialog(null, "Solo numeros para el precio", "Solo numeros", JOptionPane.PLAIN_MESSAGE, iconoo);
        }
    }//GEN-LAST:event_precioKeyTyped

    private void contenidoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_contenidoKeyTyped
        char validar = evt.getKeyChar();
        
        if (Character.isLetter(validar)) {
            getToolkit().beep();
            evt.consume();
            Icon iconoo = new ImageIcon(getClass().getResource("/img/invalid.png"));
            JOptionPane.showMessageDialog(null, "Solo numeros para el precio", "Solo numeros", JOptionPane.PLAIN_MESSAGE, iconoo);
        }
    }//GEN-LAST:event_contenidoKeyTyped

    private void AgregarProductoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AgregarProductoActionPerformed
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

        
    }//GEN-LAST:event_AgregarProductoActionPerformed

    private void searchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchActionPerformed
        Buscar();
    }//GEN-LAST:event_searchActionPerformed

    private void barrasKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_barrasKeyTyped
        char validar = evt.getKeyChar();
        
        if (Character.isLetter(validar)) {
            getToolkit().beep();
            evt.consume();
            
            Icon iconoo = new ImageIcon(getClass().getResource("/img/invalid.png"));
            JOptionPane.showMessageDialog(null, "Solo numeros para el precio", "Solo numeros", JOptionPane.PLAIN_MESSAGE, iconoo);
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
           String sql = "Select codigobarras, precio from productos where nombre_producto='"+np+"' and categoria='"+cp+"';";
           try {
               Statement st = con.createStatement();
               ResultSet res = st.executeQuery(sql);
   
               while (res.next()) {
                   String n = res.getString(1);
                   String c = res.getString(2);
                   barras.setText(n); //Para el codigo de barras
                   pre.setText(c); //precio
               }
           } catch (SQLException e) {
           JOptionPane.showMessageDialog(null, "Error, el producto no existe en JCombox");
        }
       }
       
       
    }//GEN-LAST:event_prodActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        String namee = "Reporte_"+getFecha2()+".pdf";
        if (JOptionPane.showConfirmDialog(null, "Está usted seguro de que quiere cerrar caja?")==0 ) {
            Imprimir2(namee);
            texto_nota.setText(null);
        }  
    }//GEN-LAST:event_jButton3ActionPerformed

    private void combx_prodActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_combx_prodActionPerformed
        Existente();
    }//GEN-LAST:event_combx_prodActionPerformed

    private void ActualizarPrecio_existenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ActualizarPrecio_existenteActionPerformed
        ActualizarPrecio();
        combx_prod.setSelectedIndex(0);
        barras_existente.setText(null);
        precio_existente.setText(null);
        stock_existente.setText(null);
    }//GEN-LAST:event_ActualizarPrecio_existenteActionPerformed

    private void AgregarProducto_existenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AgregarProducto_existenteActionPerformed
        AgregarExistente();
        combx_prod.setSelectedIndex(0);
        barras_existente.setText(null);
        precio_existente.setText(null);
        stock_existente.setText(null);
    }//GEN-LAST:event_AgregarProducto_existenteActionPerformed

    private void btn_ventaaaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ventaaaMouseExited
        btn_ventaaa.setBackground(new Color(248,184,48));
    }//GEN-LAST:event_btn_ventaaaMouseExited

    private void btn_ventaaaMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ventaaaMouseMoved
        btn_ventaaa.setBackground(new Color(253,230,86));
    }//GEN-LAST:event_btn_ventaaaMouseMoved

    private void btn_ventaaaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_ventaaaMousePressed
        try {
            if (cantidad.getText().isEmpty() || prod.getSelectedItem().equals("Seleccionar")) {
                JOptionPane.showMessageDialog(null, "Campos vacios, por favor llena todos los campos");
            } 
            if (!Stock_Existe()) { //falta mult * cantidad
                vender();
                ActualizarVentas();
                MostrarTablaProductos();
                MostrarTablaCerveza();
                MostrarTablaDepositos();
                CajaTot();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de UPDATE en funcion Vender del BTN "+e.getMessage());
        }
    }//GEN-LAST:event_btn_ventaaaMousePressed

    private void barrasKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_barrasKeyPressed
        char tecla = evt.getKeyChar();
        
        if (tecla==KeyEvent.VK_ENTER) {
            search.doClick();
        }
    }//GEN-LAST:event_barrasKeyPressed

    private void search1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_search1ActionPerformed
        Buscar2();
    }//GEN-LAST:event_search1ActionPerformed

    private void barras_existenteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_barras_existenteKeyPressed
        char tecla = evt.getKeyChar();
        
        if (tecla==KeyEvent.VK_ENTER) {
            search1.doClick();
        }
    }//GEN-LAST:event_barras_existenteKeyPressed

    private void cantidadKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cantidadKeyTyped
        char validar = evt.getKeyChar();
        
        if (Character.isLetter(validar)) {
            getToolkit().beep();
            evt.consume();
            
            Icon iconoo = new ImageIcon(getClass().getResource("/img/invalid.png"));
            JOptionPane.showMessageDialog(null, "Solo numeros para la cantidad", "Solo numeros", JOptionPane.PLAIN_MESSAGE, iconoo);
        }
    }//GEN-LAST:event_cantidadKeyTyped

    private void barras_existenteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_barras_existenteKeyTyped
        char validar = evt.getKeyChar();
        
        if (Character.isLetter(validar)) {
            getToolkit().beep();
            evt.consume();
            
            Icon iconoo = new ImageIcon(getClass().getResource("/img/invalid.png"));
            JOptionPane.showMessageDialog(null, "Solo numeros para el codigo de barras", "Solo numeros", JOptionPane.PLAIN_MESSAGE, iconoo);
        }
    }//GEN-LAST:event_barras_existenteKeyTyped

    private void precio_existenteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_precio_existenteKeyTyped
        char validar = evt.getKeyChar();
        
        if (Character.isLetter(validar)) {
            getToolkit().beep();
            evt.consume();
            
            Icon iconoo = new ImageIcon(getClass().getResource("/img/invalid.png"));
            JOptionPane.showMessageDialog(null, "Solo numeros para el precio", "Solo numeros", JOptionPane.PLAIN_MESSAGE, iconoo);
        }
    }//GEN-LAST:event_precio_existenteKeyTyped

    private void stock_existenteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_stock_existenteKeyTyped
        char validar = evt.getKeyChar();
        
        if (Character.isLetter(validar)) {
            getToolkit().beep();
            evt.consume();
            
            Icon iconoo = new ImageIcon(getClass().getResource("/img/invalid.png"));
            JOptionPane.showMessageDialog(null, "Solo numeros para el stock", "Solo numeros", JOptionPane.PLAIN_MESSAGE, iconoo);
        }
    }//GEN-LAST:event_stock_existenteKeyTyped

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
       String namee = "Inventario_"+getFecha2()+".pdf";
        if (JOptionPane.showConfirmDialog(null, "¿Desea imprimir el inventario?")==0 ) {
            Imprimir3(namee);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void cantidadKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cantidadKeyPressed
        char tecla = evt.getKeyChar();
        
        if (tecla==KeyEvent.VK_ENTER) {
            try {
                if (cantidad.getText().isEmpty() || prod.getSelectedItem().equals("Seleccionar")) {
                    JOptionPane.showMessageDialog(null, "Campos vacios, por favor llena todos los campos");
                } 
                if (!Stock_Existe()) { //falta mult * cantidad
                    vender();
                    ActualizarVentas();
                    MostrarTablaProductos();
                    MostrarTablaCerveza();
                    MostrarTablaDepositos();
                    CajaTot();
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error de UPDATE en funcion Vender del BTN "+e.getMessage());
            }
        }
    }//GEN-LAST:event_cantidadKeyPressed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        if (JOptionPane.showConfirmDialog(null, "Está usted seguro de que quiere Limpiar las ventas?")==0 ) {
            Reset();
            texto_nota.setText(null);
        }
        
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        this.dispose();
        System.exit(0);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.setExtendedState(ICONIFIED);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void precio_existenteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_precio_existenteKeyPressed
        char tecla = evt.getKeyChar();
        
        if (tecla==KeyEvent.VK_ENTER) {
            ActualizarPrecio_existente.doClick();
        }
    }//GEN-LAST:event_precio_existenteKeyPressed

    private void stock_existenteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_stock_existenteKeyPressed
        char tecla = evt.getKeyChar();
        
        if (tecla==KeyEvent.VK_ENTER) {
            AgregarProducto_existente.doClick();
        }
    }//GEN-LAST:event_stock_existenteKeyPressed

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
    private javax.swing.JButton ActualizarPrecio_existente;
    private javax.swing.JButton AgregarProducto;
    private javax.swing.JButton AgregarProducto_existente;
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
    private javax.swing.JTextField barras_existente;
    private javax.swing.JTextField barras_nuevo;
    private javax.swing.JLabel bienvenido;
    private javax.swing.JLabel btn_ventaa;
    private javax.swing.JPanel btn_ventaaa;
    private javax.swing.JLabel caja2;
    private javax.swing.JTextField cantidad;
    private javax.swing.JTextField categoria;
    private javax.swing.JPanel cerrar_caja;
    private javax.swing.JComboBox<String> combx_prod;
    private javax.swing.JTextField contenido;
    private javax.swing.JLabel cvz;
    private javax.swing.JScrollPane datos_inventario;
    private javax.swing.JPanel datos_inventarioo;
    private javax.swing.JLabel dep;
    private javax.swing.JLabel fecha;
    private rojerusan.RSFotoCircle foto;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
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
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator13;
    private javax.swing.JSeparator jSeparator14;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JLabel mlolts;
    private javax.swing.JLabel name;
    private javax.swing.JTextField nombre;
    private javax.swing.JLabel prd;
    private javax.swing.JLabel pre;
    private javax.swing.JTextField precio;
    private javax.swing.JTextField precio_existente;
    private javax.swing.JComboBox<String> prod;
    private javax.swing.JButton search;
    private javax.swing.JButton search1;
    private javax.swing.JTextField stock;
    private javax.swing.JTextField stock_existente;
    private javax.swing.JTable tabla_caja;
    private javax.swing.JTable tabla_cerv;
    private javax.swing.JTable tabla_cerv_inv;
    private javax.swing.JTable tabla_dep_caja;
    private javax.swing.JTable tabla_depositos;
    private javax.swing.JTable tabla_pro_inv;
    private javax.swing.JTable tabla_prod;
    private javax.swing.JTable tabla_prod_caja;
    private javax.swing.JTextArea texto_nota;
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
