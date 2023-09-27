package ku.cs.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import ku.cs.models.*;
import ku.cs.services.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

public class StaffAssetsRequestController {

    private LendAsset selectedLendAsset;
    private LendAssetList lendAssetList;
    @FXML private ListView borrowListView;
    @FXML private Label NameLabel;
    @FXML private Label SnumLabel;
    @FXML private Label PlaceLabel;
    @FXML private Label UsernameLabel;
    @FXML private Label StatusLabel;
    @FXML private ImageView ImagePath;
    @FXML private Circle circle;
    @FXML private Label nameLabel;
    @FXML private Label AlertLabel;
    @FXML
    private AnchorPane pane;
    private DataSource<AccountList> dataSource;
    private Account account;
    private AccountList accountList;
    private AssetList assetList;
    private AssetFileDataSource assetFileDataSource;
    private File imageFile;
    @FXML private LendAssetFileDataSource lendAssetFileDataSource;
    public void initialize(){
        assetFileDataSource = new AssetFileDataSource();
        lendAssetFileDataSource = new LendAssetFileDataSource();
        assetList = assetFileDataSource.readData();
        lendAssetList = lendAssetFileDataSource.readData();
        lendAssetList = lendAssetFileDataSource.getAllLendList();
        System.out.println(lendAssetList.getLendAssetList());
        showListView();
        account = (Account) FXRouter.getData();
        dataSource = new UserDataSource();
        accountList = dataSource.readData();
        DetectTheme detectTheme = new DetectTheme(pane,account);
        showUserData();
        handleSelectedListView();
    }
    private void showUserData() {
        if (account != null) {
            nameLabel.setText(account.getDisplayname());
            File image = new File(account.getImagePath());
            circle.setFill(new ImagePattern(new Image(image.toURI().toString())));
        }
    }
    private void showSelectedAsset(LendAsset lendAsset) {
        NameLabel.setText(lendAsset.getName());
        SnumLabel.setText(lendAsset.getSerialnumber());
        PlaceLabel.setText(lendAsset.getPlace());
        UsernameLabel.setText(lendAsset.getUsername());
        StatusLabel.setText(lendAsset.getStatus());
        ImagePath.setImage(new Image("file:"+selectedLendAsset.getImagePath(), true));
    }
    private void handleSelectedListView() {
        borrowListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<LendAsset>() {
                    @Override
                    public void changed(ObservableValue<? extends LendAsset> observableValue, LendAsset lendAsset, LendAsset t1) {
                        if (t1 != null) {
                            //System.out.println(newValue + " is selected");
                            selectedLendAsset = t1;
                            showSelectedAsset(selectedLendAsset);
                        }
                    }

                });
    }
    public void showListView(){
        borrowListView.getItems().clear();
        borrowListView.getItems().addAll(lendAssetList.getLendAssetList());
        System.out.println(lendAssetList.getLendAssetList());//show list view from all assets
        borrowListView.refresh();
    }

    public void handleApproveButton(ActionEvent actionEvent){
        if (selectedLendAsset.getStatus().equals("ขอคืนครุภัณฑ์")){
            AlertLabel.setText("คำร้องขอคืนไม่สามารถApproveได้");
            AlertLabel.setStyle("-fx-text-fill: #f61e1e");
        }
        else {Asset selectedAsset = assetList.searchAssetSerialNumber(selectedLendAsset.getSerialnumber());
            selectedLendAsset.setStatus("lended");
            selectedAsset.setStatus("lended");
            selectedAsset.setUsername(selectedLendAsset.getUsername());
            selectedAsset.setPlace(selectedLendAsset.getPlace());
            selectedAsset.setLendTime(selectedLendAsset.getTime());
            lendAssetList.del(selectedLendAsset);
            assetFileDataSource.writeData(assetList);
            lendAssetFileDataSource.writeData(lendAssetList);
            showListView();
        }
    }
    public void handleRejectButton(ActionEvent actionEvent){
        if (selectedLendAsset.getStatus().equals("กำลังยื่นคำร้องขอยืม")){
            AlertLabel.setText("คำร้องขอยืมไม่สามารถRejectด้");
            AlertLabel.setStyle("-fx-text-fill: #f61e1e");
        }
        else {Asset selectedAsset = assetList.searchAssetSerialNumber(selectedLendAsset.getSerialnumber());
            selectedLendAsset.setStatus("not lend");
            selectedAsset.setStatus("not lend");
            selectedAsset.setUsername("-");
            selectedAsset.setPlace("ส่วนกลาง");
            selectedAsset.setLendTime("-");
            lendAssetList.del(selectedLendAsset);
            assetFileDataSource.writeData(assetList);
            lendAssetFileDataSource.writeData(lendAssetList);
            showListView();
        }
    }


    public void handleHowtoButton(ActionEvent actionEvent){
        try {
            Desktop.getDesktop().open(new File("data" + File.separator + "howtouse.pdf"));
        } catch (IOException e) {
            System.out.println("Cannot open manual.pdf");
            e.printStackTrace();
        }
    }

    public void handleMaterialsButton(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("staff-material",account);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void handleChangeButton(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("change-pass",account);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
                        FXRouter.goTo("staff-assetsrequest", account);
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

    public void handleAssetsButton(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("staff-assetsmain",account);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
