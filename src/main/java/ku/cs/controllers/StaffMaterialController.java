package ku.cs.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
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
import java.util.Collection;
import java.util.HashSet;


public class StaffMaterialController {
    @FXML
    private ObservableList<Material> material = FXCollections.observableArrayList();
    @FXML
    private Circle circle;
    @FXML
    private Label nameLabel;
    @FXML
    private ListView MaterialListView;
    @FXML
    private ListView MaterialAddListView;
    @FXML
    private ListView MaterialLendListView;
    @FXML
    private ChoiceBox<String> CategoryChoiceBox;
    private String[] CategoryChoices = {"วัสดุสำนักงาน","วัสดุงานบ้านงานครัว","วัสดุคอมพิวเตอร์"};
    private String[] CategoryCombo = {"ALL","วัสดุสำนักงาน","วัสดุงานบ้านงานครัว","วัสดุคอมพิวเตอร์"};
    @FXML
    private ChoiceBox<String> UserChoiceBox;
    @FXML private ComboBox<String> sortByCategoryBox ;
    @FXML
    private Label NameLabel;
    @FXML
    private Label AmountLabel;
    @FXML
    private Label CategoryLabel;
    @FXML
    private TextField NameTextField;
    @FXML
    private TextField AmountAddTextField;
    @FXML
    private TextField AmountMaterialTextField;
    @FXML
    private TextField AmountBorrowTextField;
    @FXML
    private Label AmountAddLabel;
    @FXML
    private Label DateAddLabel;
    @FXML
    private Label AlertLabel;
    @FXML
    private AnchorPane pane;
    @FXML
    private Button ThemeButton;

    private String number;
    private String category;
    private String date;

    private DataSource<AccountList> dataSource;
    private Account account;
    private AccountList accountList;
    private File imageFile;
    private DataSource<MaterialList> materialListDataSource;
    private MaterialList materialList;
    private Material selectedMaterial;
    private DataSource<AddMaterialList> addMaterialListDataSource;
    private AddMaterialList addMaterialList;
    private DataSource<LendMaterialList> lendMaterialListDataSource;
    private LendMaterialList lendMaterialList;
    private MaterialList filtered;
    private MaterialFilterer filterer;
    private final String darkModePath
            = getClass().getResource("/Theme/dark.css").toExternalForm();
    private final String lightModePath
            = getClass().getResource("/Theme/light.css").toExternalForm();


    public void initialize(){
        materialListDataSource = new MaterialListDataSource("data/csv", "Materials.csv");
        materialList = materialListDataSource.readData();
        addMaterialListDataSource = new AddMaterialListDataSource("data/csv","addMaterialsHistory.csv");
        addMaterialList = addMaterialListDataSource.readData();
        lendMaterialListDataSource = new LendMaterialListDataSource("data/csv","requestMaterials.csv");
        lendMaterialList = lendMaterialListDataSource.readData();
        account = (Account) FXRouter.getData();
        dataSource = new UserDataSource("data/csv","userData.csv");
        accountList = dataSource.readData();
        CategoryChoiceBox.setValue(null);
        CategoryChoiceBox.getItems().addAll(CategoryChoices);
        sortByCategoryBox.getItems().addAll(CategoryCombo);
        UserChoiceBox.setValue(null);
        filterer = new MaterialFilterer();
        filtered = materialList;
        showUserData();
        detectTheme();
        handleSelectMaterialListView();
        showMaterialListView(filtered);
        showUserChoiceBox();
        handleSortByCategory();
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
    public void showUserChoiceBox() {
        dataSource = new UserDataSource("data/csv","userData.csv");
        dataSource.readData();
//        accountList = dataSource.getAllAccount();
        Collection<String> accountType =  new HashSet<>();
        for(Account accFind : accountList.getAllAccount()){
            if (accFind.isStudent())
                accountType.add(String.valueOf(accFind.getUsername()));
        }
        UserChoiceBox.getItems().addAll(accountType);
    }
    private void showMaterialListView(MaterialList list) {
        MaterialListView.getItems().clear();
        System.out.println(list.getMaterialList());
        MaterialListView.getItems().addAll(list.getMaterialList());
        MaterialListView.refresh();
    }
    private void showMaterialAddListView(){
        MaterialAddListView.getItems().clear();
        ArrayList<AddMaterial> selectedMaterialList = new ArrayList<>();
        if(selectedMaterial!=null){
            selectedMaterialList = addMaterialList.searchByMaterial(selectedMaterial);
            MaterialAddListView.getItems().addAll(selectedMaterialList);
        }
        MaterialAddListView.refresh();
    }
    private void showMaterialLendListView(){
        MaterialLendListView.getItems().clear();
        ArrayList<LendMaterial> selectedMaterialList = new ArrayList<>();
        selectedMaterialList = LendMaterialList.searchByMaterial(selectedMaterial);
        MaterialLendListView.getItems().addAll(selectedMaterialList);
        MaterialLendListView.refresh();
    }
    private void handleSortByCategory(){
        sortByCategoryBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println(sortByCategoryBox.getValue());
                if(((String)(sortByCategoryBox.getValue())).equals("ALL")){filterer.setCategory(null);}
                else if(sortByCategoryBox.getValue()!=null){
                    filterer.setCategory((String) sortByCategoryBox.getValue());
                }
                else {filterer.setCategory(null);}
                filtered = materialList.filterBy(filterer);
                //System.out.println(filtered.getMaterialList());
                showMaterialListView(filtered);
            }
        });
    }
    private void handleSelectMaterialListView() {
        MaterialListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Material>() {
                    @Override
                    public void changed(ObservableValue<? extends Material> observableValue,
                                        Material oldValue, Material newValue) {
                        if(newValue!=null){
                            MaterialAddListView.getItems().clear();
                            selectedMaterial = newValue;
                            System.out.println(newValue + " is selected");
                            NameLabel.setText(newValue.getName());
                            AmountLabel.setText(newValue.getAmount()+"");
                            CategoryLabel.setText(newValue.getCategory());
                            String directoryName = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "images" + File.separator + "MaterialImages";
                            String filepath = directoryName + File.separator + newValue.getNumber() + ".png";
//                        if(newValue!=null)showSelectedAccount(newValue);
                            showMaterialAddListView();
                            showMaterialLendListView();
                        }
                    }
                });
    }
    @FXML
    public void OnAddAmountButton(ActionEvent event) {
        if(!AmountAddTextField.getText().isEmpty()) {
            String amount = AmountAddTextField.getText();
            selectedMaterial.addAmount(Integer.parseInt(amount));
            addMaterialList.addMaterial(new AddMaterial(selectedMaterial.getName(), selectedMaterial.getCategory(), amount));
            addMaterialListDataSource.writeData(addMaterialList);
            materialListDataSource.writeData(materialList);
            clear();
            MaterialListView.getItems().clear();
            showMaterialListView(filtered);
            AmountLabel.setText(selectedMaterial.getAmount()+"");
            showMaterialAddListView();
            showMaterialLendListView();
        }
        else {
            AlertLabel.setText("โปรดกรอกรายละเอียดให้ครบ");
            AlertLabel.setStyle("-fx-text-fill: #f61e1e");
        }
    }
    @FXML
    public void OnAddButton(ActionEvent event) {
        String name = NameTextField.getText();
        String amount = AmountMaterialTextField.getText();
        String category = CategoryChoiceBox.getValue();
            if(materialList.MaterialNameIsUsed(name)) {
                AlertLabel.setText("มีชื่อวัสดุนี้แล้ว");
                AlertLabel.setStyle("-fx-text-fill: #f61e1e");
                NameTextField.clear();
                AmountMaterialTextField.clear();
                CategoryChoiceBox.setValue(null);
            }
            else if(NameTextField.getText() != "" && CategoryChoiceBox.getValue()!=null && AmountMaterialTextField.getText() != "") {
                materialList.addMaterial(new Material(name,category,amount));
                addMaterialList.addMaterial(new AddMaterial(name,category,amount));
                materialListDataSource.writeData(materialList);
                addMaterialListDataSource.writeData(addMaterialList);
                clear();
                filtered = materialList.filterBy(filterer);
                showMaterialListView(filtered);
                showMaterialAddListView();
            }
            else if(NameTextField.getText() != "" && CategoryChoiceBox.getValue()==null && AmountMaterialTextField.getText() == ""){
                AlertLabel.setText("");
                AlertLabel.setText("โปรดกรอกหมวดหมู่และจำนวนวัสดุ");
                AlertLabel.setStyle("-fx-text-fill: #f61e1e");
            }
            else if(NameTextField.getText() != "" && CategoryChoiceBox.getValue()!=null && AmountMaterialTextField.getText() == ""){
                AlertLabel.setText("");
                AlertLabel.setText("โปรดกรอกจำนวนวัสดุ");
                AlertLabel.setStyle("-fx-text-fill: #f61e1e");
                CategoryChoiceBox.setValue(null);
            }
            else if(NameTextField.getText() == "" && CategoryChoiceBox.getValue()!=null && AmountMaterialTextField.getText() == ""){
                AlertLabel.setText("");
                AlertLabel.setText("โปรดกรอกชื่อวัสดุและจำนวน");
                AlertLabel.setStyle("-fx-text-fill: #f61e1e");
                CategoryChoiceBox.setValue(null);
            }
            else if(NameTextField.getText() == "" && CategoryChoiceBox.getValue()!=null && !AmountMaterialTextField.getText().isEmpty()){
                AlertLabel.setText("");
                AlertLabel.setText("โปรดกรอกชื่อวัสดุ");
                AlertLabel.setStyle("-fx-text-fill: #f61e1e");
                CategoryChoiceBox.setValue(null);
            }
            else if(NameTextField.getText() == "" && CategoryChoiceBox.getValue()==null && !AmountMaterialTextField.getText().isEmpty()){
                AlertLabel.setText("");
                AlertLabel.setText("โปรดกรอกชื่อวัสดุและหมวดหมู่");
                AlertLabel.setStyle("-fx-text-fill: #f61e1e");
            }
            else if(NameTextField.getText() != "" && CategoryChoiceBox.getValue()==null && !AmountMaterialTextField.getText().isEmpty()){
                AlertLabel.setText("");
                AlertLabel.setText("โปรดกรอกหมวดหมูวัสดุ");
                AlertLabel.setStyle("-fx-text-fill: #f61e1e");
            }
    }
    @FXML
    public void OnLendButton(ActionEvent event) {
        if(!AmountBorrowTextField.getText().isEmpty() && UserChoiceBox.getValue()!=null) {
            String amount = AmountBorrowTextField.getText();
            String username = UserChoiceBox.getValue();
            if (selectedMaterial.getAmount()>=Integer.parseInt(amount)){
                selectedMaterial.lendAmount(Integer.parseInt(amount));
                lendMaterialList.lendMaterial(new LendMaterial(selectedMaterial.getName(), selectedMaterial.getCategory(), amount, username));
                lendMaterialListDataSource.writeData(lendMaterialList);
                materialListDataSource.writeData(materialList);
                clear();
                MaterialListView.getItems().clear();
                showMaterialListView(filtered);
                showMaterialAddListView();
                showMaterialLendListView();
            }else {AlertLabel.setText("จำนวนคงเหลือไม่เพียงพอ");
                AlertLabel.setStyle("-fx-text-fill: #f61e1e");
            }
        }
        else if(!AmountBorrowTextField.getText().isEmpty() && UserChoiceBox.getValue()==null){
            AlertLabel.setText("");
            AlertLabel.setText("โปรดกรอกชื่อผู้เบิก");
            AlertLabel.setStyle("-fx-text-fill: #f61e1e");
        }
        else if(AmountBorrowTextField.getText().isEmpty() && UserChoiceBox.getValue()!=null){
            AlertLabel.setText("");
            AlertLabel.setText("โปรดกรอกจำนวนที่ต้องการเบิก");
            AlertLabel.setStyle("-fx-text-fill: #f61e1e");
            UserChoiceBox.setValue(null);
        }
    }
    public void clear(){
        NameTextField.clear();
        AmountAddTextField.clear();
        CategoryChoiceBox.setValue(null);
        AlertLabel.setText("");
        AlertLabel.setText("");
        AmountMaterialTextField.clear();
    }
    private void showUserData() {
        if (account != null) {
            nameLabel.setText(account.getDisplayname());
            File image = new File(account.getImagePath());
            circle.setFill(new ImagePattern(new Image(image.toURI().toString())));
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
                    dataSource.writeData(accountList);
                    try {
                        FXRouter.goTo("staff-material", account);
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
}
