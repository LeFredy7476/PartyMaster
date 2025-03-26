package dev.FredyRedaTeam.PartyMasterBackend.model.Games.loup;

import dev.FredyRedaTeam.PartyMasterBackend.model.Action;
import dev.FredyRedaTeam.PartyMasterBackend.model.Game;
import dev.FredyRedaTeam.PartyMasterBackend.model.Lobby;
import dev.FredyRedaTeam.PartyMasterBackend.model.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.UUID;

public class LoupGame implements Game {

    private final HashMap<UUID, Joueur> joueurs = new HashMap<>();
    private GameState gameState = GameState.DISTRIBUTION_ROLE;
    private GameState nextGameState = GameState.DISTRIBUTION_ROLE;
    private int manche = 0;
    private final ArrayList<UUID> loups = new ArrayList<>();
    private final HashMap<UUID, UUID> vote = new HashMap<>();
    private Lobby lobby;
    private int confirmAmoureux = 0;
    private UUID protege=null;

    /**
     * game:state
     * game:ready
     * game:cupidon:choose
     * game:amoureux:confirm
     * game:loupgaroux:vote
     * game:voyante:choose
     * game:voyante:confirm
     * game:guardien:choose
     * game:sorciere:choose
     * game:chasseur:choose
     * game:election:vote
     * game:execution:vote
     * game:execution:verdict
     */
    @Override
    public Response receiveAction(Action action) {
        switch (action.getTarget()[1]) {
            case "state":
                return new Response(0, this.toJsonMasked(joueurs.get(action.getUuid())));
            case "loupgaroux":
                switch (action.getTarget()[2]) {
                    case "vote":
                        return loupVote(action);
                }
            case "chasseur":
                switch (action.getTarget()[2]) {
                    case "choose":
                        return chasseurVote(action);
                }
            case "cupidon":
                switch (action.getTarget()[2]) {
                    case "choose":
                        return cupidonChoix(action);
                }
            case "amoureux":
                switch (action.getTarget()[2]) {
                    case "confirm":
                        if (joueurs.get(action.getUuid()).getAmour() != null) {
                            this.confirmAmoureux += 1;
                        }
                        if (confirmAmoureux == 2) {
                            this.gameState = nextGameState;
                            lobby.queueEventForAllPlayer(new StateEvent(gameState));
                            this.nextGameState = GameState.VOYANTE_CHOIX;
                        }
                }
            case "voyante":
                switch (action.getTarget()[2]) {
                    case "choose":
                        return cupidonChoix(action); // TODO: make function
                    case "confirm":
                        return cupidonChoix(action); // TODO: make function
                }
            case "guardian":
                switch (action.getTarget()[2]) {
                    case "choose":
                        return cupidonChoix(action); // TODO: make function

                }
            case "sorciere":
                switch (action.getTarget()[2]) {
                    case "choose":
                        return cupidonChoix(action); // TODO: make function

                }
            case "election":
                switch (action.getTarget()[2]) {
                    case "vote":
                        return cupidonChoix(action); // TODO: make function

                }
            case "execution":
                switch (action.getTarget()[2]) {
                    case "vote":
                        return villageoisChoix(action); // TODO: make function

                    case "verdict":
                        return new Response(); // TODO: implementer le chef

                }

        }
        return null;
    }

    @Override
    public void tick() {

    }

    @Override
    public void init(Lobby lobby) {
        this.lobby = lobby;
        ArrayList<Role> roles = new ArrayList<>();
        int nbJoueurs = lobby.getPlayers().size();
        int nbLoups = Math.min(Math.max((int) Math.round((double) nbJoueurs / 4.0), 1), 3);

        while (nbLoups != 0) {
            roles.add(Role.LOUPGAROUX);
            nbLoups--; nbJoueurs--;
        }

        if (nbJoueurs > 8) {
            roles.add(Role.CUPIDON);
            nbJoueurs--;
        }

        if (nbJoueurs > 6) {
            roles.add(Role.GUARDIEN);
            nbJoueurs--;
        }

        if (nbJoueurs > 5) {
            roles.add(Role.CHASSEUR);
            nbJoueurs--;
        }

        if (nbJoueurs > 4) {
            roles.add(Role.SORCIERE);
            nbJoueurs--;
        }

        if (nbJoueurs > 2) {
            roles.add(Role.VOYANTE);
            nbJoueurs--;
        }

        while (nbJoueurs != 0) {
            roles.add(Role.VILLAGEOIS);
            nbJoueurs--;
        }

        Collections.shuffle(roles);
        for (UUID uuid : lobby.getPlayers().keySet()) {
            Role role = roles.removeFirst();
            joueurs.put(uuid, new Joueur(uuid, role));
        }
        this.lobby.queueEventForAllPlayer(new StateEvent(GameState.DISTRIBUTION_ROLE));
    }


    public Response loupVote(Action action){

        UUID uuid = action.getUuid();
        UUID target = UUID.fromString(action.getContent().getString("target"));
        Joueur joueur = joueurs.get(uuid);
        if (loups.contains(uuid) && joueur.isVivant()) {
            if (joueurs.get(target).isVivant()){
                vote.put(uuid, target);
                boolean endVoteVerif = true;
                for (UUID loup : loups) {
                    if (joueurs.get(loup).isVivant()) {
                        //permet de savoir si le loup a voté en vérifiant si son uuid est dans la liste de ceux qui ont voté
                        endVoteVerif = endVoteVerif && vote.containsKey(loup);
                    }
                }
                if (endVoteVerif) {
                    UUID chosenOne = resolveVote();
                    if (chosenOne != null) {
                        killJoueur(chosenOne);
                    }
                }
                return new Response();
            }else{
                Response r = new Response(3, new JSONObject());
                r.getData().put("r", "InvalidTarget");
                return r; // REFUSED

            }
        } else {
            //message erreur envoyer au joueur copié de la classe lobby
            Response r = new Response(3, new JSONObject());
            r.getData().put("r", "PasUnLoupGarou");
            r.getData().put("PS", "arrete le postman");
            return r; // REFUSED
        }
    }

    public Response chasseurVote(Action action) {
        if (joueurs.get(action.getUuid()).getRole().equals(Role.CHASSEUR)) {
            UUID target = UUID.fromString(action.getContent().getString("target"));
            if (joueurs.get(action.getUuid()).isVivant()) {
                killJoueur(target);
                return new Response(); // OK
            } else {
                Response r = new Response(3, new JSONObject());
                r.getData().put("r", "InvalidTarget");
                return r; // REFUSED
            }
        } else {
            //message erreur envoyer au joueur copié de la classe lobby
            Response r = new Response(3, new JSONObject());
            r.getData().put("r", "PasUnChasseur");
            return r; // REFUSED
        }
    }

    public void killJoueur(UUID uuid) {

        joueurs.get(uuid).setVivant(false);

        if (joueurs.get(uuid).getRole().equals(Role.CHASSEUR)) {
            this.gameState = GameState.CHASSEUR_CHOIX;
            lobby.queueEventForAllPlayer(new StateEvent(GameState.CHASSEUR_CHOIX));
        }

        if(joueurs.get(uuid).getAmour() != null) {

            uuid = joueurs.get(uuid).getAmour();

            joueurs.get(uuid).setVivant(false);

            if (joueurs.get(uuid).getRole().equals(Role.CHASSEUR)) {
                this.gameState = GameState.CHASSEUR_CHOIX;
                lobby.queueEventForAllPlayer(new StateEvent(GameState.CHASSEUR_CHOIX));
            }

        }

    }
    public Response cupidonChoix(Action action){
        if (joueurs.get(action.getUuid()).getRole().equals(Role.CUPIDON)) {
            UUID targetA= UUID.fromString(action.getContent().getString("targetA"));
            UUID targetB = UUID.fromString(action.getContent().getString("targetB"));
            joueurs.get(targetA).setAmour(targetB);
            joueurs.get(targetB).setAmour(targetA);
            this.gameState = nextGameState;
            lobby.queueEventForAllPlayer(new StateEvent(gameState));
            this.nextGameState = GameState.CUPIDON_CHOIX;
            return new Response();
        } else {
            //message erreur envoyer au joueur copié de la classe lobby
            Response r = new Response(3, new JSONObject());
            r.getData().put("r", "PasUnChasseur");
            return r; // REFUSED
        }
    }
    public Response villageoisChoix(Action action){
        //methode a verifier avec frederick
        UUID uuid = action.getUuid();
        UUID target = UUID.fromString(action.getContent().getString("target"));
        if (joueurs.get(target).isVivant()){
            vote.put(uuid, target);
            boolean endVoteVerif = true;

            for (UUID joueur:joueurs.keySet()) {
                if (joueurs.get(joueur).isVivant()) {

                    endVoteVerif = endVoteVerif && vote.containsKey(joueur);
                }
            }
            if (endVoteVerif) {
                UUID chosenOne = resolveVote();
                if (chosenOne != null) {
                    killJoueur(chosenOne);
                }
            }
            return new Response();
        }else{
            Response r = new Response(3, new JSONObject());
            r.getData().put("r", "InvalidTarget");
            return r; // REFUSED

        }




    }
    public Response gardien(Action action){
        //pas sur de celui la a reverifier en equipe
        UUID uuid = action.getUuid();
        UUID target = UUID.fromString(action.getContent().getString("target"));
        if (joueurs.get(action.getUuid()).getRole().equals(Role.GUARDIEN)&&joueurs.get(action.getUuid()).isVivant()){
            vote.put(uuid, target);


            for (UUID joueur:joueurs.keySet()) {
                if (joueurs.get(joueur).isVivant()) {
                    protege=target;
                }
            }
        }



        return new Response();
    }

    public UUID resolveVote() {
        //en gros sa vérifie si le vote est legit mathematiquement
        //la liste prend en compte qui a été voté combien de fois
        HashMap<UUID, Integer> count = new HashMap<>();
        for (UUID v : vote.values()){
            count.putIfAbsent(v,0);//initialise si il est pas présent dans le hashmap
            count.put(v,count.get(v)+1); //compte son vote et est la reponse direct si la personne a déja été voté une fois
        }
        //.max prend le nombre maximum qui a été voté mais si deux personne ont été voté exacquo, .frequency vérifie si il y une égalité
        //, si c'est le cas cela renvoie un false et donc rien n'est return et personne n'est tué
        int max = Collections.max(count.values());
        if (1 == Collections.frequency(count.values(), max)) {
            UUID chosenOne = null;
            for (UUID uuid : count.keySet()) {
                if (count.get(uuid) == max) {
                    chosenOne = uuid;
                }
            }
            return chosenOne;
        } else {
            return null;
        }
    }

    @Override
    public String getType() {
        return "Loup";
    }

    @Override
    public JSONObject toJson() {
        return null;
    }

    public JSONObject toJsonMasked( Joueur joueur ) {
        JSONObject out = new JSONObject();
        out.put("gameState", this.gameState);
        out.put("manche", this.manche);
        out.put("role", joueur.getRole());
        if (joueur.getRole().equals(Role.LOUPGAROUX)) {
            out.put("loups", new JSONArray(this.loups));
        }
        out.put("alive", joueur.isVivant());
        out.put("amour", joueur.getAmour() == null ? 0 : joueur.getAmour());
        return out;
    }
}
