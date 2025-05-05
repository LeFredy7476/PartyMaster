import {useImmer} from 'use-immer'
import './Lobby.css'
import PlayerList from './PlayerList'
import Game from './Game'
import Chat from './Chat'
import axios from 'axios'
import {useNavigate, useParams} from "react-router-dom";
import { useEffect } from 'react'
import useGames from './games/useGames'
import LobbyHome from './games/LobbyHome'
import Uno from './games/Uno'
import Loup from './games/Loup'

//heriter de routeur, cela verifie avec ton browser si tu es deja dans un groupe , si ce n'est pas le cas, sa te retourne a la page de connexion du groupe
function Lobby({ connected, setconnected }) {

    const games = useGames();
    let { room } = useParams();
    const navigate = useNavigate();

    const [data, updateData] = useImmer({
        "room": room, // les infos de la room
        "timestamp": 0,
        "lobby_master": null,// leader de la room
        "players": {}, // uuid des joueurs
        "game": null, // le jeux en cours
        "gameData": null, // les données du jeux
        "chat": [], // l'historique des messages
        "msg": "" // le message ecris en temps reel 
    });

    const app = {
        host: window.location.hostname == "partymaster.duckdns.org" ? "http://10.10.2.122" : "http://" + window.location.hostname,
        data: data,
        updateData: updateData,
        isLobbyMaster() {
            return sessionStorage.getItem("uuid") == app.data.lobby_master;
        },
        receiveEvent: function(event) {
            // quand un event est recu, sa passe par cette fonction qui va appeler la fonction correspondante comme un guide
            console.log(event); // TODO: retirer apres debug
            if      (event.type == "ChatEvent") app.ChatEvent(event)
            else if (event.type == "JoinEvent") app.JoinEvent(event)
            else if (event.type == "TerminationEvent") app.TerminationEvent(event)
            else if (event.type == "GameChangeEvent") app.GameChangeEvent(event)
            else if (event.type == "LobbyHome.HighlightEvent") app.LobbyHome_HighlightEvent(event)
            else if (event.type == "LobbyHome.SuggestEvent") app.LobbyHome_SuggestEvent(event)
            else if (event.type == "Loup.StateEvent") app.Loup_StateEvent(event)
            else if (event.type == "Loup.DeathEvent") app.Loup_DeathEvent(event)
            // app.updateData((data) => {});
        },
        //pack Action sa automatise la creation de l'action au lieu de la faire manuellement
        packAction: function(target, data) {
            return axios.post(app.host + ":8080/" + room + "/send", {
                "target": target,
                "uuid": sessionStorage.getItem("uuid"),
                "data": data
            })
        },
        sendMessage: function() {
            if (app.data.msg != "") {
                app.packAction(
                    "chat:send",
                    {
                        "content": app.data.msg,
                        "flags": "ALL"
                    }
                ).then((response) => {
                    if (response.data.code != 0) {
                        console.error("sendMessage returned error code " + response.data.code, response.data);
                    }
                }).catch(() => {console.error("axios post error")}); // TODO: change this to the actual mapping for the backend
                // /* debug purpose only */ console.log(value);
                updateData((data)=>{
                    data.msg = "";
                })
                return true;
            } else {
                return false;
            }
        }, 
        kickPlayer: function(uuid) {
            //action de kick elle se fait dans le backend player:kick sa fait juste appeller dans le backend terminationEvent
            app.packAction("player:kick", {
                "target": uuid
            }).then(()=>{
                console.log("kicked player");
            }).catch(() => {console.error("axios post error")});
        },
        quit: function() {
            //pareil mais sans le backend tu fait juste revenir a l'ecran d'acceuil
            app.packAction("player:quit", {}).then(()=>{
                console.log("quit");
            }).catch(() => {console.error("axios post error")});
            setconnected(false);
            // app.data.game.stop();
            sessionStorage.removeItem("name");
            navigate("/");
        },

        // ------------------------------
        // event handlers 
        // ------------------------------
        
        ChatEvent(event) {
            app.updateData((data) => {
                data.chat.push(event.message);
            });
        },
        JoinEvent(event) {
            app.updateData((data) => {
                data.players[event.player.uuid] = {
                    name: event.player.name,
                    icon: event.player.icon
                };
            });
        },
        TerminationEvent(event) {
            if (event.target == sessionStorage.getItem("uuid")) {
                // app.data.game.stop();
                navigate("/");
            } else {
                app.updateData((data) => {
                    delete data.players[event.target];
                });
            }
        },
        //celui qui est appeler quand ont change de jeux 
        GameChangeEvent(event) {
            // methode critique, petit changement = peut tout briser

            console.log("A new game has began: " + event.game.type);
            // let gameType = games[event.game.type];
            console.log(app);
            //arrete le jeux si tes deja en game, stop le et update le car quand sa change sa prend des chemins different que celui donner parce que 
            //c'est dans une boucle while qui s'appelle de memeoire ce qui creer des fuites de memoires donc update fait en sorte que sa lui redonne les bonnes 
            //references et le remet dans le droit chemin 
            if (app.data.game) app.data.game.stop();
            if (app.data.game) app.data.game.update( app, app.data.gameData );
            console.log("game updating");
            let game;
            if (event.game.type == "LobbyHome") {
                game = new LobbyHome(app, event.game);
            } else if (event.game.type == "Uno") {
                game = new Uno(app, event.game);
            } else if (event.game.type == "Loup") {
                game = new Loup(app, event.game);
            } else {
                game = new LobbyHome(app, event.game);
            }
            //updateGame est la pour etre sure que game est implementer et que certaine donner reste cacher par exemple qui est loup comme sa les autres ne le savent
            //pas
            app.updateData((data) => {
                console.log(game);
                console.log("game updated");
                data.game = game;
                data.gameData = game.data;
            });
            game.init();
        },

        // ------------------------------
        // event handlers / game specific 
        // ------------------------------

        LobbyHome_HighlightEvent(event) {
            console.log("game got selected: " + event.game);
            app.updateData((data) => {
                data.gameData.selected_game = event.game;
            });
        },
        LobbyHome_SuggestEvent(event) {
            console.log("game got suggested: " + event.game);
            
        },



        Loup_StateEvent(event) {
            console.log("the game has entered a new state: " + event);
            app.updateData((data) => {
                data.gameData.state = event.state;
                if (event.state != "VOYANTE_REVELATION" && event.state != "TRAITRE_REVELATION" && event.state != "AMOUREUX_REVELATION") {
                    data.gameData.hadRevelation = false;
                }
                data.gameData.votes = {};
            });
        },
        Loup_DeathEvent(event) {
            let uuid = event.uuid;
            let joueur = event.joueur;
            app.updateData((data) => {
                data.gameData.joueurs[uuid] = joueur;
            });
        },
        Loup_WinnerEvent(event) {
            let winner = event.winner;
            alert("Les " + winner + " ont gagné!");
        },
        Loup_RevelationEvent(event) {
            let uuid = event.uuid;
            let role = event.role;
            let sender = event.sender;
            app.updateData((data) => {
                data.gameData.hadRevelation = true;
                data.gameData["revelation"] = {
                    "sender": sender,
                    "uuid": uuid,
                    "role": role,
                };
            });
        },
        Loup_VoteEvent(event) {
            let uuid = event.uuid;
            let target = event.target;
            app.updateData((data) => {
                data.gameData.votes[uuid] = target;
            });
        }
    }

    //il est appeler a chaque fois que data se fait changer car il est dans ses dependencies car il est celui qui se charge de update le jeux et aussi
    //quand tu rentre dans la room il est appeler en verifiant si un jeux est deja lancer et si c'est le cas sa update, aussi quand la room est creer,
    //sa appelle la methode toJson dans le backend pour recuperer le json contenant toute les infos 
    useEffect(function() {
        if (app.data.gameData == null) {
            axios.get(
                app.host + ":8080/" + room + "/state?uuid=" + sessionStorage.getItem("uuid")
            ).then((response) => {
                if (response.data.room == "") {
                    window.location.assign(window.location.protocol + "//" + window.location.hostname + "/");
                    // window.location.reload();
                }
                if (app.data.game) app.data.game.stop();
                //il fait en sorte que la room commence en lobbyHomme et set les premieres datas
                let _game = new games.LobbyHome( app, response.data.game );
                updateData((data) => {
                    data.chat = response.data.chat;
                    data.game = _game;
                    data.gameData = _game.data;
                    data.lobby_master = response.data.lobby_master;
                    data.players = response.data.players;
                    data.room = response.data.room;
                });
                _game.init();
            });
        } else {
            app.data.game.update(app, app.data.gameData);
        }
        return function(){
            // if (app.data.game) {
            //     app.data.game.stop();
            // }
        }
    }, [app.data]);

    //fait en sorte que toute les .02 secondes sa verifie les events et sa appelle fetch event dans le backend et les donnes au joueurs 
    //tick c'est dans le backend et il peut signaler le joueur est encore present, il prend les events arriver pendant la partie, et signale que le groupe est encore
    //actif et la verification d'activite se fait lorsque la page est ferme c'est pour donner une chance de se reconnecter 
    useEffect(function(){
        let attempt = 0;
        let interval = setInterval(() => {
            axios.get(
                app.host + ":8080/" + room + "/tick?uuid=" + sessionStorage.getItem("uuid")
            ).then((response) => {
                // console.log(response.data.length);
                // console.log(response.data);
                response.data.forEach(app.receiveEvent);
                attempt = 0;
            }).catch((error) => {
                console.error(error);
                attempt++;
                if (attempt > 5) {
                    alert("Erreur de serveur.");
                    clearInterval(interval)
                    navigate("/");
                }
            });
        }, 200);
        return function() {
            clearInterval(interval);
        }
    }, [app.data]);

    return (
        <>
            <PlayerList app={app} />
            <Game app={app} />
            <Chat app={app} />
        </>
    )
}

export default Lobby
