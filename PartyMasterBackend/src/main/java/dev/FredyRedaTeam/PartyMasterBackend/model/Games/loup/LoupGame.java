package dev.FredyRedaTeam.PartyMasterBackend.model.Games.loup;

import dev.FredyRedaTeam.PartyMasterBackend.model.Action;
import dev.FredyRedaTeam.PartyMasterBackend.model.Game;
import dev.FredyRedaTeam.PartyMasterBackend.model.Lobby;
import dev.FredyRedaTeam.PartyMasterBackend.model.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class LoupGame implements Game {

    private final HashMap<UUID, Joueur> joueurs = new HashMap<>();
    private GameState gameState = GameState.DISTRIBUTION_ROLE;
    private GameState nextGameState = GameState.DISTRIBUTION_ROLE;
    private int manche = 0;
    private final ArrayList<UUID> loups = new ArrayList<>();
    private final HashMap<UUID, UUID> votes = new HashMap<>();
    private final ArrayList<UUID> vivants = new ArrayList<>();
    private final HashSet<UUID> readyCheck = new HashSet<>();
    private Lobby lobby;
    private int confirmAmoureux = 0;
    private UUID protege = null;
    private UUID connaisseur = null;

    private boolean isVoyanteAlive() {
        for (UUID uuid : vivants) {
            if (joueurs.get(uuid).getRole().equals(Role.VOYANTE)) {
                return true;
            }
        }
        return false;
    }

    private boolean isGuardianAlive() {
        for (UUID uuid : vivants) {
            if (joueurs.get(uuid).getRole().equals(Role.GUARDIEN)) {
                return true;
            }
        }
        return false;
    }

    private boolean isTraitreAlive() {
        for (UUID uuid : vivants) {
            if (joueurs.get(uuid).getRole().equals(Role.TRAITRE)) {
                return true;
            }
        }
        return false;
    }

    private boolean isCupidonAlive() {
        for (UUID uuid : vivants) {
            if (joueurs.get(uuid).getRole().equals(Role.CUPIDON)) {
                return true;
            }
        }
        return false;
    }

    /**
     * game:state
     * game:ready
     * game:cupidon:choose
     * game:amoureux:confirm
     * game:loupgaroux:vote
     * game:voyante:choose
     * game:voyante:confirm
     * game:guardien:choose
     * game:traitre:choose
     * game:traitre:confirm
     * game:chasseur:choose
     * game:election:vote
     * game:execution:vote
     * game:execution:verdict
     */
    @Override
    public Response receiveAction(Action action) {
        switch (action.getTarget(1)) {
            case "state":
                // probably unused
                return new Response(0, this.toJsonMasked(joueurs.get(action.getUuid())));
            case "ready":
                if (gameState.equals(GameState.DISTRIBUTION_ROLE)) {
                    this.readyCheck.add(action.getUuid());
                    // System.out.println("ready check needed : " + readyCheck.size() + "  needed : " + this.joueurs.size());
                    if (readyCheck.size() == this.joueurs.size()) {
                        if (isCupidonAlive()) {
                            this.gameState = GameState.CUPIDON_CHOIX;
                            this.nextGameState = GameState.AMOUREUX_REVELATION;
                        } else if (isVoyanteAlive()) {
                            this.gameState = GameState.VOYANTE_CHOIX;
                            this.nextGameState = GameState.VOYANTE_REVELATION;
                        } else if (isGuardianAlive()) {
                            this.gameState = GameState.GUARDIEN_CHOIX;
                            this.nextGameState = GameState.LOUP_CHOIX;
                        } else {
                            this.gameState = GameState.LOUP_CHOIX;
                            if (isTraitreAlive()) {
                                this.nextGameState = GameState.TRAITRE_CHOIX;
                            } else {
                                this.nextGameState = GameState.VILLAGE_EXECUTION;
                            }
                        }
                        this.lobby.queueEventForAllPlayer(new StateEvent(this.gameState));
                    }
                }
                return new Response();
            case "loupgaroux":
                switch (action.getTarget(2)) {
                    case "vote":
                        return loupVote(action);
                }
            case "chasseur":
                switch (action.getTarget(2)) {
                    case "choose":
                        return chasseurVote(action);
                }
            case "cupidon":
                switch (action.getTarget(2)) {
                    case "choose":
                        return cupidonChoix(action);
                }
            case "amoureux":
                switch (action.getTarget(2)) {
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
                switch (action.getTarget(2)) {
                    case "choose":
                        return voyante(action);
                    case "confirm":
                        if (action.getUuid().equals(connaisseur)){
                            this.connaisseur = null;
                            if (isGuardianAlive()) {
                                this.gameState = GameState.GUARDIEN_CHOIX;
                                this.nextGameState = GameState.LOUP_CHOIX;
                            } else if (isTraitreAlive()) {
                                this.gameState = GameState.LOUP_CHOIX;
                                this.nextGameState = GameState.TRAITRE_CHOIX;
                            } else {
                                this.gameState = GameState.LOUP_CHOIX;
                                this.nextGameState = GameState.VILLAGE_EXECUTION;
                            }
                            lobby.queueEventForAllPlayer(new StateEvent(gameState));
                            return new Response();
                        }
                }
            case "guardian":
                switch (action.getTarget(2)) {
                    case "choose":
                        return gardien(action);

                }
            case "traitre":
                switch (action.getTarget(2)) {
                    case "choose":
                        return traitre(action);
                    case "confirm":
                        if (action.getUuid().equals(connaisseur)){
                            this.connaisseur = null;
                            this.gameState = GameState.VILLAGE_EXECUTION;
                            if (isVoyanteAlive()) {
                                this.nextGameState = GameState.VOYANTE_CHOIX;
                            } else if (isGuardianAlive()) {
                                this.nextGameState = GameState.GUARDIEN_CHOIX;
                            } else {
                                this.nextGameState = GameState.LOUP_CHOIX;
                            }
                            lobby.queueEventForAllPlayer(new StateEvent(gameState));
                            return new Response();
                        }


                }

            // case "election":
            //     switch (action.getTarget()[2]) {
            //         case "vote":
            //             return cupidonChoix(action);

            //     }
            case "execution":
                switch (action.getTarget()[2]) {
                    case "vote":
                        return villageChoix(action);

                    // case "verdict":
                    //     return new Response(); //

                }

        }
        Response r = new Response(2, new JSONObject());
        r.getData().put("r", "UnknownAction");
        return r; // IGNORED
    }

    @Override
    public void tick() {

    }

    @Override
    public void init(Lobby lobby) {
        this.lobby = lobby;
        ArrayList<Role> roles = new ArrayList<>();
        int nbJoueurs = lobby.getPlayers().size();
        int nbLoups = Math.min(Math.max((int) Math.round((double) nbJoueurs / 4.0), 1), 4);

        while (nbLoups != 0) {
            if (nbLoups == 4) {
                roles.add(Role.LOUPBLANC);
            } else {
                roles.add(Role.LOUP);
            }
            nbLoups--; nbJoueurs--;
        }
        if (nbJoueurs > 12) { roles.add(Role.CUPIDON); nbJoueurs--; }
        if (nbJoueurs > 6) { roles.add(Role.GUARDIEN); nbJoueurs--; }
        if (nbJoueurs > 5) { roles.add(Role.CHASSEUR); nbJoueurs--; }
        if (nbJoueurs > 9) { roles.add(Role.TRAITRE); nbJoueurs--; }
        if (nbJoueurs > 2) { roles.add(Role.VOYANTE); nbJoueurs--; }
        while (nbJoueurs != 0) { roles.add(Role.VILLAGEOIS); nbJoueurs--; }

        Collections.shuffle(roles);
        for (UUID uuid : lobby.getPlayers().keySet()) {
            this.vivants.add(uuid);
            Role role = roles.removeFirst();
            Joueur joueur = new Joueur(uuid, role);
            joueurs.put(uuid, joueur);
            if (role.equals(Role.LOUPBLANC)) { loups.add(uuid); }
            else if (role.equals(Role.LOUP)) { loups.add(uuid); }
        }

        // this.lobby.queueEventForAllPlayer(new StateEvent(GameState.DISTRIBUTION_ROLE));
        this.gameState = GameState.DISTRIBUTION_ROLE;

        if (this.isCupidonAlive()) { this.nextGameState = GameState.CUPIDON_CHOIX; }
        else {
            if (this.isVoyanteAlive()) { this.nextGameState = GameState.VOYANTE_CHOIX; }
            else {
                if (this.isGuardianAlive()) { this.nextGameState = GameState.GUARDIEN_CHOIX; }
                else { this.nextGameState = GameState.LOUP_CHOIX; }
            }
        }
    }

    public Response loupVote(Action action) {

        UUID uuid = action.getUuid();
        UUID target = UUID.fromString(action.getData().getString("target"));
        Joueur joueur = joueurs.get(uuid);
        if (loups.contains(uuid) && joueur.isVivant()) {
            if (joueurs.get(target).isVivant()) {
                votes.put(uuid, target);
                boolean endVoteVerif = true;
                for (UUID loup : loups) {
                    if (joueurs.get(loup).isVivant()) {
                        //permet de savoir si le loup a voté en vérifiant si son uuid est dans la liste de ceux qui ont voté
                        endVoteVerif = endVoteVerif && votes.containsKey(loup);
                    }
                }
                if (endVoteVerif) {
                    UUID chosenOne = resolveVote();
                    if (chosenOne != null) {
                        this.lobby.queueEvent(connaisseur, new RevelationEvent(target, joueurs.get(target).getRole(), "traitre"));

                        // TODO: faire en sorte que la mise à mort se fasse au lever du jour

                        if (killJoueur(chosenOne)) {
                            if (isTraitreAlive()) {
                                this.gameState = GameState.TRAITRE_CHOIX;
                                this.nextGameState = GameState.TRAITRE_REVELATION;
                            } else {
                                this.gameState = GameState.VILLAGE_EXECUTION;
                                if (isVoyanteAlive()) {
                                    this.nextGameState = GameState.VOYANTE_CHOIX;
                                } else if (isGuardianAlive()) {
                                    this.nextGameState = GameState.GUARDIEN_CHOIX;
                                } else {
                                    this.nextGameState = GameState.LOUP_CHOIX;
                                }
                            }
                            lobby.queueEventForAllPlayer(new StateEvent(gameState));
                        }
                    }
                }
                return new Response();
            } else {
                Response r = new Response(3, new JSONObject());
                r.getData().put("r", "InvalidTarget");
                return r; // REFUSED
            }
        } else {
            //message erreur envoyer au joueur copié de la classe lobby
            Response r = new Response(3, new JSONObject());
            r.getData().put("r", "PasUnLoup");
            r.getData().put("PS", "arrete le postman");
            return r; // REFUSED
        }
    }

    public Response chasseurVote(Action action) {
        if (joueurs.get(action.getUuid()).getRole().equals(Role.CHASSEUR)) {
            UUID target = UUID.fromString(action.getData().getString("target"));
            if (joueurs.get(action.getUuid()).isVivant()) {

                killJoueur(target);

                this.gameState = nextGameState;
                if (this.gameState.equals(GameState.VOYANTE_CHOIX) && !isVoyanteAlive()) {
                    this.gameState = GameState.GUARDIEN_CHOIX;
                }
                if (this.gameState.equals(GameState.GUARDIEN_CHOIX) && !isGuardianAlive()) {
                    this.gameState = GameState.LOUP_CHOIX;
                }
                if (this.gameState.equals(GameState.TRAITRE_CHOIX) && !isTraitreAlive()) {
                    this.gameState = GameState.VILLAGE_EXECUTION;
                }
                lobby.queueEventForAllPlayer(new StateEvent(gameState));
                return new Response();
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

    public Response cupidonChoix(Action action) {
        if (joueurs.get(action.getUuid()).getRole().equals(Role.CUPIDON)) {
            UUID targetA = UUID.fromString(action.getData().getString("targetA"));
            UUID targetB = UUID.fromString(action.getData().getString("targetB"));
            joueurs.get(targetA).setAmour(targetB);
            joueurs.get(targetB).setAmour(targetA);
            
            this.gameState = GameState.VOYANTE_CHOIX;
            this.nextGameState = GameState.VOYANTE_REVELATION;
           
            lobby.queueEventForAllPlayer(new StateEvent(gameState));
            return new Response();
        } else {
            //message erreur envoyer au joueur copié de la classe lobby
            Response r = new Response(3, new JSONObject());
            r.getData().put("r", "PasUnChasseur");
            return r; // REFUSED
        }
    }

    public Response villageChoix(Action action) {
        UUID uuid = action.getUuid();
        UUID target = UUID.fromString(action.getData().getString("target"));
        if(joueurs.get(uuid).isVivant()) {
            if (joueurs.get(target).isVivant()) {
                votes.put(uuid, target);
                boolean endVoteVerif = true;

                for (UUID joueur : joueurs.keySet()) {
                    if (joueurs.get(joueur).isVivant()) {
                        endVoteVerif = endVoteVerif && votes.containsKey(joueur);
                    }
                }

                if (endVoteVerif) {
                    UUID chosenOne = resolveVote();
                    if (chosenOne != null) {
                        if (killJoueur(chosenOne)) {
                            if (isVoyanteAlive()) {
                                this.gameState = GameState.VOYANTE_CHOIX;
                                this.nextGameState = GameState.VOYANTE_REVELATION;
                            } else {
                                if (isGuardianAlive()) {
                                    this.gameState = GameState.GUARDIEN_CHOIX;
                                    this.nextGameState = GameState.LOUP_CHOIX;
                                } else {
                                    this.gameState = GameState.LOUP_CHOIX;
                                    if (isTraitreAlive()) { this.nextGameState = GameState.TRAITRE_CHOIX; }
                                    else { this.nextGameState = GameState.VILLAGE_EXECUTION; }
                                }
                            }
                            lobby.queueEventForAllPlayer(new StateEvent(gameState));
                        }
                        this.manche++;
                    }
                }
                return new Response();
            } else {
                Response r = new Response(3, new JSONObject());
                r.getData().put("r", "InvalidTarget");
                return r; // REFUSED
            }
        } else {
            Response r = new Response(3, new JSONObject());
            r.getData().put("r", "VoteurMort");
            return r; // REFUSED
        }
    }

    public Response traitre(Action action) {
        UUID uuid = action.getUuid();
        UUID target = UUID.fromString(action.getData().getString("target"));
        if (isTraitreAlive() && joueurs.get(uuid).getRole().equals(Role.TRAITRE)) {
            this.connaisseur = vivants.get(Lobby.random.nextInt(vivants.size()));
            // cette ligne ci dessous regarde le uuid de celui qui receveras l'info et il appelle l'event qui envoie le uuid de la cible vers lui
            this.lobby.queueEvent(connaisseur, new RevelationEvent(target, joueurs.get(target).getRole(), "traitre"));
            this.gameState = GameState.TRAITRE_REVELATION;
            lobby.queueEventForAllPlayer(new StateEvent(gameState));
            this.nextGameState = GameState.VILLAGE_EXECUTION;
            return new Response();
        } else {
            Response r = new Response(3, new JSONObject());
            r.getData().put("r", "pasUnTraitre");
            return r; // REFUSED
        }
    }

    public Response gardien(Action action) {
        // pas sur de celui la a reverifier en equipe
        UUID uuid = action.getUuid();
        UUID target = UUID.fromString(action.getData().getString("target"));
        if (joueurs.get(uuid).getRole().equals(Role.GUARDIEN) && joueurs.get(uuid).isVivant()) {
            if (joueurs.get(target).isVivant() && !this.protege.equals(target)) {
                this.protege = target;
                this.gameState = GameState.LOUP_CHOIX;
                if (isTraitreAlive()) { this.nextGameState = GameState.TRAITRE_CHOIX; }
                else { this.nextGameState = GameState.VILLAGE_EXECUTION; }
                lobby.queueEventForAllPlayer(new StateEvent(gameState));
                return new Response();
            } else {
                Response r = new Response(3, new JSONObject());
                r.getData().put("r", "InvalidTarget");
                return r; // REFUSED
            }
        } else {
            Response r = new Response(3, new JSONObject());
            r.getData().put("r", "pasUnGardien");
            return r; // REFUSED
        }
    }

    public Response voyante(Action action) {
        UUID uuid = action.getUuid();
        UUID target = UUID.fromString(action.getData().getString("target"));
        if (joueurs.get(uuid).getRole().equals(Role.VOYANTE)&&joueurs.get(uuid).isVivant()) {
            this.connaisseur = uuid;
            this.lobby.queueEvent(uuid, new RevelationEvent(target, joueurs.get(target).getRole(), "voyante"));
            this.gameState = GameState.VOYANTE_REVELATION;
            if (isGuardianAlive()) { this.nextGameState = GameState.GUARDIEN_CHOIX; }
            else { this.nextGameState = GameState.LOUP_CHOIX; }
            lobby.queueEventForAllPlayer(new StateEvent(gameState));
            return new Response();
        } else {
            Response r = new Response(3, new JSONObject());
            r.getData().put("r", "pasUneVoyante");
            return r; // REFUSED
        }
    }

    public boolean killJoueur(UUID uuid) {

        joueurs.get(uuid).setVivant(false);
        vivants.remove(uuid);
        this.lobby.queueEventForAllPlayer( new DeathEvent(uuid,System.currentTimeMillis()));
        boolean _continue = true;

        DecideWinner();

        if (joueurs.get(uuid).getRole().equals(Role.CHASSEUR)) {
            this.gameState = GameState.CHASSEUR_CHOIX;
            lobby.queueEventForAllPlayer(new StateEvent(GameState.CHASSEUR_CHOIX));
            _continue = false;
            DecideWinner();
        }

        if(joueurs.get(uuid).getAmour() != null) {

            UUID uuid2 = joueurs.get(uuid).getAmour();

            joueurs.get(uuid2).setVivant(false);
            vivants.remove(uuid2);

            DecideWinner();

            this.lobby.queueEventForAllPlayer(new DeathEvent(uuid2,System.currentTimeMillis()));

            if (joueurs.get(uuid2).getRole().equals(Role.CHASSEUR)) {
                this.gameState = GameState.CHASSEUR_CHOIX;
                DecideWinner();
                lobby.queueEventForAllPlayer(new StateEvent(GameState.CHASSEUR_CHOIX));
                _continue = false;
            }
        }
        return _continue;
    }

    public UUID resolveVote() {
        // en gros sa vérifie si le vote est legit mathematiquement
        // la liste prend en compte qui a été voté combien de fois
        HashMap<UUID, Integer> count = new HashMap<>();
        for (UUID vote : votes.values()) {
            // hot fix for Integer != int error sa demande un integer mais ont donne un int donc ont le transforme de force -Reda
            // java est bizarre -Fred
            count.putIfAbsent(vote, (Integer)(0));//initialise si il est pas présent dans le hashmap
            count.put(vote, (Integer)(count.get(vote) + 1)); //compte son vote et est la reponse direct si la personne a déja été voté une fois
        }
        // max prend le nombre maximum qui a été voté mais si deux personne ont été voté exacquo, .frequency vérifie si il y une égalité
        // si c'est le cas cela renvoie un false et donc rien n'est return et personne n'est tué

        if (1 == Collections.frequency(count.values(),Collections.max(count.values()))) {
            UUID chosenOne = null;
            for (UUID uuid : count.keySet()) {
                if (Objects.equals(count.get(uuid), Collections.max(count.values()))) {
                    chosenOne = uuid;
                }
            }
            return chosenOne;
        } else {
            return null;
        }
    }

    public void DecideWinner() {

        if (VillageWinner()) {
            this.gameState = GameState.RESULTAT;
            this.lobby.queueEventForAllPlayer(new StateEvent(GameState.RESULTAT));
            this.lobby.queueEventForAllPlayer(new WinnerEvent("Village", System.currentTimeMillis()));
        } else if (LoupBlancWinner()) {
            this.gameState = GameState.RESULTAT;
            this.lobby.queueEventForAllPlayer(new StateEvent(GameState.RESULTAT));
            this.lobby.queueEventForAllPlayer(new WinnerEvent("loupBlanc", System.currentTimeMillis()));
        } else if (LoupGarouWinner()) {
            this.gameState = GameState.RESULTAT;
            this.lobby.queueEventForAllPlayer(new StateEvent(GameState.RESULTAT));
            this.lobby.queueEventForAllPlayer(new WinnerEvent("loups", System.currentTimeMillis()));
        } else {
            this.gameState = GameState.VILLAGE_EXECUTION;
        }
    }

    public ArrayList<UUID> getLoupsVivants() {
        ArrayList<UUID> out = new ArrayList<>();
        for (UUID uuid : vivants) {
            if (loups.contains(uuid)) { out.add(uuid); }
        }
        return out;
    }

    public boolean VillageWinner() {
        return getLoupsVivants().isEmpty();
    }

    public boolean LoupGarouWinner() {
        ArrayList<UUID> loupsVivants = getLoupsVivants();
        return loupsVivants.size() >= (vivants.size() - loupsVivants.size());
    }

    public boolean LoupBlancWinner() {
        ArrayList<UUID> loupsVivants = getLoupsVivants();
        return loupsVivants.size() == 1 && joueurs.get(loupsVivants.getFirst()).getRole().equals(Role.LOUPBLANC);
    }

    @Override
    public String getType() {
        return "Loup";
    }

    @Override
    public JSONObject toJson() {
        JSONObject object=new JSONObject();
        object.put("type", getType());
        JSONObject jsonJoueurs = new JSONObject();
        this.joueurs.forEach((uuid, joueur) -> jsonJoueurs.put(uuid.toString(), joueur.toJson()));
        object.put("joueurs", jsonJoueurs);

        JSONObject jsonVotes = new JSONObject();
        this.votes.forEach((uuid, uuid2) -> jsonVotes.put(uuid.toString(), uuid2.toString()));
        object.put("votes", jsonVotes);

        object.put("manche", this.manche);
        object.put("state", this.gameState);

        return object;
    }

    // DEPRECTED: should be removed safely one day, do not use
    public JSONObject toJsonMasked( Joueur joueur ) {
        JSONObject out = new JSONObject();
        out.put("state", this.gameState);
        out.put("manche", this.manche);
        out.put("role", joueur.getRole());
        if (joueur.getRole().equals(Role.LOUP)) {
            JSONArray jsonLoups = new JSONArray();
            this.loups.forEach(loup -> jsonLoups.put(loup.toString()));
            out.put("loups", jsonLoups);
        }
        out.put("alive", joueur.isVivant());
        if (joueur.getAmour() == null) {
            //si célibataire sa l'inscrit comme null sinon sa met son amoureux
            out.put("amour", JSONObject.NULL);
        } else {
            out.put("amour", joueur.getAmour().toString());
        }

        return out;
    }
}
