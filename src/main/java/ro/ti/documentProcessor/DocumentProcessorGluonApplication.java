package ro.ti.documentProcessor;

import com.gluonhq.charm.glisten.visual.Theme;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import ro.ti.documentProcessor.views.ExportView;
import ro.ti.documentProcessor.views.ImportView;
import ro.ti.documentProcessor.views.MainView;
import ro.ti.documentProcessor.views.SettingsView;
import com.gluonhq.charm.glisten.application.AppManager;
import com.gluonhq.charm.glisten.visual.Swatch;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URISyntaxException;

import static com.gluonhq.charm.glisten.application.AppManager.HOME_VIEW;

public class DocumentProcessorGluonApplication extends Application {

    public static final String MAIN_VIEW = HOME_VIEW;
    public static final String SETTINGS_VIEW = "Settings View";

    public static final String IMPORT_VIEW = "Import View";
    public static final String EXPORT_VIEW = "Export View";

    private final AppManager appManager = AppManager.initialize(this::postInit);

    private static MediaView mediaView;
    private static double volume = 100;

    @Override
    public void init() throws URISyntaxException {
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
        Theme.LIGHT.assignTo(scene);
        Swatch.AMBER.assignTo(scene);

        scene.getStylesheets().add(DocumentProcessorGluonApplication.class.getResource("style.css").toExternalForm());
        ((Stage) scene.getWindow()).getIcons().add(new Image(DocumentProcessorGluonApplication.class.getResourceAsStream("/draweBannerrIcon.png")));
        ((Stage) scene.getWindow()).setTitle("Document Processor App");
    }

    public static void main(String[] args) {
        launch(args);
    }
}
