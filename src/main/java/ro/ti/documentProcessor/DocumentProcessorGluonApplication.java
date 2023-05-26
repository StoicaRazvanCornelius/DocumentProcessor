package ro.ti.documentProcessor;

import com.gluonhq.charm.glisten.visual.Theme;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import com.gluonhq.charm.glisten.application.AppManager;
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import ro.ti.documentProcessor.MVC.Interfaces.Controller;
import ro.ti.documentProcessor.MVC.Interfaces.View;
import ro.ti.documentProcessor.app.DrawerManager;
import ro.ti.documentProcessor.app.views.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.Properties;

import static com.gluonhq.charm.glisten.application.AppManager.HOME_VIEW;

public class DocumentProcessorGluonApplication extends Application implements ro.ti.documentProcessor.MVC.Interfaces.View  {
    public static final String MAIN_VIEW = HOME_VIEW;
    public static final String SETTINGS_VIEW = "Settings View";
    public static final String IMPORT_VIEW = "Import View";
    public static final String EXPORT_VIEW = "Export View";
    public static MainPresenter mainPresenter;
    public static SettingsPresenter settingsPresenter;
    public static ExportPresenter exportPresenterd;
    public static ImportPresenter importPresenter;

    public static void setImportPresenter(ImportPresenter importPresenterInstance){
        importPresenter=importPresenterInstance;
    }



    private AppManager appManager;
    private  MediaView mediaView;
    private  double volume = 100;

    public Properties getProperties() {
        return properties;
    }

    public static void setProperties(Properties appProperties) {
        properties = appProperties;
    }

    private static Properties properties;
    private static volatile Controller controller;
    @Override
    public void init() throws URISyntaxException {
        appManager = AppManager.initialize(this::postInit);
        appManager.addViewFactory(MAIN_VIEW, () -> new MainView().getView());
        appManager.addViewFactory(SETTINGS_VIEW, () -> new SettingsView().getView());
        appManager.addViewFactory(IMPORT_VIEW, () -> new ImportView().getView());
        appManager.addViewFactory(EXPORT_VIEW, () -> new ExportView().getView());

        mediaView = new MediaView();

        if (mediaView.getMediaPlayer() == null){
            String pathAudioFile = getClass().getResource("/ui-click-97915.mp3").toURI().toString();
            Media media = new Media(pathAudioFile);
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

        }


        DrawerManager.buildDrawer(appManager);
    }

    public void clickSound(){
        mediaView.getMediaPlayer().setVolume(volume);
        mediaView.getMediaPlayer().seek(mediaView.getMediaPlayer().getStartTime());
        mediaView.getMediaPlayer().play();
    }
    @Override
    public void start(Stage primaryStage) {
        try {
            appManager.start(primaryStage);
        }catch (Exception e){


        }
    }

    private void postInit(Scene scene) {

        //using the config file
        Swatch.valueOf(properties.getProperty("swatch")).assignTo(scene);
        Theme.valueOf(properties.getProperty("theme")).assignTo(scene);

        scene.getStylesheets().add(DocumentProcessorGluonApplication.class.getResource("style.css").toExternalForm());
        ((Stage) scene.getWindow()).getIcons().add(new Image(DocumentProcessorGluonApplication.class.getResourceAsStream("/draweBannerrIcon.png")));
        ((Stage) scene.getWindow()).setTitle("Document Processor App");
    }

    @Override
    public View getView() throws InterruptedException {
        return this;
    }

    @Override
    public void testView() {
        System.out.print("check\n");
        System.out.print("  From view:\n -controller: ");
        controller.testController();
    }

    @Override
    public void updateFile(String path, Timestamp time) {
        importPresenter.reloadFile(path,time);
    }


    public static Controller getController() {
        return controller;
    }

    public void setController(Controller externController) {
       controller=externController;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
