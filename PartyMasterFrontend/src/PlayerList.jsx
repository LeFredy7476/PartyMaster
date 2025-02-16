import Player from "./Player";


export default function PlayerList({data}) {
    return (
        <div className="playerlist">
            {Object.keys(data.players).map((player) => 
                <Player name={data.players[player].name} icon={data.players[player].icon}/>
            )}
        </div>
    )
}