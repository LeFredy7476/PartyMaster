import "./Player.css";
import pm from "/icon.svg";
import useAssets from "./useAssets.jsx";



export default function Player({uuid, app}) {

    const assets = useAssets();

    if (app.data.players[uuid]) {

        let player = app.data.players[uuid];
        // console.log(app.data.lobby_master);
        // console.log(localStorage.getItem("uuid"));

        return (
            <div className="player">
                <img className="player-icon" src={assets.players[player.icon].src} alt={"icon#" + player.icon} draggable="false" />
                <div className="player-name">
                    {player.name} 
                    {app.data.lobby_master == player.uuid ? <img src={pm} width="16" height="16" className="player-master" draggable="false"/> : <></>}
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