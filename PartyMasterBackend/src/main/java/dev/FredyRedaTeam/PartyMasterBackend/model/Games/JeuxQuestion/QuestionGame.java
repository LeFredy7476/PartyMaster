package dev.FredyRedaTeam.PartyMasterBackend.model.Games.JeuxQuestion;

import dev.FredyRedaTeam.PartyMasterBackend.model.Action;
import dev.FredyRedaTeam.PartyMasterBackend.model.Game;
import dev.FredyRedaTeam.PartyMasterBackend.model.Games.LoupGarou.utils.Sql;
import dev.FredyRedaTeam.PartyMasterBackend.model.Lobby;
import dev.FredyRedaTeam.PartyMasterBackend.model.Response;
import dev.FredyRedaTeam.PartyMasterBackend.model.Games.LoupGarou.*;

import java.util.*;
import java.util.HashMap;
import java.util.Random;

public class QuestionGame  {
    private Lobby lobby;
    private final ArrayList<UUID>contenders=new ArrayList<>();
    //demander a reda pourquoi un integer si pas compris
    private Integer lastSpecial=null;


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
    public Response question(Action action){
        UUID uuid=action.getUuid();
        if(verifIci(uuid)){

        }else {

            //todo faire le code d'erreur pour dire que le gars est pas la
        }

        return new Response();
    }

    public static void main(String[] args) throws Exception{
        ArrayList<Question>qst=Sql.DonnerQuestion();
        System.out.println(qst);
    }
}
