package dev.FredyRedaTeam.PartyMasterBackend;

import dev.FredyRedaTeam.PartyMasterBackend.model.Lobby;
import jakarta.servlet.http.HttpServletRequest;
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



    @GetMapping(value = "/lobby/{room}/state", produces = MediaType.APPLICATION_JSON_VALUE)
    public String state(@PathVariable String room, @ModelAttribute("uuid") String uuid, HttpServletRequest request) {
        if (Lobby.isInstance(room)) {
            Lobby lobby = Lobby.getInstance(room);
            UUID _uuid = parseUUID(uuid);
            return lobby.toJson().toString();
        } else {
            return "{}";
        }
    }

    @GetMapping(value = "/lobby/{room}/tick", produces = MediaType.APPLICATION_JSON_VALUE)
    public String tick(@PathVariable String room, @ModelAttribute("uuid") String uuid, HttpServletRequest request) {
        if (Lobby.isInstance(room)) {
            Lobby lobby = Lobby.getInstance(room);
            UUID _uuid = parseUUID(uuid);
            return lobby.toJson().toString();
        } else {
            return "{}";
        }
    }

}
