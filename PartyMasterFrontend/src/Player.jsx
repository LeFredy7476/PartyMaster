import get_player_icon from "./playerutils.jsx";
import "./Player.css";
import pm from "/icon.svg";




export default function Player({uuid, app}) {
    if (app.data.players[uuid]) {

        let player = app.data.players[uuid];
        // console.log(app.data.lobby_master);
        // console.log(localStorage.getItem("uuid"));

        return (
            <div className="player">
                <img className="player-icon" src={get_player_icon(player.icon)} alt={"icon#" + player.icon} />
                <div className="player-name">
                    {player.name} 
                    {app.data.lobby_master == player.uuid ? <img src={pm} width="16" height="16" className="player-master"/> : <></>}
                    {
                        app.data.lobby_master == localStorage.getItem("uuid") ?
                        <button className="player-kick" onClick={(e) => {
                            app.kickPlayer(uuid);
                        }}>retirer <span className="micon">block</span></button> :
                        <></>
                    }
                </div>
                {/* <span></span> */}
            </div>
        )
    } else {
        return (<></>)
    }
}