package ro.ti.documentProcessor;

import ro.ti.documentProcessor.views.MainView;
import ro.ti.documentProcessor.views.SettingsView;
import com.gluonhq.charm.glisten.application.AppManager;
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import static com.gluonhq.charm.glisten.application.AppManager.HOME_VIEW;

public class DocumentProcessorGluonApplication extends Application {

    public static final String MAIN_VIEW = HOME_VIEW;
    public static final String SETTINGS_VIEW = "Settings View";

    private final AppManager appManager = AppManager.initialize(this::postInit);

    @Override
    public void init() {
        appManager.addViewFactory(MAIN_VIEW, () -> new MainView().getView());
        appManager.addViewFactory(SETTINGS_VIEW, () -> new SettingsView().getView());

        DrawerManager.buildDrawer(appManager);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        appManager.start(primaryStage);
    }

    private void postInit(Scene scene) {
        Swatch.BLUE.assignTo(scene);

        scene.getStylesheets().add(DocumentProcessorGluonApplication.class.getResource("style.css").toExternalForm());
        ((Stage) scene.getWindow()).getIcons().add(new Image(DocumentProcessorGluonApplication.class.getResourceAsStream("/icon.png")));
    }

    public static void main(String args[]) {
        launch(args);
    }
}
