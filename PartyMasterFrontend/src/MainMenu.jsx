import {useImmer} from 'use-immer'
import './Lobby.css'
import './Menu.css'
import axios from 'axios'
import {useNavigate, useParams} from "react-router-dom";
import { useState } from 'react';

function MainMenu() {

    const [room, setRoom] = useState("");
    let navigate = useNavigate();

    return (
        <main>
            <h1>PartyMaster</h1>
            <h2>Vos jeux favoris, au même endroit.</h2>
            <section>
                <label htmlFor="lobbyCode">Joindre une partie</label>
                <input type="text" name="lobbyCode" id="lobbyCode" placeholder="Code" className='form' value={room} onChange={function(e) {
                    setRoom(e.target.value);
                }}/>
                <button id="lobbyJoin" className="form-button" onClick={()=>{
                    if (room.length == 8){
                        navigate("/" + room);
                    }
                }}>Rejoindre</button>
            </section>
            <button id="lobbyCreate" className="form-button" onClick={function() {
                // let prefix = ""
                // if (window.location.protocol) {
                //     prefix = window.Location.protocol
                // }
                console.log(window.location.protocol + "//" + window.location.hostname + ":8080/createlobby");
                axios.post(
                    window.location.protocol + "//" + window.location.hostname + ":8080/createlobby"
                ).then(function (response) {
                    if (response.data.code == 0) {
                        navigate("/" + response.data.data.lobby);
                    }
                });
            }}>Créer une partie</button>
        </main>
    )
}

export default MainMenu
