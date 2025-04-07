import ChatMessage from "./ChatMessage"

export default function ChatLog({app}) {

    var lastuuid = "";
    var lasttimestamp = 0;

    function shouldjoin(uuid, timestamp) {
        let timeok = Math.abs(timestamp - lasttimestamp) < 60000;
        let sameuuid = lastuuid == uuid;
        lasttimestamp = timestamp;
        lastuuid = uuid;
        return timeok && sameuuid;
    }

    return (
        <div className="chat-log">
            {app.data.chat.map((msg) => 
                <ChatMessage shrink={shouldjoin(msg.uuid, msg.timestamp)} key={msg.uuid + "," + msg.timestamp} uuid={msg.uuid} timestamp={msg.timestamp} content={msg.content} app={app}/>
            ).toReversed()}
        </div>
    )
}