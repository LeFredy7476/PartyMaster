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


class Card {
    constructor (name) {
        
    }
}