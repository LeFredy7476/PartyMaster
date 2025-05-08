import Player from "./Player";
import FullscreenButton from "./FullscreenButton";
import ShareButton from "./ShareButton";


export default function PlayerList({app}) {
    return (
        <div className="sidebar">
            <div className="playeractions">
                <button className="micon" id="quit" onClick={function() {
                    app.quit();
                }} >home</button>
                <FullscreenButton />
                {/* <ShareButton /> */}
                <h1 className="currentroom">{app.data.room}</h1>
            </div>
            <div className="playerlist">
                {Object.keys(app.data.players).map((uuid) => 
                    <Player key={uuid} uuid={uuid} app={app}/>
                )}
            </div>
        </div>
    )
}