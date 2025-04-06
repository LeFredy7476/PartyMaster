package dev.FredyRedaTeam.PartyMasterBackend;

import dev.FredyRedaTeam.PartyMasterBackend.model.Lobby;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

	public static void main(String[] args)throws Exception {
		Lobby.main(args);

		Lobby test = new Lobby("abcde123");


		SpringApplication.run(App.class, args);
	}

}
