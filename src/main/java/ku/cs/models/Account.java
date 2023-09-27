package ku.cs.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Account {
    private String role;
    private String username;
    private String displayname;
    private String password;
    private String imagePath;
    private String loginTime;
    private  Theme theme = new Theme();

    public Account(String displayname,String username, String password, String role, String imagePath,String loginTime) {
        this.displayname = displayname;
        this.username = username;
        this.password = password;
        this.role = role;
        this.imagePath = imagePath;
        this.loginTime = loginTime;
    }
    public Account(String displayname,String username, String password, String role, String imagePath) {
        this.displayname = displayname;
        this.username = username;
        this.password = password;
        this.role = role;
        this.imagePath = imagePath;
    }
    public Theme getTheme() {
        return theme;
    }
    public String getImagePath() {return imagePath;}
    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public String getRole() {
        return role;
    }
    public String getDisplayname() {
        return displayname;
    }
    public String getLoginTime() {
        return loginTime;
    }
    public void initialLoginTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        this.loginTime = now.format(format);
    }
    public LocalDateTime getTime() {
        String[] time = loginTime.split(" ");
        String[] data = time[0].split("-");
        int year = Integer.parseInt(data[2]);
        int month = Integer.parseInt(data[1]);
        int day = Integer.parseInt(data[0]);
        String[] splittime = time[1].split(":");
        int hour = Integer.parseInt(splittime[0]);
        int minute = Integer.parseInt(splittime[1]);
        int sec = Integer.parseInt(splittime[2]);
        return LocalDateTime.of(year, month, day, hour, minute, sec);
    }
    public boolean loginSuccess(String username, String password) {
        if (this.username.equals(username) && this.password.equals(password)) {
            return true;
        }
        return false;
    }
    public void changePassword(String newPassword){
        password = newPassword;
    }
    public boolean checkAccount(String username) {
        if (this.username.equals(username))
            return true;
        return false;
    }
    public boolean isStudent() {
        return this.role.equals("user");
    }
    public boolean isStaff() {
        return this.role.equals("staff");
    }
    public boolean isAdmin() {
        return this.role.equals("admin");
    }

    @Override
    public String toString(){
        return displayname+", "+username+", "+role+", "+", "+loginTime;
    }

}
