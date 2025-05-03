import useAssets from "../useAssets";
import CanvasHandler from "./CanvasHandler";
import ErrorHandler from "./ErrorHandler";

// https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/random
function getRandomInt(max) {
    return Math.floor(Math.random() * max);
}

const assets = useAssets();

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

    transform ( func, canvasHandler ) {
        // setup transform
        canvasHandler.ctx.save();
        canvasHandler.ctx.translate( this.trueX, this.trueY );
        canvasHandler.ctx.rotate( this.trueRotation );
        canvasHandler.octx.save();
        canvasHandler.octx.translate( this.trueX, this.trueY );
        canvasHandler.octx.rotate( this.trueRotation );

        // execute tasks
        func( canvasHandler );
        
        // reset transform
        canvasHandler.ctx.restore();
        canvasHandler.octx.restore();
    }
}



class Joueur {
    constructor ( uuid, name, images, icon, role, amour, vivant, known ) {
        this.uuid = uuid;
        this.name = name;
        this.expFollower = new ExpFollower(0, 0, 0, 1, 0);
        this.icon = icon;
        this.role = role;
        this.amour = amour;
        this.vivant = vivant;
        this.images = images;
        this._hover = false;
        this._known = known;
    }

    get hover() {
        return this._hover;
    }

    set hover(value) {
        this.expFollower.size = value ? 1.25 : 1;
        this._hover = value;
    }

    get known() {
        return this._hover;
    }

    set known(value) {
        this.expFollower.size = value ? Math.PI * 2 : 0;
        this._known = value;
    }

    place(x, y) {
        this.expFollower.x = x;
        this.expFollower.y = y;
    }

    draw() {
        let self = this;
        this.expFollower.transform((canvasHandler) => {
            // let ctx = canvasHandler.ctx;
            let ctx = document.createElement("canvas").getContext("2d");
            ctx.drawImage(self.images);
        })

    }

    update(deltaTime) {
        this.expFollower.update(deltaTime);
    }
}

export default class Loup extends CanvasHandler {
    constructor ( app, data ) {
        super( app, data );

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

        for (const key in assets.loupgaroux) {
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

        for (const uuid in this.app.data.players) {
            if (Object.prototype.hasOwnProperty.call(this.app.data.players, uuid)) {
                let player = this.app.data.players[uuid];
                let joueur = this.data.joueurs[uuid];
                this.joueurs[uuid] = new Joueur(
                    uuid, 
                    player.name, 
                    this.images, 
                    player.icon, 
                    joueur.role, 
                    joueur.amour, 
                    joueur.vivant, 
                    uuid == sessionStorage.getItem("uuid")
                );
            }
        }


    }



    gridcalculator() {
        this.columns = Math.min(Math.max(3, Math.floor(this.width % 100)), 7);
        this.rows = Math.ceil(this.players / this.columns);
    }


}