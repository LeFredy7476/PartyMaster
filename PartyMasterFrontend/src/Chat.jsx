import ChatLog from "./ChatLog"
import "./Chat.css"

export default function Chat({app}) {
    return (
        <div id="chat">
            <div className="chat-header">
                <div className="chat-icon"><span className="micon">chat</span></div>
                <h2 className="chat-title">Lobby Chat</h2>
            </div>
            <ChatLog app={app} />
            <div className="chat-input-box">
                <input type="text" name="chat-input-message" id="chat-input-message" />
                <button id="chat-send-message" onClick={app.sendMessage}>send</button>
            </div>
        </div>
    )
}