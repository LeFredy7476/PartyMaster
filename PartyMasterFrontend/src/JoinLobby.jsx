import {useImmer} from 'use-immer'
import './Lobby.css'
import axios from 'axios'
import {useParams} from "react-router-dom";

function JoinLobby() {

    const { room } = useParams();

    return (
        <div>
            <input type="number" name="icon" id="icon" />
            <input type="text" name="name" id="name" />
            <button id="join" onClick={function() {
                
            }}>join</button>
        </div>
    )
}

export default JoinLobby
