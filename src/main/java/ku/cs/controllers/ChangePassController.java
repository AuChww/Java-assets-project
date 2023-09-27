package ku.cs.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import ku.cs.models.Account;
import ku.cs.models.AccountList;
import ku.cs.models.DetectTheme;
import ku.cs.services.FXRouter;
import ku.cs.services.UserDataSource;
import ku.cs.services.DataSource;

import java.io.IOException;

public class ChangePassController {
    private Account account;
    private AccountList accountList;
    private DataSource<AccountList> dataSource;
    @FXML private TextField usernameTextField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private PasswordField oldPasswordField;
    @FXML private Label successLabel;
    @FXML
    private AnchorPane pane;
    public void initialize(){
        account = (Account) FXRouter.getData();
        readData();
        DetectTheme detectTheme = new DetectTheme(pane,account);
    }
    private void readData() {
        dataSource = new UserDataSource();
        accountList = dataSource.readData();
    }
    @FXML
    private void savePW(ActionEvent actionEvent) {
        String username = usernameTextField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String oldPassword = oldPasswordField.getText();
        if (!account.getUsername().equals(username)){
            successLabel.setText("ชื่อบัญชีผู้ใช้ไม่ถูกต้อง");
            successLabel.setStyle("-fx-text-fill: #f61e1e");
        }
        else if (!account.getPassword().equals(oldPassword)){
            successLabel.setText("รหัสผ่านเดิมไม่ถูกต้อง");
            successLabel.setStyle("-fx-text-fill: #f61e1e");
        }
        else if (newPassword.equals("") || confirmPassword.equals("")){
            successLabel.setText("กรอกรหัสผ่านไม่ครบ");
            successLabel.setStyle("-fx-text-fill: #f61e1e");
        } else if (!newPassword.equals(confirmPassword)){
            successLabel.setText("รหัสผ่านไม่ตรงกัน");
            successLabel.setStyle("-fx-text-fill: #f61e1e");
        } else {
            AccountList accountList = dataSource.readData();

            accountList.searchAccountByUsername(username);
            accountList.changePasswordByUsername(username,newPassword);
            dataSource.writeData(accountList);
            successLabel.setText("เปลี่ยนรหัสผ่านสำเร็จ");
            successLabel.setStyle("-fx-text-fill: #076b51");
        } clearPW();
    }
    public void clearPW(){
        newPasswordField.clear();
        confirmPasswordField.clear();
        usernameTextField.clear();
        oldPasswordField.clear();
    }
    @FXML
    private void handleHomeButton(ActionEvent actionEvent){
        try {
            if(account.isStaff()) FXRouter.goTo("staff-material",account);
            else if (account.isAdmin()) FXRouter.goTo("admin",account);
            else  FXRouter.goTo("user-home",account);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
