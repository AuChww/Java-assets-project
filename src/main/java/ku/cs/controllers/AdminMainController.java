package ku.cs.controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import ku.cs.models.Account;
import ku.cs.models.AccountList;
import ku.cs.services.UserDataSource;
import ku.cs.services.DataSource;
import ku.cs.services.FXRouter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class AdminMainController {
    private DataSource<AccountList> dataSource;
    private AccountList accountList;
    @FXML
    private Circle circle;
    @FXML
    private Label nameLabel;
    @FXML
    private Label NameLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label RoleLabel;
    @FXML
    private Rectangle UserPic;
    @FXML private ListView<Account> showAccListView;
    private Account account;
    private File imageFile;
    @FXML
    private AnchorPane pane;
    @FXML private Button ThemeButton;
    private final String darkModePath
            = getClass().getResource("/Theme/dark.css").toExternalForm();
    private final String lightModePath
            = getClass().getResource("/Theme/light.css").toExternalForm();
    public void initialize() {
        dataSource = new UserDataSource("data/csv/", "userData.csv");
        accountList = dataSource.readData();
        accountList.sortByTime();

        account = (Account) FXRouter.getData();
        showAccListView();
        showUserData();
        handleSelectUserListView();
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
    private void showUserData() {
        if (account != null) {
            nameLabel.setText(account.getDisplayname());
            File image = new File(account.getImagePath());
            circle.setFill(new ImagePattern(new Image(image.toURI().toString())));
        }
    }
    private void showAccListView() {
        AccountList users = new AccountList() ;
        for(Account accFind : accountList.getAllAccount()){
            if (!accFind.isAdmin())
                users.addAccount(accFind);
        }
        showAccListView.getItems().addAll(users.getAllAccount());
        showAccListView.refresh();
    }
    private void handleSelectUserListView(){
        showAccListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Account>() {
                    @Override
                    public void changed(ObservableValue<? extends Account> observableValue, Account oldValue, Account newValue) {
                        System.out.println(newValue + " is selected");
                        NameLabel.setText(newValue.getUsername());
                        RoleLabel.setText(newValue.getRole());
                        dateLabel.setText(newValue.getLoginTime());
                        File image = new File(newValue.getImagePath());
                        UserPic.setFill(new ImagePattern(new Image(image.toURI().toString())));
                    }
                });
    }
    @FXML
    public void LoginBackButton(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void ChangeNextButton(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("change-pass",account);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void AddStaffButton(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("staff-register",account);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void ListNextButton(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("list-system");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
