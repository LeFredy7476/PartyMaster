import {useImmer} from 'use-immer'
import './Lobby.css'
import axios from 'axios'
import {useNavigate, useParams} from "react-router-dom";
import { useState } from 'react';
import { get_player_icon, icons } from "./playerutils.jsx";

function JoinLobby() {

    const {room} = useParams();
    let navigate = useNavigate();
    const [icon, setIcon] = useState(0);
    const nbIcon = icons.length;

    return (
        <main id="join-main">
            <div id="icon-picker">
                <button className="icon-swap" id="icon-last" onClick={function() {
                    setIcon((icon + nbIcon - 1) % nbIcon);
                }}></button>
                <img src={get_player_icon(icon)} alt="" id="icon-preview"/>
                <button className="icon-swap" id="icon-next" onClick={function() {
                    setIcon((icon + 1) % nbIcon);
                }}></button>
                {/* <input type="number" name="icon" id="icon" style={{display: "none"}} value={icon} max={nbIcon - 1} min={0} /> */}
            </div>
            
            <input type="text" name="name" id="name" placeholder="nom" />
            <button className="form-button" id="join" onClick={function() {
                navigate("/" + room + "/lobby")
            }}>join</button>
        </main>
    )
}

export default JoinLobby
