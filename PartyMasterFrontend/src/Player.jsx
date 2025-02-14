import get_player_icon from "./playerutils.jsx";
import "./Player.css";



export default function Player({name, icon}) {

    return (
        <div className="player">
            <div className="player-icon"><img src={get_player_icon(icon)} alt={"icon#" + icon} /></div>
            <div className="player-name">{name}</div>
        </div>
    )
}