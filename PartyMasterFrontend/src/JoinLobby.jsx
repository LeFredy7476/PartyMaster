import {useImmer} from 'use-immer'
import './Lobby.css'
import axios from 'axios'
import {data, useNavigate, useParams} from "react-router-dom";
import { useState } from 'react';
import { get_player_icon, icons } from "./playerutils.jsx";

function JoinLobby() {

    const {room} = useParams();
    let navigate = useNavigate();
    const [icon, setIcon] = useState(0);
    const nbIcon = icons.length;
    const [name, setName] = useState("");
    

    return (
        <main id="join-main">
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
            
            <input type="text" name="name" id="name" className='form' placeholder="nom" value={name} onChange={(e)=>{
                setName(e.target.value);
            }}/>
            <button className="form-button" id="join" onClick={function() {
                if (name != "") {
                    let uuid = localStorage.getItem("uuid");
                    axios.post(
                        window.location.protocol + "//" + window.location.hostname + ":8080/" + room + "/send",
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
            }}>join</button>
        </main>
    )
}

export default JoinLobby
