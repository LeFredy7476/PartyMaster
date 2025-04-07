import {useImmer} from 'use-immer'
import './Lobby.css'
import PlayerList from './PlayerList'
import Game from './Game'
import Chat from './Chat'
import axios from 'axios'
import {useNavigate, useParams} from "react-router-dom";
import { useEffect } from 'react'

function Lobby({ connected, setconnected }) {

    let { room } = useParams();
    const navigate = useNavigate();

    const [data, updateData] = useImmer({
        "room": room,
        "timestamp": 0,
        "lobby_master": null,
        "players": {},
        "game": {},
        "chat": [],
        "msg": ""
    });

    const app = {
        host: window.location.hostname == "partymaster.duckdns.org" ? "http://10.10.2.122" : "http://" + window.location.hostname,
        data: data,
        updateData: updateData,
        receiveEvent: function(e) {
            console.log(e); // TODO: retirer apres debug
            if (e.type == "ChatEvent") {
                app.updateData((data) => {
                    data.chat.push(e.message);
                });
            } else if (e.type == "JoinEvent") {
                app.updateData((data) => {
                    data.players[e.player.uuid] = {
                        name: e.player.name,
                        icon: e.player.icon
                    };
                });
            } else if (e.type == "TerminationEvent") {
                if (e.target == localStorage.getItem("uuid")) {
                    navigate("/");
                } else {
                    app.updateData((data) => {
                        delete data.players[e.target];
                    });
                }
            }
            // app.updateData((data) => {});
        },
        packAction: function(target, data) {
            return axios.post(app.host + ":8080/" + room + "/send", {
                "target": target,
                "uuid": localStorage.getItem("uuid"),
                "data": data
            })
        },
        sendMessage: function() {
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
        }, 
        kickPlayer: function(uuid) {
            app.packAction("player:kick", {
                "target": uuid
            }).then(()=>{
                console.log("kicked player");
            }).catch(() => {console.error("axios post error")});
        },
        quit: function() {
            setconnected(false);
            localStorage.removeItem("name");
        }
    }

    useEffect(function(){
        axios.get(
            app.host + ":8080/" + room + "/state?uuid=" + localStorage.getItem("uuid")
        ).then((response) => {
            if (response.data.room == "") {
                window.location.assign(window.location.protocol + "//" + window.location.hostname + "/");
                // window.location.reload();
            }
            updateData((data) => {
                data.chat = response.data.chat;
                data.game = response.data.game;
                data.lobby_master = response.data.lobby_master;
                data.players = response.data.players;
                data.room = response.data.room;
            })
        });
        return function(){}
    }, []);

    useEffect(function(){
        let interval = setInterval(()=>{
            axios.get(
                app.host + ":8080/" + room + "/tick?uuid=" + localStorage.getItem("uuid")
            ).then((response) => {
                // console.log(response.data.length);
                // console.log(response.data)
                response.data.forEach(app.receiveEvent);
            });
        }, 200);
        return function() {
            clearInterval(interval);
        }
    }, []);

    return (
        <>
            <PlayerList app={app} />
            <Game app={app} />
            <Chat app={app} />
        </>
    )
}

export default Lobby
