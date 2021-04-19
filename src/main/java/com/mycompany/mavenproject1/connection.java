package com.mycompany.mavenproject1;

import java.sql.*;
import oracle.jdbc.OracleTypes;

public class connection {

    public Connection connectDb() throws Exception {
        Connection cnt = null;
        Class.forName("oracle.jdbc.driver.OracleDriver");
        cnt = DriverManager.getConnection("jdbc:oracle:oci:@//AYATANSSAIEN:1521/XEPDB1", "ANSSAIEN", "oracle");
        return cnt;
    }
    //is Admin

    public boolean isAdmin(String userName, String pass) throws Exception {
        Connection con = this.connectDb();
        CallableStatement Cst = con.prepareCall("{call is_admin(?,?,?)}");
        Cst.registerOutParameter(3, Types.INTEGER);

        Cst.setString(1, userName);
        Cst.setString(2, pass);
        Cst.execute();

        int admin = Cst.getInt(3);
        con.close();
        return admin == 1;
    }

    //nombre orders 
    public int nomberOfOrdersByClient(int idc) throws Exception {
        Connection con = this.connectDb();
        CallableStatement Cst = con.prepareCall("{?=call nb_Order_By_Client(?)}");
        Cst.registerOutParameter(1, Types.INTEGER);
        Cst.setInt(2, idc);
        Cst.execute();
        return Cst.getInt(1);
    }
    //MAX_Client 
    
    public CallableStatement maxClient() throws Exception {
        Connection con = this.connectDb();
        CallableStatement Cst = con.prepareCall("{?=call max_Client_Achat(?,?)}");
        Cst.registerOutParameter(1, Types.INTEGER);
        Cst.registerOutParameter(2, Types.VARCHAR);
        Cst.registerOutParameter(3, Types.FLOAT);
        Cst.execute();
        return Cst;
    }
    public CallableStatement minClient() throws Exception {
        Connection con = this.connectDb();
        CallableStatement Cst = con.prepareCall("{?=call min_Client_Achat(?,?)}");
        Cst.registerOutParameter(1, Types.INTEGER);
        Cst.registerOutParameter(2, Types.VARCHAR);
        Cst.registerOutParameter(3, Types.FLOAT);
        Cst.execute();
        return Cst;
    }
    //products
    //add product
    public void insertProduct(String ref, String word, int cat, int fam, float pu , float tva , int stock) throws Exception {
        Connection con = this.connectDb();
        CallableStatement Cst = con.prepareCall("{call insert_product(?,?,?,?,?,?,?)}");
        Cst.setString(1, ref);
        Cst.setString(2, word);
        Cst.setInt(3, cat);
        Cst.setInt(4, fam);
        Cst.setFloat(5, pu);
        Cst.setFloat(6, tva);
        Cst.setInt(7, stock);
        Cst.executeUpdate();
    }
    
    //ordr
     public void insertOrder(int clientId , float discount) throws Exception {
        Connection con = this.connectDb();
        CallableStatement Cst = con.prepareCall("{call insert_order(?,?)}");
        Cst.setInt(1, clientId);
        Cst.setFloat(2, discount);
        Cst.executeUpdate();
    }
     
     //orderItem
     public void insertOrderItem(String ref , int qte) throws Exception {
        Connection con = this.connectDb();
        CallableStatement Cst = con.prepareCall("{call insert_orderitem(?,?)}");
        Cst.setString(1, ref);
        Cst.setInt(2, qte);
        Cst.executeUpdate();
    }

    //Display methods
    public ResultSet ShowProducts() throws Exception {
        Connection con = this.connectDb();
        CallableStatement Cst = con.prepareCall("{call show_product(?)}");

        Cst.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
        Cst.execute();

        Object ob = Cst.getObject(1);

        ResultSet rs = (ResultSet) ob;
        if (rs.next()) {
            return rs;
        } else {
            return null;
        }
    }

    //Update products
    public void UpdateProducts(String pID, String pPU, String pTVA, String pSTOCK) throws Exception {
        Connection con = this.connectDb();
        CallableStatement Cst = con.prepareCall("{call update_product(?,?,?,?)}");

        Cst.setString(1, pID);
        Cst.setString(2, pPU);
        Cst.setString(3, pTVA);
        Cst.setString(4, pSTOCK);

        Cst.executeUpdate();

    }
    //Update total in insert !!
     public void UpdateTotalInset() throws Exception {
        Connection con = this.connectDb();
        CallableStatement Cst = con.prepareCall("{call update_total ()}");
        Cst.executeUpdate();
    }
    //updateOrderItem

    public void updateOrderItem(String idI, int idO, int qte, float dis) throws Exception {
        Connection con = this.connectDb();
        CallableStatement Cst = con.prepareCall("{call update_orderItem (?,?,?,?)}");
        Cst.setInt(1, idO);
        Cst.setString(2, idI);
        Cst.setFloat(3, dis);
        Cst.setInt(4, qte);
        Cst.executeUpdate();
    }
    //get nameClien by id 
    public String getClientById(int id) throws Exception{
        Connection con = this.connectDb();
        Statement stt = con.createStatement();
        ResultSet rs = stt.executeQuery("select nomct from clients where codect = "+id);
        if (rs.next())
            return rs.getString(1) + " (" +id+")";
        return "";
    }
    
    //Update Order
    public void updateOrder(int orderID,int clientId,float discount,String stats) throws Exception{
    Connection con = this.connectDb();
    CallableStatement cst = con.prepareCall("{call update_order(?,?,?,?)}");
    cst.setInt(1,orderID);
    cst.setInt(2,clientId);
    cst.setFloat(3,discount);
    cst.setString(4,stats);
    cst.executeUpdate();
}
    //Update Clients 
     public void updateClient(int id_c, String adrr, String email, String cmt) throws Exception {
        Connection con = this.connectDb();
        CallableStatement Cst = con.prepareCall("{call update_client(?,?,?,?)}");

        Cst.setInt(1, id_c);
        Cst.setString(2, adrr);
        Cst.setString(3, email);
        Cst.setString(4, cmt);

        Cst.executeUpdate();
    }
    //chifre d'affaire d'un produit 

    public float productTurnover(String pID) throws Exception {
        float tOver;

        Connection con = this.connectDb();
        CallableStatement Cst = con.prepareCall("{?=call product_turnover(?)}");
        Cst.registerOutParameter(1, Types.FLOAT);
        Cst.setString(2, pID);
        Cst.execute();
        tOver = Cst.getFloat(1);
        return tOver;
    }
    //nombre de ventes 

    public int salesNumber(String pID) throws Exception {

        Connection con = this.connectDb();
        CallableStatement Cst = con.prepareCall("{call sales_number(?,?)}");
        Cst.registerOutParameter(2, Types.INTEGER);
        Cst.setString(1, pID);
        Cst.execute();
        return Cst.getInt(2);
    }

    //clients
    public ResultSet ShowClients() throws Exception {
        Connection con = this.connectDb();
        CallableStatement Cst = con.prepareCall("{call show_Clients(?)}");

        Cst.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
        Cst.executeQuery();
        Object ob = Cst.getObject(1);
        ResultSet rs = (ResultSet) ob;

        if (rs.next()) {
            return rs;
        } else {
            return null;
        }
    }
    //orders
    public ResultSet ShowOrders() throws Exception {
        Connection con = this.connectDb();
        CallableStatement Cst = con.prepareCall("{call show_Orders(?) }");

        Cst.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
        Cst.executeQuery();
        Object ob = Cst.getObject(1);
        ResultSet rs = (ResultSet) ob;

        if (rs.next()) {
            return rs;
        } else {
            return null;
        }
    }
    //order Items
    public ResultSet ShowOrderItems(int id) throws Exception {
        Connection con = this.connectDb();
        CallableStatement Cst = con.prepareCall("{call show_Order_Items(?,?) }");

        Cst.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
        Cst.setInt(1, id);
        Cst.executeQuery();
        Object ob = Cst.getObject(2);
        ResultSet rs = (ResultSet) ob;

        if (rs.next()) {
            return rs;
        } else {
            return null;
        }
    }
    
    //top 5
     public ResultSet ShowTop5Client() throws Exception {
        Connection con = this.connectDb();
        CallableStatement Cst = con.prepareCall("{call TOP_5_Clients(?) }");

        Cst.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
        Cst.executeQuery();
        Object ob = Cst.getObject(1);
        ResultSet rs = (ResultSet) ob;

        if (rs.next()) {
            return rs;
        } else {
            return null;
        }
    }
    //Fill Orders by Client 
    public ResultSet ShowOrdersByClient(int cID) throws Exception {
        Connection con = this.connectDb();
        CallableStatement Cst = con.prepareCall("{call show_orders_by_client(?,?)}");

        Cst.registerOutParameter(2, oracle.jdbc.OracleTypes.CURSOR);
        Cst.setInt(1, cID);
        Cst.executeQuery();
        Object ob = Cst.getObject(2);
        ResultSet rs = (ResultSet) ob;

        if (rs.next()) {
            return rs;
        } else {
            return null;
        }
    }
    //

    //fill region box
    public String[] regionsTable() throws Exception {
        Connection con = this.connectDb();
        Statement stt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stt.executeQuery("select * from regions");

        int N = 0;
        if (rs.last()) {
            N = rs.getRow();
        }
        String[] rTab = new String[N];

        rs.beforeFirst();
        int i = 0;
        while (rs.next()) {
            rTab[i] = rs.getString(2) + " (" + rs.getString(1) + ")";
            i++;
        }
        return rTab;
    }
    //fill familly box
    public String[] famillyTable() throws Exception {
        Connection con = this.connectDb();
        Statement stt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stt.executeQuery("select * from familles");

        int N = 0;
        if (rs.last()) {
            N = rs.getRow();
        }
        String[] fTab = new String[N];

        rs.beforeFirst();
        int i = 0;
        while (rs.next()) {
            fTab[i] = rs.getString(2) + " (" + rs.getString(1) + ")";
            i++;
        }
        return fTab;
    }
    
    //fill familly box
    public String[] CategoryTable() throws Exception {
        Connection con = this.connectDb();
        Statement stt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stt.executeQuery("select * from CATEGORIES");

        int N = 0;
        if (rs.last()) {
            N = rs.getRow();
        }
        String[] fTab = new String[N];

        rs.beforeFirst();
        int i = 0;
        while (rs.next()) {
            fTab[i] = rs.getString(2) + " (" + rs.getString(1) + ")";
            i++;
        }
        return fTab;
    }
    //fill clients box
    public String[] ClientTable() throws Exception {
        Connection con = this.connectDb();
        Statement stt = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = stt.executeQuery("select codect,nomct from clients");

        int N = 0;
        if (rs.last()) {
            N = rs.getRow();
        }
        String[] cTab = new String[N];

        rs.beforeFirst();
        int i = 0;
        while (rs.next()) {
            cTab[i] = rs.getString(2) + " (" + rs.getString(1) + ")";
            i++;
        }
        return cTab;
    }

    // insert into  client  
    public void insertClient(String name, String adrr, String ville, int region, String Email) throws Exception {
        Connection con = this.connectDb();
        CallableStatement Cst = con.prepareCall("{call insert_client(?,?,?,?,?)}");
        Cst.setString(1, name);
        Cst.setString(2, adrr);
        Cst.setString(3, ville);
        Cst.setInt(4, region);
        Cst.setString(5, Email);
        Cst.executeUpdate();
    }
    
    //history
    public ResultSet ShowHistory() throws Exception {
        Connection con = this.connectDb();
        CallableStatement Cst = con.prepareCall("{call show_History(?)}");

        Cst.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
        Cst.execute();

        Object ob = Cst.getObject(1);

        ResultSet rs = (ResultSet) ob;
        if (rs.next()) {
            return rs;
        } else {
            return null;
        }
    }
    public void deleteOrder() throws Exception {
        Connection con = this.connectDb();
        CallableStatement Cst = con.prepareCall("{call delete_order()}");
        Cst.executeUpdate();
    }
    CallableStatement maxProduct() throws Exception {
         Connection con = this.connectDb();
        CallableStatement Cst = con.prepareCall("{?=call max_Product_CA(?,?)}");
        Cst.registerOutParameter(1, Types.VARCHAR);
        Cst.registerOutParameter(2, Types.VARCHAR);
        Cst.registerOutParameter(3, Types.FLOAT);
        Cst.execute();
        return Cst;
    }

    CallableStatement minProduct() throws Exception {
         Connection con = this.connectDb();
        CallableStatement Cst = con.prepareCall("{?=call min_Product_CA(?,?)}");
        Cst.registerOutParameter(1, Types.VARCHAR);
        Cst.registerOutParameter(2, Types.VARCHAR);
        Cst.registerOutParameter(3, Types.FLOAT);
        Cst.execute();
        return Cst;
    }

    ResultSet ShowTop5Products() throws Exception {
        Connection con = this.connectDb();
        CallableStatement Cst = con.prepareCall("{call TOP_5_Products(?) }");

        Cst.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
        Cst.executeQuery();
        Object ob = Cst.getObject(1);
        ResultSet rs = (ResultSet) ob;

        if (rs.next()) {
            return rs;
        } else {
            return null;
        }
    }
      public  ResultSet ArticleByFam() throws Exception {
        Connection con = this.connectDb();
        CallableStatement Cst = con.prepareCall("{call Article_By_Fam(?)}");
        Cst.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
        Cst.executeQuery();
        Object ob = Cst.getObject(1);
        ResultSet rs = (ResultSet) ob;

        if (rs.next()) {
            return rs;
        } else {
            return null;
        }
    }
        public  ResultSet ArticleByCat() throws Exception {
        Connection con = this.connectDb();
        CallableStatement Cst = con.prepareCall("{call Article_By_Cat(?)}");
        Cst.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);
        Cst.executeQuery();
       Object ob = Cst.getObject(1);
        ResultSet rs = (ResultSet) ob;

        if (rs.next()) {
            return rs;
        } else {
            return null;
        }
    }
    
        public String turnover() throws Exception{
            Connection con = this.connectDb();
            CallableStatement Cst = con.prepareCall("{? = call turnover}");
            Cst.registerOutParameter(1, Types.FLOAT);
            Cst.executeQuery();
            return String.valueOf(Cst.getFloat(1));
        }
    
    public static void main(String args[]) throws Exception {
        connection con = new connection();
        /*String[] tab = con.regionsTable();
                for (String i : tab)
                    System.out.println(i);*/
        // con.insertClient("name", "adrr", "ville", 1, "Email");
       ResultSet rs = con.ShowTop5Client();
               do{
                   System.out.println(rs.getString(1) +" "+rs.getFloat(2));
               }while(rs.next());

        //con.updateClient(1, "adr", "emal", "Hello");
    }

    

   

}
