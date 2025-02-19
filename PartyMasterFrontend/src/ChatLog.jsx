import ChatMessage from "./ChatMessage"

export default function ChatLog({data}) {
    return (
        <div className="chat-log">
            {data.chat.map((msg) => 
                <ChatMessage key={msg.uuid + "," + msg.timestamp} uuid={msg.uuid} timestamp={msg.timestamp} content={msg.content} data={data}/>
            )}
        </div>
    )
}