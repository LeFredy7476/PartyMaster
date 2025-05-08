import './index.css'
import Lobby from './Lobby.jsx'
import MainMenu from './MainMenu.jsx'
import JoinLobby from './JoinLobby.jsx'
import Docs from './Docs.jsx'
import {
    BrowserRouter,
    Routes,
    Route,
    Link,
    useParams
  } from "react-router";
import { useState } from 'react'

export default function Router() {
    
    const [connected, setconnected] = useState(false);

    return (
        <>
            <BrowserRouter>
                <Routes>
                    <Route exact path='/' element={<MainMenu connected={connected} setconnected={setconnected} />} />
                    <Route path='/docs/:game' element={<Docs />} setconnected={setconnected} />
                    <Route path='/:room' element={connected ? <Lobby connected={connected} setconnected={setconnected} /> : <JoinLobby connected={connected} setconnected={setconnected} />} />
                </Routes>
            </BrowserRouter>
        </>
    )
}