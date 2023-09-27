package ku.cs.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AddMaterial {
    private String category;
    private String name;
    private String amount;
    private String number;
    private String date;
    private String username;

    public AddMaterial(String name, String category, String amount, String date){
        this.name = name;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }
    public AddMaterial(String name, String category, String amount){
        this.name = name;
        this.category = category;
        this.amount = amount;
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

    public String getAmount() {
        return amount;
    }

    public String getNumber() {return number; }

    public String getDate() {return date; }

    @Override
    public String toString() {
        return  name + " , " + category + " , " + amount + " , " + date;
    }

    public String toCSV() {
        return  name + ',' + category + ',' + amount + ',' + date;
    }
}
