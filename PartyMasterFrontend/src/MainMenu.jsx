import {useImmer} from 'use-immer'
import './Lobby.css'
import './Menu.css'
import axios from 'axios'
import {useParams} from "react-router-dom";

function MainMenu() {

    return (
        <main>
            <h1>PartyMaster</h1>
            <h2>Vos jeux favoris, au même endroit.</h2>
            <section>
                <label htmlFor="lobbyCode">Joindre une partie</label>
                <input type="text" name="lobbyCode" id="lobbyCode" placeholder="Code"/>
                <button id="lobbyJoin">Rejoindre</button>
            </section>
            <button id="lobbyCreate">Créer une partie</button>
        </main>
    )
}

export default MainMenu
