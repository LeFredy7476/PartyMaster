import Player from "./Player";


export default function PlayerList({app}) {
    return (
        <div className="playerlist">
            {Object.keys(app.data.players).map((uuid) => 
                <Player key={uuid} uuid={uuid} app={app}/>
            )}
        </div>
    )
}