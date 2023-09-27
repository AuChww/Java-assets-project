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


public class UserHistoryController {

    private AssetFileDataSource assetFileDataSource;
    @FXML private Label nameLabel;
    @FXML private Circle circle;
    @FXML private ListView<Asset> borrowListView;
    private AssetList assetList;
    private ArrayList<Asset> UserAssetList;
    private DataSource<AccountList> dataSource;
    private Account account;
    private AccountList accountList;
    private File imageFile;
    private LendAsset selectedLendAsset;
    private LendAssetList lendAssetList;
    private DataSource<LendAssetList> lendAssetListDataSource;
    private Asset selectedAsset;
    @FXML private ImageView userImageView;
    @FXML private Label DateTimeAddLabel;
    @FXML private Label NumberLabel;
    @FXML private Label ANameLabel;
    @FXML private Label CategoryLabel;
    @FXML private Label StatusLabel;
    @FXML private Label PlaceLabel;
    @FXML private Label OwnerLabel;
    @FXML
    private AnchorPane pane;
    private void showUserData() {
        if (account != null) {
            nameLabel.setText(account.getDisplayname());
            File image = new File(account.getImagePath());
            circle.setFill(new ImagePattern(new Image(image.toURI().toString())));
        }
    }
    @FXML
    public void handleReturnButton(ActionEvent actionEvent){
        if (selectedAsset.getStatus().equals("ขอคืนครุภัณฑ์")){
        }
        else {
            selectedAsset.setStatus("ขอคืนครุภัณฑ์");
            lendAssetList.addAsset(new LendAsset(selectedAsset.getSerialNumber(), selectedAsset.getName(), selectedAsset.getPlace() , selectedAsset.getUsername(), selectedAsset.getStatus(), selectedAsset.getImagePath()));
            lendAssetListDataSource.writeData(lendAssetList);
            assetFileDataSource.writeData(assetList);
            showListView();
        }
    }
    @FXML
    public void initialize(){
        assetFileDataSource = new AssetFileDataSource();
        assetList = assetFileDataSource.readData();
        account = (Account) FXRouter.getData();
        UserAssetList = assetList.searchAssetByUsername(account.getUsername());
        dataSource = new UserDataSource();
        accountList = dataSource.readData();
        DetectTheme detectTheme = new DetectTheme(pane,account);
        showUserData();
        showListView();
        handleSelectedListView();
        lendAssetListDataSource = new LendAssetFileDataSource("data/csv","lendAssets.csv");
        lendAssetList = lendAssetListDataSource.readData();
    }
    private void showListView(){
        borrowListView.getItems().clear();
        System.out.println(UserAssetList);
        borrowListView.getItems().addAll(UserAssetList);  //show list view from all borrow
        borrowListView.refresh();
    }
    private void showSelectedAsset(Asset asset) {
        ANameLabel.setText(asset.getName());
        NumberLabel.setText(asset.getSerialNumber());
        CategoryLabel.setText(asset.getCategory());
        userImageView.setImage(new Image("file:"+selectedAsset.getImagePath(), true));
        StatusLabel.setText(asset.getStatus());
        OwnerLabel.setText(asset.getUsername());
        PlaceLabel.setText(asset.getPlace());
        DateTimeAddLabel.setText(asset.getLendTime());
    }
    private void handleSelectedListView() {
        borrowListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Asset>() {
                    @Override
                    public void changed(ObservableValue<? extends Asset> observable,
                                        Asset oldValue, Asset newValue) {
                        if (newValue != null) {
                            //System.out.println(newValue + " is selected");
                            selectedAsset = newValue;
                            showSelectedAsset(newValue);
                        }
                    }
                });
    }

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
                        FXRouter.goTo("user-history", account);
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
            FXRouter.goTo("user-home",account);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
