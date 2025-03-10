import ChatLog from "./ChatLog"
import "./Chat.css"

export default function Chat({data}) {
    return (
        <div id="chat">
            <div className="chat-header">
                <div className="chat-icon"><span className="micon">chat</span></div>
                <h2 className="chat-title">Lobby Chat</h2>
            </div>
            <ChatLog data={data} />
            <div className="chat-input-box">
                <input type="text" name="" id="chat-input-message" />
                <button id="chat-send-message" onClick={() => {
                    let value = document.querySelector("#chat-input-message").value;
                    data.chat.push({
                        "uuid": "0",
                        "timestamp": 24,
                        "content": value
                    });
                    document.querySelector("#chat-input-message").value = "";
                }}>send</button>
            </div>
        </div>
    )
}