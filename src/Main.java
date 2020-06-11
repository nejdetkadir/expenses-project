
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author nejdetkadirr
 */
public class Main extends javax.swing.JFrame {

    ArrayList<Expenses> data = new ArrayList<>();
    BufferedReader br;

    /**
     * Creates new form Main
     */
    public Main() {
        initComponents();
        readFile();
        setDataToTable();
        setInfo();
    }

    public void setDataToTable() {
        jTable.getColumnModel().getColumn(0).setHeaderValue(data.get(0).getId());
        jTable.getColumnModel().getColumn(1).setHeaderValue(data.get(0).getDate());
        jTable.getColumnModel().getColumn(2).setHeaderValue(data.get(0).getCategory());
        jTable.getColumnModel().getColumn(3).setHeaderValue(data.get(0).getPrice());

        for (int i = 1; i < data.size(); i++) {
            ((DefaultTableModel) jTable.getModel()).addRow(new Object[]{data.get(i).getId(), data.get(i).getDate(), data.get(i).getCategory(), data.get(i).getPrice()});
        }

    }

    public void readFile() {
        try {
            if (System.getProperty("java.vendor").equals("Ubuntu")) {
                br = new BufferedReader(new FileReader(new File(System.getProperty("user.dir") + "/src/harcamalar.txt")));
            } else {
                br = new BufferedReader(new FileReader(new File(System.getProperty("user.dir") + "\\src\\harcamalar.txt")));
            }
            for (String line; (line = br.readLine()) != null;) {
                String[] items = line.split(";");
                data.add(new Expenses(items[0], items[1], items[2], items[3]));
            }
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(this, ex.getLocalizedMessage());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getLocalizedMessage());
        }
    }
    
    public static HashMap<String, Double> sortByValue(HashMap<String, Double> hm) 
    { 
        // Create a list from elements of HashMap 
        LinkedList<Map.Entry<String, Double> > list = 
               new LinkedList<Map.Entry<String, Double> >(hm.entrySet()); 
  
        // Sort the list 
        Collections.sort(list, new Comparator<Map.Entry<String, Double> >() { 
            public int compare(Map.Entry<String, Double> o1,  
                               Map.Entry<String, Double> o2) 
            { 
                return (o1.getValue()).compareTo(o2.getValue()); 
            } 
        }); 
          
        // put data from sorted list to hashmap  
        HashMap<String, Double> temp = new LinkedHashMap<String, Double>(); 
        for (Map.Entry<String, Double> aa : list) { 
            temp.put(aa.getKey(), aa.getValue()); 
        } 
        return temp; 
    }

    public void setInfo() {
        //Calculate total price
        double totalPrice = 0;
        double weekEndTotal = 0;
        for (int i = 1; i < data.size(); i++) {
            totalPrice += Double.valueOf(data.get(i).getPrice());
            if (data.get(i).isWeekend()) {
                weekEndTotal += Double.valueOf(data.get(i).getPrice());
            }
        }
        if (weekEndTotal > (totalPrice - weekEndTotal)) {
            jLabelWeekendInfo.setText("Haftasonu daha fazla harcama yapılmış");
        } else {
            jLabelWeekendInfo.setText("Haftaiçi daha fazla harcama yapılmış");
        }
        jLabelTotalPrice.setText("Toplam Harcama Tutarı : " + totalPrice);

        //Calculate total prices of categories
        HashMap<String, Double> categories = new HashMap<>();
        HashMap<String, Double> days = new HashMap<>();
        HashMap<String, Double> months = new HashMap<>();
        for (Expenses e : data) {
            //Calculate total prices of every categories
            if (!e.getCategory().equals("KATEGORİ")) {
                double price = Double.valueOf(e.getPrice());
                if (categories.containsKey(e.getCategory())) {                    
                    categories.put(e.getCategory(), categories.get(e.getCategory()) + price);
                } else {
                    categories.put(e.getCategory(), Double.valueOf(e.getPrice()));
                }
            }
            //Calculate total prices of days
            if (!e.getCategory().equals("KATEGORİ")) {
                if (days.containsKey(e.getDay())) {
                    double price = Double.valueOf(e.getPrice());
                    days.put(e.getDay(), days.get(e.getDay()) + price);
                } else {
                    days.put(e.getDay(), Double.valueOf(e.getPrice()));
                }
            }
            //Calculate total prices of months
            if (!e.getCategory().equals("KATEGORİ")) {
                if (months.containsKey(e.getMonth())) {
                    double price = Double.valueOf(e.getPrice());
                    months.put(e.getMonth(), months.get(e.getMonth()) + price);
                } else {
                    months.put(e.getMonth(), Double.valueOf(e.getPrice()));
                }
            }
        }

        for (Map.Entry<String, Double> entry : categories.entrySet()) {
            String key = entry.getKey();
            double value = entry.getValue();
            ((DefaultTableModel) jTable1.getModel()).addRow(new Object[]{key, value});
        }

        for (Map.Entry<String, Double> entry : days.entrySet()) {
            String key = entry.getKey();
            double value = entry.getValue();
            ((DefaultTableModel) jTable2.getModel()).addRow(new Object[]{key, value});
        }
        
        for (Map.Entry<String, Double> entry : months.entrySet()) {
            String key = entry.getKey();
            double value = entry.getValue();
            ((DefaultTableModel) jTable3.getModel()).addRow(new Object[]{key, value});
        }
        
        String maxCatPrice = "";
        Map<String, Double> maxCat = sortByValue(categories);
        for (Map.Entry<String, Double> cat : maxCat.entrySet()) { 
            maxCatPrice = cat.getKey();
        }        
        jLabelMaxTotalPriceCat.setText("En fazlama harcama " + maxCatPrice + " kategorisine ait");
        
        String minMonthPrice = "";
        Map<String, Double> minMonth = sortByValue(months);
        for (Map.Entry<String, Double> month : minMonth.entrySet()) { 
            minMonthPrice = month.getKey();
            break;
        }        
        jLabelMinTotalPriceMon.setText("En az harcama "+ minMonthPrice + " ayında yapılmış");
        

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane = new javax.swing.JScrollPane();
        jTable = new javax.swing.JTable();
        jLabelTotalPrice = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabelMaxTotalPriceCat = new javax.swing.JLabel();
        jLabelMinTotalPriceMon = new javax.swing.JLabel();
        jLabelWeekendInfo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane.setViewportView(jTable);
        if (jTable.getColumnModel().getColumnCount() > 0) {
            jTable.getColumnModel().getColumn(0).setResizable(false);
            jTable.getColumnModel().getColumn(1).setResizable(false);
            jTable.getColumnModel().getColumn(2).setResizable(false);
            jTable.getColumnModel().getColumn(2).setHeaderValue("Title 3");
            jTable.getColumnModel().getColumn(3).setResizable(false);
            jTable.getColumnModel().getColumn(3).setHeaderValue("Title 4");
        }

        jLabelTotalPrice.setText("jLabel1");

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kategori", "Toplam"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
        }

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Kategorilerin Harcama Tutarı");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Günler", "Toplam"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setResizable(false);
            jTable2.getColumnModel().getColumn(1).setResizable(false);
        }

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Günlerin Harcama Tutarı");

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Aylar", "Toplam"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTable3);
        if (jTable3.getColumnModel().getColumnCount() > 0) {
            jTable3.getColumnModel().getColumn(0).setResizable(false);
            jTable3.getColumnModel().getColumn(1).setResizable(false);
        }

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Ayların Harcama Tutarı");

        jLabelMaxTotalPriceCat.setText("jLabel4");

        jLabelMinTotalPriceMon.setText("jLabel5");

        jLabelWeekendInfo.setText("jLabel5");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabelWeekendInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabelTotalPrice, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)
                                        .addComponent(jLabelMinTotalPriceMon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)))
                                    .addComponent(jLabelMaxTotalPriceCat, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 6, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabelTotalPrice)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(jLabelMaxTotalPriceCat)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabelMinTotalPriceMon)
                                .addGap(18, 18, 18)
                                .addComponent(jLabelWeekendInfo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabelMaxTotalPriceCat;
    private javax.swing.JLabel jLabelMinTotalPriceMon;
    private javax.swing.JLabel jLabelTotalPrice;
    private javax.swing.JLabel jLabelWeekendInfo;
    private javax.swing.JScrollPane jScrollPane;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    // End of variables declaration//GEN-END:variables
}
