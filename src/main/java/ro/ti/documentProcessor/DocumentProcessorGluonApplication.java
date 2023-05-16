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
import ro.ti.documentProcessor.MVC.controller.XlsController;
import ro.ti.documentProcessor.app.DrawerManager;
import ro.ti.documentProcessor.app.views.ExportView;
import ro.ti.documentProcessor.app.views.ImportView;
import ro.ti.documentProcessor.app.views.MainView;
import ro.ti.documentProcessor.app.views.SettingsView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

import static com.gluonhq.charm.glisten.application.AppManager.HOME_VIEW;

public class DocumentProcessorGluonApplication extends Application implements ro.ti.documentProcessor.MVC.Interfaces.View  {
    public static final String MAIN_VIEW = HOME_VIEW;
    public static final String SETTINGS_VIEW = "Settings View";
    public static final String IMPORT_VIEW = "Import View";
    public static final String EXPORT_VIEW = "Export View";
    private AppManager appManager = AppManager.initialize(this::postInit);
    private static MediaView mediaView;
    private static double volume = 100;
    private Properties properties;
    private static volatile Controller controller;
    @Override
    public void init() throws URISyntaxException {
        controller.setView(this);
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

    public static void clickSound(){
        mediaView.getMediaPlayer().setVolume(volume);
        mediaView.getMediaPlayer().seek(mediaView.getMediaPlayer().getStartTime());
        mediaView.getMediaPlayer().play();
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        appManager.start(primaryStage);
    }

    private void postInit(Scene scene) {

        //properties file. Settings remain the same even if you close the app.
        try {
            //String configPropertiesPath = DocumentProcessorGluonApplication.class.getResource("config.properties").getPath();
            String configPropertiesPath = DocumentProcessorGluonApplication.class.getResource("config.properties").getPath();
            FileInputStream propsInput = new FileInputStream(configPropertiesPath);
            properties = new Properties();
            properties.load(propsInput);
        }catch (FileNotFoundException e){
            //run default interaface and create new config file
        } catch (IOException e) {
            //throw new RuntimeException(e);
        }

        //using the config file
        Swatch.valueOf(properties.getProperty("swatch")).assignTo(scene);
        Theme.valueOf(properties.getProperty("theme")).assignTo(scene);

        scene.getStylesheets().add(DocumentProcessorGluonApplication.class.getResource("style.css").toExternalForm());
        ((Stage) scene.getWindow()).getIcons().add(new Image(DocumentProcessorGluonApplication.class.getResourceAsStream("/draweBannerrIcon.png")));
        ((Stage) scene.getWindow()).setTitle("Document Processor App");
    }

    @Override
    public View getView() throws InterruptedException {
        return null;
    }

    @Override
    public void testView() {
        System.out.print("check\n");
        System.out.print("  From view:\n -controller: ");
        controller.testController();
    }


    public static Controller getController() {
        return controller;
    }

    public static void setController(Controller externController) {
       controller=externController;
    }

    /*
    public static main(String[] args) {
        launch(args);
    }
     */
}
