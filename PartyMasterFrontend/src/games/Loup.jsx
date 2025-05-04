import useAssets from "../useAssets";
import CanvasHandler from "./CanvasHandler";
import ErrorHandler from "./ErrorHandler";

// https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/random
function getRandomInt(max) {
    return Math.floor(Math.random() * max);
}

const assets = useAssets();

const statesNames = {
    DISTRIBUTION_ROLE: "",
    CUPIDON_CHOIX: "CUPIDON_CHOIX",
    AMOUREUX_REVELATION: "AMOUREUX_REVELATION",
    GUARDIEN_CHOIX: "GUARDIEN_CHOIX",
    VOYANTE_CHOIX: "VOYANTE_CHOIX",
    VOYANTE_REVELATION: "VOYANTE_REVELATION",
    LOUP_CHOIX: "LOUP_CHOIX",
    TRAITRE_CHOIX: "TRAITRE_CHOIX",
    TRAITRE_REVELATION: "TRAITRE_REVELATION",
    VILLAGE_EXECUTION: "VILLAGE_EXECUTION",
    CHASSEUR_CHOIX: "CHASSEUR_CHOIX",
    RESULTAT: "RESULTAT",
}

class ExpFollower {
    constructor ( x, y, rot, size, opacity ) {
        this.trueX = x;
        this.targetX = x;
        this.trueY = y;
        this.targetY = y;
        this.trueRotation = rot;
        this.targetRotation = rot;
        this.trueSize = size;
        this.targetSize = size;
        this.trueOpacity = opacity;
        this.targetOpacity = opacity;
        this.ratio = 0.001;
    }

    get x() {
        return this.trueX;
    }
    set x( value ) {
        this.targetX = value;
    }
    get y() {
        return this.trueY;
    }
    set y( value ) {
        this.targetY = value;
    }
    get rotation() {
        return this.trueRotation;
    }
    set rotation( value ) {
        this.targetRotation = value;
    }
    get size() {
        return this.trueSize;
    }
    set size( value ) {
        this.targetSize = value;
    }
    get opacity() {
        return this.trueOpacity;
    }
    set opacity( value ) {
        this.targetOpacity = value;
    }

    expFollow( deltaTime, current, target ) {
        let diff = target - current;
        let new_diff = diff * Math.pow( this.ratio, deltaTime / 1000 );
        return target - new_diff;
    }

    update ( deltaTime ) {
        this.trueX = this.expFollow( deltaTime, this.trueX, this.targetX );
        this.trueY = this.expFollow( deltaTime, this.trueY, this.targetY );
        this.trueRotation = this.expFollow( deltaTime, this.trueRotation, this.targetRotation );
        this.trueSize = this.expFollow( deltaTime, this.trueSize, this.targetSize );
        this.trueOpacity = this.expFollow( deltaTime, this.trueOpacity, this.targetOpacity );
    }

    transform ( func, canvasHandler, rotate = true ) {
        // setup transform
        canvasHandler.ctx.save();
        canvasHandler.ctx.translate( this.trueX, this.trueY );
        if (rotate) canvasHandler.ctx.rotate( this.trueRotation );
        canvasHandler.octx.save();
        canvasHandler.octx.translate( this.trueX, this.trueY );
        // do not rotate the hitbox
        if (rotate) canvasHandler.octx.rotate( this.trueRotation );

        // execute tasks
        func( canvasHandler );
        
        // reset transform
        canvasHandler.ctx.restore();
        canvasHandler.octx.restore();
    }
}



class Joueur {
    constructor ( uuid, hitboxColor ) {
        this.uuid = uuid;
        this.expFollower = new ExpFollower(0, 0, 0, 0.1, 0);
        this.expFollower.size = 1;
        this._hover = false;
        this.hitboxColor = hitboxColor;
    }

    get hover() {
        return this._hover;
    }

    set hover(value) {
        this.expFollower.size = value ? 1.25 : 1;
        this._hover = value;
    }

    place(x, y, teleport = false) {
        this.expFollower.x = x;
        this.expFollower.y = y;
        if (teleport) {
            this.expFollower.trueX = x;
            this.expFollower.trueY = y;
        }
    }

    draw( game ) {
        let self = this;
        this.expFollower.transform((canvasHandler) => {
            let myRole = game.data.joueurs[self.uuid].role;
            let myIcon = game.app.data.players[self.uuid].icon;
            let ctx = canvasHandler.ctx;
            // let ctx = document.createElement("canvas").getContext("2d");
            ctx.save();
            if (myIcon != null) {
                ctx.globalAlpha = 1.0;
                let myIconAsset = game.images.players[myIcon];
                ctx.beginPath();
                ctx.arc(0, 0, myIconAsset.r * 2, 0, Math.PI * 2);
                ctx.clip();
                if (myIconAsset.image.complete) {
                    ctx.drawImage(myIconAsset.image, -myIconAsset.cx * 2, -myIconAsset.cy * 2, myIconAsset.w * 2, myIconAsset.h * 2);
                }
                // ctx.beginPath();
                // ctx.arc(0, 0, myIconAsset.r * 2, 0, Math.PI * 2);
                ctx.lineWidth = 8;
                ctx.stroke()
            }
            ctx.restore();
            if (myRole != null) {
                ctx.globalAlpha = self.expFollower.trueRotation / (Math.PI * 2);
                let myRoleAsset = game.images.roles[myRole.toLowerCase()];
                if (myRoleAsset.image.complete) {
                    ctx.drawImage(myRoleAsset.image, -myRoleAsset.cx * 2, -myRoleAsset.cy * 2, myRoleAsset.w * 2, myRoleAsset.h * 2);
                }
            }
        }, game);
        this.expFollower.transform((canvasHandler) => {
            let myName = game.app.data.players[self.uuid].name;
            let ctx = canvasHandler.ctx;
            // let ctx = document.createElement("canvas").getContext("2d");
            let octx = canvasHandler.octx;
            octx.fillStyle = canvasHandler.toRGB(self.hitboxColor);
            octx.fillRect(-100, -100, 200, 200);
            ctx.textAlign = "center";
            ctx.font = "24px Lexend";
            ctx.fillStyle = canvasHandler.toRGB(0, 0, 0);
            ctx.fillText(myName, 0, 100);
        }, game, false);
    }

    update(deltaTime, game) {
        this.expFollower.rotation = (game.data.joueurs[this.uuid].role != null) ? Math.PI * 2 : 0;
        this.expFollower.update(deltaTime);
    }
}

export default class Loup extends CanvasHandler {
    constructor ( app, data ) {

        super( app, data );
        let myUuid = sessionStorage.getItem("uuid");
        let myData = this.data.joueurs[myUuid];
        let amour = myData.amour;
        let iAmLoup = myData.role == "LOUPBLANC" || myData.role == "LOUP";

        for (let uuid in this.data.joueurs) {
            if (Object.prototype.hasOwnProperty.call(this.data.joueurs, uuid)) {
                const player = this.data.joueurs[uuid];
                if (uuid != amour && uuid != myUuid) {
                    if (!(iAmLoup && (player.role == "LOUPBLANC" || player.role == "LOUP"))) {
                        this.data.joueurs[uuid].role = null;
                    } else if (player.role == "LOUPBLANC") {
                        this.data.joueurs[uuid].role = "LOUP"
                    }
                    if (myData.role != "CUPIDON") {
                        this.data.joueurs[uuid].amour = null;
                    }
                }
            }
        }

        this.images = {
            players: [],
            roles: {}
        };
        this.joueurs = {};

        for (let i = 0; i < assets.players.length; i++) {
            let player = assets.players[i];
            let img = new Image();
            img.src = player.src;
            this.images.players.push({
                ...player,
                image: img
            });
        }

        for (let key in assets.loupgaroux) {
            if (Object.prototype.hasOwnProperty.call(assets.loupgaroux, key)) {
                let role = assets.loupgaroux[key];
                let img = new Image();
                img.src = role.src;
                this.images.roles[key] = {
                    ...role,
                    image: img
                };
            }
        }

        let i = 0;
        for (const uuid in this.app.data.players) {
            if (Object.prototype.hasOwnProperty.call(this.app.data.players, uuid)) {
                let player = this.app.data.players[uuid];
                let joueur = this.data.joueurs[uuid];
                this.joueurs[uuid] = new Joueur(
                    uuid,
                    [ i * 5, 0, 0 ]
                );
                i++;
            }
        }

        this.app.packAction("game:ready", {}).then((response) => {
            if (response.data.code != 0) {
                ErrorHandler(response.data);
            }
        }).catch(ErrorHandler);
    }

    loop ( time ) {
        super.loop( time );

        this.gridcalculator();
        this.placePlayers();

        for (const uuid in this.joueurs) {
            if (Object.prototype.hasOwnProperty.call(this.joueurs, uuid)) {
                this.joueurs[uuid].update(this.deltaTime, this);
                this.joueurs[uuid].draw(this);
            }
        }

        this.ctx.textAlign = "left"
        this.ctx.font = "50px sans-serif";
        this.ctx.fillStyle = this.toRGB(127, 127, 127);
        this.ctx.fillText( Math.round( 10000 / this.deltaTime ) / 10 + " fps", 50, 80 );
        this.ctx.fillText( this.hover[0] + ", " + this.hover[1] + ", " + this.hover[2], 50, 160 );
        this.ctx.fillText( this.mouse.x + ", " + this.mouse.y, 50, 240 );
        
        if ( sessionStorage.getItem( "debug" ) == "true" ) this.debugDraw();
    }

    placePlayers () {
        let x = Math.round((this.width - (this.columns * 200)) / 2) + 100;
        let y = Math.round((this.height - (this.rows * 240)) / 2) + 100;
        let i = 0;
        for (const uuid in this.joueurs) {
            if (Object.prototype.hasOwnProperty.call(this.joueurs, uuid)) {
                this.joueurs[uuid].place(x + (i % this.columns) * 200, y + Math.trunc(i / this.columns) * 240);
                i++;
            }
        }
    }

    gridcalculator () {
        this.columns = Math.min(Math.max(3, Math.floor(this.width % 200)), 7, Object.keys(this.joueurs).length);
        this.rows = Math.ceil(Object.keys(this.joueurs).length / this.columns);
    }
}