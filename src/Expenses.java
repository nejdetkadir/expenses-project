/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author nejdetkadirr
 */
public class Expenses {
    
    String id;
    String date;
    String category;
    String price;

    public Expenses(String id, String date, String category, String price) {
        this.id = id;
        this.date = date;
        this.category = category;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public String getPrice() {
        return price;
    }
    
}
