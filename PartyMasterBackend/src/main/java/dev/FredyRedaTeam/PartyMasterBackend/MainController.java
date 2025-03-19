package dev.FredyRedaTeam.PartyMasterBackend;

import dev.FredyRedaTeam.PartyMasterBackend.model.Lobby;
import dev.FredyRedaTeam.PartyMasterBackend.model.Response;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class MainController {

    public static UUID parseUUID(String uuid) {
        try {
            return UUID.fromString(uuid);
        } catch (java.lang.IllegalArgumentException error) {
            return null;
        }
    }



    @GetMapping(value = "/{room}/lobby/state", produces = MediaType.APPLICATION_JSON_VALUE)
    public String state(@PathVariable String room, @ModelAttribute("uuid") String uuid, HttpServletRequest request) {
        if (Lobby.isInstance(room)) {
            Lobby lobby = Lobby.getInstance(room);
            UUID _uuid = parseUUID(uuid);
            return lobby.toJson().toString();
        } else {
            return "{}";
        }
    }

    @GetMapping(value = "/{room}/lobby/ping", produces = MediaType.APPLICATION_JSON_VALUE)
    public String exist(@PathVariable String room, HttpServletRequest request) {
        boolean doesExist = Lobby.isInstance(room);
        JSONObject out = new JSONObject();
        out.put("exist", doesExist);
        return out.toString();
    }

    @GetMapping(value = "{room}/lobby/tick", produces = MediaType.APPLICATION_JSON_VALUE)
    public String tick(@PathVariable String room, @ModelAttribute("uuid") String uuid, HttpServletRequest request) {
        if (Lobby.isInstance(room)) {
            Lobby lobby = Lobby.getInstance(room);
            UUID _uuid = parseUUID(uuid);
            return lobby.toJson().toString();
        } else {
            return "{}";
        }
    }

    @PostMapping(value = "/createlobby")
    public String createLobby() {
        Lobby lobby = new Lobby();
        JSONObject data = new JSONObject();
        data.put("lobby", lobby.getRoom());
        Response out = new Response(0, data);
        return out.toJson().toString();
    }

}
