
package ict.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class ReportBean implements Serializable{
    int month,year,totalOrder,incomplete;
    ArrayList<OrdersBean> content;

    public ReportBean(){
        
    }
    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getTotalOrder() {
        return totalOrder;
    }

    public void setTotalOrder(int totalOrder) {
        this.totalOrder = totalOrder;
    }

    public int getIncomplete() {
        return incomplete;
    }

    public void setIncomplete(int incomplete) {
        this.incomplete = incomplete;
    }

    public ArrayList<OrdersBean> getContent() {
        return content;
    }

    public void setContent(ArrayList<OrdersBean> content) {
        this.content = content;
    }
    
}
