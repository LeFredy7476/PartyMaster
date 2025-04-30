import useAssets from "../useAssets";
import CanvasHandler from "./CanvasHandler";
import ErrorHandler from "./ErrorHandler";

// https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/random
function getRandomInt(max) {
    return Math.floor(Math.random() * max);
}

const assets = useAssets();

export class ExpFollower {
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



class Joueurs {
    constructor ( name, images, icon ) {
        this.name = name;
        this.expFollower = new ExpFollower(0, 0, 0, 1, 0);
        this.icon = icon;
        this.role = null;
        this.images = images;
        this._hover = false;
    }

    get hover() {
        return this._hover;
    }

    set hover(value) {
        this.expFollower.size = value ? 1.25 : 1;
        this._hover = value;
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

        this.players = [];

        for (let i = 0; i < assets.players.length; i++) {
            let player = assets.players[i];
            
        }


    }



    gridcalculator() {
        this.columns = Math.min(Math.max(3, Math.floor(this.width % 100)), 7);
        this.rows = Math.ceil(this.players / this.columns);
    }


}