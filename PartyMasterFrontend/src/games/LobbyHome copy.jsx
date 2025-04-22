import unocards from "./unocards";
import useAssets from "../useAssets";

// https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/random
function getRandomInt(max) {
    return Math.floor(Math.random() * max);
}

const assets = useAssets();

const backfaceImage = new Image();
backfaceImage.data = assets.unocards["backface"]
backfaceImage.src = backfaceImage.data.src;



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


export default class LobbyHome extends CanvasHandler {

    constructor ( app, data ) {
        super( app, data );
        this.card1 = new Card( 200, 200, 1, 0, 1, assets.unocards["red"]["pass"], [ 255, 0, 0 ] );
    }
    
    onclick ( event ) {
        // create this "flip" animation
        this.card1.targetFlip = -this.card1.targetFlip;
        this.card1.trueSize = this.card1.targetSize - 0.2;
    }

    onmousemove ( event ) {
        // make card follow mouse pointer
        this.card1.x = this.mouse.x;
        this.card1.y = this.mouse.y;
    }


    loop ( time ) {
        super.loop( time );

        // update card exponential animation
        this.card1.update( this.deltaTime );

        // draw the card
        // this.card1.transform( this.card1.draw, this );
        // another way to do it :
        this.card1.transform( ( self ) => {
            self.card1.draw( self );
        }, this );

        // detect if mouse pointer is hovering something
        let imageData = this.octx.getImageData( this.mouse.x, this.mouse.y, 1, 1 );
        let [ r, g, b ] = imageData.data;
        // console.log(this.mouse);
        if ( r == 255 && g == 0 && b == 0 ) {
            this.card1.size = 1.5;
        } else {
            this.card1.size = 1;
        }

        // show debug information
        this.ctx.font = "50px serif";
        this.ctx.fillStyle = "#880088";
        this.ctx.fillText( Math.round( 10000 / this.deltaTime ) / 10 + " fps", 50, 80 );
        this.ctx.fillText( r + ", " + g + ", " + b, 50, 160 );
        this.ctx.fillText( this.mouse.x + ", " + this.mouse.y, 50, 240 );
        
        if ( localStorage.getItem( "debug" ) == "true" ) this.debugDraw();
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