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
    private GameStateJ gameStateJ=GameStateJ.QUESTION;
    private final ArrayList<Joueur>PointEnter=new ArrayList<>();

    private Question currentQuestion;
    private QuestionSpe currentQuestionSpe;
    private long sentTimestamp;
    private HashMap<UUID,Long> tempsrecu=new HashMap<>();
    private HashMap<UUID,String>reponserecu=new HashMap<>();


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
            ArrayList<Question>qstListe=Sql.DonnerQuestion();
            int indexQuestion = Lobby.random.nextInt(qstListe.size());
            currentQuestion = qstListe.get(indexQuestion);

            ArrayList<QuestionSpe>qstSpeListe=Sql.DonnerQuestionSpe();
            int indexQuestion2=lobby.random.nextInt(qstSpeListe.size());
            currentQuestionSpe=qstSpeListe.get(indexQuestion2);


        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        this.lobby.queueEventForAllPlayer(new StateEvent(GameStateJ.QUESTION));
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
            PointEnter.add(joueur);
        }
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

    public boolean   ChoisirRandom(Lobby lobby){
        this.lobby=lobby;
        ;
        int choisisQS=Lobby.random.nextInt(contenders.size());
        if(lastSpecial==choisisQS){
            ChoisirRandom(lobby);
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
            this.lobby.queueEvent(
                    uuid,
                    new QuestionEvent(
                            currentQuestion.getId(),
                            currentQuestion.getQuestion(),
                            currentQuestion.getReponse1(),
                            currentQuestion.getReponse2(),
                            currentQuestion.getReponse3(),
                            currentQuestion.getReponse4(),
                            currentQuestion.getTypeQuestion()
                    )
            );



        }else {

            //todo faire le code d'erreur pour dire que le gars est pas la
        }

        return new Response();
    }
    public Response questionspe(Action action)throws Exception{
        UUID uuid=action.getUuid();
        UUID target = UUID.fromString(action.getData().getString("target"));
        if (verifIci(uuid) && ChoisirRandom(lobby)){
            ArrayList<QuestionSpe>qstListe=Sql.DonnerQuestionSpe();
            ArrayList<QuestionSpe>qstListeChoisis=new ArrayList<>();
            if(target.equals("1")){
                for (QuestionSpe questionSpe:qstListe){
                    if (questionSpe.getNiveauQuestion()==1){
                         qstListeChoisis.add(questionSpe);
                    }
                }
                int indexQuestion = Lobby.random.nextInt(qstListeChoisis.size());
                QuestionSpe kassos = qstListeChoisis.get(indexQuestion);
                this.lobby.queueEvent(uuid,new QuestionSpeEvent(kassos.getId(), kassos.getQuestion(), kassos.getNiveauQuestion()));

                BonneReponseSpe(action,kassos,1);
                qstListeChoisis.clear();
            }
            if(target.equals("2")){
                for (QuestionSpe questionSpe:qstListe){
                    if (questionSpe.getNiveauQuestion()==2){
                         qstListeChoisis.add(questionSpe);
                    }
                }
                int indexQuestion = Lobby.random.nextInt(qstListeChoisis.size());
                QuestionSpe kassos = qstListe.get(indexQuestion);
                this.lobby.queueEvent(uuid,new QuestionSpeEvent(kassos.getId(), kassos.getQuestion(), kassos.getNiveauQuestion()));
                BonneReponseSpe(action,kassos,2);
                qstListeChoisis.clear();
            }
            if(target.equals("3")){
                for (QuestionSpe questionSpe:qstListe){
                    if (questionSpe.getNiveauQuestion()==3){
                         qstListeChoisis.add(questionSpe);
                    }
                }
                int indexQuestion = Lobby.random.nextInt(qstListeChoisis.size());
                QuestionSpe kassos = qstListe.get(indexQuestion);
                this.lobby.queueEvent(uuid,new QuestionSpeEvent(kassos.getId(), kassos.getQuestion(), kassos.getNiveauQuestion()));
                BonneReponseSpe(action,kassos,3);
                qstListeChoisis.clear();
            }








        }
        return new Response();
    }


    //verifie que c'est la bonne reponse
    public Response BonneReponse(Action action,Question question)throws Exception{
        long now = System.currentTimeMillis();
        UUID uuid=action.getUuid();
        UUID target = UUID.fromString(action.getData().getString("target"));
        tempsrecu.put(uuid,now);
        if (verifIci(uuid)){
            if (target.equals(question.getBonneReponse())){
                for (Joueur joueur:PointEnter){
                    if (joueur.getUuid().equals(uuid)){
                        joueur.addPoint(1);
                        break;
                    }
                }
            }
        }
        return new Response();
    }
    public Response BonneReponseSpe(Action action,QuestionSpe question,int pointSpe)throws Exception{
        UUID uuid=action.getUuid();
        UUID target = UUID.fromString(action.getData().getString("target"));
        if (verifIci(uuid)){
            if (target.equals(question.getReponse1())){
                for (Joueur joueur:PointEnter){
                    if (joueur.getUuid().equals(uuid)){
                        joueur.addPoint(pointSpe);
                        break;
                    }else {
                        continue;
                    }
                }
            }else {

            }
        }
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
