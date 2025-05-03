import useAssets from "../useAssets";
import CanvasHandler from "./CanvasHandler";
import ErrorHandler from "./ErrorHandler";

// https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/random
function getRandomInt(max) {
    return Math.floor(Math.random() * max);
}

const assets = useAssets();


class Button {
    constructor ( value, index, color, label, hovered ) {
        this.value = value;
        this.index = index;
        this.color = color;
        this.label = label;
        this.hovered = hovered
    }
}

export default class LobbyHome extends CanvasHandler {

    constructor ( app, data ) {
        super( app, data );
        this.games = [
            new Button("Uno", 0, [50, 0, 0], "UNO", false),
            new Button("Loup", 1, [100, 0, 0], "LOUP-GAROU", false),
            new Button("Question", 2, [150, 0, 0], "QUESTIONS", false)
        ];
    }

    getType() {
        return "LobbyHome"
    }
    
    onclick ( event ) {
        for (let i = 0; i < this.games.length; i++) {
            let game = this.games[i];
            if (game.hovered) {
                console.log(game.value);
                this.app.packAction(
                    "game:select",
                    {"game": game.index}
                ).then((response) => {
                    if (response.data.code != 0) {
                        ErrorHandler(response);
                    }
                }).catch(ErrorHandler);
                break;
            }
        }

        if (this.isHovered([ 0, 0, 255 ])) {
            this.app.packAction(
                "game:apply",
                {}
            ).then((response) => {
                if (response.data.code != 0) {
                    ErrorHandler(response);
                }
            }).catch(ErrorHandler);
        }
    }

    onmousemove ( event ) {
        
    }


    loop ( time ) {

        super.loop( time );

        // console.log("LobbyHome loop");

        // detect if mouse pointer is hovering something
        for (let i = 0; i < this.games.length; i++) {
            let game = this.games[i];
            game.hovered = this.isHovered( game.color );
            // console.log(this.data == this.app.gameData == this.app.data.game.data != this.app.data); // this log true
            this.drawTitles(this.verticalPlace(game.index, this.games.length), game.hovered, game.color, game.label, game.index == this.data.selected_game);
        }

        // bouton JOUER
        if (this.app.isLobbyMaster()) {
            this.ctx.textAlign = "right";
            this.ctx.font = "48px Lexend";
            let jouerMesure = this.ctx.measureText("JOUER");
            this.ctx.fillStyle = this.isHovered([0,0,255]) ? "#fc4c4c" : this.toRGB(255, 198, 57);
            this.ctx.lineJoin = "round";
            this.ctx.lineWidth = 8;
            this.ctx.strokeStyle = this.isHovered([0,0,255]) ? "#fc4c4c" : this.toRGB(255, 198, 57);
            this.ctx.fillRect(this.width - 12, this.height - 12, -jouerMesure.width - 32, -68);
            this.ctx.strokeRect(this.width - 12, this.height - 12, -jouerMesure.width - 32, -68);
            this.octx.lineJoin = "round";
            this.octx.lineWidth = 8;
            this.octx.fillStyle = this.toRGB(0, 0, 255);
            this.octx.strokeStyle = this.toRGB(0, 0, 255);
            this.octx.fillRect(this.width - 12, this.height - 12, -jouerMesure.width - 32, -68);
            this.octx.strokeRect(this.width - 12, this.height - 12, -jouerMesure.width - 32, -68);
            this.ctx.fillStyle = this.toRGB(0, 0, 0);
            this.ctx.fillText("JOUER", this.width - 28, this.height - 28);
        }
        // console.log(this.app)
        // this.ctx.fillText(this.app.data.msg, 50, 50);
        // show debug information
        this.ctx.textAlign = "left"
        this.ctx.font = "50px serif";
        this.ctx.fillStyle = this.toRGB(127, 127, 127);
        this.ctx.fillText( Math.round( 10000 / this.deltaTime ) / 10 + " FPS", 50, 80 );
        this.ctx.fillText( this.hover[0] + ", " + this.hover[1] + ", " + this.hover[2], 50, 160 );
        this.ctx.fillText( this.mouse.x + ", " + this.mouse.y, 50, 240 );
        
        if ( sessionStorage.getItem( "debug" ) == "true" ) this.debugDraw();
    }

    drawTitles(pos, hovered, hitbox_color, text, selected) {
        this.ctx.font = "64px Lexend";
        this.ctx.textAlign = "center";
        if (hovered) {
            this.ctx.fillStyle = "#aaaaaa";
            this.ctx.fillRect(pos[0] - 300, pos[1] - 80, 600, 160);
        }
        this.octx.fillStyle = `rgb( ${ hitbox_color[0] }, ${ hitbox_color[1] }, ${ hitbox_color[2] } )`;
        this.octx.fillRect(pos[0] - 300, pos[1] - 80, 600, 160);
        this.ctx.lineJoin = "round";
        this.ctx.lineWidth = 8;
        this.ctx.strokeStyle = "#000000";
        if (selected) this.ctx.strokeRect(pos[0] - 300, pos[1] - 80, 600, 160);
        this.ctx.fillStyle = "#000000";
        this.ctx.fillText( text, pos[0], pos[1] + 24 );

    }

    verticalPlace (i, size) {
        let x = Math.round(this.width / 2);
        let y = Math.round((i * 2 + 1) * this.height / (size * 2));
        return [ x, y ];
    }
}
