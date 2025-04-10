export default class LobbyHome {

    constructor ( app, data ) {
        this.app = app;
        this.data = data;
        this.ctx = this.canvas.getContext( "2d" );
        this.lastTime;
        this.deltaTime;
    }

    // get width () {
    //     return this.canvas.width;
    // }

    // get height () {
    //     return this.canvas.height;
    // }

    get canvas () {
        return document.querySelector( "#mainCanvas" );
    }

    get parentDiv () {
        return document.querySelector( "#game" );
    }
    
    clearCanvas () {
        this.ctx.clearRect( 0, 0, this.canvas.width, this.canvas.height )
    }

    resizeCanvas () {
        // https://stackoverflow.com/questions/4288253/html5-canvas-100-width-height-of-viewport
        this.canvas.width = this.parentDiv.clientWidth;
        this.canvas.height = this.parentDiv.clientHeight;

    }

    loop ( self, time ) {
        self.resizeCanvas();
        // console.log("loop");
        // console.log(self);
        if (self.lastTime === undefined) {
            self.lastTime = time - 10;
        }
        self.deltaTime = time - self.lastTime;

        self.clearCanvas();

        self.ctx.font = "50px serif";
        self.ctx.fillStyle = "#880088";
        self.ctx.fillText( Math.round( 10000 / self.deltaTime ) / 10 + " fps", 50, 90 );

        self.animationFrame = window.requestAnimationFrame( function ( t ) {
            self.loop( self, t );
        } );
        self.lastTime = time;
    }

    stop () {
        window.cancelAnimationFrame( this.animationFrame );
    }
    
    init () {
        this.resizeCanvas();
        let self = this;
        this.animationFrame = window.requestAnimationFrame( function ( t ) {
            self.loop( self, t );
        } );
    }
}


class ExpFollower {
    constructor ( x, y, flip, rot ) {
        this.src
        this.height = srcHeight;
        this.width = srcWidth;
        this.trueX = x;
        this.targetX = x;
        this.trueY = y;
        this.targetY = y;
        this.trueFlip = flip;
        this.targetFlip = flip;
        this.trueRotation = rot;
        this.targetRotation = rot;
        this.ratio = 0.005;
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

    get flipped() {
        return this.flip <= 0;
    }

    _expFollow(deltaTime, current, target) {
        let diff = target - current;
        let new_diff = diff * Math.pow( this.ratio, deltaTime / 1000 );
        return target - new_diff;
    }

    update ( deltaTime ) {
        this.trueX = this._expFollow(deltaTime, this.trueX, this.targetX);
        this.trueY = this._expFollow(deltaTime, this.trueY, this.targetY);
        this.trueFlip = this._expFollow(deltaTime, this.trueFlip, this.targetFtrueFlip);
        this.trueRotation = this._expFollow(deltaTime, this.trueRotation, this.targettrueRotation);
    }

    transform ( func, ...ctxs ) {
        let self = this;
        ctxs.forEach(ctx => {
            ctx.save();
            ctx.translate(self.trueX, self.trueY);
            ctx.rotate(self.trueRotation);
            ctx.scale(self.trueFlip);
        });
        func();
        ctxs.forEach(ctx => {
            ctx.restore()
        });
    }

    draw () {} // put all the drawing stuff here when implementing as a prototype
}