package ku.cs.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LendMaterial {
    private String category;
    private String name;
    private String amount;
    private String number;
    private String date;
    private String username;

    public LendMaterial(String name, String category, String amount, String username, String date){
        this.name = name;
        this.category = category;
        this.amount = amount;
        this.username = username;
        this.date = date;
    }
    public LendMaterial(String name, String category, String amount, String username){
        this.name = name;
        this.category = category;
        this.amount = amount;
        this.username = username;
        initialTime();
    }

    public void initialTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        this.date = now.format(format);
    }

    public String getDate() {
        return date;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }
    public String getUserName() {
        return username;
    }

    public String getCategory() {
        return category;
    }

    public String getAmount() {
        return amount;
    }

    public String getNumber() {return number; }

    @Override
    public String toString() {
        return  name + " , " + category + " , " + amount + " , " + username + " , " + date;
    }

    public String toCSV() {
        return  name + ',' + category + ',' + amount + ',' + username + ',' + date;
    }

}
