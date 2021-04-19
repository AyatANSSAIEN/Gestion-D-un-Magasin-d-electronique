package com.mycompany.mavenproject1;

import java.awt.*;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.DefaultPieDataset;

public class NewJFrame extends javax.swing.JFrame {

    connection con = new connection();
    static NewJFrame fr1;
    static int row;
    static int obcRow;
    static int oiRow;

    DefaultTableModel cTable = new DefaultTableModel();
    DefaultTableModel pTable = new DefaultTableModel();
    DefaultTableModel obcTable = new DefaultTableModel();
    DefaultTableModel histTable = new DefaultTableModel();
    DefaultTableModel orders = new DefaultTableModel();
    DefaultTableModel orderItems = new DefaultTableModel();

    public NewJFrame(){
        initComponents();
        bestlast.setVisible(false);
           bestlastPro.setVisible(true);
           famOrCat.setVisible(false);
           CallableStatement pMax;
        try {
            pMax = con.maxProduct();
            productwording.setText(pMax.getString(2));
             
  
        } catch (Exception ex) {
            productwording.setText("there is no product ");
        }

           try {
           
            CallableStatement cstMax = con.maxProduct();
            CallableStatement cstMin = con.minProduct();
            bestP.setText(cstMax.getString(2));
            bpCA.setText((cstMax.getString(3)));
            lastP.setText(cstMin.getString(2));
            lpCA.setText(cstMin.getString(3));
            
            
            //Graph
            float pr = 100;
            DefaultPieDataset ds = new DefaultPieDataset();
            ResultSet rs = con.ShowTop5Products();
            do {
                ds.setValue(rs.getString(2)+ " ("+rs.getString(1)+")", rs.getFloat(3));
                pr -=  rs.getFloat(3);

            } while (rs.next());
            ds.setValue("others", pr);
            
            JFreeChart chart = ChartFactory.createPieChart3D("TOP 5 Products", ds, true, true, false);
            final PiePlot3D plot = (PiePlot3D) chart.getPlot();
            plot.setStartAngle(270);
            plot.setForegroundAlpha(0.60f);
            plot.setInteriorGap(0.02);
            plot.setBackgroundPaint(Color.WHITE);
            plot.setOutlineVisible(false);
            
            ChartPanel cp = new ChartPanel(chart);
            cp.setBackground(new Color(54, 63, 73));
            
            chartPanel.removeAll();
            chartPanel.add(cp, BorderLayout.CENTER);
            chartPanel.validate();
        } catch (Exception ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
           
        try{
            turnoverText.setText(con.turnover());
        }catch(Exception e){}
        CallableStatement cstMax;
        try {
            cstMax = con.maxClient();
            clientName.setText(cstMax.getString(2));
             
  
        } catch (Exception ex) {
            clientName.setText("there is no client");
        } 
        CallableStatement proMax;
        try {
            proMax = con.maxProduct();
            productwording.setText(proMax.getString(2));
             
  
        } catch (Exception ex) {
            productwording.setText("there is no product ");
        }
            
        
        cTable = (DefaultTableModel) ClientTable.getModel();
        pTable = (DefaultTableModel) productsTable.getModel();
        obcTable = (DefaultTableModel) OrdersByClient.getModel();
        histTable = (DefaultTableModel) historyTable.getModel();
        orders = (DefaultTableModel) ordersTable.getModel();
        orderItems = (DefaultTableModel) orderItemsTable.getModel();

    }

    //Clients
    public void refreshClient() {
        while (cTable.getRowCount() > 0) {
            for (int i = 0; i < cTable.getRowCount(); i++) {
                cTable.removeRow(0);
            }
        }
        try {
            ResultSet rs = con.ShowClients();
            do {

                String[] tab = {rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7)};
                cTable.addRow(tab);

            } while (rs.next());
        } catch (Exception e) {
        }
    }

    public void refreshOrders() {
        while (orders.getRowCount() > 0) {
            for (int i = 0; i < orders.getRowCount(); i++) {
                orders.removeRow(0);
            }
        }
        try {
            ResultSet rs = con.ShowOrders();
            do {

                Object[] tab = {rs.getInt(1), rs.getInt(2), rs.getDate(3), rs.getFloat(4), rs.getFloat(6), rs.getString(5)};
                orders.addRow(tab);

            } while (rs.next());
        } catch (Exception e) {
        }
    }
    //OrderItem

    public void refreshOrderItems(int id) {
        while (orderItems.getRowCount() > 0) {
            for (int i = 0; i < orderItems.getRowCount(); i++) {
                orderItems.removeRow(0);
            }
        }
        try {
            ResultSet rs = con.ShowOrderItems(id);
            do {

                Object[] tab = {rs.getString(2), rs.getInt(6), rs.getFloat(3), rs.getFloat(4), rs.getFloat(5)};
                orderItems.addRow(tab);

            } while (rs.next());
        } catch (Exception e) {
        }
    }

    //Products
    public void refreshProduct() {
        while (pTable.getRowCount() > 0) {
            for (int i = 0; i < pTable.getRowCount(); i++) {
                pTable.removeRow(0);
            }
        }
        try {
            ResultSet rs = con.ShowProducts();
            do {
                Object[] tab = {rs.getString(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getFloat(5), rs.getFloat(6), rs.getInt(7)};
                pTable.addRow(tab);
                System.out.print(rs.getString(7));
            } while (rs.next());
        } catch (Exception e) {
        }
        this.pPU.setText("");
        this.pTVA.setText("");
        this.pSTOCK.setText("");
        this.pTurnover.setText("");
        this.pSales.setText("");

    }

    //Orders by clients
    public void RefreshOrdersByClient(int cid) {
        while (obcTable.getRowCount() > 0) {
            for (int i = 0; i < obcTable.getRowCount(); i++) {
                obcTable.removeRow(0);
            }
        }
        try {
            ResultSet rs = con.ShowOrdersByClient(cid);
            do {
                Object[] tab = {rs.getString(1), rs.getDate(2), rs.getFloat(3)};
                obcTable.addRow(tab);
            } while (rs.next());
        } catch (Exception e) {
        }

    }

    //History
    public void refreshHistory() {
        while (histTable.getRowCount() > 0) {
            for (int i = 0; i < histTable.getRowCount(); i++) {
                histTable.removeRow(0);
            }
        }
        try {
            ResultSet rs = con.ShowHistory();
            do {
                Object[] tab = {rs.getDate(1), rs.getInt(2), rs.getInt(3), rs.getFloat(4), rs.getString(5)};
                histTable.addRow(tab);
            } while (rs.next());
        } catch (Exception e) {
        }

    }

    public void refreshProductADDORDER() {
        while (AddOrder.itmsTb.getRowCount() > 0) {
            for (int i = 0; i < AddOrder.itmsTb.getRowCount(); i++) {
                AddOrder.itmsTb.removeRow(0);
            }
        }
        try {
            ResultSet rs = con.ShowProducts();
            do {
                Object[] tab = {rs.getString(1), rs.getString(2), rs.getFloat(5)};
                AddOrder.itmsTb.addRow(tab);
            } while (rs.next());
        } catch (Exception e) {
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sideMenu = new javax.swing.JPanel();
        menuClients = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        menuOrders = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        menuProducts = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        menuHome = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        logout = new javax.swing.JLabel();
        menuHistory = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        sideClose = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jLabel27 = new javax.swing.JLabel();
        Card = new javax.swing.JPanel();
        Home = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        jPanel9_2 = new javax.swing.JPanel();
        jPanel12 = new javax.swing.JPanel();
        jLabel71 = new javax.swing.JLabel();
        jLabel72 = new javax.swing.JLabel();
        jLabel64 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel60 = new javax.swing.JLabel();
        jLabel57 = new javax.swing.JLabel();
        jLabel67 = new javax.swing.JLabel();
        jLabel62 = new javax.swing.JLabel();
        turnoverText = new javax.swing.JTextField();
        jPanel29 = new javax.swing.JPanel();
        jLabel65 = new javax.swing.JLabel();
        jLabel66 = new javax.swing.JLabel();
        clientName = new javax.swing.JLabel();
        jLabel61 = new javax.swing.JLabel();
        jPanel30 = new javax.swing.JPanel();
        jLabel63 = new javax.swing.JLabel();
        jLabel68 = new javax.swing.JLabel();
        jLabel69 = new javax.swing.JLabel();
        productwording = new javax.swing.JLabel();
        chartPanel = new javax.swing.JPanel();
        famOrCat = new javax.swing.JPanel();
        btnCat = new javax.swing.JButton();
        btnFam = new javax.swing.JButton();
        bestlastPro = new javax.swing.JPanel();
        bestP = new javax.swing.JTextField();
        jLabel80 = new javax.swing.JLabel();
        jLabel76 = new javax.swing.JLabel();
        bpCA = new javax.swing.JTextField();
        lpCA = new javax.swing.JTextField();
        jLabel81 = new javax.swing.JLabel();
        lastP = new javax.swing.JTextField();
        jLabel82 = new javax.swing.JLabel();
        bestlast = new javax.swing.JPanel();
        bestB = new javax.swing.JTextField();
        jLabel77 = new javax.swing.JLabel();
        jLabel75 = new javax.swing.JLabel();
        bbTotal = new javax.swing.JTextField();
        lbTotal = new javax.swing.JTextField();
        jLabel78 = new javax.swing.JLabel();
        lastB = new javax.swing.JTextField();
        jLabel79 = new javax.swing.JLabel();
        Orders = new javax.swing.JPanel();
        jPanel18 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jButton3 = new javax.swing.JButton();
        searchOrder = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        conversion = new javax.swing.JPanel();
        Euro = new javax.swing.JTextField();
        jLabel58 = new javax.swing.JLabel();
        jLabel59 = new javax.swing.JLabel();
        Dollar = new javax.swing.JTextField();
        jPanel23 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jLabel35 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        orderItemsTable = new javax.swing.JTable();
        discountItem = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        editItemDiscount = new javax.swing.JButton();
        jLabel56 = new javax.swing.JLabel();
        qte = new javax.swing.JTextField();
        jPanel24 = new javax.swing.JPanel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        discountOrder = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        editOrdr = new javax.swing.JButton();
        jLabel41 = new javax.swing.JLabel();
        isDelivered = new javax.swing.JCheckBox();
        clientBox = new javax.swing.JComboBox<>();
        jScrollPane5 = new javax.swing.JScrollPane();
        ordersTable = new javax.swing.JTable();
        jPanel26 = new javax.swing.JPanel();
        History = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        historyTable = new javax.swing.JTable();
        jPanel28 = new javax.swing.JPanel();
        searchHist = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        Clients = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        ClientTable = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cAdress = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        cEmail = new javax.swing.JTextField();
        jLabel20 = new javax.swing.JLabel();
        EditClient = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        cCmt = new javax.swing.JTextField();
        jLabel50 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        searchClient = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        OrdersByClient = new javax.swing.JTable();
        jLabel21 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        nombreOrders = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        jPanel17 = new javax.swing.JPanel();
        Products = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jPanelProducts = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        productsTable = new javax.swing.JTable();
        jPanel15 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        pTVA = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        pSTOCK = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        pEdit = new javax.swing.JButton();
        jLabel42 = new javax.swing.JLabel();
        pPU = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();
        jLabel55 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        jButton6 = new javax.swing.JButton();
        searchProduct = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable7 = new javax.swing.JTable();
        jPanel27 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        jLabel46 = new javax.swing.JLabel();
        pTurnover = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        jLabel53 = new javax.swing.JLabel();
        pSales = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 255, 255));
        setUndecorated(true);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        sideMenu.setBackground(new java.awt.Color(1, 31, 75));
        sideMenu.setPreferredSize(new java.awt.Dimension(314, 530));
        sideMenu.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        menuClients.setBackground(new java.awt.Color(3, 57, 108));
        menuClients.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 5, 0, 0, new java.awt.Color(255, 255, 255)));
        menuClients.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        menuClients.setPreferredSize(new java.awt.Dimension(270, 70));
        menuClients.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuClientsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuClientsMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                menuClientsMousePressed(evt);
            }
        });
        menuClients.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                menuClientsComponentHidden(evt);
            }
        });

        jLabel13.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(179, 205, 224));
        jLabel13.setText("Clients");

        jLabel4.setIcon(new javax.swing.ImageIcon("C:\\Users\\Ayat\\Desktop\\mavenproject1 (2)\\mavenproject1\\images\\icons8_gender_neutral_user_30px.png")); // NOI18N

        javax.swing.GroupLayout menuClientsLayout = new javax.swing.GroupLayout(menuClients);
        menuClients.setLayout(menuClientsLayout);
        menuClientsLayout.setHorizontalGroup(
            menuClientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuClientsLayout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        menuClientsLayout.setVerticalGroup(
            menuClientsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuClientsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(menuClientsLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        sideMenu.add(menuClients, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 260, 270, 70));

        menuOrders.setBackground(new java.awt.Color(3, 57, 108));
        menuOrders.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 5, 0, 0, new java.awt.Color(255, 255, 255)));
        menuOrders.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        menuOrders.setPreferredSize(new java.awt.Dimension(270, 70));
        menuOrders.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuOrdersMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuOrdersMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                menuOrdersMousePressed(evt);
            }
        });

        jLabel14.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(179, 205, 224));
        jLabel14.setText("Orders");

        jLabel6.setIcon(new javax.swing.ImageIcon("C:\\Users\\Ayat\\Desktop\\mavenproject1 (2)\\mavenproject1\\images\\icons8_insert_table_30px.png")); // NOI18N

        javax.swing.GroupLayout menuOrdersLayout = new javax.swing.GroupLayout(menuOrders);
        menuOrders.setLayout(menuOrdersLayout);
        menuOrdersLayout.setHorizontalGroup(
            menuOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuOrdersLayout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        menuOrdersLayout.setVerticalGroup(
            menuOrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuOrdersLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuOrdersLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        sideMenu.add(menuOrders, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 340, 270, 70));

        menuProducts.setBackground(new java.awt.Color(3, 57, 108));
        menuProducts.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 5, 0, 0, new java.awt.Color(255, 255, 255)));
        menuProducts.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        menuProducts.setPreferredSize(new java.awt.Dimension(270, 70));
        menuProducts.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuProductsMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuProductsMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                menuProductsMousePressed(evt);
            }
        });

        jLabel15.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(179, 205, 224));
        jLabel15.setText("Products");

        jLabel7.setIcon(new javax.swing.ImageIcon("C:\\Users\\Ayat\\Desktop\\mavenproject1 (2)\\mavenproject1\\images\\icons8_computer_support_30px_1.png")); // NOI18N

        javax.swing.GroupLayout menuProductsLayout = new javax.swing.GroupLayout(menuProducts);
        menuProducts.setLayout(menuProductsLayout);
        menuProductsLayout.setHorizontalGroup(
            menuProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuProductsLayout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        menuProductsLayout.setVerticalGroup(
            menuProductsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuProductsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15, javax.swing.GroupLayout.DEFAULT_SIZE, 58, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(menuProductsLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel7)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        sideMenu.add(menuProducts, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 420, 270, 70));

        jLabel5.setIcon(new javax.swing.ImageIcon("C:\\Users\\Ayat\\Desktop\\LOGO1 (4).png")); // NOI18N
        sideMenu.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(-60, 0, 280, 180));

        menuHome.setBackground(new java.awt.Color(3, 57, 108));
        menuHome.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 5, 0, 0, new java.awt.Color(255, 255, 255)));
        menuHome.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        menuHome.setPreferredSize(new java.awt.Dimension(270, 70));
        menuHome.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuHomeMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuHomeMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                menuHomeMousePressed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(179, 205, 224));
        jLabel12.setText("Home");

        jLabel1.setIcon(new javax.swing.ImageIcon("C:\\Users\\Ayat\\Desktop\\mavenproject1 (2)\\mavenproject1\\images\\icons8_home_filled_30px.png")); // NOI18N

        javax.swing.GroupLayout menuHomeLayout = new javax.swing.GroupLayout(menuHome);
        menuHome.setLayout(menuHomeLayout);
        menuHomeLayout.setHorizontalGroup(
            menuHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuHomeLayout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        menuHomeLayout.setVerticalGroup(
            menuHomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuHomeLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuHomeLayout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(19, 19, 19))
        );

        sideMenu.add(menuHome, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 270, 70));

        logout.setFont(jLabel12.getFont());
        logout.setForeground(jLabel12.getForeground());
        logout.setIcon(new javax.swing.ImageIcon("C:\\Users\\Ayat\\Desktop\\mavenproject1 (2)\\mavenproject1\\images\\icons8_shutdown_35px.png")); // NOI18N
        logout.setText("Log Out");
        logout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                logoutMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                logoutMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                logoutMousePressed(evt);
            }
        });
        sideMenu.add(logout, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 660, 130, 30));

        menuHistory.setBackground(new java.awt.Color(3, 57, 108));
        menuHistory.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 5, 0, 0, new java.awt.Color(255, 255, 255)));
        menuHistory.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        menuHistory.setPreferredSize(new java.awt.Dimension(270, 70));
        menuHistory.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                menuHistoryMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                menuHistoryMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                menuHistoryMousePressed(evt);
            }
        });

        jLabel22.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(179, 205, 224));
        jLabel22.setText("History");

        jLabel23.setIcon(new javax.swing.ImageIcon("C:\\Users\\Ayat\\Desktop\\mavenproject1 (2)\\mavenproject1\\images\\icons8_past_filled_30px.png")); // NOI18N

        javax.swing.GroupLayout menuHistoryLayout = new javax.swing.GroupLayout(menuHistory);
        menuHistory.setLayout(menuHistoryLayout);
        menuHistoryLayout.setHorizontalGroup(
            menuHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, menuHistoryLayout.createSequentialGroup()
                .addContainerGap(10, Short.MAX_VALUE)
                .addComponent(jLabel23)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        menuHistoryLayout.setVerticalGroup(
            menuHistoryLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuHistoryLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(menuHistoryLayout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel23)
                .addContainerGap(20, Short.MAX_VALUE))
        );

        sideMenu.add(menuHistory, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 500, 270, 70));
        sideMenu.add(jSeparator3, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 150, -1, -1));
        sideMenu.add(jSeparator4, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 150, -1, -1));

        getContentPane().add(sideMenu, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 270, 700));

        sideClose.setBackground(new java.awt.Color(3, 57, 108));
        sideClose.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel10.setBackground(new java.awt.Color(179, 205, 224));
        jPanel10.setForeground(new java.awt.Color(1, 31, 75));

        jLabel18.setIcon(new javax.swing.ImageIcon("C:\\Users\\Ayat\\Desktop\\mavenproject1 (2)\\mavenproject1\\images\\icons8_cancel_filled_35px.png")); // NOI18N
        jLabel18.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel18MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel10Layout.createSequentialGroup()
                .addContainerGap(1059, Short.MAX_VALUE)
                .addComponent(jLabel18)
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel18, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
        );

        sideClose.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1100, 40));

        jLabel33.setIcon(new javax.swing.ImageIcon("C:\\Users\\Ayat\\Desktop\\mavenproject1 (2)\\mavenproject1\\images\\admin (2).png")); // NOI18N
        sideClose.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 60, 60, 50));

        jLabel26.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(133, 162, 177));
        jLabel26.setText("Administrator");
        sideClose.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 94, 120, 20));
        sideClose.add(jSeparator1, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 90, -1, -1));

        jSeparator2.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator2.setForeground(new java.awt.Color(100, 151, 177));
        sideClose.add(jSeparator2, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 90, 120, -1));

        jLabel27.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(100, 151, 177));
        jLabel27.setText("OussAya");
        sideClose.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(890, 60, -1, 30));

        getContentPane().add(sideClose, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 0, 1110, 130));

        Card.setLayout(new java.awt.CardLayout());

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel9_2.setBackground(new java.awt.Color(3, 57, 108));
        jPanel9_2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel9_2.setPreferredSize(new java.awt.Dimension(270, 70));

        javax.swing.GroupLayout jPanel9_2Layout = new javax.swing.GroupLayout(jPanel9_2);
        jPanel9_2.setLayout(jPanel9_2Layout);
        jPanel9_2Layout.setHorizontalGroup(
            jPanel9_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel9_2Layout.setVerticalGroup(
            jPanel9_2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
        );

        jPanel14.add(jPanel9_2, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 50, 30, 70));

        jPanel12.setBackground(new java.awt.Color(3, 57, 108));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel71.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        jLabel71.setForeground(new java.awt.Color(179, 205, 224));
        jLabel71.setText("Articles By Category/Familly");
        jPanel12.add(jLabel71, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 40, 280, 30));

        jLabel72.setFont(new java.awt.Font("Dosis SemiBold", 1, 24)); // NOI18N
        jLabel72.setForeground(new java.awt.Color(0, 153, 51));
        jLabel72.setIcon(new javax.swing.ImageIcon("C:\\Users\\Ayat\\Desktop\\mavenproject1 (2)\\mavenproject1\\images\\icons8_bar_chart_30px.png")); // NOI18N
        jPanel12.add(jLabel72, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 30, 40));

        jLabel64.setForeground(new java.awt.Color(179, 205, 224));
        jLabel64.setIcon(new javax.swing.ImageIcon("C:\\Users\\Ayat\\Desktop\\mavenproject1 (2)\\mavenproject1\\images\\icons8_info_15px.png")); // NOI18N
        jLabel64.setText("see graph");
        jLabel64.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel64.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel64MousePressed(evt);
            }
        });
        jPanel12.add(jLabel64, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, -1, -1));

        jPanel14.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 420, 300, 110));

        jPanel4.setBackground(new java.awt.Color(3, 57, 108));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel60.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        jLabel60.setForeground(new java.awt.Color(179, 205, 224));
        jLabel60.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel60.setText("MAD");
        jPanel4.add(jLabel60, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 50, 60, 30));
        jPanel4.add(jLabel57, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));

        jLabel67.setFont(new java.awt.Font("Dosis SemiBold", 1, 24)); // NOI18N
        jLabel67.setForeground(new java.awt.Color(0, 153, 51));
        jLabel67.setIcon(new javax.swing.ImageIcon("C:\\Users\\Ayat\\Desktop\\mavenproject1 (2)\\mavenproject1\\images\\icons8_economic_improvement_30px.png")); // NOI18N
        jPanel4.add(jLabel67, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 30, 40));

        jLabel62.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        jLabel62.setForeground(new java.awt.Color(179, 205, 224));
        jLabel62.setText("TurnOver");
        jPanel4.add(jLabel62, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 10, 110, 30));

        turnoverText.setEditable(false);
        turnoverText.setBackground(new Color(0,0,0,0));
        turnoverText.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        turnoverText.setForeground(new java.awt.Color(0, 204, 102));
        turnoverText.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        turnoverText.setActionCommand("<Not Set>");
        turnoverText.setAlignmentX(2.0F);
        turnoverText.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        turnoverText.setMargin(new java.awt.Insets(0, 10, 0, 0));
        turnoverText.setOpaque(false);
        jPanel4.add(turnoverText, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 50, 190, 30));

        jPanel14.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 30, 300, 110));

        jPanel29.setBackground(new java.awt.Color(3, 57, 108));
        jPanel29.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel65.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        jLabel65.setForeground(new java.awt.Color(179, 205, 224));
        jLabel65.setText("Best Client");
        jPanel29.add(jLabel65, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, 120, 30));

        jLabel66.setFont(new java.awt.Font("Dosis SemiBold", 1, 24)); // NOI18N
        jLabel66.setForeground(new java.awt.Color(0, 153, 51));
        jLabel66.setIcon(new javax.swing.ImageIcon("C:\\Users\\Ayat\\Desktop\\mavenproject1 (2)\\mavenproject1\\images\\icons8_best_seller_30px.png")); // NOI18N
        jPanel29.add(jLabel66, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 30, 40));

        clientName.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        clientName.setForeground(new java.awt.Color(0, 204, 102));
        clientName.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        clientName.setText("Client'sName");
        clientName.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        jPanel29.add(clientName, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 260, 30));

        jLabel61.setForeground(new java.awt.Color(179, 205, 224));
        jLabel61.setIcon(new javax.swing.ImageIcon("C:\\Users\\Ayat\\Desktop\\mavenproject1 (2)\\mavenproject1\\images\\icons8_info_15px.png")); // NOI18N
        jLabel61.setText("see graph");
        jLabel61.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel61.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel61MousePressed(evt);
            }
        });
        jPanel29.add(jLabel61, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, -1, -1));

        jPanel14.add(jPanel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 160, 300, 110));

        jPanel30.setBackground(new java.awt.Color(3, 57, 108));
        jPanel30.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel63.setForeground(new java.awt.Color(179, 205, 224));
        jLabel63.setIcon(new javax.swing.ImageIcon("C:\\Users\\Ayat\\Desktop\\mavenproject1 (2)\\mavenproject1\\images\\icons8_info_15px.png")); // NOI18N
        jLabel63.setText("see graph");
        jLabel63.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jLabel63.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel63MousePressed(evt);
            }
        });
        jPanel30.add(jLabel63, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, -1, -1));

        jLabel68.setFont(new java.awt.Font("Dosis SemiBold", 1, 24)); // NOI18N
        jLabel68.setForeground(new java.awt.Color(0, 153, 51));
        jLabel68.setIcon(new javax.swing.ImageIcon("C:\\Users\\Ayat\\Desktop\\mavenproject1 (2)\\mavenproject1\\images\\icons8_trophy_30px.png")); // NOI18N
        jPanel30.add(jLabel68, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 30, 40));

        jLabel69.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        jLabel69.setForeground(new java.awt.Color(179, 205, 224));
        jLabel69.setText("Best Product");
        jPanel30.add(jLabel69, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 10, 130, 30));

        productwording.setFont(new java.awt.Font("Trebuchet MS", 1, 14)); // NOI18N
        productwording.setForeground(new java.awt.Color(0, 204, 102));
        productwording.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        productwording.setText("Prodect'sWording");
        productwording.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        jPanel30.add(productwording, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 40, 260, 30));

        jPanel14.add(jPanel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 290, 300, 110));

        chartPanel.setBackground(new java.awt.Color(255, 255, 255));
        chartPanel.setLayout(new java.awt.BorderLayout());
        jPanel14.add(chartPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 30, 660, 370));

        famOrCat.setVisible(false);
        famOrCat.setBackground(new java.awt.Color(255, 255, 255));
        famOrCat.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnCat.setBackground(new java.awt.Color(0, 153, 255));
        btnCat.setText("By Category");
        btnCat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnCatMousePressed(evt);
            }
        });
        famOrCat.add(btnCat, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 10, 160, 40));

        btnFam.setBackground(new java.awt.Color(0, 153, 255));
        btnFam.setText("By Familly");
        btnFam.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                btnFamMousePressed(evt);
            }
        });
        btnFam.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFamActionPerformed(evt);
            }
        });
        famOrCat.add(btnFam, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 10, 160, 40));

        jPanel14.add(famOrCat, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 410, 590, 60));

        bestlastPro.setVisible(false);
        bestlastPro.setBackground(new java.awt.Color(179, 205, 224));
        bestlastPro.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        bestP.setEditable(false);
        bestP.setBackground(new Color(0,0,0,0));
        bestP.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        bestP.setActionCommand("<Not Set>");
        bestP.setAlignmentX(2.0F);
        bestP.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        bestP.setMargin(new java.awt.Insets(0, 10, 0, 0));
        bestP.setOpaque(false);
        bestlastPro.add(bestP, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 220, 30));

        jLabel80.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        jLabel80.setForeground(new java.awt.Color(3, 71, 95));
        jLabel80.setText(" Best Product");
        bestlastPro.add(jLabel80, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, -10, 170, 50));

        jLabel76.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        jLabel76.setForeground(new java.awt.Color(3, 71, 95));
        jLabel76.setText("CA");
        bestlastPro.add(jLabel76, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 120, 50));

        bpCA.setEditable(false);
        bpCA.setBackground(new Color(0,0,0,0));
        bpCA.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        bpCA.setActionCommand("<Not Set>");
        bpCA.setAlignmentX(2.0F);
        bpCA.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        bpCA.setMargin(new java.awt.Insets(0, 10, 0, 0));
        bpCA.setOpaque(false);
        bestlastPro.add(bpCA, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, 220, 30));

        lpCA.setEditable(false);
        lpCA.setBackground(new Color(0,0,0,0));
        lpCA.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lpCA.setActionCommand("<Not Set>");
        lpCA.setAlignmentX(2.0F);
        lpCA.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        lpCA.setMargin(new java.awt.Insets(0, 10, 0, 0));
        lpCA.setOpaque(false);
        bestlastPro.add(lpCA, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 90, 220, 30));

        jLabel81.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        jLabel81.setForeground(new java.awt.Color(3, 71, 95));
        jLabel81.setText("CA");
        bestlastPro.add(jLabel81, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 60, 120, 30));

        lastP.setEditable(false);
        lastP.setBackground(new Color(0,0,0,0));
        lastP.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lastP.setActionCommand("<Not Set>");
        lastP.setAlignmentX(2.0F);
        lastP.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        lastP.setMargin(new java.awt.Insets(0, 10, 0, 0));
        lastP.setOpaque(false);
        bestlastPro.add(lastP, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 30, 220, 30));

        jLabel82.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        jLabel82.setForeground(new java.awt.Color(3, 71, 95));
        jLabel82.setText(" Last Product");
        bestlastPro.add(jLabel82, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, -10, 240, 50));

        jPanel14.add(bestlastPro, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 410, 590, 130));

        bestlast.setBackground(new java.awt.Color(179, 205, 224));
        bestlast.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        bestB.setEditable(false);
        bestB.setBackground(new Color(0,0,0,0));
        bestB.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        bestB.setActionCommand("<Not Set>");
        bestB.setAlignmentX(2.0F);
        bestB.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        bestB.setMargin(new java.awt.Insets(0, 10, 0, 0));
        bestB.setOpaque(false);
        bestlast.add(bestB, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 220, 30));

        jLabel77.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        jLabel77.setForeground(new java.awt.Color(3, 71, 95));
        jLabel77.setText(" Best buyer");
        bestlast.add(jLabel77, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, -10, 120, 50));

        jLabel75.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        jLabel75.setForeground(new java.awt.Color(3, 71, 95));
        jLabel75.setText(" Total");
        bestlast.add(jLabel75, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 120, 50));

        bbTotal.setEditable(false);
        bbTotal.setBackground(new Color(0,0,0,0));
        bbTotal.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        bbTotal.setActionCommand("<Not Set>");
        bbTotal.setAlignmentX(2.0F);
        bbTotal.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        bbTotal.setMargin(new java.awt.Insets(0, 10, 0, 0));
        bbTotal.setOpaque(false);
        bestlast.add(bbTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 90, 220, 30));

        lbTotal.setEditable(false);
        lbTotal.setBackground(new Color(0,0,0,0));
        lbTotal.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lbTotal.setActionCommand("<Not Set>");
        lbTotal.setAlignmentX(2.0F);
        lbTotal.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        lbTotal.setMargin(new java.awt.Insets(0, 10, 0, 0));
        lbTotal.setOpaque(false);
        bestlast.add(lbTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 90, 220, 30));

        jLabel78.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        jLabel78.setForeground(new java.awt.Color(3, 71, 95));
        jLabel78.setText(" Total");
        bestlast.add(jLabel78, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 60, 120, 30));

        lastB.setEditable(false);
        lastB.setBackground(new Color(0,0,0,0));
        lastB.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lastB.setActionCommand("<Not Set>");
        lastB.setAlignmentX(2.0F);
        lastB.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        lastB.setMargin(new java.awt.Insets(0, 10, 0, 0));
        lastB.setOpaque(false);
        bestlast.add(lastB, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 30, 220, 30));

        jLabel79.setFont(new java.awt.Font("Trebuchet MS", 1, 20)); // NOI18N
        jLabel79.setForeground(new java.awt.Color(3, 71, 95));
        jLabel79.setText(" Last buyer");
        bestlast.add(jLabel79, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, -10, 120, 50));

        jPanel14.add(bestlast, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 410, 590, 130));

        javax.swing.GroupLayout HomeLayout = new javax.swing.GroupLayout(Home);
        Home.setLayout(HomeLayout);
        HomeLayout.setHorizontalGroup(
            HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1100, Short.MAX_VALUE)
        );
        HomeLayout.setVerticalGroup(
            HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel14, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE)
        );

        Card.add(Home, "card3");

        jPanel18.setBackground(new java.awt.Color(255, 255, 255));
        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel22.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton3.setBackground(new java.awt.Color(1, 31, 75));
        jButton3.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton3.setForeground(jLabel12.getForeground());
        jButton3.setText("New Order");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton3MousePressed(evt);
            }
        });
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel22.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(898, 5, 126, -1));

        searchOrder.setBackground(new Color(0,0,0,0));
        searchOrder.setActionCommand("<Not Set>");
        searchOrder.setAlignmentX(2.0F);
        searchOrder.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        searchOrder.setMargin(new java.awt.Insets(0, 10, 0, 0));
        searchOrder.setOpaque(false);
        searchOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchOrderActionPerformed(evt);
            }
        });
        searchOrder.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchOrderKeyReleased(evt);
            }
        });
        jPanel22.add(searchOrder, new org.netbeans.lib.awtextra.AbsoluteConstraints(103, 12, 238, 30));

        jLabel29.setIcon(new javax.swing.ImageIcon("C:\\Users\\Ayat\\Desktop\\mavenproject1 (2)\\mavenproject1\\images\\icons8_search_property_filled_25px.png")); // NOI18N
        jPanel22.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 30, 30));

        jLabel8.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(1, 31, 75));
        jLabel8.setText("Search");
        jPanel22.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 10, -1, 30));

        conversion.setVisible(false);
        conversion.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        Euro.setEditable(false);
        Euro.setBackground(new Color(0,0,0,0));
        Euro.setActionCommand("<Not Set>");
        Euro.setAlignmentX(2.0F);
        Euro.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        Euro.setMargin(new java.awt.Insets(0, 10, 0, 0));
        Euro.setOpaque(false);
        Euro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EuroActionPerformed(evt);
            }
        });
        conversion.add(Euro, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 10, 140, 30));

        jLabel58.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel58.setForeground(new java.awt.Color(1, 31, 75));
        jLabel58.setText("Euro");
        conversion.add(jLabel58, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 50, 30));

        jLabel59.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel59.setForeground(new java.awt.Color(1, 31, 75));
        jLabel59.setText("Dollar");
        conversion.add(jLabel59, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 10, 50, 30));

        Dollar.setEditable(false);
        Dollar.setBackground(new Color(0,0,0,0));
        Dollar.setActionCommand("<Not Set>");
        Dollar.setAlignmentX(2.0F);
        Dollar.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        Dollar.setMargin(new java.awt.Insets(0, 10, 0, 0));
        Dollar.setOpaque(false);
        Dollar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DollarActionPerformed(evt);
            }
        });
        conversion.add(Dollar, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 10, 140, 30));

        jPanel22.add(conversion, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 0, 480, 50));

        jPanel18.add(jPanel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 1040, 50));

        jPanel23.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel30.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(1, 31, 75));
        jPanel23.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(113, 42, -1, 30));

        jLabel34.setFont(new java.awt.Font("Dosis Medium", 1, 36)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(1, 31, 75));
        jLabel34.setText("Order's Items");
        jPanel23.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 10, 210, 30));

        jLabel35.setIcon(new javax.swing.ImageIcon("C:\\Users\\Ayat\\Desktop\\mavenproject1 (2)\\mavenproject1\\images\\icons8_shopping_cart_loaded_filled_30px.png")); // NOI18N
        jLabel35.setToolTipText("");
        jPanel23.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 30, 40));

        orderItemsTable.setAutoCreateRowSorter(true);
        orderItemsTable.setForeground(new java.awt.Color(1, 31, 75));
        orderItemsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Ref", "Qte", "Total HT", "Total TTC", "Discount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Float.class, java.lang.Float.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        orderItemsTable.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        orderItemsTable.getTableHeader().setReorderingAllowed(false);
        orderItemsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                orderItemsTableMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                orderItemsTableMousePressed(evt);
            }
        });
        jScrollPane4.setViewportView(orderItemsTable);
        if (orderItemsTable.getColumnModel().getColumnCount() > 0) {
            orderItemsTable.getColumnModel().getColumn(0).setPreferredWidth(50);
            orderItemsTable.getColumnModel().getColumn(1).setPreferredWidth(50);
            orderItemsTable.getColumnModel().getColumn(2).setPreferredWidth(70);
            orderItemsTable.getColumnModel().getColumn(3).setPreferredWidth(70);
            orderItemsTable.getColumnModel().getColumn(4).setPreferredWidth(60);
        }

        jPanel23.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 430, 140));

        discountItem.setBackground(new Color(0,0,0,0));
        discountItem.setText("0");
        discountItem.setActionCommand("<Not Set>");
        discountItem.setAlignmentX(2.0F);
        discountItem.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        discountItem.setMargin(new java.awt.Insets(0, 10, 0, 0));
        discountItem.setOpaque(false);
        discountItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                discountItemActionPerformed(evt);
            }
        });
        jPanel23.add(discountItem, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 210, 60, 30));

        jLabel49.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(1, 31, 75));
        jLabel49.setText("Discount");
        jPanel23.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 210, -1, 30));

        editItemDiscount.setBackground(new java.awt.Color(1, 31, 75));
        editItemDiscount.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        editItemDiscount.setForeground(jLabel12.getForeground());
        editItemDiscount.setText("Edit");
        editItemDiscount.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        editItemDiscount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editItemDiscountActionPerformed(evt);
            }
        });
        jPanel23.add(editItemDiscount, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 210, 90, 30));

        jLabel56.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel56.setForeground(new java.awt.Color(1, 31, 75));
        jLabel56.setText("Quantity");
        jPanel23.add(jLabel56, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 210, -1, 30));

        qte.setBackground(new Color(0,0,0,0));
        qte.setText("0");
        qte.setActionCommand("<Not Set>");
        qte.setAlignmentX(2.0F);
        qte.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        qte.setMargin(new java.awt.Insets(0, 10, 0, 0));
        qte.setOpaque(false);
        qte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                qteActionPerformed(evt);
            }
        });
        jPanel23.add(qte, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 210, 60, 30));

        jPanel18.add(jPanel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 290, 470, 260));

        jPanel24.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel36.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel36.setForeground(new java.awt.Color(1, 31, 75));
        jPanel24.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(113, 42, -1, 30));

        jLabel37.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel37.setForeground(new java.awt.Color(1, 31, 75));
        jLabel37.setText("Discount");
        jPanel24.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 110, -1, 30));

        discountOrder.setBackground(new Color(0,0,0,0));
        discountOrder.setText("0");
        discountOrder.setActionCommand("<Not Set>");
        discountOrder.setAlignmentX(2.0F);
        discountOrder.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        discountOrder.setMargin(new java.awt.Insets(0, 10, 0, 0));
        discountOrder.setOpaque(false);
        discountOrder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                discountOrderActionPerformed(evt);
            }
        });
        jPanel24.add(discountOrder, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 110, 220, 30));

        jLabel38.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel38.setForeground(new java.awt.Color(1, 31, 75));
        jLabel38.setText("Delevred");
        jPanel24.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 150, -1, 50));

        jLabel39.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(1, 31, 75));
        jLabel39.setText("Client ID");
        jPanel24.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 60, -1, 30));

        jLabel40.setFont(new java.awt.Font("Dosis Medium", 1, 36)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(1, 31, 75));
        jLabel40.setText("Edit Order");
        jPanel24.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 10, -1, 30));

        editOrdr.setBackground(new java.awt.Color(1, 31, 75));
        editOrdr.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        editOrdr.setForeground(jLabel12.getForeground());
        editOrdr.setText("Edit");
        editOrdr.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        editOrdr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editOrdrActionPerformed(evt);
            }
        });
        jPanel24.add(editOrdr, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 210, 90, 30));

        jLabel41.setIcon(new javax.swing.ImageIcon("C:\\Users\\Ayat\\Desktop\\mavenproject1 (2)\\mavenproject1\\images\\icons8_edit_property_filled_30px.png")); // NOI18N
        jLabel41.setToolTipText("");
        jPanel24.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 30, 40));

        isDelivered.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        isDelivered.setForeground(new java.awt.Color(1, 31, 75));
        isDelivered.setText("Yes");
        isDelivered.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        isDelivered.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                isDeliveredActionPerformed(evt);
            }
        });
        jPanel24.add(isDelivered, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 160, -1, 30));

        clientBox.setBackground(new Color(0,0,0,0));
        clientBox.setForeground(new java.awt.Color(3, 57, 108));
        try{
            clientBox.setModel(new DefaultComboBoxModel(new connection().ClientTable() ));
        }
        catch(Exception ex){}
        clientBox.setSelectedItem("");
        clientBox.setBorder(discountOrder.getBorder());
        clientBox.setPreferredSize(new java.awt.Dimension(78, 22));
        clientBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clientBoxActionPerformed(evt);
            }
        });
        jPanel24.add(clientBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 60, 220, 30));

        jPanel18.add(jPanel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 290, 470, 260));

        ordersTable.setAutoCreateRowSorter(true);
        ordersTable.setForeground(new java.awt.Color(1, 31, 75));
        ordersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Order ID", "Client ID", "Order Date", "Total", "Discount", "Delivered"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.Object.class, java.lang.Float.class, java.lang.Float.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ordersTable.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ordersTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ordersTableMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                ordersTableMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ordersTableMousePressed(evt);
            }
        });
        jScrollPane5.setViewportView(ordersTable);

        jPanel18.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 1040, 180));

        jPanel26.setBackground(new java.awt.Color(3, 57, 108));
        jPanel26.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel26.setPreferredSize(new java.awt.Dimension(270, 70));

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
        );

        jPanel18.add(jPanel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 210, 30, 70));

        javax.swing.GroupLayout OrdersLayout = new javax.swing.GroupLayout(Orders);
        Orders.setLayout(OrdersLayout);
        OrdersLayout.setHorizontalGroup(
            OrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, 1100, Short.MAX_VALUE)
        );
        OrdersLayout.setVerticalGroup(
            OrdersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        Card.add(Orders, "card4");

        History.setBackground(new java.awt.Color(255, 255, 255));
        History.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel25.setBackground(new java.awt.Color(3, 57, 108));
        jPanel25.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel25.setPreferredSize(new java.awt.Dimension(270, 70));

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
        );

        History.add(jPanel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 370, 30, 70));

        historyTable.setAutoCreateRowSorter(true);
        historyTable.setForeground(new java.awt.Color(1, 31, 75));
        historyTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Order Date", "Order ID", "Client ID", "Total", "Statut"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Float.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        historyTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                historyTableMouseClicked(evt);
            }
        });
        jScrollPane8.setViewportView(historyTable);

        History.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 1040, 440));

        searchHist.setBackground(new Color(0,0,0,0));
        searchHist.setActionCommand("<Not Set>");
        searchHist.setAlignmentX(2.0F);
        searchHist.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        searchHist.setMargin(new java.awt.Insets(0, 10, 0, 0));
        searchHist.setOpaque(false);
        searchHist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchHistActionPerformed(evt);
            }
        });
        searchHist.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchHistKeyReleased(evt);
            }
        });

        jLabel47.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(1, 31, 75));
        jLabel47.setText("Search");

        jLabel48.setIcon(new javax.swing.ImageIcon("C:\\Users\\Ayat\\Desktop\\mavenproject1 (2)\\mavenproject1\\images\\icons8_view_file_filled_25px.png")); // NOI18N

        javax.swing.GroupLayout jPanel28Layout = new javax.swing.GroupLayout(jPanel28);
        jPanel28.setLayout(jPanel28Layout);
        jPanel28Layout.setHorizontalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel48)
                .addGap(5, 5, 5)
                .addComponent(jLabel47)
                .addGap(18, 18, 18)
                .addComponent(searchHist, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(697, Short.MAX_VALUE))
        );
        jPanel28Layout.setVerticalGroup(
            jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel28Layout.createSequentialGroup()
                .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel48, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel28Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(searchHist, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 6, Short.MAX_VALUE))
        );

        History.add(jPanel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, -1, -1));

        Card.add(History, "card5");

        Clients.setBackground(new java.awt.Color(255, 255, 255));
        Clients.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        ClientTable.setAutoCreateRowSorter(true);
        ClientTable.setForeground(new java.awt.Color(1, 31, 75));
        ClientTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Client ID", "Name", "Adress", "Country", "Region", "Email", "Comment"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ClientTable.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        ClientTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ClientTableMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                ClientTableMousePressed(evt);
            }
        });
        jScrollPane1.setViewportView(ClientTable);
        if (ClientTable.getColumnModel().getColumnCount() > 0) {
            ClientTable.getColumnModel().getColumn(0).setPreferredWidth(10);
            ClientTable.getColumnModel().getColumn(1).setPreferredWidth(30);
            ClientTable.getColumnModel().getColumn(2).setPreferredWidth(50);
            ClientTable.getColumnModel().getColumn(3).setPreferredWidth(30);
            ClientTable.getColumnModel().getColumn(4).setPreferredWidth(10);
        }

        Clients.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 1040, 180));

        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(1, 31, 75));
        jPanel5.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(113, 42, -1, 30));

        cAdress.setBackground(new Color(0,0,0,0));
        cAdress.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        cAdress.setActionCommand("<Not Set>");
        cAdress.setAlignmentX(2.0F);
        cAdress.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        cAdress.setMargin(new java.awt.Insets(0, 10, 0, 0));
        cAdress.setOpaque(false);
        cAdress.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cAdressActionPerformed(evt);
            }
        });
        jPanel5.add(cAdress, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 60, 220, 30));

        jLabel9.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(1, 31, 75));
        jLabel9.setText("Email");
        jPanel5.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 110, -1, 30));

        cEmail.setBackground(new Color(0,0,0,0));
        cEmail.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        cEmail.setActionCommand("<Not Set>");
        cEmail.setAlignmentX(2.0F);
        cEmail.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        cEmail.setMargin(new java.awt.Insets(0, 10, 0, 0));
        cEmail.setOpaque(false);
        cEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cEmailActionPerformed(evt);
            }
        });
        jPanel5.add(cEmail, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 110, 220, 30));

        jLabel20.setFont(new java.awt.Font("Dosis Medium", 1, 36)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(1, 31, 75));
        jLabel20.setText("Edit Client");
        jPanel5.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 10, -1, 30));

        EditClient.setBackground(new java.awt.Color(1, 31, 75));
        EditClient.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        EditClient.setForeground(jLabel12.getForeground());
        EditClient.setText("Edit");
        EditClient.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        EditClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditClientActionPerformed(evt);
            }
        });
        jPanel5.add(EditClient, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 220, 90, 30));

        jLabel10.setIcon(new javax.swing.ImageIcon("C:\\Users\\Ayat\\Desktop\\mavenproject1 (2)\\mavenproject1\\images\\icons8_edit_administrator_filled_30px.png")); // NOI18N
        jPanel5.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 50, 40));

        jLabel11.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(1, 31, 75));
        jLabel11.setText("Comment");
        jPanel5.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 170, -1, 30));

        cCmt.setBackground(new Color(0,0,0,0));
        cCmt.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        cCmt.setActionCommand("<Not Set>");
        cCmt.setAlignmentX(2.0F);
        cCmt.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        cCmt.setMargin(new java.awt.Insets(0, 10, 0, 0));
        cCmt.setOpaque(false);
        jPanel5.add(cCmt, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 170, 220, 30));

        jLabel50.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel50.setForeground(new java.awt.Color(1, 31, 75));
        jLabel50.setText("Adress");
        jPanel5.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, -1, 30));

        Clients.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 290, 470, 260));

        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 682, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 330, Short.MAX_VALUE)
        );

        jPanel6.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 370, -1, -1));

        jButton1.setBackground(new java.awt.Color(1, 31, 75));
        jButton1.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton1.setForeground(jLabel12.getForeground());
        jButton1.setText("New client");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        searchClient.setBackground(new Color(0,0,0,0));
        searchClient.setActionCommand("<Not Set>");
        searchClient.setAlignmentX(2.0F);
        searchClient.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        searchClient.setMargin(new java.awt.Insets(0, 10, 0, 0));
        searchClient.setOpaque(false);
        searchClient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchClientActionPerformed(evt);
            }
        });
        searchClient.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchClientKeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(1, 31, 75));
        jLabel2.setText("Search");

        jLabel17.setIcon(new javax.swing.ImageIcon("C:\\Users\\Ayat\\Desktop\\mavenproject1 (2)\\mavenproject1\\images\\icons8_find_user_male_25px.png")); // NOI18N

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addGap(5, 5, 5)
                .addComponent(jLabel2)
                .addGap(18, 18, 18)
                .addComponent(searchClient, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 557, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(searchClient, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel13Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton1))
        );

        jPanel6.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, -1));

        Clients.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 1040, 50));

        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        OrdersByClient.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Order ID", "Order Date", "Amount"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Float.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        OrdersByClient.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jScrollPane3.setViewportView(OrdersByClient);

        jPanel11.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 68, 458, 120));

        jLabel21.setFont(new java.awt.Font("Dosis Medium", 1, 36)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(1, 31, 75));
        jLabel21.setText("Orders List");
        jPanel11.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(161, 6, -1, 30));

        jLabel19.setIcon(new javax.swing.ImageIcon("C:\\Users\\Ayat\\Desktop\\mavenproject1 (2)\\mavenproject1\\images\\icons8_todo_list_filled_30px.png")); // NOI18N
        jPanel11.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 0, 52, 41));

        nombreOrders.setEditable(false);
        nombreOrders.setBackground(new Color(0,0,0,0));
        nombreOrders.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        nombreOrders.setText("   ");
        nombreOrders.setActionCommand("<Not Set>");
        nombreOrders.setAlignmentX(2.0F);
        nombreOrders.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        nombreOrders.setMargin(new java.awt.Insets(0, 10, 0, 0));
        nombreOrders.setOpaque(false);
        jPanel11.add(nombreOrders, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 200, 60, 30));

        jLabel51.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel51.setForeground(new java.awt.Color(1, 31, 75));
        jLabel51.setText("TOTAL");
        jPanel11.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 200, -1, 30));

        Clients.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 290, 470, 260));

        jPanel17.setBackground(new java.awt.Color(3, 57, 108));
        jPanel17.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel17.setPreferredSize(new java.awt.Dimension(270, 70));

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 30, Short.MAX_VALUE)
        );
        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
        );

        Clients.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 130, 30, 70));

        Card.add(Clients, "card2");

        Products.setBackground(new java.awt.Color(255, 255, 255));
        Products.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel19.setBackground(new java.awt.Color(3, 57, 108));
        jPanel19.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel19.setPreferredSize(new java.awt.Dimension(270, 70));

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 270, Short.MAX_VALUE)
        );
        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 70, Short.MAX_VALUE)
        );

        Products.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(-10, 290, 30, 70));

        jPanelProducts.setBackground(new java.awt.Color(255, 255, 255));
        jPanelProducts.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        productsTable.setAutoCreateRowSorter(true);
        productsTable.setForeground(new java.awt.Color(1, 31, 75));
        productsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Ref", "Wording", "Category", "Family", "PU", "TVA", "Stock"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Float.class, java.lang.Float.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        productsTable.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        productsTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                productsTableMousePressed(evt);
            }
        });
        jScrollPane6.setViewportView(productsTable);

        jPanelProducts.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 1040, 180));

        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel16.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(1, 31, 75));
        jPanel15.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(113, 42, -1, 30));

        jLabel24.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(1, 31, 75));
        jLabel24.setText("TVA");
        jPanel15.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 110, -1, 30));

        pTVA.setBackground(new Color(0,0,0,0));
        pTVA.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        pTVA.setActionCommand("<Not Set>");
        pTVA.setAlignmentX(2.0F);
        pTVA.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        pTVA.setMargin(new java.awt.Insets(0, 10, 0, 0));
        pTVA.setOpaque(false);
        pTVA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pTVAActionPerformed(evt);
            }
        });
        jPanel15.add(pTVA, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 110, 170, 30));

        jLabel25.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(1, 31, 75));
        jLabel25.setText("STOCK");
        jPanel15.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 160, -1, 30));

        pSTOCK.setBackground(new Color(0,0,0,0));
        pSTOCK.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        pSTOCK.setActionCommand("<Not Set>");
        pSTOCK.setAlignmentX(2.0F);
        pSTOCK.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        pSTOCK.setMargin(new java.awt.Insets(0, 10, 0, 0));
        pSTOCK.setOpaque(false);
        pSTOCK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pSTOCKActionPerformed(evt);
            }
        });
        jPanel15.add(pSTOCK, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 160, 170, 30));

        jLabel31.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(1, 31, 75));
        jLabel31.setText("PU");
        jPanel15.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 60, -1, 30));

        jLabel32.setFont(new java.awt.Font("Dosis Medium", 1, 36)); // NOI18N
        jLabel32.setForeground(new java.awt.Color(1, 31, 75));
        jLabel32.setText("Edit Product");
        jPanel15.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 10, -1, 30));

        pEdit.setBackground(new java.awt.Color(1, 31, 75));
        pEdit.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        pEdit.setForeground(jLabel12.getForeground());
        pEdit.setText("Edit");
        pEdit.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        pEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pEditActionPerformed(evt);
            }
        });
        jPanel15.add(pEdit, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 210, 90, 30));

        jLabel42.setIcon(new javax.swing.ImageIcon("C:\\Users\\Ayat\\Desktop\\mavenproject1 (2)\\mavenproject1\\images\\icons8_pencil_tip_filled_25px.png")); // NOI18N
        jPanel15.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 0, 50, 40));

        pPU.setBackground(new Color(0,0,0,0));
        pPU.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        pPU.setText("   ");
        pPU.setActionCommand("<Not Set>");
        pPU.setAlignmentX(2.0F);
        pPU.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        pPU.setMargin(new java.awt.Insets(0, 10, 0, 0));
        pPU.setOpaque(false);
        pPU.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pPUActionPerformed(evt);
            }
        });
        jPanel15.add(pPU, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 60, 170, 30));

        jLabel54.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel54.setForeground(new java.awt.Color(1, 31, 75));
        jLabel54.setText("MAD");
        jPanel15.add(jLabel54, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 60, -1, 30));

        jLabel55.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel55.setForeground(new java.awt.Color(1, 31, 75));
        jLabel55.setText("%");
        jPanel15.add(jLabel55, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 110, -1, 30));

        jPanelProducts.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 290, 470, 260));

        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 682, Short.MAX_VALUE)
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 330, Short.MAX_VALUE)
        );

        jPanel16.add(jPanel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 370, -1, -1));

        jButton6.setBackground(new java.awt.Color(1, 31, 75));
        jButton6.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        jButton6.setForeground(jLabel12.getForeground());
        jButton6.setText("New Product");
        jButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jButton6MousePressed(evt);
            }
        });
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        searchProduct.setBackground(new Color(0,0,0,0));
        searchProduct.setActionCommand("<Not Set>");
        searchProduct.setAlignmentX(2.0F);
        searchProduct.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        searchProduct.setMargin(new java.awt.Insets(0, 10, 0, 0));
        searchProduct.setOpaque(false);
        searchProduct.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchProductActionPerformed(evt);
            }
        });
        searchProduct.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                searchProductKeyReleased(evt);
            }
        });

        jLabel43.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(1, 31, 75));
        jLabel43.setText("Search");

        jLabel44.setIcon(new javax.swing.ImageIcon("C:\\Users\\Ayat\\Desktop\\mavenproject1 (2)\\mavenproject1\\images\\icons8_view_file_filled_25px.png")); // NOI18N

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel44)
                .addGap(5, 5, 5)
                .addComponent(jLabel43)
                .addGap(18, 18, 18)
                .addComponent(searchProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 553, Short.MAX_VALUE)
                .addComponent(jButton6)
                .addGap(14, 14, 14))
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel44, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel43, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(searchProduct, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel21Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jButton6))
        );

        jPanel16.add(jPanel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 6, -1, -1));

        jPanelProducts.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 1040, 50));

        jTable7.setAutoCreateRowSorter(true);
        jTable7.setForeground(new java.awt.Color(1, 31, 75));
        jTable7.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", "oussama", "mhanech II", "tetouan", "1", "oussama@gmail.com"},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Client ID", "Name", "Adress", "Country", "Region", "Email"
            }
        ));
        jTable7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable7MouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(jTable7);

        jPanelProducts.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 1040, 180));

        jPanel27.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel45.setFont(new java.awt.Font("Dosis Medium", 1, 36)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(1, 31, 75));
        jLabel45.setText("Product Stats");
        jPanel27.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(141, 11, -1, 30));

        jLabel46.setIcon(new javax.swing.ImageIcon("C:\\Users\\Ayat\\Desktop\\mavenproject1 (2)\\mavenproject1\\images\\icons8_statistics_30px_1.png")); // NOI18N
        jPanel27.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(6, 0, 52, 41));

        pTurnover.setEditable(false);
        pTurnover.setBackground(new Color(0,0,0,0));
        pTurnover.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        pTurnover.setText("   ");
        pTurnover.setActionCommand("<Not Set>");
        pTurnover.setAlignmentX(2.0F);
        pTurnover.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        pTurnover.setMargin(new java.awt.Insets(0, 10, 0, 0));
        pTurnover.setOpaque(false);
        pTurnover.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pTurnoverActionPerformed(evt);
            }
        });
        jPanel27.add(pTurnover, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 90, 170, 30));

        jLabel52.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel52.setForeground(new java.awt.Color(1, 31, 75));
        jLabel52.setText("TurnOver");
        jPanel27.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 90, -1, 30));

        jLabel53.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jLabel53.setForeground(new java.awt.Color(1, 31, 75));
        jLabel53.setText("Nombre of sales");
        jPanel27.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 140, -1, 30));

        pSales.setEditable(false);
        pSales.setBackground(new Color(0,0,0,0));
        pSales.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        pSales.setActionCommand("<Not Set>");
        pSales.setAlignmentX(2.0F);
        pSales.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 2, 0, new java.awt.Color(255, 255, 255)));
        pSales.setMargin(new java.awt.Insets(0, 10, 0, 0));
        pSales.setOpaque(false);
        pSales.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pSalesActionPerformed(evt);
            }
        });
        jPanel27.add(pSales, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 140, 170, 30));

        jPanelProducts.add(jPanel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 290, 470, 260));

        Products.add(jPanelProducts, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, 570));

        Card.add(Products, "card6");

        getContentPane().add(Card, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 130, 1100, 570));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ClientTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ClientTableMouseClicked
        // TODO add your handling code here:


    }//GEN-LAST:event_ClientTableMouseClicked

    public void hoverON(JPanel p, JPanel p2, JLabel l) {
        p.setBackground(new Color(179, 205, 224));
        p2.setBackground(new Color(179, 205, 224));
        l.setForeground(new Color(3, 57, 108));
    }

    public void hoverOFF(JPanel p, JPanel p2, JLabel l) {
        p.setBackground(new Color(3, 57, 108));
        p2.setBackground(new Color(3, 57, 108));
        l.setForeground(new Color(179, 205, 224));
    }

    public void clickBorder(JPanel p) {
        p.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
    }

    private void menuClientsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuClientsMouseEntered
        // TODO add your handling code here:
        hoverON(menuClients, jPanel17, jLabel13);

    }//GEN-LAST:event_menuClientsMouseEntered

    private void menuClientsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuClientsMouseExited
        // TODO add your handling code here:
        hoverOFF(menuClients, jPanel17, jLabel13);
    }//GEN-LAST:event_menuClientsMouseExited

    private void menuOrdersMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuOrdersMouseEntered
        // TODO add your handling code here:
        hoverON(menuOrders, jPanel26, jLabel14);
    }//GEN-LAST:event_menuOrdersMouseEntered

    private void menuOrdersMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuOrdersMouseExited
        // TODO add your handling code here:
        hoverOFF(menuOrders, jPanel26, jLabel14);

    }//GEN-LAST:event_menuOrdersMouseExited

    private void menuProductsMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuProductsMouseEntered
        // TODO add your handling code here:
        hoverON(menuProducts, jPanel19, jLabel15);
    }//GEN-LAST:event_menuProductsMouseEntered

    private void menuProductsMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuProductsMouseExited
        // TODO add your handling code here:
        hoverOFF(menuProducts, jPanel19, jLabel15);

    }//GEN-LAST:event_menuProductsMouseExited

    private void menuHomeMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuHomeMouseEntered
        // TODO add your handling code here:
        hoverON(menuHome, jPanel9_2, jLabel12);
        // hoverON(jPanel9_2,null);
    }//GEN-LAST:event_menuHomeMouseEntered

    private void menuHomeMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuHomeMouseExited
        // TODO add your handling code here:
        hoverOFF(menuHome, jPanel9_2, jLabel12);
        //  hoverOFF(jPanel9_2,null);
    }//GEN-LAST:event_menuHomeMouseExited

    private void jLabel18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel18MouseClicked
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_jLabel18MouseClicked

    private void logoutMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMousePressed
        // TODO add your handling code here:
        this.dispose();
        new Login().setVisible(true);
    }//GEN-LAST:event_logoutMousePressed

    private void cEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cEmailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cEmailActionPerformed

    private void cAdressActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cAdressActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cAdressActionPerformed

    private void EditClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditClientActionPerformed
        try {
            //Bloc EDIT
            int id = Integer.parseInt(ClientTable.getValueAt(row, 0).toString());
            con.updateClient(id, cAdress.getText(), cEmail.getText(), cCmt.getText());
        } catch (Exception ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.refreshClient();

    }//GEN-LAST:event_EditClientActionPerformed

    private void menuOrdersMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuOrdersMousePressed
        // TODO add your handling code here:
        Card.removeAll();
        Card.add(Orders);
        Card.repaint();
        Card.revalidate();
        this.refreshOrders();
        try {
            //clientbox :
            clientBox.setModel(new DefaultComboBoxModel(new connection().ClientTable() ));
        } catch (Exception ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_menuOrdersMousePressed

    private void menuProductsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuProductsMousePressed
        // TODO add your handling code here:
        Card.removeAll();
        Card.add(Products);
        Card.repaint();
        Card.revalidate();
        this.refreshProduct();


    }//GEN-LAST:event_menuProductsMousePressed

    private void menuHistoryMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuHistoryMouseEntered
        // TODO add your handling code here
        hoverON(menuHistory, jPanel25, jLabel22);
    }//GEN-LAST:event_menuHistoryMouseEntered

    private void menuHistoryMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuHistoryMouseExited
        // TODO add your handling code here:
        hoverOFF(menuHistory, jPanel25, jLabel22);
    }//GEN-LAST:event_menuHistoryMouseExited

    private void menuHistoryMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuHistoryMousePressed
        // TODO add your handling code here:
        Card.removeAll();
        Card.add(History);
        Card.repaint();
        Card.revalidate();
        this.refreshHistory();

    }//GEN-LAST:event_menuHistoryMousePressed

    private void menuHomeMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuHomeMousePressed
        // TODO add your handling code here:
        CallableStatement cstMax;
        try {
            cstMax = con.maxClient();
            clientName.setText(cstMax.getString(2));
        } catch (Exception ex) {
            clientName.setText("there is no client");
        }
        try{
            turnoverText.setText(con.turnover());
            System.out.println(con.turnover());
        }catch(Exception e){}
        
        Card.removeAll();
        Card.add(Home);
        Card.repaint();
        Card.revalidate();
    }//GEN-LAST:event_menuHomeMousePressed

    private void menuClientsMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_menuClientsMousePressed
        // TODO add your handling code here:
        Card.removeAll();
        Card.add(Clients);
        Card.repaint();
        Card.revalidate();
        this.refreshClient();

    }//GEN-LAST:event_menuClientsMousePressed

    private void searchClientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchClientActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchClientActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        new AddClient().setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void orderItemsTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_orderItemsTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_orderItemsTableMouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void searchOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchOrderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchOrderActionPerformed

    private void discountOrderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_discountOrderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_discountOrderActionPerformed

    private void editOrdrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editOrdrActionPerformed
        // TODO add your handling code here:
        
        int id = Integer.parseInt(orders.getValueAt(ordersTable.convertRowIndexToModel(row), 0).toString());
        String str = clientBox.getSelectedItem().toString();
        int index1 = str.indexOf("(") + 1;
        int index2 = str.indexOf(")");
        int clientID = Integer.parseInt(str.substring(index1, index2));
        Float dis = Float.parseFloat(discountOrder.getText());
        String str2 = "to order";
        if (isDelivered.isSelected()) {
            str2 = "delivered";
        }

        
        try {
            con.updateOrder(id, clientID, dis, str2);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(new JFrame(), "check stock !");

        }
        
        this.discountOrder.setText("0");
        this.discountItem.setText("0");
        this.isDelivered.setSelected(false);
        this.qte.setText("0");
        this.refreshOrders();
        this.refreshOrderItems(id);

    }//GEN-LAST:event_editOrdrActionPerformed

    private void isDeliveredActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_isDeliveredActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_isDeliveredActionPerformed

    private void ordersTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ordersTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_ordersTableMouseClicked

    private void jButton3MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton3MousePressed
        // TODO add your handling code here:

        JFrame AO = new AddOrder();
        refreshProductADDORDER();
        AO.setVisible(true);
    }//GEN-LAST:event_jButton3MousePressed

    private void menuClientsComponentHidden(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_menuClientsComponentHidden
        // TODO add your handling code here:
    }//GEN-LAST:event_menuClientsComponentHidden

    private void logoutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMouseEntered
        // TODO add your handling code here:
        logout.setForeground(new Color(0, 91, 150));
    }//GEN-LAST:event_logoutMouseEntered

    private void logoutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_logoutMouseExited
        // TODO add your handling code here:
        logout.setForeground(new Color(179, 205, 224));
    }//GEN-LAST:event_logoutMouseExited

    private void pPUActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pPUActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pPUActionPerformed

    private void pTVAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pTVAActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pTVAActionPerformed

    private void pSTOCKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pSTOCKActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pSTOCKActionPerformed

    private void pEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pEditActionPerformed
        // TODO add your handling code here:

        try {
            con.UpdateProducts(productsTable.getValueAt(row, 0).toString(), pPU.getText(), pTVA.getText(), pSTOCK.getText());
        } catch (Exception ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.refreshProduct();

    }//GEN-LAST:event_pEditActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void searchProductActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchProductActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchProductActionPerformed

    private void jTable7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable7MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTable7MouseClicked

    private void jButton6MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton6MousePressed
        // TODO add your handling code here:
        JFrame jfp = new AddProduct();
        jfp.setVisible(true);
    }//GEN-LAST:event_jButton6MousePressed

    private void historyTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_historyTableMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_historyTableMouseClicked

    private void searchHistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchHistActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchHistActionPerformed

    private void discountItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_discountItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_discountItemActionPerformed

    private void editItemDiscountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editItemDiscountActionPerformed
        // TODO add your handling code here:
        int id = Integer.parseInt(orders.getValueAt(ordersTable.convertRowIndexToModel(row), 0).toString());
        String ref = orderItems.getValueAt(orderItemsTable.convertRowIndexToModel(oiRow), 0).toString();
        //UPDATEEEEE 
        int qtt = Integer.parseInt(qte.getText());
        float d = Float.parseFloat(discountItem.getText());
        try {
            con.updateOrderItem(ref, id, qtt, d);
        } catch (Exception ex) {
        }
        this.discountItem.setText("0");
        this.qte.setText("0");
        this.refreshOrders();
        this.refreshOrderItems(id);
    }//GEN-LAST:event_editItemDiscountActionPerformed

    private void pTurnoverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pTurnoverActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pTurnoverActionPerformed

    private void pSalesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pSalesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pSalesActionPerformed

    private void productsTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_productsTableMousePressed
        // TODO add your handling code here:
        row = productsTable.getSelectedRow();
        DefaultTableModel tblmodel = (DefaultTableModel) productsTable.getModel();
        pPU.setText(tblmodel.getValueAt(productsTable.convertRowIndexToModel(row), 4).toString());
        pTVA.setText(tblmodel.getValueAt(productsTable.convertRowIndexToModel(row), 5).toString());
        pSTOCK.setText(tblmodel.getValueAt(productsTable.convertRowIndexToModel(row), 6).toString());
        String pid = tblmodel.getValueAt(productsTable.convertRowIndexToModel(row), 0).toString();
        String pCA = "";
        String nS = "";
        try {
            pCA = String.valueOf(con.productTurnover(pid));
            nS = String.valueOf(con.salesNumber(pid));
        } catch (Exception ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        pTurnover.setText(pCA);
        pSales.setText(nS);
    }//GEN-LAST:event_productsTableMousePressed

    private void searchProductKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchProductKeyReleased
        // TODO add your handling code here:
        DefaultTableModel tab = (DefaultTableModel) productsTable.getModel();
        String str = searchProduct.getText();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(tab);
        productsTable.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter("(?i)" + str));
    }//GEN-LAST:event_searchProductKeyReleased

    private void ClientTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ClientTableMousePressed
        // TODO add your handling code here:
        row = ClientTable.getSelectedRow();
        DefaultTableModel tblmodel = (DefaultTableModel) ClientTable.getModel();
        cAdress.setText(tblmodel.getValueAt(ClientTable.convertRowIndexToModel(row), 2).toString());
        cEmail.setText(tblmodel.getValueAt(ClientTable.convertRowIndexToModel(row), 5).toString());

        if (tblmodel.getValueAt(ClientTable.convertRowIndexToModel(row), 6) != null) {
            cCmt.setText(tblmodel.getValueAt(ClientTable.convertRowIndexToModel(row), 6).toString());
        } else {
            cCmt.setText("");
        }
        int cid = Integer.parseInt(tblmodel.getValueAt(ClientTable.convertRowIndexToModel(row), 0).toString());
        String nbOrdrs = "";

        this.RefreshOrdersByClient(cid);

        try {
            nbOrdrs = String.valueOf(con.nomberOfOrdersByClient(cid));
        } catch (Exception ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        nombreOrders.setText(nbOrdrs);

    }//GEN-LAST:event_ClientTableMousePressed

    private void searchClientKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchClientKeyReleased
        // TODO add your handling code here:
        DefaultTableModel tab = (DefaultTableModel) ClientTable.getModel();
        String str = searchClient.getText();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(tab);
        ClientTable.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter("(?i)" + str));
    }//GEN-LAST:event_searchClientKeyReleased

    private void searchHistKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchHistKeyReleased
        // TODO add your handling code here:
        DefaultTableModel tab = (DefaultTableModel) historyTable.getModel();
        String str = searchHist.getText();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(tab);
        historyTable.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter("(?i)" + str));
    }//GEN-LAST:event_searchHistKeyReleased

    private void ordersTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ordersTableMousePressed
        // TODO add your handling code here:
        

        row = ordersTable.getSelectedRow();
        conversion.setVisible(true);
        float total = Float.parseFloat(orders.getValueAt(ordersTable.convertRowIndexToModel(row), 3).toString());
        Euro.setText(String.valueOf(total/10));        
        Dollar.setText(String.valueOf(total/9));
        
        int idc = Integer.parseInt(orders.getValueAt(ordersTable.convertRowIndexToModel(row), 1).toString());
        try{
        clientBox.setSelectedItem(con.getClientById(idc));
        }catch(Exception ex){}
        discountOrder.setText(orders.getValueAt(ordersTable.convertRowIndexToModel(row), 4).toString());

        String str = orders.getValueAt(ordersTable.convertRowIndexToModel(row), 5).toString();
        if (str.equals("delivered")) {
            isDelivered.setSelected(true);
            isDelivered.setEnabled(false);
            discountOrder.setEditable(false);
            editOrdr.setVisible(false);
            discountItem.setEditable(false);
            qte.setEditable(false);
            editItemDiscount.setVisible(false);
        } else {
            isDelivered.setSelected(false);
            discountOrder.setEditable(true);
            editOrdr.setVisible(true);
            isDelivered.setEnabled(true);
            discountItem.setEditable(true);
            qte.setEditable(true);
            editItemDiscount.setVisible(true);
        }

        int id = Integer.parseInt(orders.getValueAt(ordersTable.convertRowIndexToModel(row), 0).toString());
        refreshOrderItems(id);

    }//GEN-LAST:event_ordersTableMousePressed

    private void searchOrderKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchOrderKeyReleased
        // TODO add your handling code here:
        DefaultTableModel tab = (DefaultTableModel) ordersTable.getModel();
        String str = searchOrder.getText();
        TableRowSorter<DefaultTableModel> tr = new TableRowSorter<>(tab);
        ordersTable.setRowSorter(tr);
        tr.setRowFilter(RowFilter.regexFilter("(?i)" + str));
    }//GEN-LAST:event_searchOrderKeyReleased

    private void orderItemsTableMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_orderItemsTableMousePressed
        // TODO add your handling code here:
        oiRow = orderItemsTable.getSelectedRow();
        DefaultTableModel tblmodel = (DefaultTableModel) orderItemsTable.getModel();
        discountItem.setText(tblmodel.getValueAt(orderItemsTable.convertRowIndexToModel(oiRow), 4).toString());
        qte.setText(tblmodel.getValueAt(orderItemsTable.convertRowIndexToModel(oiRow), 1).toString());

    }//GEN-LAST:event_orderItemsTableMousePressed

    private void qteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_qteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_qteActionPerformed

    private void jLabel61MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel61MousePressed
        try {
            // TODO add your handling code here:
            //BEST AND LAST 
            bestlast.setVisible(true);
            bestlastPro.setVisible(false);
            famOrCat.setVisible(false);

            

            CallableStatement cstMax = con.maxClient();
            CallableStatement cstMin = con.minClient();
            bestB.setText(cstMax.getString(2));
            bbTotal.setText((cstMax.getString(3)));
            lastB.setText(cstMin.getString(2));
            lbTotal.setText(cstMin.getString(3));
            
            
            //Graph
            float pr = 100;
            DefaultPieDataset ds = new DefaultPieDataset();
            ResultSet rs = con.ShowTop5Client();
            do {
                ds.setValue(con.getClientById(Integer.parseInt(rs.getString(1))), rs.getFloat(2));
                pr -=  rs.getFloat(2);

            } while (rs.next());
            ds.setValue("others", pr);
            
            JFreeChart chart = ChartFactory.createPieChart3D("TOP 5 Clients", ds, true, true, false);
            final PiePlot3D plot = (PiePlot3D) chart.getPlot();
            plot.setStartAngle(270);
            plot.setForegroundAlpha(0.60f);
            plot.setInteriorGap(0.02);
            plot.setBackgroundPaint(Color.WHITE);
            plot.setOutlineVisible(false);
            
            ChartPanel cp = new ChartPanel(chart);
            cp.setBackground(new Color(54, 63, 73));
            
            chartPanel.removeAll();
            chartPanel.add(cp, BorderLayout.CENTER);
            chartPanel.validate();
        } catch (Exception ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jLabel61MousePressed

    private void jLabel63MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel63MousePressed
        // TODO add your handling code here:
           bestlast.setVisible(false);
           bestlastPro.setVisible(true);
           famOrCat.setVisible(false);
           CallableStatement pMax;
        try {
            pMax = con.maxProduct();
            productwording.setText(pMax.getString(2));
             
  
        } catch (Exception ex) {
            productwording.setText("there is no product ");
        }

           try {
           
            CallableStatement cstMax = con.maxProduct();
            CallableStatement cstMin = con.minProduct();
            bestP.setText(cstMax.getString(2));
            bpCA.setText((cstMax.getString(3)));
            lastP.setText(cstMin.getString(2));
            lpCA.setText(cstMin.getString(3));
            
            
            //Graph
            float pr = 100;
            DefaultPieDataset ds = new DefaultPieDataset();
            ResultSet rs = con.ShowTop5Products();
            do {
                ds.setValue(rs.getString(2)+ " ("+rs.getString(1)+")", rs.getFloat(3));
                pr -=  rs.getFloat(3);

            } while (rs.next());
            ds.setValue("others", pr);
            
            JFreeChart chart = ChartFactory.createPieChart3D("TOP 5 Products", ds, true, true, false);
            final PiePlot3D plot = (PiePlot3D) chart.getPlot();
            plot.setStartAngle(270);
            plot.setForegroundAlpha(0.60f);
            plot.setInteriorGap(0.02);
            plot.setBackgroundPaint(Color.WHITE);
            plot.setOutlineVisible(false);
            
            ChartPanel cp = new ChartPanel(chart);
            cp.setBackground(new Color(54, 63, 73));
            
            chartPanel.removeAll();
            chartPanel.add(cp, BorderLayout.CENTER);
            chartPanel.validate();
        } catch (Exception ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
           

    }//GEN-LAST:event_jLabel63MousePressed

    private void jLabel64MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel64MousePressed
        // TODO add your handling code here:
           bestlast.setVisible(false);
           bestlastPro.setVisible(false);
           famOrCat.setVisible(true);
           DefaultCategoryDataset ds = new DefaultCategoryDataset();
        try {
            ResultSet rs = con.ArticleByCat();
            do {
           ds.setValue(rs.getInt(3),"number of articles",rs.getString(1));
           System.out.println(rs.getString(1));
         }while(rs.next()); 

        } catch (Exception ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        JFreeChart chart = ChartFactory.createBarChart("articles by categoty","categories","number of articles", ds, PlotOrientation.VERTICAL, true, true, false);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.getRenderer().setSeriesPaint(0, Color.BLUE);
        chart.setBackgroundPaint(new Color(255, 255, 255));
        chart.setBorderVisible(false);
        chart.setBorderPaint(new Color(54, 63, 73));
        chart.getCategoryPlot().setBackgroundPaint(new Color(3, 71, 95));
        chart.getCategoryPlot().setDomainGridlinePaint(new Color(54, 63, 73));
        chart.getCategoryPlot().setDomainGridlinesVisible(false);
        chart.getCategoryPlot().setOutlinePaint(new Color(54, 63, 73));

        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.WHITE);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.WHITE);

        ChartPanel cp = new ChartPanel(chart);
        cp.setBackground(new Color(54, 63, 73));

        chartPanel.removeAll();
        chartPanel.add(cp, BorderLayout.CENTER);
        chartPanel.validate();
           


    }//GEN-LAST:event_jLabel64MousePressed

    private void clientBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clientBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clientBoxActionPerformed

    private void btnFamActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFamActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnFamActionPerformed

    private void btnFamMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFamMousePressed
        // TODO add your handling code here:  
           DefaultCategoryDataset ds = new DefaultCategoryDataset();
        try {
            ResultSet rs = con.ArticleByFam();
            do {
           ds.setValue(rs.getInt(3),"number of articles",rs.getString(1));
           System.out.println(rs.getString(1));
         }while(rs.next()); 

        } catch (Exception ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        JFreeChart chart = ChartFactory.createBarChart("articles by Familly","familly","number of articles", ds, PlotOrientation.VERTICAL, true, true, false);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.getRenderer().setSeriesPaint(0, Color.BLUE);
        chart.setBackgroundPaint(new Color(255, 255, 255));
        chart.setBorderVisible(false);
        chart.setBorderPaint(new Color(54, 63, 73));
        chart.getCategoryPlot().setBackgroundPaint(new Color(3, 71, 95));
        chart.getCategoryPlot().setDomainGridlinePaint(new Color(54, 63, 73));
        chart.getCategoryPlot().setDomainGridlinesVisible(false);
        chart.getCategoryPlot().setOutlinePaint(new Color(54, 63, 73));

        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.WHITE);

        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.WHITE);

        ChartPanel cp = new ChartPanel(chart);
        cp.setBackground(new Color(54, 63, 73));

        chartPanel.removeAll();
        chartPanel.add(cp, BorderLayout.CENTER);
        chartPanel.validate();
    }//GEN-LAST:event_btnFamMousePressed

    private void btnCatMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCatMousePressed

           DefaultCategoryDataset ds = new DefaultCategoryDataset();
        try {
            ResultSet rs = con.ArticleByCat();
            do {
           ds.setValue(rs.getInt(3),"number of articles",rs.getString(1));
           System.out.println(rs.getString(1));
         }while(rs.next()); 

        } catch (Exception ex) {
            Logger.getLogger(NewJFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        JFreeChart chart = ChartFactory.createBarChart("articles by categoty","categories","number of articles", ds, PlotOrientation.VERTICAL, true, true, false);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.getRenderer().setSeriesPaint(0, Color.BLUE);
        chart.setBackgroundPaint(new Color(255, 255, 255));
        chart.setBorderVisible(false);
        chart.setBorderPaint(new Color(54, 63, 73));
        chart.getCategoryPlot().setBackgroundPaint(new Color(3, 71, 95));
        chart.getCategoryPlot().setDomainGridlinePaint(new Color(54, 63, 73));
        chart.getCategoryPlot().setDomainGridlinesVisible(false);
        chart.getCategoryPlot().setOutlinePaint(new Color(54, 63, 73));

        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.WHITE);
       // plot.get.....setLabelFont(new Font("TimesRoman", Font.PLAIN, 8));


        plot.setDomainGridlinesVisible(true);
        plot.setDomainGridlinePaint(Color.WHITE);

        ChartPanel cp = new ChartPanel(chart);
        cp.setBackground(new Color(54, 63, 73));

        chartPanel.removeAll();
        chartPanel.add(cp, BorderLayout.CENTER);
        chartPanel.validate();        // TODO add your handling code here:
    }//GEN-LAST:event_btnCatMousePressed

    private void EuroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EuroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_EuroActionPerformed

    private void DollarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DollarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_DollarActionPerformed

    private void ordersTableMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ordersTableMouseExited
        // TODO add your handling code here:
        conversion.setVisible(false);
    }//GEN-LAST:event_ordersTableMouseExited

    public boolean isNumber(String text) {
        String regex = "[0-9]{1,8}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.matches();
    }
    
    public boolean isPrecent(String text) {
        if(isNumber(text)){
            if(Float.parseFloat(text)<100) return true;
        }
        return false;
    }
    
    public boolean isNotEmpty(String text) {
        return !text.equals("");
    }
    
    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String args[]) throws Exception {

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
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            fr1 = new NewJFrame();
            fr1.setVisible(true);
        });

        //______________________________________jTable6________________________________________________ 
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    public static javax.swing.JPanel Card;
    private javax.swing.JTable ClientTable;
    public static javax.swing.JPanel Clients;
    private javax.swing.JTextField Dollar;
    private javax.swing.JButton EditClient;
    private javax.swing.JTextField Euro;
    public static javax.swing.JPanel History;
    public static javax.swing.JPanel Home;
    public static javax.swing.JPanel Orders;
    private javax.swing.JTable OrdersByClient;
    public static javax.swing.JPanel Products;
    private javax.swing.JTextField bbTotal;
    private javax.swing.JTextField bestB;
    private javax.swing.JTextField bestP;
    private javax.swing.JPanel bestlast;
    private javax.swing.JPanel bestlastPro;
    private javax.swing.JTextField bpCA;
    private javax.swing.JButton btnCat;
    private javax.swing.JButton btnFam;
    private javax.swing.JTextField cAdress;
    private javax.swing.JTextField cCmt;
    private javax.swing.JTextField cEmail;
    private javax.swing.JPanel chartPanel;
    private javax.swing.JComboBox<String> clientBox;
    private javax.swing.JLabel clientName;
    private javax.swing.JPanel conversion;
    private javax.swing.JTextField discountItem;
    private javax.swing.JTextField discountOrder;
    private javax.swing.JButton editItemDiscount;
    private javax.swing.JButton editOrdr;
    private javax.swing.JPanel famOrCat;
    private javax.swing.JTable historyTable;
    private javax.swing.JCheckBox isDelivered;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton6;
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
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
    private javax.swing.JLabel jLabel55;
    private javax.swing.JLabel jLabel56;
    private javax.swing.JLabel jLabel57;
    private javax.swing.JLabel jLabel58;
    private javax.swing.JLabel jLabel59;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel60;
    private javax.swing.JLabel jLabel61;
    private javax.swing.JLabel jLabel62;
    private javax.swing.JLabel jLabel63;
    private javax.swing.JLabel jLabel64;
    private javax.swing.JLabel jLabel65;
    private javax.swing.JLabel jLabel66;
    private javax.swing.JLabel jLabel67;
    private javax.swing.JLabel jLabel68;
    private javax.swing.JLabel jLabel69;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel71;
    private javax.swing.JLabel jLabel72;
    private javax.swing.JLabel jLabel75;
    private javax.swing.JLabel jLabel76;
    private javax.swing.JLabel jLabel77;
    private javax.swing.JLabel jLabel78;
    private javax.swing.JLabel jLabel79;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel80;
    private javax.swing.JLabel jLabel81;
    private javax.swing.JLabel jLabel82;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel30;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel9_2;
    private javax.swing.JPanel jPanelProducts;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTable jTable7;
    private javax.swing.JTextField lastB;
    private javax.swing.JTextField lastP;
    private javax.swing.JTextField lbTotal;
    private javax.swing.JLabel logout;
    private javax.swing.JTextField lpCA;
    private javax.swing.JPanel menuClients;
    private javax.swing.JPanel menuHistory;
    private javax.swing.JPanel menuHome;
    private javax.swing.JPanel menuOrders;
    private javax.swing.JPanel menuProducts;
    private javax.swing.JTextField nombreOrders;
    private javax.swing.JTable orderItemsTable;
    private javax.swing.JTable ordersTable;
    private javax.swing.JButton pEdit;
    private javax.swing.JTextField pPU;
    private javax.swing.JTextField pSTOCK;
    private javax.swing.JTextField pSales;
    private javax.swing.JTextField pTVA;
    private javax.swing.JTextField pTurnover;
    private static javax.swing.JTable productsTable;
    private javax.swing.JLabel productwording;
    private javax.swing.JTextField qte;
    private javax.swing.JTextField searchClient;
    private javax.swing.JTextField searchHist;
    private javax.swing.JTextField searchOrder;
    private javax.swing.JTextField searchProduct;
    private javax.swing.JPanel sideClose;
    private javax.swing.JPanel sideMenu;
    private javax.swing.JTextField turnoverText;
    // End of variables declaration//GEN-END:variables
}
