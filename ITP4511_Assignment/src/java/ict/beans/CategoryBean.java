/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ict.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author 1
 */
public class CategoryBean implements Serializable{
    
    private String categoryID,category;
    private ArrayList<ItemBean> item;
    
    public CategoryBean() {
    }

    public ArrayList<ItemBean> getItem() {
        return item;
    }

    public void setItem(ArrayList<ItemBean> item) {
        this.item = item;
    }

    public String getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(String categoryID) {
        this.categoryID = categoryID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    
}
