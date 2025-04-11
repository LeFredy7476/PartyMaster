package dev.FredyRedaTeam.PartyMasterBackend.model.Games.JeuxQuestion;

import dev.FredyRedaTeam.PartyMasterBackend.model.Action;
import dev.FredyRedaTeam.PartyMasterBackend.model.Game;
import dev.FredyRedaTeam.PartyMasterBackend.model.Games.LoupGarou.utils.Sql;
import dev.FredyRedaTeam.PartyMasterBackend.model.Lobby;
import dev.FredyRedaTeam.PartyMasterBackend.model.Response;
import org.json.JSONObject;

import java.util.*;

public class QuestionGame implements Game  {
    private Lobby lobby;
    private final ArrayList<UUID>contenders=new ArrayList<>();
    //demander a reda pourquoi un integer si pas compris
    private Integer lastSpecial=null;



    @Override
    public String getType() {
        return "Question";
    }

    @Override
    public JSONObject toJson() {
        return null;
    }

    @Override
    public Response receiveAction(Action action) {
      return new Response();
    }

    @Override
    public void tick() {

    }

    @Override
    public void init(Lobby lobby) {
        setListPlayer();
        try {

            ArrayList<QuestionSpe>qstSpeListe=Sql.DonnerQuestionSpe();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        this.lobby.queueEventForAllPlayer(new StateEvent(GameStateJ.QUESTION));
    }

    public void setListPlayer(){
        //pas besoin d'utiliser un for, java c'est incroyable des fois vraiment
        this.contenders.addAll(lobby.getPlayers().keySet());
    }
    public boolean verifIci(UUID uuid){

        for (UUID uuid1:contenders){
            if (uuid.equals(uuid1)){
                return true;
            }
            else {
                continue;
            }
        }
        return false;
    }

    public  void  ChoisirRandom(Lobby lobby){
        this.lobby=lobby;
        ;
        int choisisQS=Lobby.random.nextInt(contenders.size());
        if(lastSpecial==choisisQS){
            ChoisirRandom(lobby);
        }
        else {
            this.lastSpecial=choisisQS;
            //appeler la methode pour la question speciale
        }

    }
    public Response question(Action action)throws Exception{
        UUID uuid=action.getUuid();
        if(verifIci(uuid)){
            ArrayList<Question>qstListe=Sql.DonnerQuestion();



            int indexQuestion = Lobby.random.nextInt(qstListe.size());
            Question kassos = qstListe.get(indexQuestion);



            this.lobby.queueEvent(uuid,new QuestionEvent(kassos.getId(), kassos.getQuestion(), kassos.getReponse1(), kassos.getReponse2(), kassos.getReponse3(), kassos.getReponse4(), kassos.getTypeQuestion()));



        }else {

            //todo faire le code d'erreur pour dire que le gars est pas la
        }

        return new Response();
    }

    public static void main(String[] args) throws Exception{


    }
}
