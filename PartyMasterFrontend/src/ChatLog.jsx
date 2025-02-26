import ChatMessage from "./ChatMessage"

export default function ChatLog({app}) {
    return (
        <div className="chat-log">
            {app.data.chat.map((msg) => 
                <ChatMessage key={msg.uuid + "," + msg.timestamp} uuid={msg.uuid} timestamp={msg.timestamp} content={msg.content} app={app}/>
            )}
        </div>
    )
}