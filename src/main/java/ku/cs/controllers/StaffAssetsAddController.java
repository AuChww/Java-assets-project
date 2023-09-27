package ku.cs.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

public class StaffAssetsAddController {

    @FXML private TextField addAssetID;
    @FXML private Circle circle;
    @FXML private Label nameLabel;

    @FXML private TextField addQuantityAsset;
    @FXML private ChoiceBox<String> assetNameChoiceBox;
    @FXML private ChoiceBox<String> assetstypeChoicebox ;

    private AssetFileDataSource dataSource;
    private AssetList assetList;
    @FXML private ImageView assetImageView;
    @FXML
    private AnchorPane pane;
    private Asset assetForSetImagePath;
    private DataSource<AccountList> dataUserSource;
    private Account account;
    private AccountList accountList;
    private File imageFile;
    private String[] AssetStatusChoice = {"ปกติ","ชำรุด","จำหน่าย","บริจาค"};
    @FXML
    private ChoiceBox<String> AssetStatusChoicebox;
    public void initialize() {
        account = (Account) FXRouter.getData();
        dataUserSource = new UserDataSource();
        accountList = dataUserSource.readData();
        DetectTheme detectTheme = new DetectTheme(pane,account);
        showAssetsTypeChoiceBox();
        showAssetsNameChoiceBox();
        showUserData();
        AssetStatusChoicebox.setValue(null);
        AssetStatusChoicebox.getItems().addAll(AssetStatusChoice);
    }
    private void showUserData() {
        if (account != null) {
            nameLabel.setText(account.getDisplayname());
            File image = new File(account.getImagePath());
            circle.setFill(new ImagePattern(new Image(image.toURI().toString())));
        }
    }
    @FXML
    public void handleAssetsImageButton(ActionEvent event) {
        FileChooser chooser = new FileChooser();
        // SET FILECHOOSER INITIAL DIRECTORY
        chooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        // DEFINE ACCEPTABLE FILE EXTENSION
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("images PNG JPG", "*.png", "*.jpg", "*.jpeg"));
        // GET FILE FROM FILECHOOSER WITH JAVAFX COMPONENT WINDOW
        Node source = (Node) event.getSource();
        File file = chooser.showOpenDialog(source.getScene().getWindow());
        if (file != null) {
            try {
                // CREATE FOLDER IF NOT EXIST
                File destDir = new File("images");
                if (!destDir.exists()) destDir.mkdirs();
                // RENAME FILE
                String[] fileSplit = file.getName().split("\\.");
                String filename = LocalDate.now() + "_" + System.currentTimeMillis() + "."
                        + fileSplit[fileSplit.length - 1];
                Path target = FileSystems.getDefault().getPath(
                        destDir.getAbsolutePath() + System.getProperty("file.separator") + filename
                );
                // COPY WITH FLAG REPLACE FILE IF FILE IS EXIST
                Files.copy(file.toPath(), target, StandardCopyOption.REPLACE_EXISTING);
                // SET NEW FILE PATH TO IMAGE
                assetImageView.setImage(new Image(target.toUri().toString()));
                //setImagePath
                Asset assetForSetImagePath = new Asset("serialNumberSetImage","nameSetImage","categorySetImage");
                assetForSetImagePath.setImagePath(destDir + "/" + filename);
                this.assetForSetImagePath = assetForSetImagePath; //พอ uplosd imsge จะมาเก็นน image path oี้
                //System.out.println("Upload: "+accountForSetImagePath.getImagePath())
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void showAssetsNameChoiceBox(){
        dataSource = new AssetFileDataSource();
        dataSource.readData();
        assetList = dataSource.getAllAssetList();
        Collection<String> availableAssetType =  new HashSet<>();
        for(Asset assetFind : assetList.getAllAsset()){
            availableAssetType.add(String.valueOf(assetFind.getName()));
        }
        assetNameChoiceBox.getItems().addAll(availableAssetType);
    }
    public void showAssetsTypeChoiceBox() {
        dataSource = new AssetFileDataSource();
        dataSource.readData();
        assetList = dataSource.getAllAssetList();
        Collection<String> availableAssetType =  new HashSet<>();
        for(Asset assetFind : assetList.getAllAsset()){
            availableAssetType.add(String.valueOf(assetFind.getCategory()));
        }
        assetstypeChoicebox.getItems().addAll(availableAssetType);
    }

    public void handleConfirmButton(ActionEvent actionEvent){
        LocalDateTime buyTime = LocalDateTime.now();
        String time = buyTime.toString();
        DataSource<AssetList> dataSource = new AssetFileDataSource();
        AssetList assetList = dataSource.readData();
        String assetStatus = AssetStatusChoicebox.getValue();
        if(addAssetID.getText().equals("") || assetstypeChoicebox.getValue().equals("") || assetNameChoiceBox.getValue().equals("") || AssetStatusChoicebox.getValue().equals("")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ERROR");
            alert.setHeaderText(null);
            alert.setContentText("Information should not empty.");  //ถ้าค่าโล่ง มันก้จะขึ้นมาว่า ต้องใส่ข้มูล ค่ามันว่าง
            alert.showAndWait();
        }else{
            String idStr = addAssetID.getText();  //ดึง ข้อมูลจาก text field ,าเป็ย str
            String categoryStr = assetstypeChoicebox.getValue();
            String nameStr = assetNameChoiceBox.getValue(); //from choice box
            if (!(assetForSetImagePath==null)) { //ถ้าไม่เท่ากับ null เราจะเเอดใหม่
                assetList.addAsset(new Asset(idStr,nameStr,categoryStr,assetForSetImagePath.getImagePath(),"-","not lend",time,"ส่วนกลาง","-",assetStatus));

            }else{
                assetList.addAsset(new Asset(idStr,nameStr,categoryStr,"images/default.png","-","not lend",time,"ส่วนกลาง","-",assetStatus));
            } //null = default
            dataSource.writeData(assetList);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("COMPLETE");
            alert.setHeaderText(null);
            alert.setContentText("Add success.");
            alert.showAndWait(); //pop up
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
                    dataUserSource.writeData(accountList);
                    try {
                        FXRouter.goTo("staff-assetsadd", account);
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
            }  catch (IOException e) {
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
