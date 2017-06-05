package ict.db;

import ict.beans.*;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Database {

    private String url = "";
    private String username = "";
    private String password = "";

    public Database(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public Connection getConnection() throws SQLException, IOException {
        System.setProperty("jdbc.drivers", "org.apache.derby.jdbc.ClientDriver");
        return DriverManager.getConnection(url, username, password);
    }

    public boolean deleteCustByID(String clientID) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean isSuccess = false;
        try {
            cnnct = getConnection();
            String preQueryStatement = "delete from client where clientID=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, clientID);
            int rowCount = pStmnt.executeUpdate();
            if (rowCount >= 1) {
                isSuccess = true;
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return isSuccess;
    }

    public ArrayList<CategoryBean> queryAllCategory() {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        CategoryBean cb = null;
        ArrayList<CategoryBean> al = new ArrayList();
        try {
            cnnct = getConnection();
            String preQueryStatement = "select * from category";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            while (rs.next()) {
                cb = new CategoryBean();
                cb.setCategoryID(rs.getString("categoryID"));
                cb.setCategory(rs.getString("category"));
                ArrayList<ItemBean> ib = queryByItem(rs.getString("categoryID"), null, 0, 0, null, null);
                cb.setItem(ib);
                al.add(cb);
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return al;
    }

    public CategoryBean queryByCategoryID(String categoryID) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        CategoryBean cb = null;
        try {
            cnnct = getConnection();
            String preQueryStatement = "select * from category where categoryID=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, categoryID);
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            if (rs.next()) {
                cb = new CategoryBean();
                cb.setCategoryID(rs.getString("categoryID"));
                cb.setCategory(rs.getString("category"));
                ArrayList<ItemBean> ib = queryByItem(rs.getString("categoryID"), null, 0, 0, null, null);
                cb.setItem(ib);
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return cb;
    }

    public ArrayList<ItemBean> queryAllItem() {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        ItemBean ib = null;
        ArrayList<ItemBean> al = new ArrayList();
        try {
            cnnct = getConnection();
            String preQueryStatement = "select * from item";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            while (rs.next()) {
                ib = new ItemBean();
                ib.setItemID(rs.getString("itemid"));
                ib.setCategoryID(rs.getString("categoryID"));
                ib.setName(rs.getString("name"));
                ib.setPrice(rs.getDouble("price"));
                ib.setDesigner(rs.getString("designer"));
                ib.setDescription(rs.getString("description"));
                ib.setQuantity(rs.getInt("quantity"));
                ib.setImage(rs.getString("image"));
                CategoryBean cb = queryByCategoryID(rs.getString("categoryID"));
                ib.setCategory(cb);
                al.add(ib);
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return al;
    }

    public ItemBean queryByItemID(String itemID) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        ItemBean ib = null;
        try {
            cnnct = getConnection();
            String preQueryStatement = "select * from item where itemID=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, itemID);
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            if (rs.next()) {
                ib = new ItemBean();
                ib.setItemID(rs.getString("itemid"));
                ib.setCategoryID(rs.getString("categoryID"));
                ib.setName(rs.getString("name"));
                ib.setPrice(rs.getDouble("price"));
                ib.setDesigner(rs.getString("designer"));
                ib.setDescription(rs.getString("description"));
                ib.setQuantity(rs.getInt("quantity"));
                ib.setImage(rs.getString("image"));
                CategoryBean cb = queryByCategoryID(rs.getString("categoryID"));
                ib.setCategory(cb);
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return ib;
    }

    public ArrayList<ItemBean> queryByItem(String categoryID, String name, double min, double max, String designer, String sorting) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null, pst = null;
        ItemBean ib = null;
        CategoryBean cb = null;
        ArrayList<ItemBean> al = new ArrayList();
        double[] price = new double[2];
        if (min == 0 && max == 0) {
            price = null;
        } else {
            price[0] = min;
            price[1] = max;
        }
        Object[] array = {categoryID, name, price, designer};
        String[] element = {"categoryID", "name", "price", "designer"};
        String sortBy = null;
        if (sorting != null) {
            for (int i = 0; i < element.length; i++) {
                if (sorting.equals(element[i])) {
                    sortBy = sorting;
                }
            }
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            cnnct = getConnection();
            String preQueryStatement = "select * from item";
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < array.length; i++) {
                if (array[i] != null) {
                    map.put(element[i], array[i]);
                }
            }
            if (map.size() > 0) {
                preQueryStatement += " where";
            }
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (builder.length() != 0) {
                    builder.append("and");
                }
                if (entry.getKey().equals("price")) {
                    builder.append(" " + entry.getKey() + " between ? and ? ");
                } else {
                    builder.append(" " + entry.getKey() + " like ? ");
                }
            }
            if (sortBy != null) {
                builder.append("order by " + sorting + " asc");
            }
            pStmnt = cnnct.prepareStatement(preQueryStatement + builder.toString());
            int index = 1;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getKey().equals("categoryID") || entry.getKey().equals("name")
                        || entry.getKey().equals("designer")) {
                    pStmnt.setString(index, "%" + (String) entry.getValue() + "%");
                    index++;
                } else if (entry.getKey().equals("price")) {
                    for (int i = 0; i < price.length; i++) {
                        pStmnt.setDouble(index, price[i]);
                        index++;
                    }
                }
            }

            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            while (rs.next()) {
                ib = new ItemBean();
                ib.setItemID(rs.getString("itemid"));
                ib.setCategoryID(rs.getString("categoryID"));
                ib.setName(rs.getString("name"));
                ib.setPrice(rs.getDouble("price"));
                ib.setDesigner(rs.getString("designer"));
                ib.setDescription(rs.getString("description"));
                ib.setQuantity(rs.getInt("quantity"));
                ib.setImage(rs.getString("image"));

                String querySt = "select * from category where categoryID=?";
                pst = cnnct.prepareStatement(querySt);
                pst.setString(1, rs.getString("categoryID"));
                ResultSet rsItem = null;
                rsItem = pst.executeQuery();
                if (rsItem.next()) {
                    cb = new CategoryBean();
                    cb.setCategoryID(rsItem.getString("categoryID"));
                    cb.setCategory(rsItem.getString("category"));
                    ib.setCategory(cb);
                }
                al.add(ib);
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return al;
    }

    public ArrayList<AccessoryBean> queryAllAccessory() {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        AccessoryBean ab = null;
        ArrayList<AccessoryBean> al = new ArrayList();
        try {
            cnnct = getConnection();
            String preQueryStatement = "select * from accessory";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            while (rs.next()) {
                ab = new AccessoryBean();
                ab.setAccessoryID(rs.getString("accessoryID"));
                ab.setBonus(rs.getDouble("bonus"));
                ab.setName(rs.getString("name"));
                ab.setDesigner(rs.getString("designer"));
                ab.setDescription(rs.getString("description"));
                ab.setQuantity(rs.getInt("quantity"));
                ab.setImage(rs.getString("image"));
                al.add(ab);
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return al;
    }

    public AccessoryBean queryByAccessoryID(String accessoryID) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        AccessoryBean ab = null;
        try {
            cnnct = getConnection();
            String preQueryStatement = "select * from accessory where accessoryid=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, accessoryID);
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            if (rs.next()) {
                ab = new AccessoryBean();
                ab.setAccessoryID(rs.getString("accessoryID"));
                ab.setBonus(rs.getDouble("bonus"));
                ab.setName(rs.getString("name"));
                ab.setDesigner(rs.getString("designer"));
                ab.setDescription(rs.getString("description"));
                ab.setQuantity(rs.getInt("quantity"));
                ab.setImage(rs.getString("image"));
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return ab;
    }

    /**
     *
     * @param name
     * @param price
     * @param designer
     */
    public ArrayList<AccessoryBean> queryByAccessory(String name, double min, double max, String designer, String sorting) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        AccessoryBean ab = null;
        ArrayList<AccessoryBean> al = new ArrayList();
        Double[] bonus = new Double[2];
        if (min < 0) {
            bonus[0] = null;
        } else {
            bonus[0] = min;
        }
        if (max < 0) {
            bonus[1] = null;
        } else {
            bonus[1] = max;
        }
        Object[] array = {name, bonus[0], bonus[1], designer};
        String[] element = {"name", "bonus >=", "bonus <=", "designer"};
        String sortBy = null;
        if (sorting != null) {
            if (sorting.equalsIgnoreCase("name asc") || sorting.equalsIgnoreCase("bonus asc") || sorting.equalsIgnoreCase("bonus desc")) {
                sortBy = sorting;
            }
        }
        HashMap<String, Object> map = new HashMap<String, Object>();
        try {
            cnnct = getConnection();
            String preQueryStatement = "select * from accessory";
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < array.length; i++) {
                if (array[i] != null) {
                    map.put(element[i], array[i]);
                }
            }
            if (map.size() > 0) {
                preQueryStatement += " where";
            }
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (builder.length() != 0) {
                    builder.append(" and");
                }
                if (entry.getKey().equals("bonus >=") || entry.getKey().equals("bonus <=")) {
                    builder.append(" " + entry.getKey() + " ?");
                } else {
                    builder.append(" " + entry.getKey() + " like ?");
                }
            }
            if (sortBy != null) {
                builder.append(" order by " + sortBy);
            }
            pStmnt = cnnct.prepareStatement(preQueryStatement + builder.toString());
            int index = 1;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                if (entry.getKey().equals("name") || entry.getKey().equals("designer")) {
                    pStmnt.setString(index, "%" + (String) entry.getValue() + "%");
                    index++;
                } else if (entry.getKey().equals("bonus >=")) {
                    pStmnt.setDouble(index, bonus[0]);
                    index++;
                } else if (entry.getKey().equals("bonus <=")) {
                    pStmnt.setDouble(index, bonus[1]);
                    index++;
                }
            }

            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            while (rs.next()) {
                ab = new AccessoryBean();
                ab.setAccessoryID(rs.getString("accessoryID"));
                ab.setBonus(rs.getDouble("bonus"));
                ab.setName(rs.getString("name"));
                ab.setDesigner(rs.getString("designer"));
                ab.setDescription(rs.getString("description"));
                ab.setQuantity(rs.getInt("quantity"));
                ab.setImage(rs.getString("image"));
                al.add(ab);
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return al;
    }

    public String getCustomerIDByLoginID(String loginID, String password) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        String clientid = null;
        try {
            cnnct = getConnection();
            String preQueryStatement = "select * from client where loginID=? and password=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, loginID);
            pStmnt.setString(2, password);
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            if (rs.next()) {
                clientid = rs.getString("clientid");
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return clientid;
    }

    /**
     *
     * @param name
     * @param tel
     * @param email
     * @param address
     */
    public boolean addCustomer(String name, String tel, String email, String address) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean isSuccess = false;
        Statement stmt = null;
        try {
            cnnct = getConnection();
            stmt = cnnct.createStatement();
            String qry = "select count(*) as noOfRows from client";
            ResultSet rs = stmt.executeQuery(qry);
            rs.next();
            String fCustID = String.format("%05d", rs.getInt("noOfRows") + 1);

            String preQueryStatement = "insert into client(clientid,name,address,telephone,email) values (?,?,?,?,?)";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, "C" + fCustID);
            pStmnt.setString(2, name);
            pStmnt.setString(3, address);
            pStmnt.setString(4, tel);
            pStmnt.setString(5, email);
            int rowCount = pStmnt.executeUpdate();
            if (rowCount >= 1) {
                isSuccess = true;
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return isSuccess;
    }

    /**
     *
     * @param name
     * @param tel
     * @param email
     * @param address
     */
    public boolean editCustomer(String custID, String name, String tel, String email, String address) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean isSuccess = false;
        try {
            cnnct = getConnection();
            String preQueryStatement = "update client set name=? , telephone=? , email=?,address=? where clientid=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, name);
            pStmnt.setString(2, tel);
            pStmnt.setString(3, email);
            pStmnt.setString(4, address);
            pStmnt.setString(5, custID);
            int rowCount = pStmnt.executeUpdate();
            if (rowCount >= 1) {
                isSuccess = true;
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return isSuccess;
    }

    /**
     *
     * @param custID
     */
    public ClientBean queryCustomerByID(String custID) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        ClientBean cb = null;
        try {
            cnnct = getConnection();
            String preQueryStatement = "select * from client where clientID=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, custID);
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            if (rs.next()) {
                cb = new ClientBean();
                cb.setClientID(rs.getString("clientid"));
                cb.setLoginID(rs.getString("loginid"));
                cb.setName(rs.getString("name"));
                cb.setAddress(rs.getString("address"));
                cb.setTelephone(rs.getString("telephone"));
                cb.setEmail(rs.getString("email"));
                cb.setAmount(rs.getDouble("amount"));
                cb.setBonus(rs.getDouble("bonus"));
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return cb;
    }

    public ArrayList<OrdersBean> queryAllOrders() {
        Connection cnnct = null;
        PreparedStatement pStmnt = null, pst = null;
        OrdersBean ob = null;
        ArrayList<OrdersBean> al = new ArrayList();
        OrderItemBean oib = null;
        ArrayList<OrderItemBean> ol = new ArrayList();
        try {
            cnnct = getConnection();
            String preQueryStatement = "select * from orders";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            while (rs.next()) {
                ob = new OrdersBean();

                ob.setOrderID(rs.getString("orderID"));
                ob.setClientID(rs.getString("clientID"));
                ob.setAmount(rs.getDouble("amount"));
                ob.setOrderDateTime(rs.getTimestamp("orderDateTime"));
                ob.setPickupDateTime(rs.getDate("pickupDateTime"));
                ob.setType(rs.getString("type"));
                ob.setAddress(rs.getString("address"));
                ob.setStatus(rs.getString("status"));
                ClientBean cb = queryCustomerByID(rs.getString("clientID"));
                ob.setClient(cb);

                String querySt = "select * from orderitem where orderid=?";
                pst = cnnct.prepareStatement(querySt);
                pst.setString(1, rs.getString("orderID"));
                ResultSet rsItem = null;
                rsItem = pst.executeQuery();
                while (rsItem.next()) {
                    oib = new OrderItemBean();
                    oib.setOrderItemID(rsItem.getString("orderItemID"));
                    oib.setOrderID(rsItem.getString("orderID"));
                    oib.setCategoryID(rsItem.getString("categoryID"));
                    oib.setName(rsItem.getString("name"));
                    oib.setPrice(rsItem.getDouble("Price"));
                    oib.setDesigner(rsItem.getString("Designer"));
                    oib.setDescription(rsItem.getString("Description"));
                    oib.setQuantity(rsItem.getInt("quantity"));
                    oib.setImage(rsItem.getString("image"));
                    ol.add(oib);
                }
                ob.setItem(ol);
                al.add(ob);
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return al;
    }

    public ArrayList<RedemptionBean> queryAllRedeem() {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        RedemptionBean rb = null;
        ArrayList<RedemptionBean> al = new ArrayList();
        try {
            cnnct = getConnection();
            String preQueryStatement = "select * from Redemption order by RedeemedDateTime asc";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            while (rs.next()) {
                rb = new RedemptionBean();
                rb.setRedemptionID(rs.getString("RedemptionID"));
                rb.setClientID(rs.getString("clientid"));
                rb.setName(rs.getString("name"));
                rb.setDesigner(rs.getString("designer"));
                rb.setDescription(rs.getString("description"));
                rb.setRedeemedDateTime(rs.getTimestamp("RedeemedDateTime"));
                rb.setQuantity(rs.getInt("quantity"));
                rb.setBonus(rs.getDouble("bonus"));
                rb.setImage(rs.getString("image"));
                rb.setPickupDateTime(rs.getDate("pickupDateTime"));
                rb.setType(rs.getString("type"));
                rb.setAddress(rs.getString("address"));
                rb.setStatus(rs.getString("status"));
                ClientBean cb = queryCustomerByID(rs.getString("clientid"));
                rb.setClient(cb);
                al.add(rb);
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return al;
    }

    /**
     *
     * @param custID
     * @return
     */
    public ArrayList<OrdersBean> queryOrders(String custID) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null, pst = null;
        OrdersBean ob = null;
        ArrayList<OrdersBean> al = new ArrayList();
        OrderItemBean oib = null;
        ArrayList<OrderItemBean> ol = new ArrayList();
        try {
            cnnct = getConnection();
            String preQueryStatement = "select * from orders where clientid=? order by orderDateTime desc fetch first 10 rows only";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, custID);
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            while (rs.next()) {
                ob = new OrdersBean();

                ob.setOrderID(rs.getString("orderID"));
                ob.setClientID(rs.getString("clientID"));
                ob.setAmount(rs.getDouble("amount"));
                ob.setOrderDateTime(rs.getTimestamp("orderDateTime"));
                ob.setPickupDateTime(rs.getDate("pickupDateTime"));
                ob.setType(rs.getString("type"));
                ob.setAddress(rs.getString("address"));
                ob.setStatus(rs.getString("status"));
                ClientBean cb = queryCustomerByID(rs.getString("clientID"));
                ob.setClient(cb);

                String querySt = "select * from orderitem where orderid=?";
                pst = cnnct.prepareStatement(querySt);
                pst.setString(1, rs.getString("orderID"));
                ResultSet rsItem = null;
                rsItem = pst.executeQuery();
                while (rsItem.next()) {
                    oib = new OrderItemBean();
                    oib.setOrderItemID(rsItem.getString("orderItemID"));
                    oib.setOrderID(rsItem.getString("orderID"));
                    oib.setCategoryID(rsItem.getString("categoryID"));
                    oib.setName(rsItem.getString("name"));
                    oib.setPrice(rsItem.getDouble("Price"));
                    oib.setDesigner(rsItem.getString("Designer"));
                    oib.setDescription(rsItem.getString("Description"));
                    oib.setQuantity(rsItem.getInt("quantity"));
                    oib.setImage(rsItem.getString("image"));
                    ol.add(oib);
                }
                ob.setItem(ol);
                al.add(ob);
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return al;
    }

    public OrdersBean queryOrdersByID(String orderID) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null, pst = null;
        OrdersBean ob = null;
        OrderItemBean oib = null;
        ArrayList<OrderItemBean> ol = new ArrayList();
        try {
            cnnct = getConnection();
            String preQueryStatement = "select * from orders where orderid=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, orderID);
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            if (rs.next()) {
                ob = new OrdersBean();

                ob.setOrderID(rs.getString("orderID"));
                ob.setClientID(rs.getString("clientID"));
                ob.setAmount(rs.getDouble("amount"));
                ob.setOrderDateTime(rs.getTimestamp("orderDateTime"));
                ob.setPickupDateTime(rs.getDate("pickupDateTime"));
                ob.setType(rs.getString("type"));
                ob.setAddress(rs.getString("address"));
                ob.setStatus(rs.getString("status"));
                ClientBean cb = queryCustomerByID(rs.getString("clientID"));
                ob.setClient(cb);

                String querySt = "select * from orderitem where orderid=?";
                pst = cnnct.prepareStatement(querySt);
                pst.setString(1, rs.getString("orderID"));
                ResultSet rsItem = null;
                rsItem = pst.executeQuery();
                while (rsItem.next()) {
                    oib = new OrderItemBean();
                    oib.setOrderItemID(rsItem.getString("orderItemID"));
                    oib.setOrderID(rsItem.getString("orderID"));
                    oib.setCategoryID(rsItem.getString("categoryID"));
                    oib.setName(rsItem.getString("name"));
                    oib.setPrice(rsItem.getDouble("Price"));
                    oib.setDesigner(rsItem.getString("Designer"));
                    oib.setDescription(rsItem.getString("Description"));
                    oib.setQuantity(rsItem.getInt("quantity"));
                    oib.setImage(rsItem.getString("image"));
                    ol.add(oib);
                }
                ob.setItem(ol);
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return ob;
    }

    /**
     *
     * @param orderItem
     * @param orderDetail
     * @param quantity
     */
    public boolean addOrder(OrdersBean orderDetail) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        Statement stmt = null;
        boolean firSuccess = false, secSuccess = false, isSuccess = false;
        try {
            cnnct = getConnection();
            stmt = cnnct.createStatement();
            String qry;
            qry = "select count(*) as noOfRows from orders";
            ResultSet rs;
            rs = stmt.executeQuery(qry);
            rs.next();
            String fOrderID = String.format("%05d", rs.getInt("noOfRows") + 1);
            String fedOrderID = "O" + fOrderID;
            java.sql.Date sqlDate = null;
            java.util.Date utilDate = orderDetail.getPickupDateTime();
            if (utilDate != null) {
                sqlDate = new java.sql.Date(utilDate.getTime());
            }

            String preQueryStatement;
            preQueryStatement = "insert into orders(orderid,clientid,amount,orderDateTime,pickupDateTime,type,address,status) values (?,?,?,?,?,?,?,?)";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, fedOrderID);
            pStmnt.setString(2, orderDetail.getClientID());
            pStmnt.setDouble(3, orderDetail.getAmount());
            pStmnt.setTimestamp(4, orderDetail.getOrderDateTime());
            pStmnt.setDate(5, sqlDate);
            pStmnt.setString(6, orderDetail.getType());
            ClientBean cb = queryCustomerByID(orderDetail.getClientID());
            pStmnt.setString(7, cb.getAddress());
            pStmnt.setString(8, orderDetail.getStatus());
            int rowCount = pStmnt.executeUpdate();
            if (rowCount >= 1) {
                firSuccess = true;
            }
            for (int i = 0; i < orderDetail.getItem().size(); i++) {
                OrderItemBean oib = (OrderItemBean) orderDetail.getItem().get(i);
                qry = "select count(*) as noOfRows from orderitem";
                rs = stmt.executeQuery(qry);
                rs.next();
                String fOrderItemID = String.format("%05d", rs.getInt("noOfRows") + 1);
                String fedOrderItemID = "O" + fOrderItemID;
                //edit amount & item quantity
                preQueryStatement = "insert into orderitem(orderItemID,orderID,categoryID,name,price,designer,description,image,quantity) values (?,?,?,?,?,?,?,?,?)";
                pStmnt = cnnct.prepareStatement(preQueryStatement);
                pStmnt.setString(1, fedOrderItemID);
                pStmnt.setString(2, fedOrderID);
                pStmnt.setString(3, oib.getCategoryID());
                pStmnt.setString(4, oib.getName());
                pStmnt.setDouble(5, oib.getPrice());
                pStmnt.setString(6, oib.getDesigner());
                pStmnt.setString(7, oib.getDescription());
                pStmnt.setString(8, oib.getImage());
                pStmnt.setInt(9, oib.getQuantity());
                int rowCount2 = pStmnt.executeUpdate();
                if (rowCount2 >= 1) {
                    secSuccess = true;
                }
                if (firSuccess && secSuccess) {
                    isSuccess = true;
                }
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return isSuccess;
    }

    /**
     *
     * @param custID
     */
    public ArrayList<RedemptionBean> queryRedeem(String custID) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        RedemptionBean rb = null;
        ArrayList<RedemptionBean> al = new ArrayList();
        try {
            cnnct = getConnection();
            String preQueryStatement = "select * from Redemption where clientid=? order by RedeemedDateTime desc fetch first 10 rows only";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, custID);
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            while (rs.next()) {
                rb = new RedemptionBean();
                rb.setRedemptionID(rs.getString("RedemptionID"));
                rb.setClientID(rs.getString("clientid"));
                rb.setName(rs.getString("name"));
                rb.setDesigner(rs.getString("designer"));
                rb.setDescription(rs.getString("description"));
                rb.setRedeemedDateTime(rs.getTimestamp("RedeemedDateTime"));
                rb.setQuantity(rs.getInt("quantity"));
                rb.setBonus(rs.getDouble("bonus"));
                rb.setImage(rs.getString("image"));
                rb.setPickupDateTime(rs.getDate("pickupDateTime"));
                rb.setType(rs.getString("type"));
                rb.setAddress(rs.getString("address"));
                rb.setStatus(rs.getString("status"));
                ClientBean cb = queryCustomerByID(rs.getString("clientid"));
                rb.setClient(cb);
                al.add(rb);
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return al;
    }

    public RedemptionBean queryRedeemByID(String redeemID) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        RedemptionBean rb = null;
        try {
            cnnct = getConnection();
            String preQueryStatement = "select * from Redemption where RedemptionID=? order by RedeemedDateTime asc";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, redeemID);
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            while (rs.next()) {
                rb = new RedemptionBean();
                rb.setRedemptionID(rs.getString("RedemptionID"));
                rb.setClientID(rs.getString("clientid"));
                rb.setName(rs.getString("name"));
                rb.setDesigner(rs.getString("designer"));
                rb.setDescription(rs.getString("description"));
                rb.setRedeemedDateTime(rs.getTimestamp("RedeemedDateTime"));
                rb.setQuantity(rs.getInt("quantity"));
                rb.setBonus(rs.getDouble("bonus"));
                rb.setImage(rs.getString("image"));
                rb.setPickupDateTime(rs.getDate("pickupDateTime"));
                rb.setType(rs.getString("type"));
                rb.setAddress(rs.getString("address"));
                rb.setStatus(rs.getString("status"));
                ClientBean cb = queryCustomerByID(rs.getString("clientid"));
                rb.setClient(cb);
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return rb;
    }

    /**
     *
     * @param custID
     * @param amount
     * @param date
     * @param quantity
     * @param redeemItem
     */
    public boolean addRedeem(ArrayList<RedemptionBean> redeemItem) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean isSuccess = false;
        Statement stmt = null;
        try {
            cnnct = getConnection();
            stmt = cnnct.createStatement();
            for (int i = 0; i < redeemItem.size(); i++) {
                RedemptionBean rb = (RedemptionBean) redeemItem.get(i);
                String qry;
                qry = "select count(*) as noOfRows from Redemption";
                ResultSet rs;
                rs = stmt.executeQuery(qry);
                rs.next();
                String fRedeemID = String.format("%05d", rs.getInt("noOfRows") + 1);
                String fedRedeemID = "R" + fRedeemID;
                java.sql.Date sqlDate = null;
                java.util.Date utilDate = rb.getPickupDateTime();
                if (utilDate != null) {
                    sqlDate = new java.sql.Date(utilDate.getTime());
                }

                String preQueryStatement;
                //update item left
                preQueryStatement = "insert into Redemption(redemptionID,clientid,name,designer,description,redeemedDateTime,quantity,bonus,image,pickupDateTime,type,address,status) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
                pStmnt = cnnct.prepareStatement(preQueryStatement);
                pStmnt.setString(1, fedRedeemID);
                pStmnt.setString(2, rb.getClientID());
                pStmnt.setString(3, rb.getName());
                pStmnt.setString(4, rb.getDesigner());
                pStmnt.setString(5, rb.getDescription());
                pStmnt.setTimestamp(6, rb.getRedeemedDateTime());
                pStmnt.setInt(7, rb.getQuantity());
                pStmnt.setDouble(8, rb.getBonus());
                pStmnt.setString(9, rb.getImage());
                pStmnt.setDate(10, sqlDate);
                pStmnt.setString(11, rb.getType());
                ClientBean cb = queryCustomerByID(rb.getClientID());
                pStmnt.setString(12, cb.getAddress());
                pStmnt.setString(13, rb.getStatus());
                int rowCount = pStmnt.executeUpdate();
                if (rowCount >= 1) {
                    isSuccess = true;
                }
            }
            if (pStmnt != null) {
                pStmnt.close();
            }
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return isSuccess;
    }

    public boolean addManager(ManagerBean mb) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean isSuccess = false;
        Statement stmt = null;
        try {
            cnnct = getConnection();
            stmt = cnnct.createStatement();
            String qry = "select count(*) as noOfRows from manager";
            ResultSet rs = stmt.executeQuery(qry);
            rs.next();
            String fManagerID = String.format("%05d", rs.getInt("noOfRows") + 1);

            String preQueryStatement = "insert into manager(managerID,password,name) values (?,?,?)";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, "M" + fManagerID);
            pStmnt.setString(2, mb.getPassword());
            pStmnt.setString(3, mb.getName());
            int rowCount = pStmnt.executeUpdate();
            if (rowCount >= 1) {
                isSuccess = true;
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return isSuccess;
    }

    public boolean isValidManager(String managerID, String password) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean valid = false;
        try {
            cnnct = getConnection();
            String preQueryStatement = "select * from manager where managerid=? and password=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, managerID);
            pStmnt.setString(2, password);
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            if (rs.next()) {
                valid = true;
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return valid;
    }

    public ManagerBean queryManagerByID(String managerID) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        ManagerBean mb = null;
        try {
            cnnct = getConnection();
            String preQueryStatement = "select * from manager where managerID=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, managerID);
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            if (rs.next()) {
                mb = new ManagerBean();
                mb.setName(rs.getString("name"));
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return mb;
    }

    public boolean addCategory(CategoryBean cb) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean isSuccess = false;
        Statement stmt = null;
        try {
            cnnct = getConnection();
            stmt = cnnct.createStatement();
            String qry = "select count(*) as noOfRows from category";
            ResultSet rs = stmt.executeQuery(qry);
            rs.next();
            String fCategoryID = String.format("%05d", rs.getInt("noOfRows") + 1);

            String preQueryStatement = "insert into category(categoryID,category) values (?,?)";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, "CA" + fCategoryID);
            pStmnt.setString(2, cb.getCategory());
            int rowCount = pStmnt.executeUpdate();
            if (rowCount >= 1) {
                isSuccess = true;
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return isSuccess;
    }

    public boolean addItem(ItemBean ib) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean isSuccess = false;
        Statement stmt = null;
        try {
            cnnct = getConnection();
            stmt = cnnct.createStatement();
            String qry = "select count(*) as noOfRows from item";
            ResultSet rs = stmt.executeQuery(qry);
            rs.next();
            String fItemID = String.format("%05d", rs.getInt("noOfRows") + 1);

            String preQueryStatement = "insert into item(itemID,categoryID,name,price,designer,description,quantity,image) values (?,?,?,?,?,?,?,?)";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, "I" + fItemID);
            pStmnt.setString(2, ib.getCategoryID());
            pStmnt.setString(3, ib.getName());
            pStmnt.setDouble(4, ib.getPrice());
            pStmnt.setString(5, ib.getDesigner());
            pStmnt.setString(6, ib.getDescription());
            pStmnt.setInt(7, ib.getQuantity());
            pStmnt.setString(8, ib.getImage());
            int rowCount = pStmnt.executeUpdate();
            if (rowCount >= 1) {
                isSuccess = true;
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return isSuccess;
    }

    /**
     *
     * @param itemDetail
     */
    public boolean editItem(ItemBean itemDetail) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean isSuccess = false;
        try {
            cnnct = getConnection();
            String preQueryStatement = "update item set categoryid=? , name=? , price=?,designer=?,description=?,quantity=?,image=? where itemid=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, itemDetail.getCategoryID());
            pStmnt.setString(2, itemDetail.getName());
            pStmnt.setDouble(3, itemDetail.getPrice());
            pStmnt.setString(4, itemDetail.getDesigner());
            pStmnt.setString(5, itemDetail.getDescription());
            pStmnt.setInt(6, itemDetail.getQuantity());
            pStmnt.setString(7, itemDetail.getImage()); //!
            pStmnt.setString(8, itemDetail.getItemID());
            int rowCount = pStmnt.executeUpdate();
            if (rowCount >= 1) {
                isSuccess = true;
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return isSuccess;
    }

    public boolean addAccessory(AccessoryBean ab) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean isSuccess = false;
        Statement stmt = null;
        try {
            cnnct = getConnection();
            stmt = cnnct.createStatement();
            String qry = "select count(*) as noOfRows from accessory";
            ResultSet rs = stmt.executeQuery(qry);
            rs.next();
            String fAccessoryID = String.format("%05d", rs.getInt("noOfRows") + 1);

            String preQueryStatement = "insert into accessory(accessoryID,bonus,name,designer,description,quantity,image) values (?,?,?,?,?,?,?)";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, "A" + fAccessoryID);
            pStmnt.setDouble(2, ab.getBonus());
            pStmnt.setString(3, ab.getName());
            pStmnt.setString(4, ab.getDesigner());
            pStmnt.setString(5, ab.getDescription());
            pStmnt.setInt(6, ab.getQuantity());
            pStmnt.setString(7, ab.getImage());
            int rowCount = pStmnt.executeUpdate();
            if (rowCount >= 1) {
                isSuccess = true;
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return isSuccess;
    }

    /**
     *
     * @param accessoryDetail
     */
    public boolean editAccessory(AccessoryBean accessoryDetail) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean isSuccess = false;
        try {
            cnnct = getConnection();
            String preQueryStatement = "update accessory set bonus=? , name=? ,designer=?,description=?,quantity=?,image=? where accessoryID=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setDouble(1, accessoryDetail.getBonus());
            pStmnt.setString(2, accessoryDetail.getName());
            pStmnt.setString(3, accessoryDetail.getDesigner());
            pStmnt.setString(4, accessoryDetail.getDescription());
            pStmnt.setInt(5, accessoryDetail.getQuantity());
            pStmnt.setString(6, accessoryDetail.getImage());
            pStmnt.setString(7, accessoryDetail.getAccessoryID());

            int rowCount = pStmnt.executeUpdate();
            if (rowCount >= 1) {
                isSuccess = true;
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return isSuccess;
    }

    public boolean deleteByItemID(String itemID) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean isSuccess = false;
        try {
            cnnct = getConnection();
            String preQueryStatement = "delete from item where itemID=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, itemID);
            int rowCount = pStmnt.executeUpdate();
            if (rowCount >= 1) {
                isSuccess = true;
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return isSuccess;
    }

    public boolean deleteByAccessoryID(String accessoryID) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean isSuccess = false;
        try {
            cnnct = getConnection();
            String preQueryStatement = "delete from accessory where accessoryID=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, accessoryID);
            int rowCount = pStmnt.executeUpdate();
            if (rowCount >= 1) {
                isSuccess = true;
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return isSuccess;
    }

    /**
     *
     * @param orderID
     * @param status
     */
    public boolean editOrderStatus(String orderID, String status) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean isSuccess = false;
        try {
            cnnct = getConnection();
            String preQueryStatement = "update orders set status=? where orderid=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, status);
            pStmnt.setString(2, orderID);
            int rowCount = pStmnt.executeUpdate();
            if (rowCount >= 1) {
                isSuccess = true;
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return isSuccess;
    }

    public boolean editRedeemStatus(String redeemID, String status) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean isSuccess = false;
        try {
            cnnct = getConnection();
            String preQueryStatement = "update Redemption set status=? where redemptionID=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, status);
            pStmnt.setString(2, redeemID);
            int rowCount = pStmnt.executeUpdate();
            if (rowCount >= 1) {
                isSuccess = true;
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return isSuccess;
    }

    public boolean editOrderPickupDateTime(String orderID, java.util.Date pickupDateTime) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean isSuccess = false;
        try {
            cnnct = getConnection();
            java.sql.Date sqlDate = null;
            java.util.Date utilDate = pickupDateTime;
            if (utilDate != null) {
                sqlDate = new java.sql.Date(utilDate.getTime());
            }

            String preQueryStatement = "update orders set pickupDateTime=? where orderid=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setDate(1, sqlDate);
            pStmnt.setString(2, orderID);
            int rowCount = pStmnt.executeUpdate();
            if (rowCount >= 1) {
                isSuccess = true;
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return isSuccess;
    }

    public boolean editRedeemPickupDateTime(String redeemID, java.util.Date pickupDateTime) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean isSuccess = false;
        try {
            cnnct = getConnection();
            java.sql.Date sqlDate = null;
            java.util.Date utilDate = pickupDateTime;
            if (utilDate != null) {
                sqlDate = new java.sql.Date(utilDate.getTime());
            }

            String preQueryStatement = "update Redemption set pickupDateTime=? where redemptionID=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setDate(1, sqlDate);
            pStmnt.setString(2, redeemID);
            int rowCount = pStmnt.executeUpdate();
            if (rowCount >= 1) {
                isSuccess = true;
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return isSuccess;
    }

    public ArrayList<ClientBean> queryAllCustomer() {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        ClientBean cb = null;
        ArrayList<ClientBean> al = new ArrayList();
        try {
            cnnct = getConnection();
            String preQueryStatement = "select * from client";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            while (rs.next()) {
                cb = new ClientBean();
                cb.setClientID(rs.getString("clientid"));
                cb.setName(rs.getString("name"));
                cb.setAddress(rs.getString("address"));
                cb.setTelephone(rs.getString("telephone"));
                cb.setEmail(rs.getString("email"));
                cb.setAmount(rs.getDouble("amount"));
                cb.setBonus(rs.getDouble("bonus"));
                al.add(cb);
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return al;
    }

    /**
     *
     * @param orderID
     * @param status
     */
    public ArrayList<ClientBean> queryCustomerNonConfirm() {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        ClientBean cb = null;
        ArrayList<ClientBean> al = new ArrayList();
        try {
            cnnct = getConnection();
            String preQueryStatement = "select * from client where confirm=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setBoolean(1, false);
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            while (rs.next()) {
                cb = new ClientBean();
                cb.setClientID(rs.getString("clientid"));
                cb.setName(rs.getString("name"));
                cb.setAddress(rs.getString("address"));
                cb.setTelephone(rs.getString("telephone"));
                cb.setEmail(rs.getString("email"));
                cb.setAmount(rs.getDouble("amount"));
                cb.setBonus(rs.getDouble("bonus"));
                cb.setConfirm(rs.getBoolean("confirm"));
                al.add(cb);
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return al;
    }

    /**
     *
     * @param custID
     * @param pwd
     * @param bonus
     */
    public boolean editCustomerConfirm(String custID, String loginID, String pwd, double bonus) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean isSuccess = false;
        try {
            cnnct = getConnection();
            String preQueryStatement = "update client set loginID=?,password=?,bonus=?,confirm=? where clientid=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, loginID);
            pStmnt.setString(2, pwd);
            pStmnt.setDouble(3, bonus);
            pStmnt.setBoolean(4, true);
            pStmnt.setString(5, custID);
            int rowCount = pStmnt.executeUpdate();
            if (rowCount >= 1) {
                isSuccess = true;
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return isSuccess;
    }

    /**
     *
     * @param custID
     * @param amount
     */
    public boolean editCustomerAmount(String custID, double amount) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean isSuccess = false;
        try {
            cnnct = getConnection();
            String preQueryStatement = "update client set amount=? where clientid=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setDouble(1, amount);
            pStmnt.setString(2, custID);
            int rowCount = pStmnt.executeUpdate();
            if (rowCount >= 1) {
                isSuccess = true;
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return isSuccess;
    }

    /**
     *
     * @param custID
     * @param amount
     */
    public boolean editCustomerBonus(String custID, double bonus) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        boolean isSuccess = false;
        try {
            cnnct = getConnection();
            String preQueryStatement = "update client set bonus=? where clientid=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setDouble(1, bonus);
            pStmnt.setString(2, custID);
            int rowCount = pStmnt.executeUpdate();
            if (rowCount >= 1) {
                isSuccess = true;
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return isSuccess;
    }

    public ArrayList<OrdersBean> queryOrdersByDate(Timestamp start, Timestamp end) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null, pst = null;
        OrdersBean ob = null;
        ArrayList<OrdersBean> al = new ArrayList();
        OrderItemBean oib = null;
        ArrayList<OrderItemBean> ol = new ArrayList();
        try {
            cnnct = getConnection();
            String preQueryStatement = "select * from orders where orderDatetime between ? and ?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setTimestamp(1, start);
            pStmnt.setTimestamp(2, end);
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            while (rs.next()) {
                ob = new OrdersBean();

                ob.setOrderID(rs.getString("orderID"));
                ob.setClientID(rs.getString("clientID"));
                ob.setAmount(rs.getDouble("amount"));
                ob.setOrderDateTime(rs.getTimestamp("orderDateTime"));
                ob.setPickupDateTime(rs.getDate("pickupDateTime"));
                ob.setType(rs.getString("type"));
                ob.setAddress(rs.getString("address"));
                ob.setStatus(rs.getString("status"));
                ClientBean cb = queryCustomerByID(rs.getString("clientID"));
                ob.setClient(cb);

                String querySt = "select * from orderitem where orderid=?";
                pst = cnnct.prepareStatement(querySt);
                pst.setString(1, rs.getString("orderID"));
                ResultSet rsItem = null;
                rsItem = pst.executeQuery();
                while (rsItem.next()) {
                    oib = new OrderItemBean();
                    oib.setOrderItemID(rsItem.getString("orderItemID"));
                    oib.setOrderID(rsItem.getString("orderID"));
                    oib.setCategoryID(rsItem.getString("categoryID"));
                    oib.setName(rsItem.getString("name"));
                    oib.setPrice(rsItem.getDouble("Price"));
                    oib.setDesigner(rsItem.getString("Designer"));
                    oib.setDescription(rsItem.getString("Description"));
                    oib.setQuantity(rsItem.getInt("quantity"));
                    oib.setImage(rsItem.getString("image"));
                    ol.add(oib);
                }
                ob.setItem(ol);
                al.add(ob);
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return al;
    }

    public ArrayList<OrdersBean> queryOrdersByStatus(String status) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null, pst = null;
        OrdersBean ob = null;
        ArrayList<OrdersBean> al = new ArrayList();
        OrderItemBean oib = null;
        ArrayList<OrderItemBean> ol = new ArrayList();
        try {
            cnnct = getConnection();
            String preQueryStatement = "select * from orders where status=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, status);
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            while (rs.next()) {
                ob = new OrdersBean();

                ob.setOrderID(rs.getString("orderID"));
                ob.setClientID(rs.getString("clientID"));
                ob.setAmount(rs.getDouble("amount"));
                ob.setOrderDateTime(rs.getTimestamp("orderDateTime"));
                ob.setPickupDateTime(rs.getDate("pickupDateTime"));
                ob.setType(rs.getString("type"));
                ob.setAddress(rs.getString("address"));
                ob.setStatus(rs.getString("status"));
                ClientBean cb = queryCustomerByID(rs.getString("clientID"));
                ob.setClient(cb);

                String querySt = "select * from orderitem where orderid=?";
                pst = cnnct.prepareStatement(querySt);
                pst.setString(1, rs.getString("orderID"));
                ResultSet rsItem = null;
                rsItem = pst.executeQuery();
                while (rsItem.next()) {
                    oib = new OrderItemBean();
                    oib.setOrderItemID(rsItem.getString("orderItemID"));
                    oib.setOrderID(rsItem.getString("orderID"));
                    oib.setCategoryID(rsItem.getString("categoryID"));
                    oib.setName(rsItem.getString("name"));
                    oib.setPrice(rsItem.getDouble("Price"));
                    oib.setDesigner(rsItem.getString("Designer"));
                    oib.setDescription(rsItem.getString("Description"));
                    oib.setQuantity(rsItem.getInt("quantity"));
                    oib.setImage(rsItem.getString("image"));
                    ol.add(oib);
                }
                ob.setItem(ol);
                al.add(ob);
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return al;
    }

    public ArrayList<OrdersBean> queryOrdersByStatus(String custID, String status) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null, pst = null;
        OrdersBean ob = null;
        ArrayList<OrdersBean> al = new ArrayList();
        OrderItemBean oib = null;
        ArrayList<OrderItemBean> ol = new ArrayList();
        try {
            cnnct = getConnection();
            String preQueryStatement = "select * from orders where clientid=? and status=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, custID);
            pStmnt.setString(2, status);
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            while (rs.next()) {
                ob = new OrdersBean();

                ob.setOrderID(rs.getString("orderID"));
                ob.setClientID(rs.getString("clientID"));
                ob.setAmount(rs.getDouble("amount"));
                ob.setOrderDateTime(rs.getTimestamp("orderDateTime"));
                ob.setPickupDateTime(rs.getDate("pickupDateTime"));
                ob.setType(rs.getString("type"));
                ob.setAddress(rs.getString("address"));
                ob.setStatus(rs.getString("status"));
                ClientBean cb = queryCustomerByID(rs.getString("clientID"));
                ob.setClient(cb);

                String querySt = "select * from orderitem where orderid=?";
                pst = cnnct.prepareStatement(querySt);
                pst.setString(1, rs.getString("orderID"));
                ResultSet rsItem = null;
                rsItem = pst.executeQuery();
                while (rsItem.next()) {
                    oib = new OrderItemBean();
                    oib.setOrderItemID(rsItem.getString("orderItemID"));
                    oib.setOrderID(rsItem.getString("orderID"));
                    oib.setCategoryID(rsItem.getString("categoryID"));
                    oib.setName(rsItem.getString("name"));
                    oib.setPrice(rsItem.getDouble("Price"));
                    oib.setDesigner(rsItem.getString("Designer"));
                    oib.setDescription(rsItem.getString("Description"));
                    oib.setQuantity(rsItem.getInt("quantity"));
                    oib.setImage(rsItem.getString("image"));
                    ol.add(oib);
                }
                ob.setItem(ol);
                al.add(ob);
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return al;
    }

    public ArrayList<OrdersBean> queryOrdersAvailable() {
        Connection cnnct = null;
        PreparedStatement pStmnt = null, pst = null;
        OrdersBean ob = null;
        ArrayList<OrdersBean> al = new ArrayList();
        OrderItemBean oib = null;
        ArrayList<OrderItemBean> ol = new ArrayList();
        try {
            cnnct = getConnection();
            String preQueryStatement = "select * from orders where status=? or status=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, "waiting");
            pStmnt.setString(2, "available pick up");
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            while (rs.next()) {
                ob = new OrdersBean();

                ob.setOrderID(rs.getString("orderID"));
                ob.setClientID(rs.getString("clientID"));
                ob.setAmount(rs.getDouble("amount"));
                ob.setOrderDateTime(rs.getTimestamp("orderDateTime"));
                ob.setPickupDateTime(rs.getDate("pickupDateTime"));
                ob.setType(rs.getString("type"));
                ob.setAddress(rs.getString("address"));
                ob.setStatus(rs.getString("status"));
                ClientBean cb = queryCustomerByID(rs.getString("clientID"));
                ob.setClient(cb);

                String querySt = "select * from orderitem where orderid=?";
                pst = cnnct.prepareStatement(querySt);
                pst.setString(1, rs.getString("orderID"));
                ResultSet rsItem = null;
                rsItem = pst.executeQuery();
                while (rsItem.next()) {
                    oib = new OrderItemBean();
                    oib.setOrderItemID(rsItem.getString("orderItemID"));
                    oib.setOrderID(rsItem.getString("orderID"));
                    oib.setCategoryID(rsItem.getString("categoryID"));
                    oib.setName(rsItem.getString("name"));
                    oib.setPrice(rsItem.getDouble("Price"));
                    oib.setDesigner(rsItem.getString("Designer"));
                    oib.setDescription(rsItem.getString("Description"));
                    oib.setQuantity(rsItem.getInt("quantity"));
                    oib.setImage(rsItem.getString("image"));
                    ol.add(oib);
                }
                ob.setItem(ol);
                al.add(ob);
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return al;
    }

    public ArrayList<OrdersBean> queryOrdersAvailable(String custID) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null, pst = null;
        OrdersBean ob = null;
        ArrayList<OrdersBean> al = new ArrayList();
        OrderItemBean oib = null;
        ArrayList<OrderItemBean> ol = new ArrayList();
        try {
            cnnct = getConnection();
            String preQueryStatement = "select * from orders where clientid=? and (status=? or status=?)";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, custID);
            pStmnt.setString(2, "waiting");
            pStmnt.setString(3, "available pick up");
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            while (rs.next()) {
                ob = new OrdersBean();

                ob.setOrderID(rs.getString("orderID"));
                ob.setClientID(rs.getString("clientID"));
                ob.setAmount(rs.getDouble("amount"));
                ob.setOrderDateTime(rs.getTimestamp("orderDateTime"));
                ob.setPickupDateTime(rs.getDate("pickupDateTime"));
                ob.setType(rs.getString("type"));
                ob.setAddress(rs.getString("address"));
                ob.setStatus(rs.getString("status"));
                ClientBean cb = queryCustomerByID(rs.getString("clientID"));
                ob.setClient(cb);

                String querySt = "select * from orderitem where orderid=?";
                pst = cnnct.prepareStatement(querySt);
                pst.setString(1, rs.getString("orderID"));
                ResultSet rsItem = null;
                rsItem = pst.executeQuery();
                while (rsItem.next()) {
                    oib = new OrderItemBean();
                    oib.setOrderItemID(rsItem.getString("orderItemID"));
                    oib.setOrderID(rsItem.getString("orderID"));
                    oib.setCategoryID(rsItem.getString("categoryID"));
                    oib.setName(rsItem.getString("name"));
                    oib.setPrice(rsItem.getDouble("Price"));
                    oib.setDesigner(rsItem.getString("Designer"));
                    oib.setDescription(rsItem.getString("Description"));
                    oib.setQuantity(rsItem.getInt("quantity"));
                    oib.setImage(rsItem.getString("image"));
                    ol.add(oib);
                }
                ob.setItem(ol);
                al.add(ob);
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return al;
    }

    public ArrayList<RedemptionBean> queryRedeemByDate(Timestamp start, Timestamp end) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        RedemptionBean rb = null;
        ArrayList<RedemptionBean> al = new ArrayList();
        try {
            cnnct = getConnection();
            String preQueryStatement = "select * from Redemption where redeemedDateTime between ? and ?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setTimestamp(1, start);
            pStmnt.setTimestamp(2, end);
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            while (rs.next()) {
                rb = new RedemptionBean();
                rb.setRedemptionID(rs.getString("RedemptionID"));
                rb.setClientID(rs.getString("clientid"));
                rb.setName(rs.getString("name"));
                rb.setDesigner(rs.getString("designer"));
                rb.setDescription(rs.getString("description"));
                rb.setRedeemedDateTime(rs.getTimestamp("RedeemedDateTime"));
                rb.setQuantity(rs.getInt("quantity"));
                rb.setBonus(rs.getDouble("bonus"));
                rb.setImage(rs.getString("image"));
                rb.setPickupDateTime(rs.getDate("pickupDateTime"));
                rb.setType(rs.getString("type"));
                rb.setAddress(rs.getString("address"));
                rb.setStatus(rs.getString("status"));
                ClientBean cb = queryCustomerByID(rs.getString("clientid"));
                rb.setClient(cb);
                al.add(rb);
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return al;
    }

    public ArrayList<RedemptionBean> queryRedeemByStatus(String status) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        RedemptionBean rb = null;
        ArrayList<RedemptionBean> al = new ArrayList();
        try {
            cnnct = getConnection();
            String preQueryStatement = "select * from Redemption where status=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, status);
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            while (rs.next()) {
                rb = new RedemptionBean();
                rb.setRedemptionID(rs.getString("RedemptionID"));
                rb.setClientID(rs.getString("clientid"));
                rb.setName(rs.getString("name"));
                rb.setDesigner(rs.getString("designer"));
                rb.setDescription(rs.getString("description"));
                rb.setRedeemedDateTime(rs.getTimestamp("RedeemedDateTime"));
                rb.setQuantity(rs.getInt("quantity"));
                rb.setBonus(rs.getDouble("bonus"));
                rb.setImage(rs.getString("image"));
                rb.setPickupDateTime(rs.getDate("pickupDateTime"));
                rb.setType(rs.getString("type"));
                rb.setAddress(rs.getString("address"));
                rb.setStatus(rs.getString("status"));
                ClientBean cb = queryCustomerByID(rs.getString("clientid"));
                rb.setClient(cb);
                al.add(rb);
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return al;
    }

    public ArrayList<RedemptionBean> queryRedeemByStatus(String custID, String status) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        RedemptionBean rb = null;
        ArrayList<RedemptionBean> al = new ArrayList();
        try {
            cnnct = getConnection();
            String preQueryStatement = "select * from Redemption where clientid=? and status=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, custID);
            pStmnt.setString(2, status);
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            while (rs.next()) {
                rb = new RedemptionBean();
                rb.setRedemptionID(rs.getString("RedemptionID"));
                rb.setClientID(rs.getString("clientid"));
                rb.setName(rs.getString("name"));
                rb.setDesigner(rs.getString("designer"));
                rb.setDescription(rs.getString("description"));
                rb.setRedeemedDateTime(rs.getTimestamp("RedeemedDateTime"));
                rb.setQuantity(rs.getInt("quantity"));
                rb.setBonus(rs.getDouble("bonus"));
                rb.setImage(rs.getString("image"));
                rb.setPickupDateTime(rs.getDate("pickupDateTime"));
                rb.setType(rs.getString("type"));
                rb.setAddress(rs.getString("address"));
                rb.setStatus(rs.getString("status"));
                ClientBean cb = queryCustomerByID(rs.getString("clientid"));
                rb.setClient(cb);
                al.add(rb);
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return al;
    }

    public ArrayList<RedemptionBean> queryRedeemAvailable() {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        RedemptionBean rb = null;
        ArrayList<RedemptionBean> al = new ArrayList();
        try {
            cnnct = getConnection();
            String preQueryStatement = "select * from Redemption where status=? or status=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, "waiting");
            pStmnt.setString(2, "available pick up");
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            while (rs.next()) {
                rb = new RedemptionBean();
                rb.setRedemptionID(rs.getString("RedemptionID"));
                rb.setClientID(rs.getString("clientid"));
                rb.setName(rs.getString("name"));
                rb.setDesigner(rs.getString("designer"));
                rb.setDescription(rs.getString("description"));
                rb.setRedeemedDateTime(rs.getTimestamp("RedeemedDateTime"));
                rb.setQuantity(rs.getInt("quantity"));
                rb.setBonus(rs.getDouble("bonus"));
                rb.setImage(rs.getString("image"));
                rb.setPickupDateTime(rs.getDate("pickupDateTime"));
                rb.setType(rs.getString("type"));
                rb.setAddress(rs.getString("address"));
                rb.setStatus(rs.getString("status"));
                ClientBean cb = queryCustomerByID(rs.getString("clientid"));
                rb.setClient(cb);
                al.add(rb);
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return al;
    }

    public ArrayList<RedemptionBean> queryRedeemAvailable(String custID) {
        Connection cnnct = null;
        PreparedStatement pStmnt = null;
        RedemptionBean rb = null;
        ArrayList<RedemptionBean> al = new ArrayList();
        try {
            cnnct = getConnection();
            String preQueryStatement = "select * from Redemption where clientid=? and (status=? or status=?)";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, custID);
            pStmnt.setString(2, "waiting");
            pStmnt.setString(3, "available pick up");
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            while (rs.next()) {
                rb = new RedemptionBean();
                rb.setRedemptionID(rs.getString("RedemptionID"));
                rb.setClientID(rs.getString("clientid"));
                rb.setName(rs.getString("name"));
                rb.setDesigner(rs.getString("designer"));
                rb.setDescription(rs.getString("description"));
                rb.setRedeemedDateTime(rs.getTimestamp("RedeemedDateTime"));
                rb.setQuantity(rs.getInt("quantity"));
                rb.setBonus(rs.getDouble("bonus"));
                rb.setImage(rs.getString("image"));
                rb.setPickupDateTime(rs.getDate("pickupDateTime"));
                rb.setType(rs.getString("type"));
                rb.setAddress(rs.getString("address"));
                rb.setStatus(rs.getString("status"));
                ClientBean cb = queryCustomerByID(rs.getString("clientid"));
                rb.setClient(cb);
                al.add(rb);
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return al;
    }

    public ArrayList<OrdersBean> queryCancelOrders() {
        Connection cnnct = null;
        PreparedStatement pStmnt = null, pst = null;
        OrdersBean ob = null;
        ArrayList<OrdersBean> al = new ArrayList();
        OrderItemBean oib = null;
        ArrayList<OrderItemBean> ol = new ArrayList();
        String status = "cancel";
        try {
            cnnct = getConnection();
            String preQueryStatement = "select * from orders where status=?";
            pStmnt = cnnct.prepareStatement(preQueryStatement);
            pStmnt.setString(1, "canceled");
            ResultSet rs = null;
            rs = pStmnt.executeQuery();
            while (rs.next()) {
                ob = new OrdersBean();

                ob.setOrderID(rs.getString("orderID"));
                ob.setClientID(rs.getString("clientID"));
                ob.setAmount(rs.getDouble("amount"));
                ob.setOrderDateTime(rs.getTimestamp("orderDateTime"));
                ob.setPickupDateTime(rs.getDate("pickupDateTime"));
                ob.setType(rs.getString("type"));
                ob.setAddress(rs.getString("address"));
                ob.setStatus(rs.getString("status"));

                String querySt = "select * from orderitem where orderid=?";
                pst = cnnct.prepareStatement(querySt);
                pst.setString(1, rs.getString("orderID"));
                ResultSet rsItem = null;
                rsItem = pst.executeQuery();
                while (rsItem.next()) {
                    oib = new OrderItemBean();
                    oib.setOrderItemID(rsItem.getString("orderItemID"));
                    oib.setOrderID(rsItem.getString("orderID"));
                    oib.setCategoryID(rsItem.getString("categoryID"));
                    oib.setName(rsItem.getString("name"));
                    oib.setPrice(rsItem.getDouble("Price"));
                    oib.setDesigner(rsItem.getString("Designer"));
                    oib.setDescription(rsItem.getString("Description"));
                    oib.setQuantity(rsItem.getInt("quantity"));
                    oib.setImage(rsItem.getString("image"));
                    ol.add(oib);
                }
                ob.setItem(ol);
                al.add(ob);
            }
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return al;
    }

    public void createManagerTable() {
        Connection cnnct = null;
        Statement stmnt = null;
        try {
            cnnct = getConnection();
            stmnt = cnnct.createStatement();
            String sql = "create table manager(\n"
                    + "	managerID varchar(255) not null primary key,\n"
                    + "	password varchar(255) not null,\n"
                    + "	name varchar(255) not null\n"
                    + ")";
            stmnt.execute(sql);
            stmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void createClientTable() {
        Connection cnnct = null;
        Statement stmnt = null;
        try {
            cnnct = getConnection();
            stmnt = cnnct.createStatement();
            String sql = "create table client(\n"
                    + "	clientID varchar(255) not null primary key,\n"
                    + "	loginID varchar(255),\n"
                    + "	password varchar(255),\n"
                    + "	name varchar(255) not null,\n"
                    + "	address varchar(255) not null,\n"
                    + "	telephone varchar(255) not null,\n"
                    + "	email varchar(255) not null,\n"
                    + "	amount double not null default 0,\n"
                    + "	bonus double not null default 0,\n"
                    + "	confirm BOOLEAN not null default false\n"
                    + ")";
            stmnt.execute(sql);
            stmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void createCategoryTable() {
        Connection cnnct = null;
        Statement stmnt = null;
        try {
            cnnct = getConnection();
            stmnt = cnnct.createStatement();
            String sql = "create table category(\n"
                    + "	categoryID varchar(255) not null primary key,\n"
                    + "	category varchar(255) not null\n"
                    + ")";
            stmnt.execute(sql);
            stmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void createItemTable() {
        Connection cnnct = null;
        Statement stmnt = null;
        try {
            cnnct = getConnection();
            stmnt = cnnct.createStatement();
            String sql = "create table item(\n"
                    + "	itemID  varchar(255) not null primary key,\n"
                    + "	categoryID  varchar(255) not null references category(categoryID),\n"
                    + "	name varchar(255) not null,\n"
                    + "	price double not null,\n"
                    + "	designer varchar(255) not null,\n"
                    + "	description varchar(255) not null,\n"
                    + "	quantity int not null,\n"
                    + "	image varchar(255) not null\n"
                    + ")";
            stmnt.execute(sql);
            stmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void createOrdersTable() {
        Connection cnnct = null;
        Statement stmnt = null;
        try {
            cnnct = getConnection();
            stmnt = cnnct.createStatement();
            String sql = "create table orders(\n"
                    + "	orderID varchar(255) not null primary key,\n"
                    + "	clientID varchar(255) not null references client(clientID),\n"
                    + "	amount double not null,\n"
                    + "	orderDateTime TIMESTAMP not null,\n"
                    + "	pickupDateTime Date,\n"
                    + "	type varchar(255) not null,\n"
                    + "	address varchar(255) not null,\n"
                    + "	status varchar(255) not null\n"
                    + ")";
            stmnt.execute(sql);
            stmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void createOrderItemTable() {
        Connection cnnct = null;
        Statement stmnt = null;
        try {
            cnnct = getConnection();
            stmnt = cnnct.createStatement();
            String sql = "create table orderItem(\n"
                    + "	orderItemID varchar(255) not null primary key,\n"
                    + "	orderID varchar(255) not null references orders(orderID),\n"
                    + "	categoryID varchar(255) not null references category(categoryID),\n"
                    + "	name varchar(255) not null,\n"
                    + "	designer varchar(255) not null,\n"
                    + "	description varchar(255) not null,\n"
                    + "	price double not null,\n"
                    + "	image varchar(255) not null,\n"
                    + "	quantity int not null\n"
                    + ")";
            stmnt.execute(sql);
            stmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void createAccessoryTable() {
        Connection cnnct = null;
        Statement stmnt = null;
        try {
            cnnct = getConnection();
            stmnt = cnnct.createStatement();
            String sql = "create table Accessory(\n"
                    + "	accessoryID varchar(255) not null primary key,\n"
                    + "	bonus double not null,\n"
                    + "	name varchar(255) not null,\n"
                    + "	designer varchar(255) not null,\n"
                    + "	description varchar(255) not null,\n"
                    + "	image varchar(255) not null,\n"
                    + "	quantity int not null\n"
                    + ")";
            stmnt.execute(sql);
            stmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void createRedemptionTable() {
        Connection cnnct = null;
        Statement stmnt = null;
        try {
            cnnct = getConnection();
            stmnt = cnnct.createStatement();
            String sql = "create table redemption(\n"
                    + "	redemptionID varchar(255) not null primary key,\n"
                    + "	clientID varchar(255) not null references client(ClientID),\n"
                    + "	name varchar(255) not null,\n"
                    + "	designer varchar(255) not null,\n"
                    + "	description varchar(255) not null,\n"
                    + "	redeemedDateTime Timestamp not null,\n"
                    + "	quantity int not null,\n"
                    + "	image varchar(255) not null,\n"
                    + "	bonus double not null,\n"
                    + "	pickupDateTime DATE,\n"
                    + "	type varchar(255) not null,\n"
                    + "	address varchar(255) not null,\n"
                    + "	status varchar(255) not null\n"
                    + ")";
            stmnt.execute(sql);
            stmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public boolean dropManagerTable() {
        Connection cnnct = null;
        Statement pStmnt = null;
        boolean isSuccess = false;
        try {
            cnnct = getConnection();
            String preQueryStatement = "drop table manager";
            pStmnt = cnnct.createStatement();
            pStmnt.executeUpdate(preQueryStatement);
            isSuccess = true;
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return isSuccess;
    }

    public boolean dropClientTable() {
        Connection cnnct = null;
        Statement pStmnt = null;
        boolean isSuccess = false;
        try {
            cnnct = getConnection();
            String preQueryStatement = "drop table client";
            pStmnt = cnnct.createStatement();
            pStmnt.executeUpdate(preQueryStatement);
            isSuccess = true;
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return isSuccess;
    }

    public boolean dropCategoryTable() {
        Connection cnnct = null;
        Statement pStmnt = null;
        boolean isSuccess = false;
        try {
            cnnct = getConnection();
            String preQueryStatement = "drop table category";
            pStmnt = cnnct.createStatement();
            pStmnt.executeUpdate(preQueryStatement);
            isSuccess = true;
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return isSuccess;
    }

    public boolean dropItemTable() {
        Connection cnnct = null;
        Statement pStmnt = null;
        boolean isSuccess = false;
        try {
            cnnct = getConnection();
            String preQueryStatement = "drop table item";
            pStmnt = cnnct.createStatement();
            pStmnt.executeUpdate(preQueryStatement);
            isSuccess = true;
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return isSuccess;
    }

    public boolean dropOrdersTable() {
        Connection cnnct = null;
        Statement pStmnt = null;
        boolean isSuccess = false;
        try {
            cnnct = getConnection();
            String preQueryStatement = "drop table orders";
            pStmnt = cnnct.createStatement();
            pStmnt.executeUpdate(preQueryStatement);
            isSuccess = true;
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return isSuccess;
    }

    public boolean dropOrderItemTable() {
        Connection cnnct = null;
        Statement pStmnt = null;
        boolean isSuccess = false;
        try {
            cnnct = getConnection();
            String preQueryStatement = "drop table orderItem";
            pStmnt = cnnct.createStatement();
            pStmnt.executeUpdate(preQueryStatement);
            isSuccess = true;
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return isSuccess;
    }

    public boolean dropAccessoryTable() {
        Connection cnnct = null;
        Statement pStmnt = null;
        boolean isSuccess = false;
        try {
            cnnct = getConnection();
            String preQueryStatement = "drop table accessory";
            pStmnt = cnnct.createStatement();
            pStmnt.executeUpdate(preQueryStatement);
            isSuccess = true;
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return isSuccess;
    }

    public boolean dropRedemptionTable() {
        Connection cnnct = null;
        Statement pStmnt = null;
        boolean isSuccess = false;
        try {
            cnnct = getConnection();
            String preQueryStatement = "drop table redemption";
            pStmnt = cnnct.createStatement();
            pStmnt.executeUpdate(preQueryStatement);
            isSuccess = true;
            pStmnt.close();
            cnnct.close();
        } catch (SQLException ex) {
            while (ex != null) {
                ex.printStackTrace();
                ex = ex.getNextException();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return isSuccess;
    }

//
//    public boolean addRecord(String CustId, String pwd, String name, String address, String tel, String email, double amount, double bonus) {
//        Connection cnnct = null;
//        PreparedStatement pStmnt = null;
//        boolean isSuccess = false;
//        try {
//            cnnct = getConnection();
//            String preQueryStatement = "insert into client(clientid,password,name,address,telephone,email,amount,bonus) values (?,?,?,?,?,?,?,?)";
//            pStmnt = cnnct.prepareStatement(preQueryStatement);
//            pStmnt.setString(1, CustId);
//            pStmnt.setString(2, pwd);
//            pStmnt.setString(3, name);
//            pStmnt.setString(4, address);
//            pStmnt.setString(5, tel);
//            pStmnt.setString(6, email);
//            pStmnt.setDouble(7, amount);
//            pStmnt.setDouble(8, bonus);
//            int rowCount = pStmnt.executeUpdate();
//            if (rowCount >= 1) {
//                isSuccess = true;
//            }
//            pStmnt.close();
//            cnnct.close();
//        } catch (SQLException ex) {
//            while (ex != null) {
//                ex.printStackTrace();
//                ex = ex.getNextException();
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        return isSuccess;
//    }
//
//    public CustomerBean queryCustByID(String id) {
//        Connection cnnct = null;
//        PreparedStatement pStmnt = null;
//        CustomerBean cb = null;
//        try {
//            cnnct = getConnection();
//            String preQueryStatement = "select * from customer where custid=?";
//            pStmnt = cnnct.prepareStatement(preQueryStatement);
//            pStmnt.setString(1, id);
//            ResultSet rs = null;
//            rs = pStmnt.executeQuery();
//            if (rs.next()) {
//                cb = new CustomerBean();
//                cb.setCustID(rs.getString("clientID"));
//                cb.setPassword(rs.getString("password"));
//                cb.setName(rs.getString("name"));
//                cb.setAddress(rs.getInt("address"));
//                cb.setTelephone(rs.getInt("telephone"));
//                cb.setEmail(rs.getString("email"));
//                cb.setAmount(rs.getInt("Age"));
//                cb.setBonus(rs.getInt("bonus"));
//            }
//            pStmnt.close();
//            cnnct.close();
//        } catch (SQLException ex) {
//            while (ex != null) {
//                ex.printStackTrace();
//                ex = ex.getNextException();
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        return cb;
//    }
//
//    public ArrayList queryCustByName(String name) {
//        Connection cnnct = null;
//        PreparedStatement pStmnt = null;
//        CustomerBean cb = null;
//        ArrayList al = new ArrayList();
//        try {
//            cnnct = getConnection();
//            String preQueryStatement = "select * from customer where name=?";
//            pStmnt = cnnct.prepareStatement(preQueryStatement);
//            pStmnt.setString(1, name);
//            ResultSet rs = null;
//            rs = pStmnt.executeQuery();
//            while (rs.next()) {
//                cb = new CustomerBean();
//                cb.setCustID(rs.getString("clientID"));
//                cb.setPassword(rs.getString("password"));
//                cb.setName(rs.getString("name"));
//                cb.setAddress(rs.getInt("address"));
//                cb.setTelephone(rs.getInt("telephone"));
//                cb.setEmail(rs.getString("email"));
//                cb.setAmount(rs.getInt("Age"));
//                cb.setBonus(rs.getInt("bonus"));
//                cb.setConfirm(rs.getBoolean("confirm"));
//                al.add(cb);
//            }
//            pStmnt.close();
//            cnnct.close();
//        } catch (SQLException ex) {
//            while (ex != null) {
//                ex.printStackTrace();
//                ex = ex.getNextException();
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        return al;
//    }
//
//    public ArrayList queryCustByTel(String name) {
//        Connection cnnct = null;
//        PreparedStatement pStmnt = null;
//        CustomerBean cb = null;
//        ArrayList al = new ArrayList();
//        try {
//            cnnct = getConnection();
//            String preQueryStatement = "select * from customer where tel=?";
//            pStmnt = cnnct.prepareStatement(preQueryStatement);
//            pStmnt.setString(1, name);
//            ResultSet rs = null;
//            rs = pStmnt.executeQuery();
//            while (rs.next()) {
//                cb = new CustomerBean();
//                cb.setCustID(rs.getString("CustID"));
//                cb.setName(rs.getString("Name"));
//                cb.setTel(rs.getString("Tel"));
//                cb.setAge(rs.getInt("Age"));
//                al.add(cb);
//            }
//            pStmnt.close();
//            cnnct.close();
//        } catch (SQLException ex) {
//            while (ex != null) {
//                ex.printStackTrace();
//                ex = ex.getNextException();
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        return al;
//    }
//
//    public ArrayList queryCust() {
//        Connection cnnct = null;
//        PreparedStatement pStmnt = null;
//        CustomerBean cb = null;
//        ArrayList al = new ArrayList();
//        try {
//            cnnct = getConnection();
//            String preQueryStatement = "select * from customer ";
//            pStmnt = cnnct.prepareStatement(preQueryStatement);
//            ResultSet rs = null;
//            rs = pStmnt.executeQuery();
//            while (rs.next()) {
//                cb = new CustomerBean();
//                cb.setCustID(rs.getString("CustID"));
//                cb.setName(rs.getString("Name"));
//                cb.setTel(rs.getString("Tel"));
//                cb.setAge(rs.getInt("Age"));
//                al.add(cb);
//            }
//            pStmnt.close();
//            cnnct.close();
//        } catch (SQLException ex) {
//            while (ex != null) {
//                ex.printStackTrace();
//                ex = ex.getNextException();
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        return al;
//    }
//
//    public boolean delRecord(String custId) {
//        Connection cnnct = null;
//        PreparedStatement pStmnt = null;
//        CustomerBean cb = null;
//        boolean isSuccess = false;
//        try {
//            cnnct = getConnection();
//            String preQueryStatement = "delete from customer where custid=? ";
//            pStmnt = cnnct.prepareStatement(preQueryStatement);
//            pStmnt.setString(1, custId);
//            int rowCount = pStmnt.executeUpdate();
//            if (rowCount >= 1) {
//                isSuccess = true;
//            }
//            pStmnt.close();
//            cnnct.close();
//        } catch (SQLException ex) {
//            while (ex != null) {
//                ex.printStackTrace();
//                ex = ex.getNextException();
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        return isSuccess;
//    }
//
//    public boolean editRecord(CustomerBean cb) {
//        Connection cnnct = null;
//        PreparedStatement pStmnt = null;
//        boolean isSuccess = false;
//        try {
//            cnnct = getConnection();
//            String preQueryStatement = "update customer set name=? , tel=? , age=? where custid=?";
//            pStmnt = cnnct.prepareStatement(preQueryStatement);
//            pStmnt.setString(1, cb.getName());
//            pStmnt.setString(2, cb.getTel());
//            pStmnt.setInt(3, cb.getAge());
//            pStmnt.setString(4, cb.getCustID());
//            int rowCount = pStmnt.executeUpdate();
//            if (rowCount >= 1) {
//                isSuccess = true;
//            }
//            pStmnt.close();
//            cnnct.close();
//        } catch (SQLException ex) {
//            while (ex != null) {
//                ex.printStackTrace();
//                ex = ex.getNextException();
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        return isSuccess;
//    }
//
//    public boolean dropCustTable() {
//        Connection cnnct = null;
//        PreparedStatement pStmnt = null;
//        boolean isSuccess = false;
//        try {
//            cnnct = getConnection();
//            String preQueryStatement = "drop table customer";
//            pStmnt = cnnct.prepareStatement(preQueryStatement);
//            int rowCount = pStmnt.executeUpdate();
//            if (rowCount >= 1) {
//                isSuccess = true;
//            }
//            pStmnt.close();
//            cnnct.close();
//        } catch (SQLException ex) {
//            while (ex != null) {
//                ex.printStackTrace();
//                ex = ex.getNextException();
//            }
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        return isSuccess;
//    }
}
