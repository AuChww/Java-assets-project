package ku.cs.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
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

public class UserHomeController {
    @FXML
    private Circle circle;
    @FXML
    private Label nameLabel;
    @FXML
    private ImageView userImageView;
    @FXML
    private Label buytimeLabel;
    @FXML
    private Label SerialNumberLabel;
    @FXML
    private Label NameLabel;
    @FXML
    private Label StatusLabel;
    @FXML
    private Label PlaceLabel;
    @FXML
    private Label CategoryLabel;
    @FXML
    private Label OwnerLabel;
    @FXML
    private Label AlertLabel;
    private Asset selectedAsset;
    private StaffAssetsRequestController staffAssetsRequestController;
    @FXML
    private TextField LocationTextField;
    private DataSource<AccountList> dataSource;
    private Account account;
    private AccountList accountList;
    private File imageFile;
    @FXML
    private AssetList assetList;
    @FXML
    private AssetFileDataSource assetFileDataSource;
    @FXML
    private ListView<Asset> assetsListView;
    private Asset asset;
    private LendAssetList lendAssetList;
    private DataSource<LendAssetList> lendAssetListDataSource;
    @FXML
    private AnchorPane pane;
    @FXML private Button ThemeButton;
    private final String darkModePath
            = getClass().getResource("/Theme/dark.css").toExternalForm();
    private final String lightModePath
            = getClass().getResource("/Theme/light.css").toExternalForm();
    @FXML private Label AssetStatusLabel;

    public void initialize(){
        assetFileDataSource = new AssetFileDataSource();
        lendAssetListDataSource = new LendAssetFileDataSource("data/csv","lendAssets.csv");
        assetFileDataSource.readData();
        lendAssetList = lendAssetListDataSource.readData();
        assetList = assetFileDataSource.getAllAssetList();
        account = (Account) FXRouter.getData();
        dataSource = new UserDataSource();
        accountList = dataSource.readData();
        showUserData();
        showListView();
        handleSelectedListView();
        detectTheme();
    }
    private void detectTheme() {
        if (account.getTheme().isLightMode()) {
            setLightMode();
        } else {
            setDarkMode();
        }
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
    private void showListView() {
        assetsListView.getItems().clear();
        assetsListView.getItems().addAll(assetList.getAllAsset());
        assetsListView.refresh();
    }
    private void showSelectedAsset(Asset asset) {
        NameLabel.setText(asset.getName());
        CategoryLabel.setText(asset.getCategory());
        SerialNumberLabel.setText(asset.getSerialNumber());
        userImageView.setImage(new Image("file:"+selectedAsset.getImagePath(), true));
        StatusLabel.setText(asset.getStatus());
        OwnerLabel.setText(asset.getUsername());
        PlaceLabel.setText(asset.getPlace());
        buytimeLabel.setText(asset.getbuytime());
        AssetStatusLabel.setText(asset.getAssetStatus());
    }
    private void handleSelectedListView() {
        assetsListView.getSelectionModel().selectedItemProperty().addListener(
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
    @FXML
    public void handleLoanButton(ActionEvent event) {
        if (selectedAsset.getStatus().equals("lended")){
            AlertLabel.setText("มีคนยืมครุภัณฑ์ไปแล้ว");
            AlertLabel.setStyle("-fx-text-fill: #f61e1e");
        }
        else if (!LocationTextField.getText().isEmpty()) {
            String username = account.getUsername();
            String place = LocationTextField.getText();
            selectedAsset.setStatus("กำลังยื่นคำร้องขอยืม");
            lendAssetList.addAsset(new LendAsset(selectedAsset.getSerialNumber(), selectedAsset.getName(), place , username, selectedAsset.getStatus(), selectedAsset.getImagePath()));
            lendAssetListDataSource.writeData(lendAssetList);
            assetFileDataSource.writeData(assetList);
            clear();
            showListView();
        }
        else {
            AlertLabel.setText("โปรดกรอกรายละเอียดให้ครบ");
            AlertLabel.setStyle("-fx-text-fill: #f61e1e");
        }
    }
    private void showUserData() {
        if (account != null) {
            nameLabel.setText(account.getDisplayname());
            File image = new File(account.getImagePath());
            circle.setFill(new ImagePattern(new Image(image.toURI().toString())));
        }
    }
    public void clear(){
        LocationTextField.clear();
        AlertLabel.setText("");
        AlertLabel.setText("");
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
                        FXRouter.goTo("user-home", account);
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
    @FXML
    public void handleAssetsButton(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("user-home",account);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void handleHistoryButton(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("user-history",account);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
