import {useImmer} from 'use-immer'
import './Lobby.css'
import axios from 'axios'
import {useParams} from "react-router-dom";

function JoinLobby() {

    const { room } = useParams();

    return (
        <p>
            join room {room}
        </p>
    )
}

export default JoinLobby
