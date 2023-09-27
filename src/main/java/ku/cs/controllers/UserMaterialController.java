package ku.cs.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import ku.cs.models.*;
import ku.cs.services.DataSource;
import ku.cs.services.FXRouter;
import ku.cs.services.LendMaterialListDataSource;
import ku.cs.services.UserDataSource;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;


public class UserMaterialController {
    private DataSource<AccountList> dataSource;
    private Account account;
    private AccountList accountList;
    @FXML
    private Label nameLabel;
    @FXML
    private Label NameLabel;
    @FXML
    private Label CategoryLabel;
    @FXML
    private Label DateLabel;
    @FXML
    private Label WithdrawLabel;
    @FXML
    private ListView materialListView;
    @FXML private Circle circle;
    private File imageFile;
    private DataSource<LendMaterialList> lendMaterialListDataSource;
    private LendMaterialList lendMaterialList;
    private LendMaterial selectedMaterial;
    @FXML
    private AnchorPane pane;

    public void initialize(){
        lendMaterialListDataSource = new LendMaterialListDataSource("data/csv","requestMaterials.csv");
        lendMaterialList = lendMaterialListDataSource.readData();
        account = (Account) FXRouter.getData();
        dataSource = new UserDataSource();
        accountList = dataSource.readData();
        DetectTheme detectTheme = new DetectTheme(pane,account);
        showUserData();
        showMaterialListView();
        handleSelectMaterialListView();
    }
    private void showMaterialListView() {
        materialListView.getItems().clear();
        ArrayList<LendMaterial> selectedMaterialList = new ArrayList<>();
        selectedMaterialList = LendMaterialList.searchByUsername(account);
        materialListView.getItems().addAll(selectedMaterialList);
        materialListView.refresh();
    }
    private void handleSelectMaterialListView() {
        materialListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<LendMaterial>() {
                    @Override
                    public void changed(ObservableValue<? extends LendMaterial> observableValue,
                                        LendMaterial oldValue, LendMaterial newValue) {
                        if(newValue!=null){
                            selectedMaterial = newValue;
                            System.out.println(newValue + " is selected");
                            NameLabel.setText(newValue.getName());
                            CategoryLabel.setText(newValue.getCategory()+"");
                            DateLabel.setText(newValue.getDate()+"");
                            WithdrawLabel.setText(newValue.getAmount()+"");
//                        if(newValue!=null)showSelectedAccount(newValue);
                        }
                    }
                });
    }
    private void showUserData() {
        if (account != null) {
            nameLabel.setText(account.getDisplayname());
            File image = new File(account.getImagePath());
            circle.setFill(new ImagePattern(new Image(image.toURI().toString())));
        }
    }
    @FXML
    public void handleHowtoButton(ActionEvent actionEvent){
        try {
            Desktop.getDesktop().open(new File("data" + File.separator + "howtouse.pdf"));
        } catch (IOException e) {
            System.out.println("Cannot open manual.pdf");
            e.printStackTrace();
        }
    }
    @FXML
    public void handleMaterialsButton(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("user-material",account);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void handleChangeButton(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("change-pass",account);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void handleLogoutButton(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            throw new RuntimeException(e);
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
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                String username = account.getUsername();
                File destFile = new File("data/profileUsers/" + username + ".png");
                Files.copy(selectedFile.toPath(), destFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                if (destFile.exists()) {
                    String imagePath = "data/profileUsers/" + username + ".png";
                    accountList.searchAccountByUsername(username).setImagePath(imagePath);
                    try {
                        Image image = new Image(destFile.toURI().toURL().toExternalForm());
                        circle.setFill(new ImagePattern(image));
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to load image from file: " + imagePath, e);
                    }
                    dataSource.writeData(accountList);
                    try {
                        FXRouter.goTo("user-material", account);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    throw new RuntimeException("File not found: " + destFile.getAbsolutePath());
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            System.err.println("Can't upload image");
        }
    }
    @FXML
    public void handleAssetsButton(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("user-home",account);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
