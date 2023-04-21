package ro.ti.documentProcessor;

import com.gluonhq.attach.lifecycle.LifecycleService;
import com.gluonhq.attach.util.Platform;
import com.gluonhq.attach.util.Services;
import com.gluonhq.charm.glisten.application.AppManager;
import com.gluonhq.charm.glisten.application.ViewStackPolicy;
import com.gluonhq.charm.glisten.control.Avatar;
import com.gluonhq.charm.glisten.control.NavigationDrawer;
import com.gluonhq.charm.glisten.control.NavigationDrawer.Item;
import com.gluonhq.charm.glisten.control.NavigationDrawer.ViewItem;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.scene.image.Image;

import static ro.ti.documentProcessor.DocumentProcessorGluonApplication.MAIN_VIEW;
import static ro.ti.documentProcessor.DocumentProcessorGluonApplication.SETTINGS_VIEW;

public class DrawerManager {

    public static void buildDrawer(AppManager app) {
        NavigationDrawer drawer = app.getDrawer();
        NavigationDrawer.Header header = new NavigationDrawer.Header("Document Processor",
                "Views",
                new Avatar(40, new Image(DrawerManager.class.getResourceAsStream("/draweBannerrIcon.png"))));
        header.setStyle("-fx-background-color: white;");
       // ((Avatar)header.getGraphic()).
        drawer.setHeader(header);
        final Item mainItem = new ViewItem("Main", MaterialDesignIcon.HOME.graphic(), MAIN_VIEW, ViewStackPolicy.SKIP);
        final Item settingsItem = new ViewItem("Settings", MaterialDesignIcon.DASHBOARD.graphic(), SETTINGS_VIEW);
        System.out.println(mainItem.getStyle());
        drawer.getItems().addAll(mainItem, settingsItem);
        
        if (Platform.isDesktop()) {
            final Item quitItem = new Item("Quit", MaterialDesignIcon.EXIT_TO_APP.graphic());
            quitItem.selectedProperty().addListener((obs, ov, nv) -> {
                if (nv) {
                    Services.get(LifecycleService.class).ifPresent(LifecycleService::shutdown);
                }
            });
            drawer.getItems().add(quitItem);
        }
    }
}