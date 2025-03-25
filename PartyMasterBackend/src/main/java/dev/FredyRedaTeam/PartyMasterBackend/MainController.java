package dev.FredyRedaTeam.PartyMasterBackend;

import dev.FredyRedaTeam.PartyMasterBackend.model.Action;
import dev.FredyRedaTeam.PartyMasterBackend.model.Event;
import dev.FredyRedaTeam.PartyMasterBackend.model.Lobby;
import dev.FredyRedaTeam.PartyMasterBackend.model.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    public String exist(@PathVariable String room, HttpServletRequest request) {
        boolean doesExist = Lobby.isInstance(room);
        JSONObject out = new JSONObject();
        out.put("exist", doesExist);
        return out.toString();
    }

    @GetMapping(value = "{room}/tick", produces = MediaType.APPLICATION_JSON_VALUE)
    public String tick(@PathVariable String room, @ModelAttribute("uuid") String uuid, HttpServletRequest request) {
        if (Lobby.isInstance(room)) {
            Lobby lobby = Lobby.getInstance(room);
            UUID _uuid = parseUUID(uuid);
            LinkedList<Event> list = lobby.fetchEvents(_uuid);
            JSONArray array = new JSONArray();
            for (Event event : list) {
                array.put(event.toJson(_uuid));
            }
            return array.toString();
        } else {
            return "{}";
        }
    }

    @PostMapping(value = "/createlobby")
    public String createLobby(HttpServletRequest request) {
        Lobby lobby = new Lobby();
        JSONObject data = new JSONObject();
        data.put("lobby", lobby.getRoom());
        Response out = new Response(0, data);
        System.out.println("new room created : " + lobby.getRoom());
        return out.toJson().toString();
    }

    @PostMapping(value = "/{room}/send")
    public String send(@PathVariable String room, @RequestBody String input, HttpServletRequest request) {
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
