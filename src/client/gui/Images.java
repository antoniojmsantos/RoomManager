package client.gui;

import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class Images implements Constants {
    private static final Map<String, Image> images = new HashMap<>();

    static {
        images.put(AVATAR, new Image(Resources.getResourceFile(PATH_IMG_AVATAR)));
        images.put(LOGO, new Image(Resources.getResourceFile(PATH_IMG_LOGO)));

    }
    public static Image getImage(String name) {
        return images.get(name);
    }
}
