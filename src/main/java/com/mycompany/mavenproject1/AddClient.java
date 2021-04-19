package com.mycompany.mavenproject1;

import java.awt.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import java.util.regex.*;

public class AddClient extends javax.swing.JFrame {
    
    connection con = new connection();
    public AddClient() {
        initComponents();
    }
    public boolean isEmail(String email) {

        String regex = "^[a-zA-Z0-9]+@[a-z]{5,8}[.][a-z]{2,3}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    
    public boolean isNotEmpty(String text) {
        return !text.equals("");
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        nameCL = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        adressCL = new javax.swing.JTextField();
        countryCL = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        emailCL = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        regionBox = new javax.swing.JComboBox<>();
        addclient = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setLocation(new java.awt.Point(0, 0));
        setUndecorated(true);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(1, 31, 75)));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setFont(new java.awt.Font("Dosis Medium", 1, 48)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(1, 31, 75));
        jLabel1.setText("Add Client");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 50, -1, 90));

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(1, 31, 75));
        jLabel2.setText("Name");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 140, 70, 20));

        nameCL.setBackground(new Color(0,0,0,0));
        nameCL.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        nameCL.setForeground(new java.awt.Color(3, 57, 108));
        nameCL.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 0, new java.awt.Color(1, 31, 75)));
        nameCL.setOpaque(false);
        nameCL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameCLActionPerformed(evt);
            }
        });
        jPanel1.add(nameCL, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 170, 300, 30));

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(1, 31, 75));
        jLabel4.setText("Adress");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 210, 70, 20));

        adressCL.setBackground(new Color(0,0,0,0));
        adressCL.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        adressCL.setForeground(new java.awt.Color(3, 57, 108));
        adressCL.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 0, new java.awt.Color(1, 31, 75)));
        adressCL.setOpaque(false);
        adressCL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adressCLActionPerformed(evt);
            }
        });
        jPanel1.add(adressCL, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 240, 300, 30));

        countryCL.setBackground(new Color(0,0,0,0));
        countryCL.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        countryCL.setForeground(new java.awt.Color(3, 57, 108));
        countryCL.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 0, new java.awt.Color(1, 31, 75)));
        countryCL.setOpaque(false);
        countryCL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                countryCLActionPerformed(evt);
            }
        });
        jPanel1.add(countryCL, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 380, 300, 30));

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(1, 31, 75));
        jLabel5.setText("Region");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 420, 70, 30));

        emailCL.setBackground(new Color(0,0,0,0));
        emailCL.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        emailCL.setForeground(new java.awt.Color(3, 57, 108));
        emailCL.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 0, new java.awt.Color(1, 31, 75)));
        emailCL.setOpaque(false);
        emailCL.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                emailCLActionPerformed(evt);
            }
        });
        jPanel1.add(emailCL, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 310, 300, 30));

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(1, 31, 75));
        jLabel6.setText("Email");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 280, 70, 20));

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(1, 31, 75));
        jLabel7.setText("Country");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 350, 70, 20));

        regionBox.setBackground(new Color(0,0,0,0));
        regionBox.setFont(nameCL.getFont());
        regionBox.setForeground(new java.awt.Color(3, 57, 108));
        try{
            regionBox.setModel(new DefaultComboBoxModel(new connection().regionsTable() ));
        }
        catch(Exception ex){}
        regionBox.setSelectedItem("");
        regionBox.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 0, new java.awt.Color(1, 31, 75)));
        regionBox.setPreferredSize(new java.awt.Dimension(78, 22));
        regionBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                regionBoxActionPerformed(evt);
            }
        });
        jPanel1.add(regionBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 450, 300, 30));

        addclient.setBackground(new java.awt.Color(3, 57, 108));
        addclient.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        addclient.setForeground(new java.awt.Color(179, 205, 224));
        addclient.setText("Add");
        addclient.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addclientActionPerformed(evt);
            }
        });
        jPanel1.add(addclient, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 510, 100, 30));

        jPanel2.setBackground(new java.awt.Color(0, 91, 150));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setIcon(new javax.swing.ImageIcon("C:\\Users\\Ayat\\Desktop\\mavenproject1 (2)\\mavenproject1\\images\\Sans titre-1.png")); // NOI18N
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 130, 270, 280));

        jPanel1.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 320, 580));

        jLabel18.setIcon(new javax.swing.ImageIcon("C:\\Users\\Ayat\\Desktop\\mavenproject1 (2)\\mavenproject1\\images\\icons8_cancel_filled_35px.png")); // NOI18N
        jLabel18.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel18MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(715, 0, 40, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 752, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 579, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void nameCLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameCLActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameCLActionPerformed

    private void adressCLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adressCLActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_adressCLActionPerformed

    private void countryCLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_countryCLActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_countryCLActionPerformed

    private void emailCLActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_emailCLActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_emailCLActionPerformed

    private void jLabel18MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel18MouseClicked
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jLabel18MouseClicked

    private void addclientActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addclientActionPerformed
        // TODO add your handling code here:
        if (!isNotEmpty(nameCL.getText()) || !isNotEmpty(adressCL.getText()) || !isNotEmpty(adressCL.getText()) || !isNotEmpty(countryCL.getText())) {
            JOptionPane.showMessageDialog(new JFrame(), "there is an empty field  ");
        } 
        else {

            String str = regionBox.getSelectedItem().toString();
            int index1 = str.indexOf("(") + 1;
            int index2 = str.indexOf(")");
            int regionID = Integer.parseInt(str.substring(index1, index2));
            if (!isEmail(emailCL.getText())) {
                JOptionPane.showMessageDialog(new JFrame(), "Check the Email");
            } else {
                try {
                    con.insertClient(nameCL.getText(), adressCL.getText(), countryCL.getText(), regionID, emailCL.getText());
                } catch (Exception ex) {
                    Logger.getLogger(AddClient.class.getName()).log(Level.SEVERE, null, ex);
                }
                NewJFrame.Card.removeAll();
                NewJFrame.Card.add(NewJFrame.Clients);
                NewJFrame.Card.repaint();
                NewJFrame.Card.revalidate();
                NewJFrame.fr1.refreshClient();
                this.dispose();
            }
        }

    }//GEN-LAST:event_addclientActionPerformed

    private void regionBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_regionBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_regionBoxActionPerformed

   
    public static void main(String args[]) throws Exception{
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
            java.util.logging.Logger.getLogger(AddClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddClient.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JFrame clt = new AddClient();
                clt.setVisible(true);
                clt.setLocationRelativeTo(null);
                
                    
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addclient;
    private javax.swing.JTextField adressCL;
    private javax.swing.JTextField countryCL;
    private javax.swing.JTextField emailCL;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTextField nameCL;
    private javax.swing.JComboBox<String> regionBox;
    // End of variables declaration//GEN-END:variables
}
