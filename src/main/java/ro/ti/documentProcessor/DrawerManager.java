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

import static ro.ti.documentProcessor.DocumentProcessorGluonApplication.*;

public class DrawerManager {

    public static void buildDrawer(AppManager app) {
        NavigationDrawer drawer = app.getDrawer();
        NavigationDrawer.Header header = new NavigationDrawer.Header("Document Processor",
                "Views",
                new Avatar(40, new Image(DrawerManager.class.getResourceAsStream("/draweBannerrIcon.png"))));
        header.setStyle("-fx-background-color: white;");
        drawer.setHeader(header);
        final Item mainItem = new ViewItem("Main", MaterialDesignIcon.HOME.graphic(), MAIN_VIEW, ViewStackPolicy.SKIP);
        final Item settingsItem = new ViewItem("Settings", MaterialDesignIcon.SETTINGS.graphic(), SETTINGS_VIEW,ViewStackPolicy.SKIP);
        final Item scannerItem = new Item("Scanner", MaterialDesignIcon.SCANNER.graphic());
        final Item importItem = new ViewItem("Import", MaterialDesignIcon.FILE_DOWNLOAD.graphic(), IMPORT_VIEW,ViewStackPolicy.SKIP);
        final Item exportItem = new ViewItem("Export", MaterialDesignIcon.FILE_UPLOAD.graphic(), EXPORT_VIEW,ViewStackPolicy.SKIP);
        drawer.getItems().addAll(mainItem, settingsItem,scannerItem,importItem,exportItem);
        
        if (Platform.isDesktop()) {
            final Item quitItem = new Item("Quit", MaterialDesignIcon.EXIT_TO_APP.graphic());
            quitItem.selectedProperty().addListener((obs, ov, nv) -> {
                if (nv) {
                    System.exit(0);
                    Services.get(LifecycleService.class).ifPresent(LifecycleService::shutdown);
                }
            });
            drawer.getItems().add(quitItem);
        }
    }
}