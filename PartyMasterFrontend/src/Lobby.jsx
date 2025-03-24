import {useImmer} from 'use-immer'
import './Lobby.css'
import PlayerList from './PlayerList'
import Game from './Game'
import Chat from './Chat'
import axios from 'axios'
import {useNavigate, useParams} from "react-router-dom";
import { useEffect } from 'react'

function Lobby() {

    let { room } = useParams();

    const [data, updateData] = useImmer({
        "room": room,
        "timestamp": 0,
        "party_master": null,
        "players": {},
        "game": {},
        "chat": []
    });

    useEffect(
        function(){
            axios.get(
                window.location.protocol + "//" + window.location.hostname + ":8080/" + room + "/state?uuid=" + localStorage.getItem("uuid")
            ).then((response) => {
                if (response.data.room == "") {
                    window.location.assign(window.location.protocol + "//" + window.location.host + "/");
                    // window.location.reload();
                }
                updateData((data) => {
                    data.chat = response.data.chat;
                    data.game = response.data.game;
                    data.party_master = response.data.party_master;
                    data.players = response.data.players;
                    data.room = response.data.room;
                })
            });
            return function(){}
        },
        [data, updateData]
    );
    

    const app = {
        data: data,
        updateData: updateData,
        packAction(target, content) {
            return JSON.stringify({
                "room": app.room,
                "target": target,
                "uuid": app.uuid,
                "content": content
            })
        },
        sendMessage() {
            let value = document.querySelector("#chat-input-message").value;
            // app.updateData(data => {data.chat.push({
            //     "uuid": "0",
            //     "timestamp": Date.now(),
            //     "content": value
            // })});
            document.querySelector("#chat-input-message").value = "";
            let out = app.packAction(
                "chat:send",
                {
                    "content": value,
                    "flags": "ALL"
                }
            );
            axios.post("", out).then((response) => {
                let responsedata = JSON.parse(response.data)
                if (responsedata.code != 0) {
                    console.error("sendMessage returned error code " + responsedata.code, response.data);
                }
            }).catch(() => {console.error("axios post error")}); // TODO: change this to the actual mapping for the backend
            /* debug purpose only */ console.log(value);
        }
    }

    return (
        <>
            <PlayerList app={app} />
            <Game app={app} />
            <Chat app={app} />
        </>
    )
}

export default Lobby
