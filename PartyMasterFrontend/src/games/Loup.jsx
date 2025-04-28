import useAssets from "../useAssets";
import CanvasHandler from "./CanvasHandler";
import ErrorHandler from "./ErrorHandler";

// https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Math/random
function getRandomInt(max) {
    return Math.floor(Math.random() * max);
}

const assets = useAssets();

class Joueurs {
    constructor () {
        
    }


}

export default class Loup extends CanvasHandler {
    constructor ( app, data ) {
        super( app, data );

    }
}