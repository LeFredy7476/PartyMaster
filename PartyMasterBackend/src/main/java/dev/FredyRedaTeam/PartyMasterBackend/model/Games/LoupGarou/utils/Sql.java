package dev.FredyRedaTeam.PartyMasterBackend.model.Games.LoupGarou.utils;

import dev.FredyRedaTeam.PartyMasterBackend.model.Games.JeuxQuestion.Question;
import dev.FredyRedaTeam.PartyMasterBackend.model.Games.JeuxQuestion.QuestionSpe;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
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
    public static ArrayList<Question> DonnerQuestion()throws Exception{
        Connection con = null;
        Properties props = new Properties();
        PreparedStatement prtm= null;
        ArrayList<Question> questionTout=new ArrayList<>();

        props = Connexion.getProps("./PartyMasterBackend/src/main/resources/application.properties");
        con= DriverManager.getConnection(props.getProperty("spring.datasource.url"),
                props.getProperty("spring.datasource.username"),
                props.getProperty("spring.datasource.password"));
        String sql="select * from question";
        prtm=con.prepareStatement(sql);
        ResultSet rs=prtm.executeQuery();
        try {

            while (rs.next()){
                Question question=new Question();
                question.setId(rs.getInt(1));
                question.setQuestion(rs.getString(2));
                question.setReponse1(rs.getString(3));
                question.setReponse2(rs.getString(4));
                question.setReponse3(rs.getString(5));
                question.setReponse4(rs.getString(6));
                question.setTypeQuestion(rs.getString(7));
                question.setBonneReponse(rs.getString(8));
                questionTout.add(question);
              //  System.out.println(question.getQuestion());
            }


        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            con.close();
            prtm.close();

        }
        return questionTout;
    } public static ArrayList<QuestionSpe> DonnerQuestionSpe()throws Exception{
        Connection con = null;
        Properties props = new Properties();
        PreparedStatement prtm= null;
        ArrayList<QuestionSpe> questionTout=new ArrayList<>();

        props = Connexion.getProps("./PartyMasterBackend/src/main/resources/application.properties");
        con= DriverManager.getConnection(props.getProperty("spring.datasource.url"),
                props.getProperty("spring.datasource.username"),
                props.getProperty("spring.datasource.password"));
        String sql="select * from questionspecial";
        prtm=con.prepareStatement(sql);
        ResultSet rs=prtm.executeQuery();
        try {

            while (rs.next()){
                QuestionSpe questionS=new QuestionSpe();
                questionS.setId(rs.getInt(1));
                questionS.setQuestion(rs.getString(2));
                questionS.setReponse1(rs.getString(3));
                questionS.setNiveauQuestion(rs.getInt(4));
                questionTout.add(questionS);
              //  System.out.println(question.getQuestion());
            }


        }catch (Exception e){
            System.out.println(e.getMessage());
        }finally {
            con.close();
            prtm.close();

        }
        return questionTout;
    }

}
