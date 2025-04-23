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
    private GameStateJ gameStateJ=GameStateJ.QUESTION;
    private final HashMap<UUID,Joueur>pointEnter=new HashMap<>();
    private Question currentQuestion;
    private QuestionSpe currentQuestionSpe;
    private UUID gagnant=null;
    private HashMap<UUID,Long> tempsrecu=new HashMap<>();
    private HashMap<UUID,JSONObject>reponserecu=new HashMap<>();
    private Joueur getJoueur(UUID uuid) {return this.pointEnter.get(uuid);}
    private final ArrayList<Question>QuestionUsed=new ArrayList<>();

    private final ArrayList<QuestionSpe>QuestionSpeUsed=new ArrayList<>();


    @Override
    public String getType() {
        return "Question";
    }

    @Override
    public JSONObject toJson() {
    JSONObject obj=new JSONObject();

    JSONObject point=new JSONObject();
    this.pointEnter.forEach((uuid, joueur) -> {point.put(uuid.toString(),joueur.toJson());
    });
    obj.put("point",point);

    obj.put("currentQuestion",this.currentQuestion);

    obj.put("currentQuestionSpe",this.currentQuestionSpe);

    obj.put("gagnant",this.gagnant);
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
            QuestionUsed.add(currentQuestion);

            ArrayList<QuestionSpe>qstSpeListe=Sql.DonnerQuestionSpeInit();
            int indexQuestion2=lobby.random.nextInt(qstSpeListe.size());
            currentQuestionSpe=qstSpeListe.get(indexQuestion2);
            QuestionSpeUsed.add(currentQuestionSpe);



        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
    public Response ReceiveAction(Action action) throws Exception{
        switch (action.getTarget()[1]){
            case "state":
                return new Response(0,this.toJsonMasked(pointEnter.get(action.getUuid())));

            case "question":
                switch (action.getTarget(2)){
                    case "receiveResponse":
                        return BonneReponse(action,currentQuestion);

                }
            case "special":
                switch (action.getTarget(2)){
                    case "receiveResponseSpe":
                        return BonneReponseSpe(action,currentQuestionSpe);
                }

        }
        return new Response();
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


public boolean verifQuestionUsed(int chiffre){
        for (Question question:QuestionUsed){
            if (question.getId()==chiffre){
                return false;

            }
        }
        return true;
}


    public Response question(Action action)throws Exception{
        UUID uuid=action.getUuid();
        if(verifIci(uuid)){
            ArrayList<Question>qstListe=Sql.DonnerQuestion();
            int indexQuestion = Lobby.random.nextInt(qstListe.size());

            if(verifQuestionUsed(indexQuestion)) {
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
                BonneReponse(action,question);
                QuestionUsed.add(currentQuestion);
            }else {
                question(action);
            }


        }else {

            //todo faire le code d'erreur pour dire que le gars est pas la
        }

        return new Response();
    }
    public Response questionspe(Action action)throws Exception{
        UUID uuid=action.getUuid();
        int ptint=Lobby.random.nextInt(3);
        ArrayList<QuestionSpe>qstListe=Sql.DonnerQuestionSpe(ptint);
        int indexQuestion = Lobby.random.nextInt(qstListe.size());
        if (verifIci(uuid)){
            if (currentQuestionSpe.getId()!=indexQuestion) {
                QuestionSpe kassos = qstListe.get(indexQuestion);
                this.lobby.queueEvent(uuid, new QuestionSpeEvent(kassos.getId(), kassos.getQuestion(), kassos.getNiveauQuestion()));
                currentQuestionSpe = kassos;
                BonneReponseSpe(action, kassos);
                qstListe.clear();
            }else {
                questionspe(action);
            }
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


    public Response BonneReponseSpe(Action action,QuestionSpe question)throws Exception{

        UUID uuid=action.getUuid();
        //reponseJoueur est la reponse en json et target la transforme en String
        JSONObject reponseJoueur=action.getData();
        String target =reponseJoueur.getString("target");


        if (verifIci(uuid)){
            if(reponserecu.containsKey(uuid)){
                return new Response();
            }

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
                            joueur.addPoint(question.getNiveauQuestion());
                        }
                        else {
                            for (Map.Entry<UUID,JSONObject> truc:reponserecu.entrySet()) {
                                String reponseTruc=truc.getValue().getString("target");
                                UUID challenger=truc.getKey();
                                if (!challenger.equals(gagnant) && reponseTruc!= null) {
                                    Joueur joueur=getJoueur(challenger);
                                    joueur.removePoint(question.getNiveauQuestion());
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

}
