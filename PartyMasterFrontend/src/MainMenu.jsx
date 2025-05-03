import {useImmer} from 'use-immer'
import './Lobby.css'
import './Menu.css'
import axios from 'axios'
import {useNavigate, useParams} from "react-router-dom";
import { useEffect, useState } from 'react';

function MainMenu({ connected, setconnected }) {

    setconnected(false);

    const host = window.location.hostname == "partymaster.duckdns.org" ? "http://10.10.2.122" : "http://" + window.location.hostname;

    const [room, setRoom] = useState("");
    let navigate = useNavigate();

    // // use effect test
    // useEffect(()=>{
    //     let i = setTimeout(()=>{
    //         console.log("asking smth to the server");
    //     }, 10);
    //     return ()=>{
    //         clearTimeout(i)
    //     }
    // }, [])

    return (
        <main>
            <h1>PartyMaster</h1>
            <h2>Vos jeux favoris, au même endroit.</h2>
            <section className="join-section">
                <label htmlFor="lobbyCode">Rejoindre un groupe</label>
                <input type="text" name="lobbyCode" id="lobbyCode" placeholder="Code" className='form' autoComplete="off" value={room} onChange={function(e) {
                    setRoom(e.target.value);
                }}/>
                <button id="lobbyJoin" className="form-button" onClick={()=>{
                    if (room.length == 8) {
                        navigate("/" + room.toLowerCase());
                    }
                }}>Rejoindre</button>
            </section>
            <button id="lobbyCreate" className="form-button" onClick={function() {
                // let prefix = ""
                // if (window.location.protocol) {
                //     prefix = window.Location.protocol
                // }
                console.log(host + ":8080/createlobby");
                axios.post(
                    host + ":8080/createlobby"
                ).then(function (response) {
                    if (response.data.code == 0) {
                        navigate("/" + response.data.data.lobby);
                    }
                });
            }}>Créer un groupe</button>
        </main>
    )
}

export default MainMenu
