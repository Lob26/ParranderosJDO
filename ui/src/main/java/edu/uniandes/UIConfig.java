package edu.uniandes;

import edu.uniandes.view.ParranderosV;

import java.awt.*;
import java.net.URL;
import java.util.Objects;

public class UIConfig {
    private static final URL[] CONFIG_ROUTES = {
            UIConfig.class.getResource("/app.yaml"),
            UIConfig.class.getResource("/two_level_events.yaml"),
    };
    public static Color[] DARK_STATUS = {
            new Color(243, 243, 243),//null
            new Color(0, 62, 31),//true
            new Color(89, 0, 4)//false
    };
    public static Color[] LIGHT_STATUS = {
            new Color(13, 13, 13),//null
            new Color(41, 144, 61),//true
            new Color(140, 28, 19)//false
    };

    static void run(ParranderosV v)
            throws Exception {
        v.configMenu(Objects.requireNonNull(CONFIG_ROUTES[1]));
        v.configView(Objects.requireNonNull(CONFIG_ROUTES[0]));
        v.light();
    }
}
