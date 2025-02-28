package dev.FredyRedaTeam.PartyMasterBackend.model.Games.LoupGarou;


import dev.FredyRedaTeam.PartyMasterBackend.model.Games.LoupGarou.utils.Connexion;

import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
public class LoupGarouSysteme  {
    public static void GererSysteme() throws Exception{
        Connection con = null;
        Properties props = new Properties();
        props= Connexion.getProps("./src/main/resources/application.properties");
        con = DriverManager.getConnection(props.getProperty("spring.datasource.url"),
                props.getProperty("spring.datasource.username"),
                props.getProperty("spring.datasource.password"));
        PreparedStatement prtm = null;
        ResultSet rs = null;
}
    
}
