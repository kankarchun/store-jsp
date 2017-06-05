package ict.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

public class CartBean implements Serializable {

    private HashMap<ItemBean, Integer> cart;

    public CartBean() {
        cart = new HashMap();
    }

    public HashMap<ItemBean, Integer> getCart() {
        return cart;
    }

    public void addItem(ItemBean id) {
        cart.put(id, 1);
    }

    public void setQuality(ItemBean id, int quality) {
        cart.put(id, quality);
    }

    public int getQuality(ItemBean id) {
        return cart.get(id);
    }

    public void removeItem(ItemBean id) {
        cart.remove(id);
    }

    public boolean isEmpty(){
        return cart.isEmpty();
    }
    
    public Set<ItemBean> getItems(){
        return cart.keySet();
    }
    
    public ItemBean contain(String id) {
        for (ItemBean ib : cart.keySet()) {
            if (ib.getItemID().equals(id)) {
                return ib;
            }
        }
        return null;
    }
    
    public int getSubtotal(){
        int total = 0;
        for (ItemBean ib : cart.keySet()) {
            total+=ib.getPrice()*getQuality(ib);
        }
        return total;
    }
}
