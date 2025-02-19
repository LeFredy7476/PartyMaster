import ChatMessage from "./ChatMessage"

export default function ChatLog({data}) {
    return (
        <div className="chat-log">
            <ChatMessage data={data} uuid={1} content={"hello there"} />
            <ChatMessage data={data} uuid={0} content={"kjghadsfigabsdkfhbsadk hbfkhbasdkfhbashdbfhbajsdfh bgaskdjbfhsjadbfhjbasdghjbsfdagh ahjsgkfwaeiu eajhkfwiebfwe feygwfkawg eyawegfywgbaeuf wegfiagskbfe iwaeg fl kbaiesfg waegfaki fiwgae fk"} />
        </div>
    )
}