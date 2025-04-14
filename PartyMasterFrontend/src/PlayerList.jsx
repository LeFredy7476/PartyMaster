import Player from "./Player";
import FullscreenButton from "./FullscreenButton";
import ShareButton from "./ShareButton";


export default function PlayerList({app}) {
    return (
        <div className="sidebar">
            <div className="playeractions">
                <h1 className="currentroom">{app.data.room}</h1>
                <FullscreenButton />
                <ShareButton />
            </div>
            <div className="playerlist">
                {Object.keys(app.data.players).map((uuid) => 
                    <Player key={uuid} uuid={uuid} app={app}/>
                )}
            </div>
        </div>
    )
}