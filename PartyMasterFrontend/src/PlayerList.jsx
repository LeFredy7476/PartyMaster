import Player from "./Player";


export default function PlayerList({data}) {
    return (
        <div className="playerlist">
            {data.map((player) => 
                <Player name={player.name} icon={player.icon}/>
            )}
        </div>
    )
}