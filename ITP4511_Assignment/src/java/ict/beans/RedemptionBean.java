/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.beans;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

/**
 *
 * @author 1
 */
public class RedemptionBean implements Serializable{
    //type
    public static String TYPE_SELF_PICK_UP = "self-pick up";
    public static String TYPE_DELIVERY = "delivery";
    //status
    public static String STATUS_WAITING = "waiting";
    public static String STATUS_CANCELED = "canceled";
    public static String STATUS_DELIVERED = "delivered";
    public static String STATUS_PICKED_UP = "picked-up";
    public static String STATUS_DELAY_PICKED_UP = "delay picked-up";    
    //var
    private String redemptionID,clientID,name,designer,description,image,type,address,status;
    private Timestamp redeemedDateTime;
    private int quantity;
    private double bonus;
    private Date pickupDateTime;
    private ClientBean client;
    
    public RedemptionBean() {
    }

    public ClientBean getClient() {
        return client;
    }

    public void setClient(ClientBean client) {
        this.client = client;
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

    public Date getPickupDateTime() {
        return pickupDateTime;
    }

    public void setPickupDateTime(Date pickupDateTime) {
        this.pickupDateTime = pickupDateTime;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRedemptionID() {
        return redemptionID;
    }

    public void setRedemptionID(String redemptionID) {
        this.redemptionID = redemptionID;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesigner() {
        return designer;
    }

    public void setDesigner(String designer) {
        this.designer = designer;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Timestamp getRedeemedDateTime() {
        return redeemedDateTime;
    }

    public void setRedeemedDateTime(Timestamp redeemedDateTime) {
        this.redeemedDateTime = redeemedDateTime;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getBonus() {
        return bonus;
    }

    public void setBonus(double bonus) {
        this.bonus = bonus;
    }
    
}
