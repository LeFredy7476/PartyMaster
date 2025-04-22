package dev.FredyRedaTeam.PartyMasterBackend.model.Games.JeuxQuestion;

import dev.FredyRedaTeam.PartyMasterBackend.model.Action;
import dev.FredyRedaTeam.PartyMasterBackend.model.Game;
import dev.FredyRedaTeam.PartyMasterBackend.model.Games.LoupGarou.utils.Sql;
import dev.FredyRedaTeam.PartyMasterBackend.model.Lobby;
import dev.FredyRedaTeam.PartyMasterBackend.model.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class QuestionGame implements Game  {
    private Lobby lobby;
    private final ArrayList<UUID>contenders=new ArrayList<>();
    //demander a reda pourquoi un integer si pas compris
    private Integer lastSpecial=null;
    private GameStateJ gameStateJ=GameStateJ.QUESTION;
    private final HashMap<UUID,Joueur>pointEnter=new HashMap<>();
    private Question currentQuestion;
    private QuestionSpe currentQuestionSpe;
    private UUID gagnant=null;
    private HashMap<UUID,Long> tempsrecu=new HashMap<>();
    private HashMap<UUID,JSONObject>reponserecu=new HashMap<>();
    private Joueur getJoueur(UUID uuid) {return this.pointEnter.get(uuid);}


    @Override
    public String getType() {
        return "Question";
    }

    @Override
    public JSONObject toJson() {
    JSONObject obj=new JSONObject();

    JSONObject point=new JSONObject();
    //this.pointEnter.forEach((uuid, joueur) -> point.put(uuid.toString()));

    return obj;
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
            ArrayList<Question>qstListe=Sql.DonnerQuestion();
            int indexQuestion = Lobby.random.nextInt(qstListe.size());
            currentQuestion = qstListe.get(indexQuestion);




        }catch (Exception e){
            System.out.println(e.getMessage());
        }


    }
    public void ReceiveAction(Action action){
        switch (action.getTarget()[1]){

        }
    }

    public void setListPlayer(){
        //pas besoin d'utiliser un for, java c'est incroyable des fois vraiment
        this.contenders.addAll(lobby.getPlayers().keySet());

        for (UUID uuid:contenders){
            Joueur joueur=new Joueur(uuid,0);
            pointEnter.put(uuid,joueur);
        }

    }
    public boolean verifIci(UUID uuid){

        for (UUID uuid1:contenders){
            if (uuid.equals(uuid1)){
                return true;
            }
            else {
                System.out.println("player not found: "+uuid);
                continue;
            }
        }
        return false;
    }




    public boolean choisirRandomSpe(Lobby lobby){
        this.lobby=lobby;
        int choisisQS=Lobby.random.nextInt(contenders.size());
        if(lastSpecial==choisisQS){
            choisirRandomSpe(lobby);
            return false;
        }
        else {
            this.lastSpecial=choisisQS;
            return true;
            //appeler la methode pour la question speciale
        }

    }
    public Response question(Action action)throws Exception{



        UUID uuid=action.getUuid();
        if(verifIci(uuid)){
            ArrayList<Question>qstListe=Sql.DonnerQuestion();
            int indexQuestion = Lobby.random.nextInt(qstListe.size());
            if(currentQuestion.getId()!=indexQuestion) {
                Question question = qstListe.get(indexQuestion);
                this.lobby.queueEvent(uuid,

                        new QuestionEvent(
                                question.getId(),
                                question.getQuestion(),
                                question.getReponse1(),
                                question.getReponse2(),
                                question.getReponse3(),
                                question.getReponse4(),
                                question.getTypeQuestion()
                        )
                );
                currentQuestion = qstListe.get(question.getId());
                this.lobby.queueEventForAllPlayer(new StateEvent(GameStateJ.QUESTION));
            }else {
                return new Response();
            }


        }else {

            //todo faire le code d'erreur pour dire que le gars est pas la
        }

        return new Response();
    }
    public Response questionspe(Action action)throws Exception{
        UUID uuid=action.getUuid();
        JSONObject ptJoueur=action.getData();
        int ptint=ptJoueur.getInt("niveau");

        if (verifIci(uuid) && choisirRandomSpe(lobby)){
            ArrayList<QuestionSpe>qstListe=Sql.DonnerQuestionSpe(ptint);


                int indexQuestion = Lobby.random.nextInt(qstListe.size());
                QuestionSpe kassos = qstListe.get(indexQuestion);
                this.lobby.queueEvent(uuid,new QuestionSpeEvent(kassos.getId(), kassos.getQuestion(), kassos.getNiveauQuestion()));

                BonneReponseSpe(action,kassos,ptint);
                qstListe.clear();
        }
        return new Response();
    }


    //verifie que c'est la bonne reponse
    public Response BonneReponse(Action action,Question question)throws Exception{
        long now = System.currentTimeMillis();
        UUID uuid=action.getUuid();
        //reponseJoueur est la reponse en json et target la transforme en String
        JSONObject reponseJoueur=action.getData();
        String target =reponseJoueur.getString("target");


        if (verifIci(uuid)){
            if(reponserecu.containsKey(uuid)){
                return new Response();
            }
            tempsrecu.put(uuid,now);
            reponserecu.put(uuid,reponseJoueur);

            if (target.equals(question.getBonneReponse())){
                if (gagnant!=null){
                    return new Response();
                }
                else if (gagnant==null){
                    gagnant=uuid;
                    //methode pour boucler avec for trouver sur internet
                    for (Map.Entry<UUID,JSONObject> truc:reponserecu.entrySet()){
                        UUID challenger=truc.getKey();
                        //get la reponse en json et la prochaine c'est comme en haut qui transforme en string
                        String reponseTruc=truc.getValue().getString("target");
                        //verifie que sa challenge pas contre lui meme
                            if (!challenger.equals(uuid)&&reponseTruc.equals(question.getBonneReponse())){
                                if (tempsrecu.get(challenger)<tempsrecu.get(uuid)){
                                    gagnant=challenger;
                                }
                            }
                    }
                    if (uuid.equals(gagnant)){
                            Joueur joueur=getJoueur(uuid);
                                joueur.addPoint(1);
                            }

                }
            }
        }
        else {
                System.out.println("mauvaise reponse ");
            }
        gagnant=null;
        tempsrecu.clear();
        reponserecu.clear();
        return new Response();
        }


    public Response BonneReponseSpe(Action action,QuestionSpe question,int ptint)throws Exception{
        long now = System.currentTimeMillis();
        UUID uuid=action.getUuid();
        //reponseJoueur est la reponse en json et target la transforme en String
        JSONObject reponseJoueur=action.getData();
        String target =reponseJoueur.getString("target");


        if (verifIci(uuid)){
            if(reponserecu.containsKey(uuid)){
                return new Response();
            }
            tempsrecu.put(uuid,now);
            reponserecu.put(uuid,reponseJoueur);

            if (target.equals(question.getReponse1())){
                if (gagnant!=null){
                    return new Response();
                }
               else if (gagnant==null){
                    gagnant=uuid;
                    //methode pour boucler avec for trouver sur internet
                    for (Map.Entry<UUID,JSONObject> truc:reponserecu.entrySet()){
                        UUID challenger=truc.getKey();
                        //get la reponse en json et la prochaine c'est comme en haut qui transforme en string
                        String reponseTruc=truc.getValue().getString("target");
                        //verifie que sa challenge pas contre lui meme
                        if (!challenger.equals(uuid)&&reponseTruc.equals(question.getReponse1())){
                            if (tempsrecu.get(challenger)<tempsrecu.get(uuid)){
                                gagnant=challenger;
                            }
                        }
                    }

                        if (uuid.equals(gagnant)){
                            Joueur joueur=getJoueur(uuid);
                            joueur.addPoint(ptint);
                        }
                        else {
                            for (Map.Entry<UUID,JSONObject> truc:reponserecu.entrySet()) {
                                String reponseTruc=truc.getValue().getString("target");
                                UUID challenger=truc.getKey();
                                if (!challenger.equals(gagnant) && reponseTruc!= null) {
                                    Joueur joueur=getJoueur(challenger);
                                    joueur.removePoint(ptint);
                                }
                                else {
                                    continue;
                                }
                            }
                        }

                }
            }else {
                System.out.println("mauvaise reponse ");
            }return new Response();
        }
        gagnant=null;
        tempsrecu.clear();
        reponserecu.clear();
        return new Response();
    }
public JSONObject toJsonMasked(Joueur joueur){
        JSONObject out=new JSONObject();
        out.put("gameStateJ",this.gameStateJ);
        out.put("point",joueur.getPoint());
        out.put("question",currentQuestion);
        out.put("questionSpeciale",currentQuestionSpe);
        return out;
}
    public static void main(String[] args) throws Exception{


    }
}
