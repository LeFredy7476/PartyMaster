package dev.FredyRedaTeam.PartyMasterBackend;

import dev.FredyRedaTeam.PartyMasterBackend.model.Games.LoupGarou.utils.Connexion;
import dev.FredyRedaTeam.PartyMasterBackend.model.Lobby;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

@SpringBootApplication
public class App {

	public static Connection connection = null;
	public static Statement statement = null;

	public static void startConnection(String path) throws Exception {
        Properties props = Connexion.getProps(path);
		connection = DriverManager.getConnection(
				props.getProperty("spring.datasource.url"),
				props.getProperty("spring.datasource.username"),
				props.getProperty("spring.datasource.password")
		);
		statement = connection.createStatement();
	}

	public static void main(String[] args) throws Exception {
		Lobby.main(args);

		startConnection("./src/main/resources/application.properties");


		// TODO: initialiser les tables si elles n'éxistent pas

		Lobby test = new Lobby("abcde123");


		SpringApplication.run(App.class, args);
	}

}
