package ku.cs.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import ku.cs.models.Account;
import ku.cs.models.DetectTheme;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class OrganizersController {
    @FXML
    private ImageView AuImageView;
    @FXML
    private ImageView EarthImageView;
    @FXML
    private ImageView AomImageView;
    @FXML
    private AnchorPane pane;
    private Account account;
    public void initialize(){
        String url = getClass().getResource("/images/au.jpg").toExternalForm();
        AuImageView.setImage(new Image(url));
        String url1 = getClass().getResource("/images/earth.jpg").toExternalForm();
        EarthImageView.setImage(new Image(url1));
        String url2 = getClass().getResource("/images/aom.jpg").toExternalForm();
        AomImageView.setImage(new Image(url2));
        account = (Account) FXRouter.getData();
    }

    @FXML
    public void LoginBackButton(ActionEvent actionEvent) {
        try {
            FXRouter.goTo("login");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
