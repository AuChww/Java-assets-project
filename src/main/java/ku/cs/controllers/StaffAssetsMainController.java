package ku.cs.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import ku.cs.models.*;
import ku.cs.services.AssetFileDataSource;
import ku.cs.services.DataSource;
import ku.cs.services.FXRouter;
import ku.cs.services.UserDataSource;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;


public class StaffAssetsMainController {

    private Asset selectedAsset;
    @FXML private Label StatusLabel;
    @FXML private Label OwnerLabel;
    @FXML private Label DateTimeLendLabel;
    @FXML private Circle circle;
    @FXML private Label nameLabel;
    @FXML private AssetFileDataSource assetFileDataSource;
    @FXML private ListView<Asset> assetsListView;
    @FXML private Label SerialNumberLabel;
    @FXML private Label NameLabel;
    @FXML private Label PlaceLabel;
    @FXML private Label CategoryLabel;
    @FXML private ImageView userImageView;
    @FXML private Label buytimeLabel;
    private AssetList assetList;
    private DataSource<AccountList> dataSource;
    private Account account;
    private LendAssetList lendAssetList;
    private AccountList accountList;
    private File imageFile;
    @FXML
    private TextField searchField;
    @FXML
    private AnchorPane pane;
    @FXML private Label AssetStatusLabel;
    @FXML
    public void initialize(){
        assetFileDataSource = new AssetFileDataSource();
        assetFileDataSource.readData();
        assetList = assetFileDataSource.getAllAssetList();
        showListView();
        handleSelectedListView();
        account = (Account) FXRouter.getData();
        dataSource = new UserDataSource();
        accountList = dataSource.readData();
        DetectTheme detectTheme = new DetectTheme(pane,account);
        showUserData();
        searchAsset();
    }
    private void showUserData() {
        if (account != null) {
            nameLabel.setText(account.getDisplayname());
            File image = new File(account.getImagePath());
            circle.setFill(new ImagePattern(new Image(image.toURI().toString())));
        }
    }
    private void showListView() {
        assetsListView.getItems().clear();
        assetsListView.getItems().addAll(assetList.getAllAsset());  //show list view from all assets
        assetsListView.refresh();
    }
    @FXML
    public void StaffRqButton(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("staff-assetsrequest",account);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void StaffAddButton(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("staff-assetsadd",account);
        } catch (IOException e) {
            System.err.println(e);
            throw new RuntimeException(e);
        }
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
    private void showSelectedAsset(Asset asset) {
        NameLabel.setText(asset.getName());
        CategoryLabel.setText(asset.getCategory());
        SerialNumberLabel.setText(asset.getSerialNumber());
        userImageView.setImage(new Image("file:"+selectedAsset.getImagePath(), true));
        StatusLabel.setText(asset.getStatus());
        OwnerLabel.setText(asset.getUsername());
        PlaceLabel.setText(asset.getPlace());
        buytimeLabel.setText(asset.getbuytime());
        DateTimeLendLabel.setText(asset.getLendTime());
        AssetStatusLabel.setText(asset.getAssetStatus());
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
                    dataSource.writeData(accountList);
                    try {
                        FXRouter.goTo("staff-assetsmain", account);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    try {
                        Image image = new Image(destFile.toURI().toURL().toExternalForm());
                        circle.setFill(new ImagePattern(image));
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to load image from file: " + imagePath, e);
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

    public void searchAsset() {
        ObservableList<Asset> observableList = assetsListView.getItems();
        observableList.addAll(assetList.getAllAsset());

        // set a listener to the search text field
        searchField.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            String searchText = searchField.getText().toLowerCase();

            // clear listview
            observableList.clear();

            // add matching assets to the list view
            assetList.getAllAsset().stream()
                    .filter(asset -> asset.getName().toLowerCase().contains(searchText) || asset.getCategory().toLowerCase().contains(searchText))
                    .forEach(observableList::add);
        });
    }
}
