import {useImmer} from 'use-immer'
import './Lobby.css'
import axios from 'axios'
import {data, useNavigate, useParams} from "react-router-dom";
import { useState, useEffect } from 'react';
import { get_player_icon, icons } from "./playerutils.jsx";

function JoinLobby() {

    const {room} = useParams();
    let navigate = useNavigate();
    const [icon, setIcon] = useState(0);
    const nbIcon = icons.length;
    const [name, setName] = useState("");
    const [lobbyStatus, setLobbyStatus] = useState(false);
    const [lobbyIcon, setLobbyIcon] = useState("sync");
    const [lobbyMessage, setLobbyMessage] = useState("recherche...");

    useEffect(() => {
        let interval = setInterval(() => {
            axios.get(
                "http://" + window.location.hostname + ":8080/" + room + "/ping"
            ).then((response) => {
                setLobbyStatus(true);
                setLobbyIcon(response.data.exist ? (response.data.open ? "check_circle" : "lock") : "warning");
                setLobbyMessage(response.data.exist ? (response.data.open ? "Groupe disponible" : "Groupe fermé") : "Groupe inexistant");
            }).catch((error) => {
                setLobbyStatus(true);
                setLobbyIcon("signal_wifi_bad");
                setLobbyMessage("Serveur inatteignable");
            })
        }, 2000);
        return () => {
            clearInterval(interval);
        }
    }, [room]);

    return (
        <main id="join-main">
            <button className="back" id="back" onClick={(e) => {
                navigate("/")
            }}>
                Retour
                <span className="micon">undo</span>
            </button>
            <h2>Rejoindre un groupe</h2>
            <div id="icon-picker">
                <button className="icon-swap" id="icon-last" onClick={function() {
                    setIcon((icon + nbIcon - 1) % nbIcon);
                }}><span>arrow_forward_ios</span></button>
                <img src={get_player_icon(icon)} alt="" id="icon-preview"/>
                <button className="icon-swap" id="icon-next" onClick={function() {
                    setIcon((icon + 1) % nbIcon);
                }}><span>arrow_forward_ios</span></button>
                {/* <input type="number" name="icon" id="icon" style={{display: "none"}} value={icon} max={nbIcon - 1} min={0} /> */}
            </div>
            
            <input type="text" name="name" id="name" className='form' placeholder="Nom" value={name} onChange={(e)=>{
                setName(e.target.value);
            }}/>
            <button className="form-button" id="join" onClick={function() {
                if (name != "") {
                    let uuid = localStorage.getItem("uuid");
                    axios.post(
                        "http://" + window.location.hostname + ":8080/" + room + "/send",
                        {
                            uuid: uuid,
                            target: "player:join",
                            data: {
                                name: name,
                                icon: icon
                            }
                        }
                    ).then(function (response) {
                        if (response.data.code == 0) {
                            localStorage.setItem("uuid", response.data.data.uuid)
                            localStorage.setItem("name", response.data.data.name)
                            navigate("/" + room + "/lobby");
                        }
                    });
                }
                // navigate("/" + room + "/lobby")
            }}>Rejoindre</button>
            <div id="lobby-check">
                {lobbyMessage}
                <span 
                    id="lobby-check-status"
                    className={lobbyIcon + " micon"}
                >{lobbyIcon}</span>
            </div>
        </main>
    )
}

export default JoinLobby
