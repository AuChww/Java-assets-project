package ku.cs.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import ku.cs.models.Account;
import ku.cs.models.AccountList;
import ku.cs.models.DetectTheme;
import ku.cs.services.FXRouter;
import ku.cs.services.UserDataSource;
import ku.cs.services.DataSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class StaffRegisterController {
    @FXML private Label failed;
    @FXML private TextField inputUsernameTextField;
    @FXML private TextField inputDisplaynameTextField;
    @FXML private PasswordField inputPasswordTextField;
    @FXML private PasswordField confirmPasswordTextField;
    @FXML private Rectangle previewImage;
    private AccountList accountList;
    private DataSource<AccountList> dataSource;
    private File imageFile;
    @FXML
    private AnchorPane pane;
    private Account account;

    public void initialize(){
        readData();
        DetectTheme detectTheme = new DetectTheme(pane,account);
    }
    private void readData() {
        dataSource = new UserDataSource();
        accountList = dataSource.readData();
        account = (Account) FXRouter.getData();
    }
    public void handleLogoutButton(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("admin");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void handleSignUpButton(ActionEvent actionEvent) {
        String displayname = inputDisplaynameTextField.getText();
        String password = inputPasswordTextField.getText();
        String confirmPass = confirmPasswordTextField.getText();
        String username = inputUsernameTextField.getText();
        if(displayname == "" || password == "" || confirmPass == "" || username == ""){
            failed.setText("กรอกข้อมูลให้ครบ");
            failed.setStyle("-fx-text-fill: #f61e1e");
        }
        else if(accountList.usernameIsUsed(username)) {
            failed.setText("มีชื่อผู้ใช้บัญชีนี้แล้ว");
            failed.setStyle("-fx-text-fill: #f61e1e");
        }
        else if (!(password).equals(confirmPass)) {
            failed.setText("รหัสผ่านไม่ตรงกัน");
            failed.setStyle("-fx-text-fill: #f61e1e");
        }
        else {
            Account account = new Account(displayname,username, password, "staff","");
            account.initialLoginTime();
            accountList.addAccount(account);
            String imageFilePath;
            if(imageFile != null){
                File tempImagePNG = new File("data/profileUsers/"+ File.separator+ "temp.png");
                String imageName = username+".png";
                File renameImage = new File("data/profileUsers/" + File.separator + imageName);
                if (tempImagePNG.renameTo(renameImage)) {
                    System.out.println(renameImage.getPath());
                    imageFilePath = "data/profileUsers/" + File.separator + imageName;
                    account.setImagePath(imageFilePath);
                    }
                } else {
                    account.setImagePath("data/profileUsers/userDefaultProfile.png");
                }

            DataSource<AccountList> write;
            write = new UserDataSource("data/csv/", "userData.csv");
            write.writeData(accountList);
            try {
                FXRouter.goTo("admin");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void handleUploadImageButton(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilterJPG
                = new FileChooser.ExtensionFilter("JPG files (*.JPG)", "*.JPG");
        FileChooser.ExtensionFilter extFilterjpg
                = new FileChooser.ExtensionFilter("jpg files (*.jpg)", "*.jpg");
        FileChooser.ExtensionFilter extFilterPNG
                = new FileChooser.ExtensionFilter("PNG files (*.PNG)", "*.PNG");
        FileChooser.ExtensionFilter extFilterpng
                = new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
        fileChooser.getExtensionFilters()
                .addAll(extFilterJPG, extFilterjpg, extFilterPNG, extFilterpng);
        imageFile = fileChooser.showOpenDialog(null);
        if(imageFile != null){
            try {
                String imagePath = imageFile.getAbsolutePath();
                File tempImagePNG = new File("data/profileUsers/"+ File.separator+ "temp.png");
                Path pathOut = (Path) Paths.get(tempImagePNG.getAbsolutePath());
                Files.copy(imageFile.toPath(), pathOut, StandardCopyOption.REPLACE_EXISTING);
                System.out.println(imagePath);
                previewImage.setFill(new ImagePattern(new Image(tempImagePNG.toURI().toString())));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }else {
            System.err.println("Can't upload image");
        }
    }
}
