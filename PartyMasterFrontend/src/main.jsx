import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import Lobby from './Lobby.jsx'
import MainMenu from './MainMenu.jsx'
import JoinLobby from './JoinLobby.jsx'
import Docs from './Docs.jsx'
import {
    BrowserRouter as Router,
    Routes,
    Route,
    Link,
    useParams
  } from "react-router";

createRoot(document.getElementById('root')).render(
    <StrictMode>
        <Router>
            <Routes>
                <Route exact path='/' element={<MainMenu />} />
                <Route path='/docs/:game' element={<Docs />} />
                <Route path='/:room' element={<JoinLobby />} />
                <Route path='/:room/lobby' element={<Lobby />} />
            </Routes>
        </Router>
    </StrictMode>,
)
