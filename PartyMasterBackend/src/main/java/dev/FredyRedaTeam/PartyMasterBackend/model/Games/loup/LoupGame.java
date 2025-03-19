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
    private int manche = 0;
    private final ArrayList<UUID> loups = new ArrayList<>();
    private final HashMap<UUID, UUID> vote = new HashMap<>();

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
            case "":
                return null;
        }
        return null;
    }

    @Override
    public void tick() {

    }

    @Override
    public void init(Lobby lobby) {
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
