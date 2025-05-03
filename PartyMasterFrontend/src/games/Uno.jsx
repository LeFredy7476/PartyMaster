import useAssets from "../useAssets";
import CanvasHandler from "./CanvasHandler";
import ErrorHandler from "./ErrorHandler";

// https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/random
function getRandomInt(max) {
    return Math.floor(Math.random() * max);
}

const assets = useAssets();

export class ExpFollower {
    constructor ( x, y, flip, rot, size ) {
        this.trueX = x;
        this.targetX = x;
        this.trueY = y;
        this.targetY = y;
        this.trueFlip = flip;
        this.targetFlip = flip;
        this.trueRotation = rot;
        this.targetRotation = rot;
        this.trueSize = size;
        this.targetSize = size;
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
    get flip() {
        return this.trueFlip;
    }
    set flip( value ) {
        this.targetFlip = value;
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

    get flipped() {
        return this.flip <= 0;
    }

    expFollow( deltaTime, current, target ) {
        let diff = target - current;
        let new_diff = diff * Math.pow( this.ratio, deltaTime / 1000 );
        return target - new_diff;
    }

    update ( deltaTime ) {
        this.trueX = this.expFollow( deltaTime, this.trueX, this.targetX );
        this.trueY = this.expFollow( deltaTime, this.trueY, this.targetY );
        this.trueFlip = this.expFollow( deltaTime, this.trueFlip, this.targetFlip );
        this.trueRotation = this.expFollow( deltaTime, this.trueRotation, this.targetRotation );
        this.trueSize = this.expFollow( deltaTime, this.trueSize, this.targetSize );
    }

    transform ( func, canvasHandler ) {
        // setup transform
        canvasHandler.ctx.save();
        canvasHandler.ctx.translate( this.trueX, this.trueY );
        canvasHandler.ctx.rotate( this.trueRotation );
        canvasHandler.ctx.scale( this.trueFlip, 1 );
        canvasHandler.octx.save();
        canvasHandler.octx.translate( this.trueX, this.trueY );
        canvasHandler.octx.rotate( this.trueRotation );
        canvasHandler.octx.scale( this.trueFlip, 1 );

        // execute tasks
        func( canvasHandler );
        
        // reset transform
        canvasHandler.ctx.restore();
        canvasHandler.octx.restore();
    }

    draw ( canvasHandler ) {} // put all the drawing stuff here when implementing as a prototype
}

// const backfaceImage = {
//     image: new Image(),
//     asset: assets.unocards.backface
// };
// backfaceImage.image.src = backfaceImage.asset.src;

class Card extends ExpFollower {

    constructor ( x, y, flip, rot, size, asset, id, color, number, hitbox = [ 0, 0, 0 ] ) {
        super( x, y, flip, rot, size );
        this.image = new Image();
        this.asset = asset;
        this.image.src = this.asset.src;
        this.id = id;
        this.color = color;
        this.number = number;
        this.hitbox = hitbox;
        this.backfaceImage = {
            image: new Image(),
            asset: assets.unocards.backface
        };
        this.backfaceImage.image.src = this.backfaceImage.asset.src;

    }

    draw ( canvasHandler ) {
        canvasHandler.octx.scale( this.size, this.size );
        canvasHandler.octx.translate( -this.backfaceImage.asset.cx, -this.backfaceImage.asset.cy );
        canvasHandler.octx.fillStyle = `rgb( ${ this.hitbox[0] }, ${ this.hitbox[1] }, ${ this.hitbox[2] } )`;
        canvasHandler.octx.fillRect( 0, 0, this.backfaceImage.asset.w, this.backfaceImage.asset.h );

        if ( this.image.complete && this.backfaceImage.image.complete ) {
            canvasHandler.ctx.scale( this.size, this.size );
            if ( this.flip > 0 ) {
                canvasHandler.ctx.translate( -this.asset.cx, -this.asset.cy );
                canvasHandler.ctx.drawImage( this.image, 0, 0, this.asset.w, this.asset.h );
            } else {
                canvasHandler.ctx.translate( -this.backfaceImage.asset.cx, -this.backfaceImage.asset.cy );
                canvasHandler.ctx.drawImage( this.backfaceImage.image, 0, 0, this.backfaceImage.asset.w, this.backfaceImage.asset.h );
            }
        }
        
    }
}

export default class Uno extends CanvasHandler {
    constructor ( app, data ) {
        super( app, data );
        
        let halfWidth = Math.round(this.width / 2);
        let halfHeight = Math.round(this.height / 2);

        this.allCards = [];
        let colors = ["BLUE", "GREEN", "RED", "YELLOW"];
        let id = 0;
        for (let color = 0; color < colors.length; color++) {
            for (let number = 0; number < 13; number++) {
                let id1 = id++;
                let id2 = id++;
                this.allCards.push(new Card (
                    halfWidth, halfHeight,
                    -1, 0, 1,
                    assets.unocards[colors[color].toLowerCase()][number],
                    id1, colors[color], number,
                    [ id1, 0, 0 ]
                ));
                this.allCards.push(new Card (
                    halfWidth, halfHeight,
                    -1, 0, 1,
                    assets.unocards[colors[color].toLowerCase()][number],
                    id2, colors[color], number,
                    [ id2, 0, 0 ]
                ));
            }
        }
        for (let i = 0; i < 4; i++) {
            let _id = id++;
            this.allCards.push(new Card (
                halfWidth, halfHeight,
                -1, 0, 1,
                assets.unocards.color,
                _id, "MULTI", 13,
                [ _id, 0, 0 ]
            ));
        }
        for (let i = 0; i < 4; i++) {
            let _id = id++;
            this.allCards.push(new Card (
                halfWidth, halfHeight,
                -1, 0, 1,
                assets.unocards.p4,
                _id, "MULTI", 14,
                [ _id, 0, 0 ]
            ));
        }


    }

    getType() {
        return "Uno";
    }

    onclick( event ) {
        
    }

    loop ( time ) {
        super.loop(time);

        // console.log("Uno loop");

        for (let id = 0; id < this.allCards.length; id++) {
            let card = this.allCards[id];
            card.update(this.deltaTime);
            // card.draw(this);
            card.transform( ( self ) => {
                card.draw( self );
                // console.log("drawn: " + card.id);
            }, this );
        }

        // console.log(this.app)
        // this.ctx.fillText(this.app.data.msg, 50, 50);
        // show debug information
        this.ctx.textAlign = "left"
        this.ctx.font = "50px serif";
        this.ctx.fillStyle = this.toRGB(127, 127, 127);
        this.ctx.fillText( Math.round( 10000 / this.deltaTime ) / 10 + " fps", 50, 80 );
        this.ctx.fillText( this.hover[0] + ", " + this.hover[1] + ", " + this.hover[2], 50, 160 );
        this.ctx.fillText( this.mouse.x + ", " + this.mouse.y, 50, 240 );
        
        if ( sessionStorage.getItem( "debug" ) == "true" ) this.debugDraw();
    }


    placeOwnCards() {
        let amount = this.data.players[sessionStorage.getItem("uuid")].length
        for (let index = 0; index < amount; index++) {
            let cardId = this.data.players[sessionStorage.getItem("uuid")][index];
            let card = this.allCards[cardId];
        }
    }
}