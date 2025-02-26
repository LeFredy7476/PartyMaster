import get_player_icon from "./playerutils.jsx";
import "./Player.css";



export default function Player({uuid, app}) {

    let player = app.data.players[uuid];

    return (
        <div className="player">
            <img className="player-icon" src={get_player_icon(player.icon)} alt={"icon#" + player.icon} />
            <div className="player-name">{player.name}</div>
        </div>
    )
}