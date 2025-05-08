import useAssets from "./useAssets.jsx";
import empty from '/players/empty.svg'

export default function ChatMessage({app, uuid, content, shrink}) {

    const assets = useAssets();

    let name = "Utilisateur Inconnu";
    let icon = empty;
    if (app.data.players[uuid]) {
        name = app.data.players[uuid].name;
        icon = assets.players[app.data.players[uuid].icon].src;
    }

    return (
        <div className={shrink ? "message-box shrink" : "message-box"}>
            <div className="message">
                <img src={icon} alt={"icon#" + icon} className="message-icon" draggable="false" />
                <div className="message-separator"></div>
                <div className="message-name">{name}</div>
                <div className="message-content">{content}</div>
            </div>
        </div>
    )
}