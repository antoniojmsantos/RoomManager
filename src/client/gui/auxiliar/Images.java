package client.gui.auxiliar;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class Images implements Constants {
    private static final Map<String, Image> images = new HashMap<>();

    static {
        images.put(ICON, new Image(Resources.getResourceFile(PATH_IMG_ICON)));
        images.put(AVATAR, new Image(Resources.getResourceFile(PATH_IMG_AVATAR)));
        images.put(LOGO, new Image(Resources.getResourceFile(PATH_IMG_LOGO)));
        images.put(QUESTION, new Image(Resources.getResourceFile(PATH_IMG_QUESTION)));
        images.put(UPLOAD, new Image(Resources.getResourceFile(PATH_IMG_UPLOAD)));
        images.put(DELETE, new Image(Resources.getResourceFile(PATH_IMG_DELETE)));
    }
    public static Image getImage(String name) {
        return images.get(name);
    }
}