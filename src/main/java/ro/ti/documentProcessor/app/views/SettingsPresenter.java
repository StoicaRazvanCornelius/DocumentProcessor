package ro.ti.documentProcessor.app.views;

import com.gluonhq.charm.glisten.animation.BounceInRightTransition;
import com.gluonhq.charm.glisten.application.AppManager;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.DropdownButton;
import com.gluonhq.charm.glisten.control.FloatingActionButton;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import com.gluonhq.charm.glisten.visual.Swatch;
import com.gluonhq.charm.glisten.visual.Theme;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuButton;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.MediaView;
import ro.ti.documentProcessor.DocumentProcessorGluonApplication;

import java.awt.*;
import java.net.URISyntaxException;
import java.util.Properties;

public class SettingsPresenter {

    public Properties getAppProperties() {
        return appProperties;
    }

    public void setAppProperties(Properties appProperties) {
        this.appProperties = appProperties;
    }

    Properties appProperties;
    @FXML
    private View settings;

    @FXML
    private static MediaView mediaView;

    @FXML
    Label settingsLabel;
    private static double volume = 100;

    @FXML
    private DropdownButton selectSwatchMenu;
    @FXML
    private DropdownButton selectThemeMenu;

    

    public void initialize() throws URISyntaxException {
        DocumentProcessorGluonApplication.settingsPresenter(this);
        settings.setShowTransitionFactory(BounceInRightTransition::new);
        mediaView = new MediaView();

        /*
        FloatingActionButton fab = new FloatingActionButton(MaterialDesignIcon.INFO.text,
                e -> System.out.println("Info"));
        fab.showOn(settings);
        */
        settings.showingProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue) {
                AppBar appBar = AppManager.getInstance().getAppBar();
                appBar.setNavIcon(MaterialDesignIcon.MENU.button(e -> 
                        AppManager.getInstance().getDrawer().open()));
                appBar.setTitleText("Settings");
                /*
                appBar.getActionItems().add(MaterialDesignIcon.FAVORITE.button(e ->
                System.out.println("Favorite")));
                 */
            }
        });
        selectSwatchMenu.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                System.out.println("Focusing out from ");
            }
        });
    }

    public void clickSound() throws URISyntaxException {
        //clickSound();
    }
    @FXML
    void changeSwatch(ActionEvent event) {
        this.appProperties.setProperty("swatch",selectSwatchMenu.selectedItemProperty().get().getText());
        Swatch.valueOf(appProperties.getProperty("swatch")).assignTo(selectSwatchMenu.getScene());
    }

    @FXML
    void changeTheme(ActionEvent event) {
        this.appProperties.setProperty("theme",selectThemeMenu.selectedItemProperty().get().getText());
        Theme.valueOf(appProperties.getProperty("theme")).assignTo(selectThemeMenu.getScene());

    }

}
