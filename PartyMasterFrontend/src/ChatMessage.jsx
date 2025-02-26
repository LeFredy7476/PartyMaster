import get_player_icon from "./playerutils.jsx";

export default function ChatMessage({app, uuid, content}) {

    return (
        <div className="message-box">
            <div className="message">
                <img src={get_player_icon(app.data.players[uuid].icon)} alt={"icon#" + get_player_icon(app.data.players[uuid].icon)} className="message-icon" />
                <div className="message-separator"></div>
                <div className="message-name">{app.data.players[uuid].name}</div>
                <div className="message-content">{content}</div>
            </div>
        </div>
    )
}