/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package formularios;

import conexion.conexionSQL;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Panel;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
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
import java.awt.Color;
        
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;



/**
 *
 * @author Compax
 */
public class sistema extends javax.swing.JFrame {
    conexionSQL cc = new conexionSQL();
    Connection con = cc.conexion();
    double cajaa=500;
    
    public sistema() {
        initComponents();
        this.setLocationRelativeTo(null);
        setNombre();
        this.setIconImage(new ImageIcon(getClass().getResource("/img/icon.png")).getImage());
        cajaa = Double.parseDouble(JOptionPane.showInputDialog(null, "Base de caja: "));
        caja.setText(Double.toString(cajaa));
        
        b1_vent.setVisible(false);
        b2_agprod.setVisible(false);
        b3_agprom.setVisible(false);
        b4_inv.setVisible(false);
        b5_cerrarc1.setVisible(false);
        
        
        Reg_venta.setVisible(false);
        datos_inventario.setVisible(false);
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
          
        } catch (SQLException e) {
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
        double tot=0;
        nom = (String) prod.getSelectedItem();  
        String[] cat = nom.split(", ");
        nom = cat[0];
        String ca = cat[1];
        String barr = barras.getText();
        String sql = "Select stock, categoria from productos where codigobarras="+barr+";"; //Se actualiza el stock
        try {
        Statement st = con.createStatement();
        ResultSet res = st.executeQuery(sql);
            while (res.next()) {
                if (res.getString(2).equals("Promo")) {
                    a = res.getInt(1)-(Integer.parseInt(cantidad.getText())*2);
                } if (res.getString(2).equals("Six")) {
                    a = res.getInt(1)-(Integer.parseInt(cantidad.getText())*6);
                } else {
                    a = res.getInt(1)-Integer.parseInt(cantidad.getText());
                }
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de Venta, en stock "+e.getMessage());
        }
        
        /*String sql2 = "Select idproductos from productos where nombre_producto='"+nom+"' and categoria='"+ca+"';"; //Para obtener el id
        try {
        Statement st = con.createStatement();
        ResultSet res = st.executeQuery(sql2);
            while (res.next()) {
                id = res.getInt(1);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de Consulta ID "+e.getMessage());
        }*/
        
        String sql3 = "update productos set stock="+a+" where codigobarras="+barr+";"; //Se actualiza el stock
        try {
        Statement st = con.createStatement();
        st.executeUpdate(sql3);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de UPDATE "+e.getMessage());
        }
        
        //Para hacer la operacion del calculo de precio final
        
        ActualizarVentas();
        
        //Operacion para cajas
        
        String sql4 = "SELECT total FROM ventas where idventa=(Select max(idventa) from ventas);";
        try {
        Statement st = con.createStatement();
        ResultSet res = st.executeQuery(sql4);
            while (res.next()) {
                tot = Double.parseDouble(res.getString(1));
                System.out.println(tot);
                cajaa+=tot;
                System.out.println(cajaa);
                caja.setText(Double.toString(cajaa));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de Consulta ID "+e.getMessage());
        }
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
                JOptionPane.showMessageDialog(null, "Error obtener suma total ventas en la impresion"+e.getMessage());
            }
            
            caja2.setText(ven);
    }
    
    public void AgregarProducto() {
        double pr = Double.parseDouble(precio.getText());
        int sto = Integer.parseInt(stock.getText());
        String sql = "insert into productos (codigobarras, nombre_producto, contenido, precio, categoria, stock) VALUES ("+barras_existente.getText()+",'"+nombre.getText()+"', '"+contenido.getText()+"', '"+pr+"', '"+categoria.getText()+"', '"+sto+"');";
        try {
        Statement st = con.createStatement();
        st.executeUpdate(sql);
        JOptionPane.showMessageDialog(null, "Producto Agregado Correctamente");
        nombre.setText(null);
        barras_nuevo.setText(null);
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
            tabla_ventas.setModel(modelo);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de Muestra "+e.getMessage());
        }
    }
    
    public String getFecha() {
        Date fecha = new Date();
        SimpleDateFormat fech = new SimpleDateFormat("dd/MM/YYYY");
        return fech.format(fecha);
    }
    
    public String getFecha2() {
        Date fecha = new Date();
        SimpleDateFormat fech = new SimpleDateFormat("dd-MM-YYYY");
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
                pre.setText(res.getString(3));
                for (int j = 0; j < tam-1; j++) { //Recorrer los nombres y las categorias por separado
                    if (nombree[j].equals(n) && categ[j].equals(c)) {
                        ee = j;
                    }
                } 
            }
            if (ee==0) {
                prod.setSelectedIndex(0);
                JOptionPane.showMessageDialog(null, "Error, el producto no existe ");
                pre.setText("0.0");
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
           /*for (int i=0;i<pre.getText().length();i++) {
               if(Character.isDigit(pre.getText().charAt(i))) {
                   prp+=pre.getText().charAt(i);
               }
           }*/
        double ca=0;  
        String sql2 = "select categoria FROM ventas where idventa=(SELECT max(idventa) FROM ventas);";
        try {
        Statement st = con.createStatement();
        ResultSet res = st.executeQuery(sql2);
            while (res.next()) {
                if (res.getString(1).equals("Promo")) {
                    ca = Double.parseDouble(cantidad.getText())*2;
                } if (res.getString(1).equals("Six")) {
                    ca = Double.parseDouble(cantidad.getText())*6;
                } else {
                    ca = Double.parseDouble(cantidad.getText());
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de obtener categoria en ventas"+e.getMessage());
        }
           String caa = Double.toString(ca);
           double tot = Double.parseDouble(pre.getText())*Double.parseDouble(cantidad.getText());
           String sql = "insert into ventas (producto, categoria, cantidad, total) VALUES ('"+np+"', '"+cp+"', "+caa+", "+tot+");";
           try {
               Statement st = con.createStatement();
               st.executeUpdate(sql);
               barras.setText(null);
               pre.setText(null);
               cantidad.setText(null);
               prod.setSelectedIndex(0);
               precio.setText(null);
               cambio.setText(null);
               efectivo.setText(null);
               JOptionPane.showMessageDialog(null, "Venta agregada Correctamente");
           } catch (SQLException e) {
               JOptionPane.showMessageDialog(null, "Error agregando venta "+e.getMessage());
           }
           
        }
    }
    
    public void Calcular() {
        double c,p=0,e,t;
        
        c = Double.parseDouble(cantidad.getText());
    
        p = Double.parseDouble(pre.getText());
        e = Double.parseDouble(efectivo.getText());
        
        t = e-(c*p);
        String tot = Double.toString(t);
        cambio.setText("$ "+tot);
    }
    
    public void MostrarTablaVentas2() {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Venta No");
        modelo.addColumn("Producto");
        modelo.addColumn("Categoria");
        modelo.addColumn("Cantidad");
        modelo.addColumn("Total");
        
        
        tabla_caja.setModel(modelo);
        
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
            tabla_caja.setModel(modelo);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de Muestra "+e.getMessage());
        }
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
                JOptionPane.showMessageDialog(null, "Error de impresion: "+printerException.getMessage());
            }
        }
    }
    
    public void Imprimir2(String name) {
        Document documento = new Document();
        
        try {
            String ruta = System.getProperty("user.home"); //para la ruta principal
            PdfWriter.getInstance(documento, new FileOutputStream(ruta + "/Desktop/"+name)); //Cambiar nombre del reporte
            Image header = Image.getInstance(getClass().getResource("/img/md_fa.png"));
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
            tabla.setWidthPercentage(100);
            Font f = FontFactory.getFont(FontFactory.COURIER);
            f.setColor(BaseColor.WHITE);
            Font f2 = FontFactory.getFont(FontFactory.COURIER);
            f2.setColor(BaseColor.WHITE);
            
            //Colores
            BaseColor head = new BaseColor(33,47,61);
            BaseColor tab1 = new BaseColor(97,106,107);
            BaseColor tab2 = new BaseColor(66,73,73);
            
            PdfPCell venta = new PdfPCell(new Phrase("Venta no",f));
            venta.setBackgroundColor(head);
            venta.setHorizontalAlignment(Element.ALIGN_CENTER);
            venta.setVerticalAlignment(Element.ALIGN_CENTER);
            venta.setFixedHeight(50);
            venta.setPaddingTop(15);
            venta.setBorderWidth(0);
            
            PdfPCell producto = new PdfPCell(new Phrase("Producto",f));
            producto.setBackgroundColor(head);
            producto.setHorizontalAlignment(Element.ALIGN_LEFT);
            producto.setVerticalAlignment(Element.ALIGN_CENTER);
            producto.setFixedHeight(50);
            producto.setPaddingTop(15);
            producto.setBorderWidth(0);
            
            PdfPCell categoriaa = new PdfPCell(new Phrase("Categoria",f));
            categoriaa.setBackgroundColor(head);
            categoriaa.setHorizontalAlignment(Element.ALIGN_LEFT);
            categoriaa.setVerticalAlignment(Element.ALIGN_CENTER);
            categoriaa.setFixedHeight(50);
            categoriaa.setPaddingTop(15);
            categoriaa.setBorderWidth(0);
            
            PdfPCell cantidadd = new PdfPCell(new Phrase("Cantidad",f));
            cantidadd.setBackgroundColor(head);
            cantidadd.setHorizontalAlignment(Element.ALIGN_LEFT);
            cantidadd.setVerticalAlignment(Element.ALIGN_CENTER);
            cantidadd.setFixedHeight(50);
            cantidadd.setPaddingTop(15);
            cantidadd.setBorderWidth(0);
            
            PdfPCell total = new PdfPCell(new Phrase("Total",f));
            total.setBackgroundColor(head);
            total.setHorizontalAlignment(Element.ALIGN_LEFT);
            total.setVerticalAlignment(Element.ALIGN_CENTER);
            total.setFixedHeight(50);
            total.setPaddingTop(15);
            total.setBorderWidth(0);
            
            tabla.addCell(venta);
            tabla.addCell(producto);
            tabla.addCell(categoriaa);
            tabla.addCell(cantidadd);
            tabla.addCell(total);
            
            String sql="SELECT idventa, producto, categoria, cantidad, total FROM ventas Order by idventa;";
            try {
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(sql);
                
                if (res.next()) {
                    do {
                        PdfPCell idd = new PdfPCell(new Phrase(res.getString(1),f2)); //IDVENTA
                        if (Integer.parseInt(res.getString(1))%2==0) {
                            idd.setBackgroundColor(tab1);
                        } else {
                            idd.setBackgroundColor(tab2);
                        }
                        idd.setHorizontalAlignment(Element.ALIGN_CENTER);
                        idd.setBorderWidth(0);
                        tabla.addCell(idd);
                        
                        PdfPCell proo = new PdfPCell(new Phrase(res.getString(2),f2)); //NOMBRE PRODUCTO
                        if (Integer.parseInt(res.getString(1))%2==0) {
                            proo.setBackgroundColor(tab1);
                        } else {
                            proo.setBackgroundColor(tab2);
                        }
                        proo.setHorizontalAlignment(Element.ALIGN_LEFT);
                        proo.setBorderWidth(0);
                        tabla.addCell(proo);
                        
                        PdfPCell cat = new PdfPCell(new Phrase(res.getString(3),f2)); //CATEGORIA
                        if (Integer.parseInt(res.getString(1))%2==0) {
                            cat.setBackgroundColor(tab1);
                        } else {
                            cat.setBackgroundColor(tab2);
                        }
                        cat.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cat.setBorderWidth(0);
                        tabla.addCell(cat);
                        
                        PdfPCell cant = new PdfPCell(new Phrase(res.getString(4),f2)); //CANTIDAD
                        if (Integer.parseInt(res.getString(1))%2==0) {
                            cant.setBackgroundColor(tab1);
                        } else {
                            cant.setBackgroundColor(tab2);
                        }
                        cant.setHorizontalAlignment(Element.ALIGN_LEFT);
                        cant.setBorderWidth(0);
                        tabla.addCell(cant);
                        
                        PdfPCell prr = new PdfPCell(new Phrase("$ "+res.getString(5),f2)); //PRECIO
                        if (Integer.parseInt(res.getString(1))%2==0) {
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
                JOptionPane.showMessageDialog(null, "Error Generando PDF"+e.getMessage());
            }
            
            
            String sql2 = "Select sum(total) from ventas;";
            String ven ="";
            try {
                Statement st = con.createStatement();
                ResultSet res = st.executeQuery(sql2);
                while(res.next()) {
                    ven = res.getString(1);
                }          
            } catch (NumberFormatException | SQLException e) {
                JOptionPane.showMessageDialog(null, "Error obtener suma total ventas en la impresion"+e.getMessage());
            }
            
            Paragraph parrafo2 = new Paragraph();
            parrafo2.setAlignment(Paragraph.ALIGN_LEFT);
            parrafo2.setFont(FontFactory.getFont(FontFactory.COURIER_BOLD, 14, Font.NORMAL));
            parrafo2.add("Total en caja: $ "+ven+"\n\n");
            documento.add(parrafo2);
            Paragraph parrafo3 = new Paragraph();
            parrafo3.setAlignment(Paragraph.ALIGN_LEFT);
            parrafo3.setFont(FontFactory.getFont(FontFactory.COURIER_BOLD, 14, Font.NORMAL));
            parrafo3.add("\n\nNota: \n");
            documento.add(parrafo3);
            Paragraph parrafo4 = new Paragraph();
            parrafo4.setAlignment(Paragraph.ALIGN_LEFT);
            parrafo4.setFont(FontFactory.getFont(FontFactory.COURIER));
            parrafo4.add(texto_nota.getText());
            documento.add(parrafo4);
            documento.close();
            JOptionPane.showMessageDialog(null, "Reporte generado Exitosamente");
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
               JOptionPane.showMessageDialog(null, "Las ventas se han limpiado");
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
           JOptionPane.showMessageDialog(null, "Error, el producto no existe en JCombox, no se puede agregar productos");
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
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de UPDATE en actualizar Precio "+e.getMessage());
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
        cambio = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        jSeparator8 = new javax.swing.JSeparator();
        caja = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        efectivo = new javax.swing.JTextField();
        jSeparator9 = new javax.swing.JSeparator();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        calcular = new javax.swing.JButton();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        caja2 = new javax.swing.JLabel();
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
        cerrar_caja = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tabla_caja = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        fecha = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel24 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        texto_nota = new javax.swing.JTextArea();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(10, 34, 64));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(10, 34, 64));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        b4.setBackground(new java.awt.Color(248, 184, 48));
        b4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        b4_inv.setBackground(new java.awt.Color(10, 34, 64));

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

        b1_vent.setBackground(new java.awt.Color(10, 34, 64));

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

        b2_agprod.setBackground(new java.awt.Color(10, 34, 64));

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

        b3_agprom.setBackground(new java.awt.Color(10, 34, 64));

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

        b5.setBackground(new java.awt.Color(248, 184, 48));
        b5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        b5_cerrarc1.setBackground(new java.awt.Color(10, 34, 64));
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

        jPanel3.add(b5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 380, 150, 50));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 150, 560));

        jPanel4.setBackground(new java.awt.Color(10, 34, 64));
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

        jPanel2.setBackground(new java.awt.Color(10, 34, 64));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 110, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 560, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(860, 0, 110, 560));

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
        tabla_productos.setFocusable(false);
        tabla_productos.setRowHeight(19);
        tabla_productos.setShowHorizontalLines(false);
        tabla_productos.getTableHeader().setResizingAllowed(false);
        tabla_productos.getTableHeader().setReorderingAllowed(false);
        datos_inventario.setViewportView(tabla_productos);

        jPanel1.add(datos_inventario, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 140, 710, 420));

        Reg_venta.setBackground(new java.awt.Color(35, 76, 119));
        Reg_venta.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        barras.setBackground(new java.awt.Color(35, 76, 119));
        barras.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        barras.setForeground(new java.awt.Color(255, 255, 255));
        barras.setBorder(null);
        barras.setOpaque(false);
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
        Reg_venta.add(btn_venta, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 300, 120, 30));

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

        cantidad.setBackground(new java.awt.Color(35, 76, 119));
        cantidad.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        cantidad.setForeground(new java.awt.Color(255, 255, 255));
        cantidad.setBorder(null);
        cantidad.setOpaque(false);
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
        tabla_ventas.getTableHeader().setResizingAllowed(false);
        tabla_ventas.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(tabla_ventas);

        Reg_venta.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 80, 370, 340));

        jPanel5.setBackground(new java.awt.Color(10, 34, 64));
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
        Reg_venta.add(pre, new org.netbeans.lib.awtextra.AbsoluteConstraints(152, 170, 60, -1));

        cambio.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        cambio.setForeground(new java.awt.Color(255, 255, 255));
        Reg_venta.add(cambio, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 250, 70, -1));
        Reg_venta.add(jSeparator7, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 60, 130, 10));
        Reg_venta.add(jSeparator8, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 230, 50, 10));

        caja.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        caja.setForeground(new java.awt.Color(255, 255, 255));
        caja.setText("0");
        Reg_venta.add(caja, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 350, 100, 30));

        jLabel18.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 255, 255));
        jLabel18.setText("Caja Total:");
        Reg_venta.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 380, 90, 30));

        jLabel20.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 255, 255));
        jLabel20.setText("Precio:");
        Reg_venta.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 170, 60, -1));

        efectivo.setBackground(new java.awt.Color(35, 76, 119));
        efectivo.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        efectivo.setForeground(new java.awt.Color(255, 255, 255));
        efectivo.setBorder(null);
        efectivo.setOpaque(false);
        Reg_venta.add(efectivo, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 210, 50, 20));
        Reg_venta.add(jSeparator9, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 150, 50, 10));

        jLabel21.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 255, 255));
        jLabel21.setText("Efectivo:");
        Reg_venta.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 210, 70, -1));

        jLabel22.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 255, 255));
        jLabel22.setText("Cambio:");
        Reg_venta.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 250, 70, -1));

        calcular.setText("Calcular");
        calcular.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                calcularActionPerformed(evt);
            }
        });
        Reg_venta.add(calcular, new org.netbeans.lib.awtextra.AbsoluteConstraints(73, 300, 110, 30));

        jLabel35.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel35.setForeground(new java.awt.Color(255, 255, 255));
        jLabel35.setText("Caja:");
        Reg_venta.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 350, 50, 30));

        jLabel36.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(255, 255, 255));
        jLabel36.setText("Caja:");
        Reg_venta.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 350, 50, 30));

        caja2.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        caja2.setForeground(new java.awt.Color(255, 255, 255));
        caja2.setText("0");
        Reg_venta.add(caja2, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 381, -1, 30));

        jPanel1.add(Reg_venta, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 140, 710, 420));

        Agregar_prod.setBackground(new java.awt.Color(68, 132, 206));
        Agregar_prod.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel9.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Categoría");
        Agregar_prod.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 270, 90, -1));

        nombre.setBackground(new java.awt.Color(68, 132, 206));
        nombre.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        nombre.setForeground(new java.awt.Color(255, 255, 255));
        nombre.setBorder(null);
        nombre.setOpaque(false);
        Agregar_prod.add(nombre, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 110, 160, 20));
        Agregar_prod.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 130, 160, 20));

        jLabel10.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("Nombre del producto ");
        Agregar_prod.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 110, 170, -1));
        Agregar_prod.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 170, 110, 10));

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
        Agregar_prod.add(contenido, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 190, 60, 20));
        Agregar_prod.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 250, 110, 20));

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
        Agregar_prod.add(precio, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 230, 100, 20));

        jLabel11.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Codigo de barras");
        Agregar_prod.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 150, 130, -1));
        Agregar_prod.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 250, 110, 20));
        Agregar_prod.add(jSeparator5, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 290, 110, 20));

        categoria.setBackground(new java.awt.Color(68, 132, 206));
        categoria.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        categoria.setForeground(new java.awt.Color(255, 255, 255));
        categoria.setBorder(null);
        categoria.setOpaque(false);
        Agregar_prod.add(categoria, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 270, 110, 20));

        if (contenido.getText().equals("1.02") || contenido.getText().equals("2.5")) {
            mlolts.setText("lts");
        } else {
            mlolts.setText("ml");
        }
        mlolts.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        mlolts.setForeground(new java.awt.Color(255, 255, 255));
        mlolts.setText("ml/lts");
        Agregar_prod.add(mlolts, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 190, 60, 20));

        stock.setBackground(new java.awt.Color(68, 132, 206));
        stock.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        stock.setForeground(new java.awt.Color(255, 255, 255));
        stock.setBorder(null);
        stock.setOpaque(false);
        Agregar_prod.add(stock, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 310, 110, 20));
        Agregar_prod.add(jSeparator6, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 330, 110, 20));

        jLabel13.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Stock");
        Agregar_prod.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 310, 60, -1));

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
        Agregar_prod.add(AgregarProducto, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 350, 200, 40));

        jLabel14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/beer.png"))); // NOI18N
        Agregar_prod.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 330, 60, 70));

        jLabel15.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 255, 255));
        jLabel15.setText("Precio");
        Agregar_prod.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 230, 60, -1));

        jLabel16.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 255, 255));
        jLabel16.setText("$");
        Agregar_prod.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 230, 30, -1));

        jSeparator10.setOrientation(javax.swing.SwingConstants.VERTICAL);
        jSeparator10.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jSeparator10.setPreferredSize(new java.awt.Dimension(50, 20));
        Agregar_prod.add(jSeparator10, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 0, 30, 420));

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Berlin Sans FB", 0, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Existente");
        Agregar_prod.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 350, 90));

        jLabel23.setBackground(new java.awt.Color(255, 255, 255));
        jLabel23.setFont(new java.awt.Font("Berlin Sans FB", 0, 36)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 255, 255));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("Nuevo");
        Agregar_prod.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 1, 360, 90));

        combx_prod.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                combx_prodActionPerformed(evt);
            }
        });
        Agregar_prod.add(combx_prod, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 110, 160, -1));

        jLabel25.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 255, 255));
        jLabel25.setText("Nombre del producto ");
        Agregar_prod.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 110, 170, -1));

        jLabel26.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 255, 255));
        jLabel26.setText("Contenido");
        Agregar_prod.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 190, 90, -1));
        Agregar_prod.add(jSeparator11, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 210, 110, 10));

        jLabel27.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 255, 255));
        jLabel27.setText("Precio");
        Agregar_prod.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 230, 60, -1));

        jLabel28.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 255, 255));
        jLabel28.setText("Categoría");
        Agregar_prod.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 270, 90, -1));

        jLabel29.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 255, 255));
        jLabel29.setText("Stock");
        Agregar_prod.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 310, 60, -1));

        jLabel30.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 255, 255));
        jLabel30.setText("Precio");
        Agregar_prod.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 190, 50, -1));

        jLabel32.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(255, 255, 255));
        jLabel32.setText("Stock");
        Agregar_prod.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 280, 50, -1));

        precio_existente.setBackground(new java.awt.Color(68, 132, 206));
        precio_existente.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        precio_existente.setForeground(new java.awt.Color(255, 255, 255));
        precio_existente.setBorder(null);
        precio_existente.setOpaque(false);
        Agregar_prod.add(precio_existente, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 190, 110, 20));
        Agregar_prod.add(jSeparator12, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 300, 110, 20));
        Agregar_prod.add(jSeparator13, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 210, 110, 20));

        stock_existente.setBackground(new java.awt.Color(68, 132, 206));
        stock_existente.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        stock_existente.setForeground(new java.awt.Color(255, 255, 255));
        stock_existente.setBorder(null);
        stock_existente.setOpaque(false);
        Agregar_prod.add(stock_existente, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 280, 110, 20));

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
        Agregar_prod.add(ActualizarPrecio_existente, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 230, 170, -1));

        barras_existente.setBackground(new java.awt.Color(68, 132, 206));
        barras_existente.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        barras_existente.setForeground(new java.awt.Color(255, 255, 255));
        barras_existente.setBorder(null);
        Agregar_prod.add(barras_existente, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 150, 110, 20));

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
        Agregar_prod.add(AgregarProducto_existente, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 330, 170, -1));

        jLabel34.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/money.png"))); // NOI18N
        Agregar_prod.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 220, 70, 50));

        jLabel31.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/stock.png"))); // NOI18N
        Agregar_prod.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 310, 60, -1));

        jLabel33.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 255, 255));
        jLabel33.setText("Codigo de barras");
        Agregar_prod.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 150, 130, -1));
        Agregar_prod.add(jSeparator14, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 170, 110, 10));

        barras_nuevo.setBackground(new java.awt.Color(68, 132, 206));
        barras_nuevo.setFont(new java.awt.Font("Berlin Sans FB", 0, 18)); // NOI18N
        barras_nuevo.setForeground(new java.awt.Color(255, 255, 255));
        barras_nuevo.setBorder(null);
        Agregar_prod.add(barras_nuevo, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 150, 110, -1));

        jPanel1.add(Agregar_prod, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 140, 710, 420));

        cerrar_caja.setBackground(new java.awt.Color(10, 34, 64));
        cerrar_caja.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jScrollPane3.setBorder(null);

        tabla_caja = new javax.swing.JTable() {
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        tabla_caja.getTableHeader().setReorderingAllowed(false);
        tabla_caja.setFont(new java.awt.Font("Berlin Sans FB", 0, 21)); // NOI18N
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

        cerrar_caja.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 90, 710, 240));

        jPanel9.setBackground(new java.awt.Color(10, 34, 64));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        fecha.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        fecha.setForeground(new java.awt.Color(255, 255, 255));
        jPanel9.add(fecha, new org.netbeans.lib.awtextra.AbsoluteConstraints(355, 11, 193, 78));

        jLabel19.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 255, 255));
        jLabel19.setText("Ventas del dia");
        jPanel9.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(205, 11, 160, 78));

        cerrar_caja.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 710, -1));

        jButton3.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/img/print.png"))); // NOI18N
        jButton3.setContentAreaFilled(false);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        cerrar_caja.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 350, 70, 70));

        jLabel24.setFont(new java.awt.Font("Berlin Sans FB", 0, 24)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 255, 255));
        jLabel24.setText("Nota:");
        cerrar_caja.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 330, -1, -1));

        jScrollPane2.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N

        texto_nota.setColumns(20);
        texto_nota.setFont(new java.awt.Font("Berlin Sans FB", 0, 14)); // NOI18N
        texto_nota.setRows(5);
        jScrollPane2.setViewportView(texto_nota);

        cerrar_caja.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 330, 370, 80));

        jPanel1.add(cerrar_caja, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 140, 710, 420));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 560));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel4MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MousePressed
        b2_agprod.setVisible(true); //AGREGAR PRODUCTOOOOOOOO
        b1_vent.setVisible(false);
        b3_agprom.setVisible(false);
        b4_inv.setVisible(false);
        b5_cerrarc1.setVisible(false);
        
        
        datos_inventario.setVisible(false);
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
        
        
        
        datos_inventario.setVisible(false);
        cerrar_caja.setVisible(false);
        Agregar_prod.setVisible(false);
        Reg_venta.setVisible(true);
        prod.setModel(llenar());
        MostrarTablaVentas();
        labelfecha.setText(getFecha());
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
        datos_inventario.setVisible(true);
        
        MostrarTabla();
    }//GEN-LAST:event_jLabel1MousePressed

    private void jLabel3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MousePressed
        b5_cerrarc1.setVisible(true); //CERRAR CAJAAAAAAA
        b2_agprod.setVisible(false);
        b3_agprom.setVisible(false);
        b1_vent.setVisible(false);
        b4_inv.setVisible(false);
        
        Reg_venta.setVisible(false);
        Agregar_prod.setVisible(false);
        datos_inventario.setVisible(false);
        cerrar_caja.setVisible(true);
        MostrarTablaVentas2();
        fecha.setText(getFecha());
    }//GEN-LAST:event_jLabel3MousePressed

    private void btn_ventaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_ventaActionPerformed
        try {
            if (cantidad.getText().isEmpty() || prod.getSelectedItem().equals("Seleccionar")) {
                JOptionPane.showMessageDialog(null, "Campos vacios, por favor llena todos los campos");
            } else {
                vender();
                ActualizarVentas();
                MostrarTablaVentas();
                CajaTot();
                JOptionPane.showMessageDialog(null, "Venta Exitosa");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error de UPDATE en funcion Vender del BTN "+e.getMessage());
        }
    }//GEN-LAST:event_btn_ventaActionPerformed

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

    private void calcularActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_calcularActionPerformed
        Calcular();
    }//GEN-LAST:event_calcularActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        //ImprimirCaja(cerrar_caja);
        String namee = "Reporte_"+getFecha2()+".pdf";
        if (JOptionPane.showConfirmDialog(null, "Está usted seguro de que quiere cerrar caja?")==0 ) {
            Imprimir2(namee);
        }
        
        //Reset();
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
    private javax.swing.JButton btn_venta;
    private javax.swing.JLabel caja;
    private javax.swing.JLabel caja2;
    private javax.swing.JButton calcular;
    private javax.swing.JLabel cambio;
    private javax.swing.JTextField cantidad;
    private javax.swing.JTextField categoria;
    private javax.swing.JPanel cerrar_caja;
    private javax.swing.JComboBox<String> combx_prod;
    private javax.swing.JTextField contenido;
    private javax.swing.JScrollPane datos_inventario;
    private javax.swing.JTextField efectivo;
    private javax.swing.JLabel fecha;
    private rojerusan.RSFotoCircle foto;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
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
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JLabel labelfecha;
    private javax.swing.JLabel mlolts;
    private javax.swing.JLabel name;
    private javax.swing.JTextField nombre;
    private javax.swing.JLabel pre;
    private javax.swing.JTextField precio;
    private javax.swing.JTextField precio_existente;
    private javax.swing.JComboBox<String> prod;
    private javax.swing.JTextField stock;
    private javax.swing.JTextField stock_existente;
    private javax.swing.JTable tabla_caja;
    private javax.swing.JTable tabla_productos;
    private javax.swing.JTable tabla_ventas;
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
