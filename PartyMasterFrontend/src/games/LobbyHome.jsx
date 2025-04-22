import useAssets from "../useAssets";

// https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/random
function getRandomInt(max) {
    return Math.floor(Math.random() * max);
}

const assets = useAssets();

class CanvasHandler {
    
    constructor ( app, data ) {
        this.app = app;
        this.data = data;

        this.offscreenCanvas = new OffscreenCanvas( this.width, this.height );

        this.ctx = this.canvas.getContext( "2d" );
        this.octx = this.offscreenCanvas.getContext( "2d", { willReadFrequently: true } );

        this.lastTime;
        this.deltaTime;

        this.mouse = { x: 0, y: 0 }

        let self = this;

        this.canvas.onclick = function ( e ) {
            self.onclick( e );
        };

        this.canvas.onmousemove = function ( e ) {
            let rect = self.canvas.getBoundingClientRect();
            self.mouse = {
                x: e.clientX - rect.left,
                y: e.clientY - rect.top
            };
            self.onmousemove( e );
        };
    }

    debugDraw () {
        this.ctx.drawImage(this.offscreenCanvas, 0, 0);
    }

    onclick ( event ) {}

    onmousemove ( event ) {}

    get width () {
        return this.canvas.width;
    }

    get height () {
        return this.canvas.height;
    }

    get canvas () {
        return document.querySelector( "#mainCanvas" );
    }

    get parentDiv () {
        return document.querySelector( "#game" );
    }
    
    clearCanvas () {
        this.ctx.clearRect( 0, 0, this.canvas.width, this.canvas.height );
        this.octx.clearRect( 0, 0, this.offscreenCanvas.width, this.offscreenCanvas.height );
        this.octx.fillStyle = "#ffffff";
        this.octx.fillRect( 0, 0, this.offscreenCanvas.width, this.offscreenCanvas.height );
    }

    resizeCanvas () {
        // https://stackoverflow.com/questions/4288253/html5-canvas-100-width-height-of-viewport
        let parent = this.parentDiv;
        // console.log(parent)
        this.canvas.width = parent.clientWidth;
        this.canvas.height = parent.clientHeight;
        this.offscreenCanvas.width = parent.clientWidth;
        this.offscreenCanvas.height = parent.clientHeight;
    }

    loop ( time ) {
        let self = this;
        this.resizeCanvas();
        // console.log("loop");
        // console.log(this);
        if (this.lastTime === undefined) {
            this.lastTime = time - 10;
        }
        this.deltaTime = time - this.lastTime;

        // clear canvas (remove last frame from display)
        this.clearCanvas();

        this.animationFrame = window.requestAnimationFrame( function ( t ) {
            self.loop( t );
        } );
        this.lastTime = time;
    }

    stop () {
        window.cancelAnimationFrame( this.animationFrame );
    }
    
    init () {
        let self = this;
        this.animationFrame = window.requestAnimationFrame( function ( t ) {
            self.loop( t );
        } );
    }
}

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
            new Button("Uno", 0, [10, 0, 0], "UNO", false),
            new Button("Loup", 1, [20, 0, 0], "LOUP-GAROU", false),
            new Button("Question", 2, [30, 0, 0], "QUESTIONS", false)
        ];
    }
    
    onclick ( event ) {
        for (let i = 0; i < this.games.length; i++) {
            let game = this.games[i];
            if (game.hovered) {
                console.log(game.value)
            }
        }
    }

    onmousemove ( event ) {
        
    }


    loop ( time ) {
        let imageData = this.octx.getImageData( this.mouse.x, this.mouse.y, 1, 1 );
        let [ r, g, b ] = imageData.data;

        super.loop( time );

        // detect if mouse pointer is hovering something
        for (let i = 0; i < this.games.length; i++) {
            let game = this.games[i];
            game.hovered = ( r == game.color[0] && g == game.color[1] && b == game.color[2] );
            this.drawTitles(this.verticalPlace(game.index, this.games.length), game.hovered, game.color, game.label);
        }
        
        if ( localStorage.getItem( "debug" ) == "true" ) this.debugDraw();
    }

    drawTitles(pos, hovered, hitbox_color, text) {
        this.ctx.font = "64px Lexend";
        this.ctx.textAlign = "center";
        if (hovered) {
            this.ctx.fillStyle = "#aaaaaa";
            this.ctx.fillRect(pos[0] - 300, pos[1] - 80, 600, 160);
        }
        this.octx.fillStyle = `rgb( ${ hitbox_color[0] }, ${ hitbox_color[1] }, ${ hitbox_color[2] } )`;
        this.octx.fillRect(pos[0] - 300, pos[1] - 80, 600, 160);
        this.ctx.fillStyle = "#000000";
        this.ctx.fillText( text, pos[0], pos[1] + 24 );

    }

    verticalPlace (i, size) {
        let x = Math.round(this.width / 2);
        let y = Math.round((i * 2 + 1) * this.height / (size * 2));
        return [ x, y ];
    }
}


class ExpFollower {
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


class Card extends ExpFollower {

    constructor ( x, y, flip, rot, size, img, color = [ 0, 0, 0 ] ) {
        super( x, y, flip, rot, size );
        this.image = new Image();
        this.image.data = img;
        this.image.src = this.image.data.src;
        this.color = color;
    }

    draw ( canvasHandler ) {

        canvasHandler.octx.scale( this.size, this.size );
        canvasHandler.octx.translate( -backfaceImage.data.cx, -backfaceImage.data.cy );
        canvasHandler.octx.fillStyle = `rgb( ${ this.color[0] }, ${ this.color[1] }, ${ this.color[2] } )`;
        canvasHandler.octx.fillRect( 0, 0, backfaceImage.data.w, backfaceImage.data.h );

        if ( this.image.complete && backfaceImage.complete ) {
            canvasHandler.ctx.scale( this.size, this.size );
            if ( this.flip > 0 ) {
                canvasHandler.ctx.translate( -this.image.data.cx, -this.image.data.cy );
                canvasHandler.ctx.drawImage( this.image, 0, 0, this.image.data.w, this.image.data.h );
            } else {
                canvasHandler.ctx.translate( -backfaceImage.data.cx, -backfaceImage.data.cy );
                canvasHandler.ctx.drawImage( backfaceImage, 0, 0, backfaceImage.data.w, backfaceImage.data.h );
            }
        }
    }
}