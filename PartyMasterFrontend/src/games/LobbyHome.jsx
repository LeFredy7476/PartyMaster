import unocards from "./unocards";

// https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/random
function getRandomInt(max) {
    return Math.floor(Math.random() * max);
}

const backfaceImage = new Image();
backfaceImage.src = unocards()["backface"];

export default class LobbyHome {

    constructor ( app, data ) {
        this.app = app;
        this.data = data;
        this.ctx = this.canvas.getContext( "2d" );
        this.offscreenCanvas = new OffscreenCanvas( this.width, this.height );
        this.octx = this.offscreenCanvas.getContext( "2d" );
        this.lastTime;
        this.deltaTime;
        this.card1 = new Card( 200, 200, 1, 0, 1, unocards()["red"]["8"], [ 255, 0, 0 ] );
        this.mouse = { x: 0, y: 0 }
        let self = this;
        this.canvas.onclick = function ( e ) {
            // self.card1.targetX = getRandomInt(self.canvas.width);
            // self.card1.targetY = getRandomInt(self.canvas.height);
            self.card1.targetFlip = -self.card1.targetFlip;
            self.card1.trueSize = self.card1.targetSize - 0.2;
        }
        this.canvas.onmousemove = function ( e ) {
            let rect = self.canvas.getBoundingClientRect();
            self.mouse = {
                x: e.clientX - rect.left,
                y: e.clientY - rect.top
            };
        }
    }

    debugDraw() {
        this.ctx.drawImage(this.offscreenCanvas, 0, 0);
    }

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
        this.canvas.width = this.parentDiv.clientWidth;
        this.canvas.height = this.parentDiv.clientHeight;
        this.offscreenCanvas.width = this.parentDiv.clientWidth;
        this.offscreenCanvas.height = this.parentDiv.clientHeight;
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

        

        this.clearCanvas();

        this.card1.update(this.deltaTime);
        this.card1.transform( () => {
            self.card1.draw( self.ctx, self.octx );
        }, this.ctx, this.octx );


        let imageData = this.octx.getImageData( this.mouse.x, this.mouse.y, 1, 1 );
        let [ r, g, b ] = imageData.data;
        // console.log(this.mouse);
        if ( r == 255 && g == 0 && b == 0 ) {
            this.card1.size = 1.5;
        } else {
            this.card1.size = 1;
        }


        this.card1.x = this.mouse.x;
        this.card1.y = this.mouse.y;

        this.ctx.font = "50px serif";
        this.ctx.fillStyle = "#880088";
        this.ctx.fillText( Math.round( 10000 / this.deltaTime ) / 10 + " fps", 50, 80 );
        this.ctx.fillText( r + ", " + g + ", " + b, 50, 160 );
        this.ctx.fillText( this.mouse.x + ", " + this.mouse.y, 50, 240 );

        if (localStorage.getItem("debug") == "true") this.debugDraw();

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
        this.resizeCanvas();
        this.animationFrame = window.requestAnimationFrame( function ( t ) {
            self.loop( t );
        } );
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

    transform ( func, ...ctxs ) {
        let self = this;
        ctxs.forEach( ctx => {
            ctx.save();
            ctx.translate( self.trueX, self.trueY );
            ctx.rotate( self.trueRotation );
            ctx.scale( self.trueFlip, 1 );
        } );
        func();
        ctxs.forEach( ctx => {
            ctx.restore()
        } );
    }

    draw () {} // put all the drawing stuff here when implementing as a prototype
}


class Card extends ExpFollower {

    constructor ( x, y, flip, rot, size, path, color = [ 0, 0, 0 ] ) {
        super( x, y, flip, rot, size );
        this.image = new Image();
        this.image.src = path;
        this.color = color;
    }

    draw ( ctx, octx ) {

        octx.scale( this.size, this.size );
        octx.translate( -50, -75 );
        octx.fillStyle = `rgb( ${ this.color[0] }, ${ this.color[1] }, ${ this.color[2] } )`;
        octx.fillRect( 0, 0, 100, 150 );

        if ( this.image.complete && backfaceImage.complete ) {
            ctx.scale( this.size, this.size );
            ctx.translate( -50, -75 );
            if ( this.flip > 0 ) {
                ctx.drawImage( this.image, 0, 0, 100, 150 );
            } else {
                ctx.drawImage( backfaceImage, 0, 0, 100, 150 );
            }
        }
    }
}