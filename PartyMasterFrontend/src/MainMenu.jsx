import {useImmer} from 'use-immer'
import './Lobby.css'
import './Menu.css'
import axios from 'axios'
import {useNavigate, useParams} from "react-router-dom";

function MainMenu() {

    const {room} = useParams();
    let navigate = useNavigate();

    return (
        <main>
            <h1>PartyMaster</h1>
            <h2>Vos jeux favoris, au même endroit.</h2>
            <section>
                <label htmlFor="lobbyCode">Joindre une partie</label>
                <input type="text" name="lobbyCode" id="lobbyCode" placeholder="Code"/>
                <button id="lobbyJoin">Rejoindre</button>
            </section>
            <button id="lobbyCreate" className="form-button" onClick={function() {
                // let prefix = ""
                // if (window.location.protocol) {
                //     prefix = window.Location.protocol
                // }
                console.log(window.location.protocol + "//" + window.location.hostname)
                axios.post(window.location.protocol + "//" + window.location.hostname + ":");
                navigate("/" + room);
            }}>Créer une partie</button>
        </main>
    )
}

export default MainMenu
