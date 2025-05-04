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

function Lobby({ connected, setconnected }) {

    const games = useGames();
    let { room } = useParams();
    const navigate = useNavigate();

    const [data, updateData] = useImmer({
        "room": room,
        "timestamp": 0,
        "lobby_master": null,
        "players": {},
        "game": null,
        "gameData": null,
        "chat": [],
        "msg": ""
    });

    const app = {
        host: window.location.hostname == "partymaster.duckdns.org" ? "http://10.10.2.122" : "http://" + window.location.hostname,
        data: data,
        updateData: updateData,
        isLobbyMaster() {
            return sessionStorage.getItem("uuid") == app.data.lobby_master;
        },
        receiveEvent: function(event) {
            console.log(event); // TODO: retirer apres debug
            if      (event.type == "ChatEvent") app.ChatEvent(event)
            else if (event.type == "JoinEvent") app.JoinEvent(event)
            else if (event.type == "TerminationEvent") app.TerminationEvent(event)
            else if (event.type == "GameChangeEvent") app.GameChangeEvent(event)
            else if (event.type == "LobbyHome.HighlightEvent") app.LobbyHome_HighlightEvent(event)
            else if (event.type == "LobbyHome.SuggestEvent") app.LobbyHome_SuggestEvent(event)
            // app.updateData((data) => {});
        },
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
            app.packAction("player:kick", {
                "target": uuid
            }).then(()=>{
                console.log("kicked player");
            }).catch(() => {console.error("axios post error")});
        },
        quit: function() {
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
        GameChangeEvent(event) {
            console.log("A new game has began: " + event.game.type);
            // let gameType = games[event.game.type];
            console.log(app);
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
            });
        }


    }

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

    useEffect(function(){
        let attempt = 0;
        let interval = setInterval(()=>{
            axios.get(
                app.host + ":8080/" + room + "/tick?uuid=" + sessionStorage.getItem("uuid")
            ).then((response) => {
                // console.log(response.data.length);
                // console.log(response.data)
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
