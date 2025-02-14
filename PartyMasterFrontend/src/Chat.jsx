import ChatLog from "./ChatLog"
import "./Chat.css"

export default function Chat() {
    return (
        <div id="chat">
            <div className="chat-header">
                <div className="chat-icon"></div>
            </div>
            <ChatLog />
            <div className="chat-input-box">
                <input type="text" name="" id="chat-input-message" />
                <button id="chat-send-message">send</button>
            </div>
        </div>
    )
}