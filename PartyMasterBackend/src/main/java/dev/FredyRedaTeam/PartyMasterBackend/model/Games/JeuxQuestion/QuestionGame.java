package dev.FredyRedaTeam.PartyMasterBackend.model.Games.JeuxQuestion;

import dev.FredyRedaTeam.PartyMasterBackend.model.Action;
import dev.FredyRedaTeam.PartyMasterBackend.model.Game;
import dev.FredyRedaTeam.PartyMasterBackend.model.Games.utils.Sql;
import dev.FredyRedaTeam.PartyMasterBackend.model.Lobby;
import dev.FredyRedaTeam.PartyMasterBackend.model.Response;
import org.json.JSONObject;

import java.util.*;

public class QuestionGame implements Game  {
    private Lobby lobby;
    private final ArrayList<UUID> contenders = new ArrayList<>();
    private GameStateJ gameStateJ = GameStateJ.QUESTION;
    private final HashMap<UUID,Joueur> pointEnter = new HashMap<>();
    private Question currentQuestion;
    private QuestionSpe currentQuestionSpe;
    private UUID gagnant = null;
    private HashMap<UUID,Long> tempsrecu = new HashMap<>();
    private HashMap<UUID,String> reponserecu = new HashMap<>();
    private Joueur getJoueur(UUID uuid) { return this.pointEnter.get(uuid); }
    private final ArrayList<Question> QuestionUsed = new ArrayList<>();
    private final ArrayList<QuestionSpe> QuestionSpeUsed = new ArrayList<>();
    private int nbrQuestion = 0;
    private boolean readyNextQuestion = false;
    private long nextQuestionTimer = System.currentTimeMillis();


    @Override
    public String getType() {
        return "Question";
    }

    @Override
    public JSONObject toJson() {
        JSONObject obj=new JSONObject();

        JSONObject point=new JSONObject();
        this.pointEnter.forEach((uuid, joueur) -> { point.put(uuid.toString(),joueur.toJson()); });
        obj.put("point", point);

        obj.put("currentQuestion", this.currentQuestion);

        obj.put("currentQuestionSpe", this.currentQuestionSpe);

        obj.put("gagnant", this.gagnant);
        return obj;
    }

    @Override
    public Response receiveAction(Action action) {
        try {
            switch (action.getTarget()[1]) {
                case "state":
                    return new Response(0, this.toJsonMasked(pointEnter.get(action.getUuid())));
                case "question":
                    switch (action.getTarget(2)) {
                        case "receiveResponse":
                            return recevoirReponse(action);


                    }
                case "questionSpe":
                    switch (action.getTarget(2)){
                        case "receiveResponse":
                            return recevoirReponseSpe(action);
                    }
//            case "special":
//                switch (action.getTarget(2)){
//                    case "receiveResponseSpe":
//                        return questionspe(action);
//
//                }
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        return new Response();
    }

    @Override
    public void tick() {
        if (readyNextQuestion) {
            if (System.currentTimeMillis() > (nextQuestionTimer + 5000)) {
                try {
                    envoyerQuestion();
                }catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }

        }
    }

    @Override
    public void init(Lobby lobby) {
        setListPlayer();
        try {
            ArrayList<Question> qstListe = Sql.DonnerQuestion();
            int indexQuestion = Lobby.random.nextInt(qstListe.size());
            this.currentQuestion = qstListe.get(indexQuestion);
            QuestionUsed.add(currentQuestion);

            ArrayList<QuestionSpe> qstSpeListe = Sql.DonnerQuestionSpeInit();
            int indexQuestion2 = lobby.random.nextInt(qstSpeListe.size());
            this.currentQuestionSpe = qstSpeListe.get(indexQuestion2);
            QuestionSpeUsed.add(currentQuestionSpe);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }



    public void setListPlayer() {
        //pas besoin d'utiliser un for, java c'est incroyable des fois vraiment
        this.contenders.addAll(lobby.getPlayers().keySet());

        for (UUID uuid : contenders) {
            Joueur joueur = new Joueur(uuid, 0);
            pointEnter.put(uuid, joueur);
        }
    }
    public Response envoyerQuestion()throws Exception {
        if(!(nbrQuestion>=5)) {
            ArrayList<Question> qstListe = Sql.DonnerQuestion();

            if (!qstListe.isEmpty()) {
                int indexQuestion = Lobby.random.nextInt(qstListe.size());
                if (verifQuestionUsed(indexQuestion)) {
                    Question question = qstListe.get(indexQuestion);
                    this.lobby.queueEventForAllPlayer(
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
                    this.currentQuestion = qstListe.get(question.getId());
                    this.lobby.queueEventForAllPlayer(new StateEvent(GameStateJ.QUESTION));

                    QuestionUsed.add(currentQuestion);
                    this.nbrQuestion++;
                    return new Response();
                } else {

                    return envoyerQuestion();


                }
            }
        }else {
            envoyerQuestionSpe();
        }
        return new Response();
    }

    public Response envoyerQuestionSpe()throws Exception{
        int ptint = Lobby.random.nextInt(3);
        ArrayList<QuestionSpe> qstListe = Sql.DonnerQuestionSpe(ptint);
        int indexQuestion = Lobby.random.nextInt(qstListe.size());

            if (verifQuestionSpeUsed(indexQuestion)) {
                QuestionSpe kassos = qstListe.get(indexQuestion);
                this.lobby.queueEventForAllPlayer(new QuestionSpeEvent(kassos.getId(), kassos.getQuestion(), kassos.getNiveauQuestion()));
                this.currentQuestionSpe = kassos;

                this.lobby.queueEventForAllPlayer(new StateEvent(GameStateJ.QUESTION_SPECIAL));
                QuestionSpeUsed.add(currentQuestionSpe);
                this.nbrQuestion = 0;
                return new Response();
            } else {
                return envoyerQuestionSpe();
            }

    }

    public boolean verifIci(UUID uuid) {
        return pointEnter.containsKey(uuid);
    }

    public boolean verifQuestionUsed(int chiffre) {
        for (Question question : QuestionUsed) {
            if (question.getId() == chiffre || chiffre == currentQuestion.getId() && currentQuestion != null) {
                return false;
            }
        }
        return true;
    }

    public boolean verifQuestionSpeUsed(int chiffre) {
        for (QuestionSpe question : QuestionSpeUsed) {
            if (question.getId() == chiffre || chiffre == currentQuestionSpe.getId() && currentQuestionSpe != null) {
                return false;
            }
        }
        return true;
    }

    public Response recevoirReponse(Action action) throws Exception {

        long now = System.currentTimeMillis();
        UUID uuid = action.getUuid();

        JSONObject reponseJoueur = action.getData();
        String target = reponseJoueur.getString("target");
        if (verifIci(uuid)) {
            if (reponserecu.containsKey(uuid)) {
                Response r = new Response(3, new JSONObject());
                r.getData().put("r", "AlreadyAnswered");
                return r; // REFUSED
            }else {
                tempsrecu.put(uuid, now);
                reponserecu.put(uuid, target);
            }
            if (contenders.size()==reponserecu.size()){
                br();
            }else {
                Response r = new Response(3, new JSONObject());
                r.getData().put("r", "NotEveryoneResponded");
                return r; // IGNORED
            }
            } else {
            Response r = new Response(3, new JSONObject());
            r.getData().put("r", "UnknownPlayer");
            return r; // IGNORED
        }

        return new Response();
    }

    public Response recevoirReponseSpe(Action action) throws Exception{
        long now = System.currentTimeMillis();
        UUID uuid = action.getUuid();
        //reponseJoueur est la reponse en json et target la transforme en String
        JSONObject reponseJoueur=action.getData();
        String target =reponseJoueur.getString("target");

        if (verifIci(uuid)) {
            if (reponserecu.containsKey(uuid)) {
                Response r = new Response(3, new JSONObject());
                r.getData().put("r", "AlreadyAnswered");
                return r; // REFUSED
            }else {
                tempsrecu.put(uuid, now);
                reponserecu.put(uuid, target);

            }
            if (contenders.size()==reponserecu.size()){
               brSpe();
            }else {
                Response r = new Response(3, new JSONObject());
                r.getData().put("r", "NotEveryoneResponded");
                return r; // IGNORED
            }

        }else {
            Response r = new Response(3, new JSONObject());
            r.getData().put("r", "UnknownPlayer");
            return r; // IGNORED
        }



        return new Response();
    }
    public ArrayList<UUID> verifReponse(){
        ArrayList <UUID>bonneReponse=null;
        for (Map.Entry<UUID,String> reponseMec:reponserecu.entrySet()){
            if (reponseMec.getValue().equals(currentQuestion.getBonneReponse())){
                 bonneReponse.add(reponseMec.getKey());
            }
            else {
                System.out.println("mauvaise reponse de : "+reponseMec.getKey());

            }
        }
        return bonneReponse;
    }
    public ArrayList<UUID>verifReponseSpe(){
        ArrayList<UUID>bonneReponse=null;
        for (Map.Entry<UUID,String> reponseMec:reponserecu.entrySet()){
            if (reponseMec.getValue().equals(currentQuestion.getBonneReponse())){
                bonneReponse.add(reponseMec.getKey());
            }
            else {
                if (reponseMec.getValue()!=null) {
                    Joueur joueur = getJoueur(reponseMec.getKey());
                    joueur.removePoint(currentQuestionSpe.getNiveauQuestion());

                }else{
                    System.out.println("joueur n'a pas repondu ");
                }
            }
        }
        return bonneReponse;
    }
    public Response br(){
        ArrayList<UUID> gagnantPotentiel=verifReponse();
        Question question=currentQuestion;
        UUID premier=null;
        int compteur=0;
        if (gagnantPotentiel.isEmpty()){
            Response r = new Response(3, new JSONObject());
            r.getData().put("r", "NoWinner");
            return r; // REFUSED
        }
        for(Map.Entry<UUID,Long> mec:tempsrecu.entrySet()){
           if (mec.getKey()==gagnantPotentiel.get(compteur)){
               premier=mec.getKey();
               Joueur joueur=getJoueur(premier);
               joueur.addPoint(1);
               lobby.queueEventForAllPlayer(new StateEvent(GameStateJ.REVELATIONBM));
               lobby.queueEventForAllPlayer(new QuestionResultatEvent(question.getId(),question.getBonneReponse()));
               tempsrecu.clear();
               reponserecu.clear();
               return new Response();
           }else{
               compteur++;
           }
        }

        System.out.println("gagnant "+premier);

        return new Response();
    }
    public Response brSpe(){
        ArrayList<UUID> gagnantPotentiel=verifReponseSpe();
        QuestionSpe question=currentQuestionSpe;
        UUID premier=null;
        int compteur=0;
        if (gagnantPotentiel.isEmpty()){
            Response r = new Response(3, new JSONObject());
            r.getData().put("r", "NoWinner");
            return r; // REFUSED
        }
        for(Map.Entry<UUID,Long> mec:tempsrecu.entrySet()){
           if (mec.getKey()==gagnantPotentiel.get(compteur)){
               premier=mec.getKey();
               Joueur joueur=getJoueur(premier);
               joueur.addPoint(question.getNiveauQuestion());
               lobby.queueEventForAllPlayer(new StateEvent(GameStateJ.REVELATIONBM));
               lobby.queueEventForAllPlayer(new QuestionResultatEvent(question.getId(),question.getReponse1()));
               tempsrecu.clear();
               reponserecu.clear();
               return new Response();
           }else{
               compteur++;
           }
        }

        System.out.println("gagnant "+premier);

        return new Response();
    }

 //  sa doit envoyer le state du jeux au joueur mais que le joueur ne puisse pas le voir avec wire shark pour tricher 
public JSONObject toJsonMasked(Joueur joueur) {
        JSONObject out = new JSONObject();
        out.put("gameStateJ", this.gameStateJ);
        out.put("point", joueur.getPoint());
        out.put("question", currentQuestion);
        out.put("questionSpeciale", currentQuestionSpe);
        return out;
}

}
