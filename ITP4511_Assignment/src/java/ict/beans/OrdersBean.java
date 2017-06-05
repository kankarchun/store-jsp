package ict.beans;

import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;
import java.util.ArrayList;

public class OrdersBean implements Serializable{
    //type
    public static String TYPE_SELF_PICK_UP = "self-pick up";
    public static String TYPE_DELIVERY = "delivery";
    //status
    public static String STATUS_WAITING = "waiting";
    public static String STATUS_CANCELED = "canceled";
    public static String STATUS_DELIVERED = "delivered";
    public static String STATUS_PICKED_UP = "picked-up";
    public static String STATUS_DELAY_PICKED_UP = "delay picked-up"; 
    public static String STATUS_AVAILABLE_PICK_UP = "available pick up";
    //var
    private String orderID,clientID,type,address,status;
    private double amount;
    private Timestamp orderDateTime;
    private Date pickupDateTime;
    private ArrayList<OrderItemBean> item;
    private ClientBean client;
    
    public OrdersBean() {
    }

    public ClientBean getClient() {
        return client;
    }

    public void setClient(ClientBean client) {
        this.client = client;
    }

    public Date getPickupDateTime() {
        return pickupDateTime;
    }

    public void setPickupDateTime(Date pickupDateTime) {
        this.pickupDateTime = pickupDateTime;
    }

    public ArrayList<OrderItemBean> getItem() {
        return item;
    }

    public void setItem(ArrayList<OrderItemBean> item) {
        this.item = item;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Timestamp getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(Timestamp orderDateTime) {
        this.orderDateTime = orderDateTime;
    }
    
}
