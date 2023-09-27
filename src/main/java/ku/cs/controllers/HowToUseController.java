package ku.cs.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import ku.cs.models.Account;
import ku.cs.models.AccountList;
import ku.cs.services.DataSource;
import ku.cs.services.FXRouter;
import ku.cs.services.UserDataSource;

import java.io.File;
import java.io.IOException;

public class HowToUseController {
    private DataSource<AccountList> dataSource;
    private Account account;
    private AccountList accountList;
    private File imageFile;
    public void initialize(){
        account = (Account) FXRouter.getData();
        dataSource = new UserDataSource();
        accountList = dataSource.readData();
    }
    @FXML
    public void HowBackButton(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
