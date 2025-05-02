package dev.FredyRedaTeam.PartyMasterBackend.model.Games.utils;

import java.io.FileInputStream;
import java.util.Properties;

public class Connexion {
    public static Properties getProps(String path) throws Exception {
        Properties props = new Properties();
        props.load(new FileInputStream(path));
        return props;
    }
}
