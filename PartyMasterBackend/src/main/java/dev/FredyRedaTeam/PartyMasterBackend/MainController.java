package dev.FredyRedaTeam.PartyMasterBackend;

import dev.FredyRedaTeam.PartyMasterBackend.model.Lobby;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class MainController {



    @GetMapping(value = "/lobby/{room}/state", produces = MediaType.APPLICATION_JSON_VALUE,)
    public String state(@PathVariable String room, @ModelAttribute("uuid") String uuid, HttpServletRequest request) {
        System.out.print("got request for |");
        System.out.print(room);
        System.out.println("|");
        System.out.println(Lobby.getAllRooms());
        System.out.println(Lobby.isInstance(room));
        if (Lobby.isInstance(room)) {
            Lobby lobby = Lobby.getInstance(room);
            UUID _uuid = UUID.fromString(uuid);
            return lobby.toJson().toString();
        } else {
            return "{}";
        }
    }

}
