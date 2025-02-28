package dev.FredyRedaTeam.PartyMasterBackend;

import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController
public class MainController {



    @GetMapping(value = "/lobby/{lobby}/state")
    public JSONObject state(@PathVariable String lobby, @RequestAttribute(value = "uuid", required = true) String uuid, HttpServletRequest request) {
        return new JSONObject(); // TODO: fill the actions
    }

}
