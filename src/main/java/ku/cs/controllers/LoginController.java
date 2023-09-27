package ku.cs.controllers;

import javafx.css.Stylesheet;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import ku.cs.models.Account;
import ku.cs.models.AccountList;
import ku.cs.services.DataSource;
import ku.cs.services.FXRouter;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import javafx.scene.control.TextField;
import ku.cs.services.UserDataSource;

public class LoginController {
    @FXML private TextField PasswordTextField;
    @FXML private TextField UsernameTextField;
    @FXML private Label FailedLabel;
    @FXML private ImageView LogoImageView;
    @FXML private AnchorPane pane;
    @FXML private Button ThemeButton;
    private DataSource<AccountList> dataSource = new UserDataSource();
    private AccountList accountList = dataSource.readData();
    private Account loginAccount;
    private boolean isLightMode = true;
    private Account account;
    private final String darkModePath
            = getClass().getResource("/Theme/dark.css").toExternalForm();
    private final String lightModePath
            = getClass().getResource("/Theme/light.css").toExternalForm();

    public void initialize() {
        String url = getClass().getResource("/images/logo-aeiou.png").toExternalForm();
        LogoImageView.setImage(new Image(url));
        account = (Account) FXRouter.getData();

    }
    public void ThemeButton(ActionEvent actionevent) {
        if (account.getTheme().isLightMode()) {
            setDarkMode();
        } else {
            setLightMode();
        }
        account.getTheme().setLightMode(!account.getTheme().isLightMode());

    }
    private void setLightMode(){
        ThemeButton.setText("Dark Mode");
        pane.getStylesheets().add(lightModePath);
        pane.getStylesheets().remove(darkModePath);
    }
    private void setDarkMode(){
        pane.getStylesheets().remove(lightModePath);
        pane.getStylesheets().add(darkModePath);
        ThemeButton.setText("Light Mode");
    }
    @FXML
    public void RegisterNextButton(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("register");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void OrganizersButton(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("organizers");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML public void HowToButton(ActionEvent actionEvent){
        try {
            Desktop.getDesktop().open(new File("data" + File.separator + "howtouse.pdf"));
        } catch (IOException e) {
            System.out.println("Cannot open manual.pdf");
            e.printStackTrace();
        }
    }

    public void LoginUserButton(ActionEvent actionEvent){
        String username = UsernameTextField.getText();
        String password = PasswordTextField.getText();
        Account user = accountList.searchAccountByUsername(username);
        if(username == "" && password == ""){
            FailedLabel.setText("โปรดกรอกข้อมูลให้ครบ");
            FailedLabel.setStyle("-fx-text-fill: #f61e1e");

        } else if (!accountList.loginSuccess(username,password)){
            FailedLabel.setText("ชื่อผู้ใช้หรือรหัสผ่านไม่ถูกต้อง");
            FailedLabel.setStyle("-fx-text-fill: #f61e1e");
        } else {
            try {
                user.initialLoginTime();
                if (user.isStudent())
                    FXRouter.goTo("user-home", user);
                else if (user.isStaff())
                    FXRouter.goTo("staff-material", user);
                else if (user.isAdmin())
                    FXRouter.goTo("admin", user);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            DataSource<AccountList> write;
            write = new UserDataSource("data/csv/", "userData.csv");
            write.writeData(accountList);
        }
    }
}
