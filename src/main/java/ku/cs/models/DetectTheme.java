package ku.cs.models;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class DetectTheme {
    private final String lightModePath = getClass().getResource("/Theme/light.css").toExternalForm();
    private final String darkModePath = getClass().getResource("/Theme/dark.css").toExternalForm();
    @FXML
    private AnchorPane pane;
    private Account account;

    public DetectTheme(AnchorPane pane, Account account) {
        this.pane = pane;
        this.account = account;
        detectTheme();
    }

    private void detectTheme() {
        if (account.getTheme().isLightMode()) {
            setLightMode();
        } else {
            setDarkMode();
        }
    }

    private void setLightMode(){
        pane.getStylesheets().add(lightModePath);
        pane.getStylesheets().remove(darkModePath);
    }

    private void setDarkMode(){
        pane.getStylesheets().add(darkModePath);
        pane.getStylesheets().remove(lightModePath);
    }

}
