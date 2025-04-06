package dev.FredyRedaTeam.PartyMasterBackend.model.Games.LoupGarou.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import java.util.Properties;

public class Sql {
    public static void InsertGame(String id,long creation)throws Exception{
        Connection con = null;
        Properties props = new Properties();
        PreparedStatement prtm=null;
        props = Connexion.getProps("./PartyMasterBackend/src/main/resources/application.properties");
        con= DriverManager.getConnection(props.getProperty("spring.datasource.url"),
             props.getProperty("spring.datasource.username"),
             props.getProperty("spring.datasource.password"));

        try {
            String sql="INSERT INTO historiquegame(id,creation) VALUES (?,?)";
            prtm=con.prepareStatement(sql);
            prtm.setString(1,id);
            prtm.setLong(2,creation);
            prtm.executeUpdate();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            con.close();
            prtm.close();

        }
    }

}
