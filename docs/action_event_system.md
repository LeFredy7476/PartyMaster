# Action Event reference sheet
Action are information sent to the server that may or may not expect an anwser.
Events are information fetch from the server.

## System
### action

- `create_party`
  - used on the home page to create a new party. the player automatically join the party upon creation. No permission or anything special are given (everything works by vote, maybe)
  - `send` desired username, avatar
  - `receive` confirm, uuid, final username

- `join_party`
  - used once by session by the client in order to get assigned to a uuid when joining an existing party.
  - `send` desired username, avatar
  - `receive` confirm, uuid, final username

- `quit_party`
  - used on page close.
  - `send` uuid

### events

- `ping`
  - used once every 4-5s in order to signal the server that you are still connected. If a party doesnt receive any ping for more than one minute, the party delete himself.
  - `send` uuid
  - `receive` confirm
    - if confirm is not ok, disconnect the player and refresh the page

## Party
### action

- `abort_game`
  - used by player to abort the current game. when more than 70% of the players wants to quit the game, the game will be aborted
  - `send` uuid, vote
    - vote will be "quit" or "stay"
  - `receive` 

### event

- `player_list`
  - used when entering a party in order to get all players already in the party
  - `send` uuid
  - `receive` list of players (uuid, avatar, username)

- `player`
  - used to get the info of a specific player
  - `send` uuid, target_uuid
  - `receive` confirm, avatar, username

- `player_event`
  - fetch the player event queue. On the server, each player has a player event queue of his own that is emptied by this command. 
  - `send` uuid
  - `receive` new_players, leavers, reactions

- `abort_vote`
  - fetch the amount of player who wants to quit the current game.
  - `send` uuid
  - `receive` vote_count

## Chat
### action

- `send_chat`
  - send a message in chat
  - `send` uuid, content
  - `receive` confirm
 
### event

- `chat_event`
  - retreive every new message in chat since last chat_event call
  - `send` uuid
  - `receive` list of message (uuid, content)

- `chat_history`
  - retreive the last 10 message sent in the chat. To be used when joining a party.
  - `send` uuid
  - `receive` list of message (uuid, content)

## Games (global)
### action

- `vote_game`
  - used to vote for the next game to play during voting phase
  - `send` uuid, game
  - `receive` confirm

### event

- `game_event`
  - retreive major game event like a game start, a game end, an aborted game ect...
  - `send` uuid
  - `receive` list of game_events (uuid, content)