import {useImmer} from 'use-immer'
import './App.css'
import PlayerList from './PlayerList'
import Game from './Game'
import Chat from './Chat'
import axios from 'axios'

function App() {

    if (true) {
        axios.get("/state", )
    }

    const [data, updateData] = useImmer({
        "room": "GH23487G2B",
        "timestamp": 0,
        "party_master": "4",
        "players": {
            "0": { "name": "Bob Ross", "icon": 1 },
            "1": { "name": "Jack Dalton", "icon": 0 },
            "2": { "name": "Mike Brown", "icon": 2 },
            "3": { "name": "Joe Dassin", "icon": 3 },
            "4": { "name": "Marc Robinson", "icon": 4 },
            "5": { "name": "Derick Robert", "icon": 5 },
            "6": { "name": "Bob Ross", "icon": 8 },
            "7": { "name": "Jack Dalton", "icon": 3 },
            "8": { "name": "Jack Dalton", "icon": 4 },
            "9": { "name": "Jack Dalton", "icon": 8 },
            "a": { "name": "Jack Dalton", "icon": 7 },
            "b": { "name": "Jack Dalton", "icon": 3 },
            "c": { "name": "Jack Dalton", "icon": 6 }
        },
        "game": {
            "name": "lobby",
            "ready": true,
            "selected_game": "uno",
            "ready_check": {
                "0": true,
                "1": false,
                "2": true,
                "3": false,
                "4": false,
                "5": true,
                "6": true,
                "7": false,
                "8": false,
                "9": false,
                "a": true,
                "b": false,
                "c": true
            }
        },
        "chat": [
            {
                "uuid": "2",
                "timestamp": 6,
                "content": "Hey! hello!"
            }, {
                "uuid": "3",
                "timestamp": 18,
                "content": "Hello! How u doing?"
            }, {
                "uuid": "2",
                "timestamp": 24,
                "content": "I'm doin fine"
            }
        ]
    });

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

export default App
