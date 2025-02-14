import { useState } from 'react'
import reactLogo from './assets/react.svg'
import viteLogo from '/vite.svg'
import Player from './Player'
import './App.css'
import PlayerList from './PlayerList'
import Game from './Game'
import Chat from './Chat'

function App() {
  
  const data = [
    {"name": "Bob Ross", "icon": 1},
    {"name": "Jack Dalton", "icon": 0},
    {"name": "Mike Brown", "icon": 1},
    {"name": "Joe Dassin", "icon": 1},
    {"name": "Marc Robinson", "icon": 0},
    {"name": "Derick Robert", "icon": 1},
    {"name": "Bob Ross", "icon": 1},
    {"name": "Jack Dalton", "icon": 0},
    {"name": "Jack Dalton", "icon": 1},
    {"name": "Jack Dalton", "icon": 1},
    {"name": "Jack Dalton", "icon": 0},
    {"name": "Jack Dalton", "icon": 1},
    {"name": "Jack Dalton", "icon": 0}
  ]

  return (
    <>
      <PlayerList data={data}/>
      <Game />
      <Chat />
    </>
  )
}

export default App
