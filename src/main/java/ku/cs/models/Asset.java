package ku.cs.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Asset {
    private String name;
    private String place;
    private String category;
    private String serialNumber;
    private String imagePath;
    private String username;
    private String status;
    private LocalDateTime buyTime;
    private String lendTime;
    private String assetStatus;

    public Asset(String serialNumber, String name, String category) {
        this.name = name;
        this.category = category;
        this.serialNumber = serialNumber;
        this.imagePath = "images/default.png";
    }
    public Asset(String serialNumber, String name, String category,String imagePath,String username,String status,String buyTime,String place, String lendTime,String assetStatus) {
        this.name = name;
        this.category = category;
        this.serialNumber = serialNumber;
        this.imagePath = imagePath;
        this.username = username;
        this.status = status;
        this.buyTime = LocalDateTime.parse(buyTime);
        this.lendTime = lendTime;
        this.place = place;
        this.assetStatus = assetStatus;
    }

    public String getAssetStatus() {return assetStatus;}
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getCategory() {
        return category;
    }
    public String getStatus(){return  status;}
    public String getPlace() {return place;}
    public String getSerialNumber() {
        return serialNumber;
    }
    public void setLendTime(String time){
        this.lendTime = time;
    }

    @Override
    public String toString() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String BorrowFormatDateTime = buyTime.format(format);
        return serialNumber+", "+name+", "+BorrowFormatDateTime+", "+ category;
    }
    public String getbuytime() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String BorrowFormatDateTime = buyTime.format(format);
        return BorrowFormatDateTime ;
    }
    public boolean isUsername(String existName){
        return this.username.equals(existName);
    }
    public boolean isSerialNumber(String serialNumber){
        return this.serialNumber.equals(serialNumber);
    }
    public String getUsername() {
        return username;
    }

    public String toCsv(){
        return serialNumber+","+ name +","+ category+","+imagePath+","+username+","+status+","+buyTime+","+place+","+lendTime+","+assetStatus;
    }
    public String getImagePath() {
        return imagePath;
    }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
    public void setUsername(String currentUsername){this.username = currentUsername;}
    public void setStatus(String newStatus){this.status = newStatus;}
    public void setPlace(String place) {this.place = place;}
    public String getLendTime() { return  this.lendTime;
    }
}
