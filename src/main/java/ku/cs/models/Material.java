package ku.cs.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Material {
    private String category;
    private String name;
    private int amount;
    private String number;
    private String date;
    private String username;

    public Material(String name,String category, String amount, String date){
        this.name = name;
        this.category = category;
        this.amount = Integer.parseInt(amount);
        this.date = date;
    }
    public Material(String name,String category, String amount){
        this.name = name;
        this.category = category;
        this.amount = Integer.parseInt(amount);
        initialTime();
    }
    public void initialTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        this.date = now.format(format);
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public int getAmount() {
        return amount;
    }

    public String getNumber() {return number; }

    public String getDate() {return date; }
    public void  addAmount(int n){
        amount+=n;
    }
    public void  lendAmount(int n){
        amount-=n;
    }

    @Override
    public String toString() {
        return  name + " , " + category + " , " + amount;
    }

    public String toCSV() {
        return  name + ',' + category + ',' + amount + ',' + date ;
    }
    public boolean checkMaterial(String name) {
        if (this.name.equals(name))
            return true;
        return false;
    }

}
