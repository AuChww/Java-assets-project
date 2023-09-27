package ku.cs;

import javafx.application.Application;
import javafx.stage.Stage;
import ku.cs.services.FXRouter;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXRouter.bind(this, stage, "", 800, 600);
        configRoute();
        FXRouter.goTo("login");
    }

    private static void configRoute() {
        String viewPath = "ku/cs/views/";
        FXRouter.when("admin", viewPath + "admin.fxml");
        FXRouter.when("change-pass", viewPath + "change-pass.fxml");
        FXRouter.when("how-to-use", viewPath + "how-to-use.fxml");
        FXRouter.when("login", viewPath + "login.fxml");
        FXRouter.when("organizers", viewPath + "organizers.fxml");
        FXRouter.when("register", viewPath + "register.fxml");
        FXRouter.when("staff-assetsadd", viewPath + "staff-assetsadd.fxml");
        FXRouter.when("staff-assetsmain", viewPath + "staff-assetsmain.fxml");
        FXRouter.when("staff-assetsrequest", viewPath + "staff-assetsrequest.fxml");
        FXRouter.when("staff-material", viewPath + "staff-material.fxml");
        FXRouter.when("staff-register", viewPath + "staff-register.fxml");
        FXRouter.when("user-history", viewPath + "user-history.fxml");
        FXRouter.when("user-home", viewPath + "user-home.fxml");
        FXRouter.when("user-material", viewPath + "user-material.fxml");
    }

    public static void main(String[] args) {
        launch(args);
    }
}