import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import Player from './Player'
import './App.css'
import PlayerList from './PlayerList'
import Game from './Game'
import Chat from './Chat'

function App() {

    const data = {
        "players": {
            "0": { "name": "Bob Ross", "icon": 1 },
            "1": { "name": "Jack Dalton", "icon": 0 },
            "2": { "name": "Mike Brown", "icon": 1 },
            "3": { "name": "Joe Dassin", "icon": 1 },
            "4": { "name": "Marc Robinson", "icon": 0 },
            "5": { "name": "Derick Robert", "icon": 1 },
            "6": { "name": "Bob Ross", "icon": 1 },
            "7": { "name": "Jack Dalton", "icon": 0 },
            "8": { "name": "Jack Dalton", "icon": 1 },
            "9": { "name": "Jack Dalton", "icon": 1 },
            "a": { "name": "Jack Dalton", "icon": 0 },
            "b": { "name": "Jack Dalton", "icon": 1 },
            "c": { "name": "Jack Dalton", "icon": 0 }
        }
    }

    return (
        <>
            <PlayerList data={data} />
            <Game />
            <Chat />
        </>
    )
}

export default App
