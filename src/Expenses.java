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
    
    int id;
    String date;
    String category;
    float price;

    public Expenses(int id, String date, String category, float price) {
        this.id = id;
        this.date = date;
        this.category = category;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    public float getPrice() {
        return price;
    }
    
}
