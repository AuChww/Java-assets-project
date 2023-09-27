package ku.cs.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LendAsset {
    private String imagePath;
    private String status;
    private String serialnumber;
    private String place;
    private String date;
    private String time;
    private String category;
    private String name;
    private String username;

    public LendAsset(String serialnumber, String name,String place,String username,String status,String imagePath) {
        this.serialnumber = serialnumber;
        this.name = name;
        this.place = place;
        this.username = username;
        this.status = status;
        this.imagePath = imagePath;
        initialTime();
    }
    public LendAsset(String serialnumber, String name,String place,String username,String status,String imagePath,String date) {
        this.serialnumber = serialnumber;
        this.name = name;
        this.place = place;
        this.username = username;
        this.status = status;
        this.imagePath = imagePath;
        this.date = date;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public String getUsername() {
        return username;
    }

    public String getImagePath() {return imagePath;}

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}
    public String getTime(){
        return this.date;
    }

    public String getSerialnumber() {return serialnumber;}

    public String getPlace() {return place;}

    public void setUsername(String username) {
        this.username = username;
    }
    public void initialTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        this.date = now.format(format);
    }

    @Override
    public String toString() {
        return  serialnumber+" "+ name +" "+ place+" "+username+" "+status+" "+imagePath+" "+date;
    }

//    public void setSerialNumber(String serialNumber) {
//        this.serialNumber = serialNumber;
//    }

    public String toCsv(){
        return serialnumber+","+ name +","+ place+","+username+","+status+","+imagePath+","+date;
    }

}
