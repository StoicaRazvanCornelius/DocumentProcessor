package ro.ti.documentProcessor.app.views;

import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.AppManager;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.FloatingActionButton;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.fxml.FXML;
import javafx.scene.media.MediaView;
import ro.ti.documentProcessor.DocumentProcessorGluonApplication;

import java.awt.*;
import java.net.URISyntaxException;

public class SettingsPresenter {

    @FXML
    private View settings;

    @FXML
    private static MediaView mediaView;

    @FXML
    Label settingsLabel;
    private static double volume = 100;

    public void initialize() throws URISyntaxException {
        settings.setShowTransitionFactory(BounceInRightTransition::new);
        mediaView = new MediaView();
        FloatingActionButton fab = new FloatingActionButton(MaterialDesignIcon.INFO.text,
                e -> System.out.println("Info"));
        fab.showOn(settings);
        
        settings.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = AppManager.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> 
                        AppManager.getInstance().getDrawer().open()));
                appBar.setTitleText("Settings");
                appBar.getActionItems().add(MaterialDesignIcon.FAVORITE.button(e -> 
                        System.out.println("Favorite")));
            }
        });

    }

    public void clickSound() throws URISyntaxException {
        clickSound();
    }



}
