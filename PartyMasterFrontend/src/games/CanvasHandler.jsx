
export default class CanvasHandler {
    
    constructor ( app, data ) {
        this.app = app;
        this.data = data;

        this.offscreenCanvas = new OffscreenCanvas( this.width, this.height );

        this.ctx = this.canvas.getContext( "2d" );
        this.octx = this.offscreenCanvas.getContext( "2d", { willReadFrequently: true } );

        this.lastTime;
        this.deltaTime;

        this.mouse = { x: 0, y: 0 };
        this.hover = [255, 255, 255];

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

    getType() {
        return "default"
    }
    
    update ( app, data ) {
        this.app = app;
        this.data = data;
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
    
//quand tu sors d'une certaine limite le carrer dans lequel le jeux est afficher va se retrecir 
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
        // console.log(this.getType());
        // console.log(this.app.data);

        if (true || this.getType() == this.app.data.gameData.type) {

            this.hover = this.getMousePointerHover();
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
    }

    stop () {
        window.cancelAnimationFrame( this.animationFrame );
    }
    
    init () {
        console.log(this);
        let self = this;
        window.cancelAnimationFrame( this.animationFrame );
        this.animationFrame = window.requestAnimationFrame( function ( t ) {
            self.loop( t );
        } );
    }

//la couleur est en rgb et la souris detecte la couleur du background invisible, il faut pour les boutons creer des carree invisible de couleur differente comme 
//sa si la couleur est cliquer et differente sa fait comme si un bouton avait ete cliquer
    getMousePointerHover() {
        if (this.canvas.matches("#mainCanvas:hover")) {
            let imageData = this.octx.getImageData( this.mouse.x, this.mouse.y, 1, 1 );
            let [ r, g, b ] = imageData.data;
            return [ r, g, b ];
        } else {
            return [ 255, 255, 255 ];
        }
    }

    toRGB ( r, g = null, b = null ) {
        if ( b == null ) {
            return `rgb( ${ r[0] }, ${ r[1] }, ${ r[2] } )`;
        } else {
            return `rgb( ${ r }, ${ g }, ${ b } )`;
        }
    }

    compareColor ( c1, c2 ) {
        return c1[0] == c2[0] && c1[1] == c2[1] && c1[2] == c2[2]
    }

    isHovered ( color ) {
        return this.compareColor( color, this.hover );
    }
}