import Player from "./Player";


export default function PlayerList({data}) {
    return (
        <div className="playerlist">
            {Object.keys(data.players).map((uuid) => 
                <Player key={uuid} uuid={uuid} data={data}/>
            )}
        </div>
    )
}