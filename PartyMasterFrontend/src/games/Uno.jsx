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
        this.ratio = 0.0001;
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

    draw ( canvasHandler, owned = false ) {
        canvasHandler.octx.scale( this.size, this.size );
        canvasHandler.octx.translate( -this.backfaceImage.asset.cx, -this.backfaceImage.asset.cy );
        let switchOpacity = 5 * (this.size - 1.0);
        
        canvasHandler.octx.fillStyle = `rgb( ${ this.hitbox[0] }, ${ 0 }, ${ 0 } )`;
        canvasHandler.octx.fillRect( 0, 0, this.backfaceImage.asset.w, this.backfaceImage.asset.h );
        if (owned && this.color == "MULTI") {
            canvasHandler.octx.fillStyle = `rgb( ${ this.hitbox[0] }, ${ 64 }, ${ 0 } )`;
            canvasHandler.octx.fillRect( 50, 0, this.backfaceImage.asset.w - 50, this.backfaceImage.asset.h );
            canvasHandler.octx.fillStyle = `rgb( ${ this.hitbox[0] }, ${ 128 }, ${ 0 } )`;
            canvasHandler.octx.fillRect( 0, 75, this.backfaceImage.asset.w, this.backfaceImage.asset.h - 75 );
            canvasHandler.octx.fillStyle = `rgb( ${ this.hitbox[0] }, ${ 192 }, ${ 0 } )`;
            canvasHandler.octx.fillRect( 50, 75, this.backfaceImage.asset.w - 50, this.backfaceImage.asset.h - 75 );
        }
        if ( this.image.complete && this.backfaceImage.image.complete ) {
            canvasHandler.ctx.scale( this.size, this.size );
            if ( this.flip > 0 ) {
                canvasHandler.ctx.translate( -this.asset.cx, -this.asset.cy );
                canvasHandler.ctx.drawImage( this.image, 0, 0, this.asset.w, this.asset.h );
                if (switchOpacity > 0.05 && owned && this.color == "MULTI" && canvasHandler.switchImage.complete) {
                    canvasHandler.ctx.globalAlpha = switchOpacity;
                    canvasHandler.ctx.drawImage( canvasHandler.switchImage, 0, 0, this.asset.w, this.asset.h );
                    canvasHandler.ctx.globalAlpha = 1;

                }
            } else {
                canvasHandler.ctx.translate( -this.backfaceImage.asset.cx, -this.backfaceImage.asset.cy );
                canvasHandler.ctx.drawImage( this.backfaceImage.image, 0, 0, this.backfaceImage.asset.w, this.backfaceImage.asset.h );
            }
        }
        
    }
}

class Spinner extends ExpFollower {
    constructor(x, y, asset) {
        super(x, y, 1, 0, 1);
        this.image = new Image();
        this.asset = asset;
        this.image.src = this.asset.src;
    }

    draw ( canvasHandler ) {
        if (this.image.complete) {
            canvasHandler.ctx.translate( -this.asset.cx, -this.asset.cy );
            canvasHandler.ctx.drawImage( this.image, 0, 0, this.asset.w, this.asset.h );
        }
    }
}

export default class Uno extends CanvasHandler {
    constructor ( app, data ) {
        super( app, data );
        
        let halfWidth = Math.round(this.width / 2);
        let halfHeight = Math.round(this.height / 2);

        this.allCards = [];
        this.colors = ["BLUE", "GREEN", "RED", "YELLOW"];
        let id = 0;
        for (let color = 0; color < this.colors.length; color++) {
            for (let number = 0; number < 13; number++) {
                let id1 = id++;
                let id2 = id++;
                this.allCards.push(new Card (
                    halfWidth, halfHeight,
                    -1, 0, 1,
                    assets.unocards[this.colors[color].toLowerCase()][number],
                    id1, this.colors[color], number,
                    [ id1, 0, 0 ]
                ));
                this.allCards.push(new Card (
                    halfWidth, halfHeight,
                    -1, 0, 1,
                    assets.unocards[this.colors[color].toLowerCase()][number],
                    id2, this.colors[color], number,
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

        this.ownCardDrawOrder = [];
        this.resetOwnCardDrawOrder();

        let selfIndex = this.data.table.indexOf(sessionStorage.getItem("uuid"));
        let tableBefor = this.data.table.slice(0, selfIndex);
        let tableAfter = this.data.table.slice(selfIndex + 1);
        console.log(this.data.table);
        console.log(sessionStorage.getItem("uuid"));
        console.log(tableBefor);
        console.log(tableAfter);
        this.customTable = [].concat(tableAfter, tableBefor);

        this.players = {};
        this.placePlayers();

        this.spinner = new Spinner(halfWidth, halfHeight, assets.unospinner);
        console.log(this);

        this.switchImage = new Image();
        this.switchImage.src = assets.unoswitch.src;
    }

    isHoveredBeyond ( color ) {
        let r = this.hover[0];
        let g = this.hover[1];
        let b = this.hover[2];
        return [this.compareColor( color, [r, 0, b] ), Math.round(g / 64)];
    }

    placePlayers() {
        let topspan = this.width - 240;
        let sidespan = Math.round((this.height - 240) * 0.75);
        let totalspan = sidespan * 2 + topspan;
        let playernb = this.customTable.length;
        let playerspan = totalspan / playernb;
        let halfplayerspan = playerspan / 2;
        for (let i = 0; i < this.customTable.length; i++) {
            let uuid = this.customTable[i];
            let pos = Math.round(i * playerspan + halfplayerspan);
            let x = 0;
            let y = 0;
            if (sidespan + topspan < pos) {
                // right side
                y = pos - (sidespan + topspan) + 120;
                x = this.width - 120;
            } else if (sidespan < pos) {
                // top side
                y = 120;
                x = pos - sidespan + 120;
            } else {
                x = 120;
                y = 120 + sidespan - pos;
            }
            this.players[uuid] = {x:x,y:y}
        }
    }

    resetOwnCardDrawOrder() {
        if (this.ownCardDrawOrder.length != this.data.players[sessionStorage.getItem("uuid")].length) {
            this.ownCardDrawOrder = this.data.players[sessionStorage.getItem("uuid")].slice();
        }
    }

    getType() {
        return "Uno";
    }

    onclick( event ) {
        // check if card clicked 
        let [r, g, b] = this.hover;
        if (r < 128) {
            if (this.data.deck.includes(r)) {
                // action for drawing a card
                this.app.packAction("game:draw", {}).then((response) => {
                    console.log(response);
                    if (response.data.code != 0) {
                        ErrorHandler(response.data);
                    }
                }).catch(ErrorHandler);
            } else if (this.data.players[sessionStorage.getItem("uuid")].includes(r)) {
                // action for playing a card
                if (this.getCard(r).color == "MULTI") {
                    let color = this.colors[Math.round(g / 64)];
                    this.app.packAction("game:play:color", {card: r, color: color}).then((response) => {
                        console.log(response);
                        if (response.data.code != 0) {
                            ErrorHandler(response.data);
                        }
                    }).catch(ErrorHandler);
                } else {
                    this.app.packAction("game:play:normal", {card: r}).then((response) => {
                        console.log(response);
                        if (response.data.code != 0) {
                            ErrorHandler(response.data);
                        }
                    }).catch(ErrorHandler);
                }
            }
        }
    }

    loop ( time ) {
        super.loop(time);

        this.placePlayers();
        this.drawSpinner();
        this.resetOwnCardDrawOrder();
        this.placeOwnCards();
        this.placeAllPlayersCards();
        this.placeTableCenter();

        // show debug information
        // if ( sessionStorage.getItem( "debug" ) == "true" ) this.debugDraw();
        // this.ctx.textAlign = "left"
        // this.ctx.font = "50px serif";
        // this.ctx.fillStyle = this.toRGB(127, 127, 127);
        // this.ctx.fillText( Math.round( 10000 / this.deltaTime ) / 10 + " fps", 50, 80 );
        // this.ctx.fillText( this.hover[0] + ", " + this.hover[1] + ", " + this.hover[2], 50, 160 );
        // this.ctx.fillText( this.mouse.x + ", " + this.mouse.y, 50, 240 );
    }

    getCard(index) {
        return this.allCards[index];
    }

    drawSpinner() {
        let halfWidth = Math.round(this.width / 2);
        let halfHeight = Math.round(this.height / 2);
        let angle = 0;
        if (this.data.currentPlayer != sessionStorage.getItem("uuid")) {
            let pos = this.players[this.data.currentPlayer];
            angle = Math.atan2(pos.x - halfWidth, pos.y - halfHeight);
        }
        this.spinner.rotation = angle;
        this.spinner.x = halfWidth;
        this.spinner.y = halfHeight;
        this.spinner.update(this.deltaTime);
        this.spinner.transform((self) => {
            self.ctx.beginPath();
            self.ctx.arc(0, 0, assets.unospinner.r, 0, Math.PI * 2);
            let color = {
                BLUE: "#5050e2",
                GREEN: "#368e36",
                RED: "#e54545",
                YELLOW: "#ffc639"
            }[self.data.currentColor];
            console.log(color);
            self.ctx.fillStyle = color;
            self.ctx.fill();
            self.spinner.draw(self);
        }, this);
    }

    placeTableCenter() {
        let halfWidth = Math.round(this.width / 2);
        let halfHeight = Math.round(this.height / 2);
        for (let index = this.data.deck.length - 1; index >= 0; index--) {
            let cardId = this.data.deck[index];
            let card = this.getCard(cardId);
            card.flip = -1;
            card.x = halfWidth - 60;
            card.y = halfHeight;
            card.size = 1;
            card.rotation = 0;
            card.update(this.deltaTime);
            card.transform( ( self ) => {
                card.draw( self );
            }, this );
        }
        for (let index = this.data.flush.length - 1; index >= 0; index--) {
            let cardId = this.data.flush[index];
            let card = this.getCard(cardId);
            card.flip = 1;
            card.x = halfWidth + 60;
            card.y = halfHeight;
            card.size = 1;
            card.rotation = 0;
            card.update(this.deltaTime);
            card.transform( ( self ) => {
                card.draw( self );
            }, this );
        }
    }

    placeAllPlayersCards() {
        // console.log(this);
        for (let playerIndex = 0; playerIndex < this.customTable.length; playerIndex++) {
            let uuid = this.customTable[playerIndex];
            let pos = this.players[uuid];
            // console.log(pos);
            let rot = 0;
            let lastCard = pos;
            for (let cardindex = 0; cardindex < this.data.players[uuid].length; cardindex++) {
                let cardId = this.data.players[uuid][cardindex];
                let card = this.getCard(cardId);
                card.flip = -1;
                card.size = 1;
                card.x = pos.x;
                card.y = pos.y;
                card.rotation = rot;
                rot = rot + 0.1;
                card.update(this.deltaTime);
                card.transform( ( self ) => {
                    card.draw( self );
                }, this );
                lastCard = card;
            }
            let playername = this.app.data.players[uuid].name;
            this.ctx.lineJoin = "round";
            this.ctx.lineCap = "round";
            this.ctx.textBaseline = "middle";
            this.ctx.strokeStyle = "#fff";
            this.ctx.fillStyle = "#000";
            this.ctx.font = "600 24px Lexend";
            this.ctx.textAlign = "center";
            this.ctx.lineWidth = 8;
            this.ctx.strokeText(playername, lastCard.x, lastCard.y);
            this.ctx.fillText(playername, lastCard.x, lastCard.y);
        }
    }


    placeOwnCards() {
        // let halfWidth = Math.round(this.width / 2);
        let amount = this.data.players[sessionStorage.getItem("uuid")].length;
        let y = this.height - 100;
        let widthSpan = Math.min(this.width - 200, 80 * (amount - 1));
        let left = Math.round((this.width - widthSpan) / 2);
        let spacing = 0;
        if (amount > 1) {
            spacing = widthSpan / (amount - 1);
        }
        let topped = false;
        let drawOrder = [];
        for (let index = 0; index < amount; index++) {
            let cardId = this.data.players[sessionStorage.getItem("uuid")][index];
            let card = this.getCard(cardId);
            if (topped) {
                drawOrder.push(cardId);
            } else {
                drawOrder.unshift(cardId);
            }
            card.flip = 1;
            card.x = left + spacing * index;
            card.y = y;
            card.size = 1;
            card.rotation = 0;
            let [r,g,b] = card.hitbox;

            if (this.isHoveredBeyond(card.hitbox)[0]) {
                topped = true;
                card.y = y - 15;
                card.size = 1.2;
            }
            card.update(this.deltaTime);
        }
        if (topped) {
            drawOrder.reverse();
            this.ownCardDrawOrder = drawOrder;
        }
        for (let index = 0; index < this.ownCardDrawOrder.length; index++) {
            let card = this.getCard(this.ownCardDrawOrder[index]);
            card.transform( ( self ) => {
                card.draw( self, true );
            }, this );
        }
    }
}