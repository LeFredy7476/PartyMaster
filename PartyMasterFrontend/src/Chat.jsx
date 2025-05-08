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
                <input type="text" name="chat-input-message" id="chat-input-message" value={app.data.msg} onChange={(e) => {
                    app.updateData((data)=>{
                        data.msg = e.target.value
                    });
                }} onKeyDown={(e) => {
                    if (e.key === 'Enter') {
                        e.preventDefault();
                        let send = document.querySelector("#chat-send-message");
                        send.click();

                        // for style purpose :
                        send.classList.add("_clck");
                        setTimeout(function() {
                            send.classList.remove("_clck");
                        }, 10);
                    }
                }}/>
                <button id="chat-send-message" onClick={(e) => {
                    e.preventDefault();
                    app.sendMessage();
                    document.querySelector("#chat-input-message").focus();
                }}>send</button>
            </div>
        </div>
    )
}