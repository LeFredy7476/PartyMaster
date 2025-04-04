import get_player_icon from "./playerutils.jsx";
import empty from '/players/empty.svg'

export default function ChatMessage({app, uuid, content}) {

    let name = "Utilisateur Inconnu";
    let icon = empty;
    if (app.data.players[uuid]) {
        name = app.data.players[uuid].name;
        icon = get_player_icon(app.data.players[uuid].icon);
    }

    return (
        <div className="message-box">
            <div className="message">
                <img src={icon} alt={"icon#" + icon} className="message-icon" />
                <div className="message-separator"></div>
                <div className="message-name">{name}</div>
                <div className="message-content">{content}</div>
            </div>
        </div>
    )
}