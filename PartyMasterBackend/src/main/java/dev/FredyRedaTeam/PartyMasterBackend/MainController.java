package dev.FredyRedaTeam.PartyMasterBackend;

import dev.FredyRedaTeam.PartyMasterBackend.model.Event;
import dev.FredyRedaTeam.PartyMasterBackend.model.Lobby;
import dev.FredyRedaTeam.PartyMasterBackend.model.Response;
import dev.FredyRedaTeam.PartyMasterBackend.model.TerminationEvent;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.UUID;

@RestController
@CrossOrigin
public class MainController {

    public static UUID parseUUID(String uuid) {
        try {
            return UUID.fromString(uuid);
        } catch (IllegalArgumentException | NullPointerException error) {
            return null;
        }
    }

    @GetMapping(value = "/{room}/state", produces = MediaType.APPLICATION_JSON_VALUE)
    public String state(@PathVariable String room, @ModelAttribute("uuid") String uuid, HttpServletRequest request) {
        Lobby.checkRooms();
        if (Lobby.isInstance(room)) {
            Lobby lobby = Lobby.getInstance(room);
            UUID _uuid = parseUUID(uuid);
            if (lobby.getPlayers().containsKey(_uuid)) {
                return lobby.toJson().toString();
            } else {
                return "{\"room\": \"\"}";
            }
        } else {
            return "{\"room\": \"\"}";
        }
    }

    @GetMapping(value = "/{room}/ping", produces = MediaType.APPLICATION_JSON_VALUE)
    public String ping(@PathVariable String room, HttpServletRequest request) {
        Lobby.checkRooms();
        boolean doesExist = Lobby.isInstance(room);
        JSONObject out = new JSONObject();
        out.put("exist", doesExist);
        if (doesExist) {
            out.put("open", Lobby.getInstance(room).isOpen());
        } else {
            out.put("open", false);
        }
        return out.toString();
    }

    @GetMapping(value = "/{room}/tick", produces = MediaType.APPLICATION_JSON_VALUE)
    public String tick(@PathVariable String room, @ModelAttribute("uuid") String uuid, HttpServletRequest request) {
        UUID _uuid = parseUUID(uuid);
        if (Lobby.isInstance(room)) {
            Lobby lobby = Lobby.getInstance(room);
            LinkedList<Event> list = lobby.fetchEvents(_uuid);
            JSONArray array = new JSONArray();
            for (Event event : list) {
                array.put(event.toJson());
            }
            return array.toString();
        } else {
            JSONArray array = new JSONArray();
            TerminationEvent evt = new TerminationEvent(_uuid);
            array.put(evt.toJson());
            return array.toString();
        }
    }

    @PostMapping(value = "/createlobby")
    public String createLobby(HttpServletRequest request) throws Exception {
        Lobby.checkRooms();
        Lobby lobby = new Lobby();
        JSONObject data = new JSONObject();
        data.put("lobby", lobby.getRoom());
        Response out = new Response(0, data);
        return out.toJson().toString();
    }

    @PostMapping(value = "/{room}/send")
    public String send(@PathVariable String room, @RequestBody String input, HttpServletRequest request) {
        Lobby.checkRooms();
        if (Lobby.isInstance(room)) {
            JSONObject inputdata = new JSONObject(input);
            String uuid;
            try {
                uuid = inputdata.getString("uuid");
            } catch (JSONException e) {
                uuid = null;
            }
            Response response = Lobby.getInstance(room).receiveAction(
                    parseUUID(uuid),
                    inputdata.getString("target"),
                    inputdata.getJSONObject("data")
            );
            return response.toJson().toString();
        } else {
            return "{}";
        }
    }
}
