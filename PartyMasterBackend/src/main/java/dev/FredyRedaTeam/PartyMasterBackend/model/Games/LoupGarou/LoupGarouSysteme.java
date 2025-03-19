package dev.FredyRedaTeam.PartyMasterBackend.model.Games.LoupGarou;


import dev.FredyRedaTeam.PartyMasterBackend.model.Games.LoupGarou.utils.Connexion;

import java.util.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;
public class LoupGarouSysteme  {

    private static Random random = new Random();

    public static void GererSysteme() throws Exception{
        try {

            Connection con = null;
            Properties props = new Properties();
            props= Connexion.getProps("./src/main/resources/application.properties");
            con = DriverManager.getConnection(
                props.getProperty("spring.datasource.url"),
                props.getProperty("spring.datasource.username"),
                props.getProperty("spring.datasource.password")
            );
            PreparedStatement prtm = null;
            ResultSet rs = null;
            List<LoupGarou> Personnage = new ArrayList<>();
            List<String> joueurs = new ArrayList<>();
            List<String> specialRoles = new ArrayList<>();

            String sqlJoueurs = "SELECT nom FROM joueurs";
            PreparedStatement stmtJoueurs = con.prepareStatement(sqlJoueurs);
            ResultSet rsJoueurs = stmtJoueurs.executeQuery();
            while (rsJoueurs.next()) {
                joueurs.add(rsJoueurs.getString(1));
            }

            int numJoueurs = joueurs.size();
            int nbrLoups = Math.round(numJoueurs / 3);
            if (nbrLoups>3){
                nbrLoups=3;
            }

            String sqlRoles = "SELECT nom FROM roles";
            PreparedStatement stmtRoles = con.prepareStatement(sqlRoles);
            ResultSet rsRoles = stmtRoles.executeQuery();
            while (rsRoles.next()) {
                specialRoles.add(rsRoles.getString(1));
            }
            LoupGarou loupGarou=new LoupGarou();
            for (int i = 0; i < nbrLoups; i++) {

                int randomDistributeur = random.nextInt(joueurs.size());
                System.out.println(joueurs.get(randomDistributeur) + " loup garou");
                loupGarou.setNom(joueurs.get(randomDistributeur));
                loupGarou.setRole("loup garou");
                loupGarou.setVivant(true);
                Personnage.add(loupGarou);
                joueurs.remove(randomDistributeur);
            }

            int numVillageois = joueurs.size();
            for (int i = 0; i < numVillageois; i++) {
                int randomDistributeur2 = random.nextInt(joueurs.size());
                int roleAleatoire = random.nextInt(specialRoles.size());
                System.out.println(joueurs.get(randomDistributeur2) + " " + specialRoles.get(roleAleatoire));
                loupGarou.setNom(joueurs.get(randomDistributeur2));
                loupGarou.setRole(specialRoles.get(roleAleatoire));
                loupGarou.setVivant(true);
                Personnage.add(loupGarou);
                joueurs.remove(randomDistributeur2);
                specialRoles.remove(roleAleatoire);
            }

            System.out.println("role distribuer.");
            LancerPartie(Personnage);


        } catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
    public static void LancerPartie(List<LoupGarou>Personnage)throws Exception{
        List<String> Amoureux=new ArrayList<>();
        Scanner scanner=new Scanner(System.in);

        for(LoupGarou loupGarou:Personnage){

            if (loupGarou.getRole().equals("cupidon")&& loupGarou.isVivant()){
                loupGarou.setNom("villageois");
                for (int i=0;i<2;i++) {
                    System.out.println("Choisissez un joueurs à rendre amoureux :");
                    String joueur1 = scanner.nextLine();
                    for (LoupGarou amour : Personnage) {
                        if (amour.getNom().equals(joueur1) && amour.isVivant() ) {
                            Amoureux.add(joueur1);

                        }
                    }

                }
            }
            String lover1=Amoureux.get(0);
            String lover2=Amoureux.get(1);
            for (LoupGarou checkAmour:Personnage){
                if (checkAmour.getNom().equals(lover1)&& !checkAmour.isVivant()){
                    for (LoupGarou checkAmour2:Personnage){
                        if (checkAmour2.getNom().equals(lover2)){
                            checkAmour2.setVivant(false);
                        }
                    }
                }else {
                    continue;
                }
                if (checkAmour.getNom().equals(lover2)&& !checkAmour.isVivant()){
                    for (LoupGarou checkAmour2:Personnage){
                        if (checkAmour2.getNom().equals(lover1)){
                            checkAmour2.setVivant(false);
                        }
                    }
                }else {
                    continue;
                }
            }

            if( loupGarou.getRole().equals("loup garou")&&loupGarou.isVivant()){
                System.out.println("entrer la personne a devorer");
                String reponse=scanner.nextLine();
                for (LoupGarou loupGarou1:Personnage) {
                    if (loupGarou1.getNom().equals(reponse)){
                      loupGarou1.setVivant(false);
                    }
                }
            }
            if (loupGarou.getRole().equals("sorciere")&& loupGarou.isVivant()){
                for (LoupGarou loupGarou2:Personnage) {
                    if (!loupGarou2.isVivant()){
                        System.out.println("voulez vous ressuciter"+" "+loupGarou2.getNom()+" "+"oui/ne rien ecrire");
                        String reponse2= scanner.nextLine();
                        if (reponse2=="oui"){
                            loupGarou2.setVivant(true);
                            loupGarou.setRole("Villageois");
                        }else {
                            continue;
                        }
                    }
                }
            }

        }
    }

    public static void main(String[] args) throws Exception{
      GererSysteme();

    }
}
