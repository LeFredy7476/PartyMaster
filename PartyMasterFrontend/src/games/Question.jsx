import useAssets from "../useAssets";
import CanvasHandler from "./CanvasHandler";
import ErrorHandler from "./ErrorHandler";

const assets = useAssets();

class Button {
    constructor(value, index, color, label, hovered) {
        this.value = value;
        this.index = index;
        this.color = color;
        this.label = label;
        this.hovered = hovered
    }
}
const statesNames = {
    QUESTION: {
        titre: "Question",
        confirm: false,
        confirmText: "envoie"
    },
    REVELATIONBM: {
        titre: "voila la bonne reponse",
        confirm: false,
        confirmText: ""
    },
    QUESTION_SPECIAL: {
        titre: "Question Speciale",
        confirm: false,
        confirmText: "envoie"
    },
    REVELATIONQSBM: {
        titre: "voila la bonne reponse",
        confirm: false,
        confirmText: ""
    }
};

export default class Question extends CanvasHandler {
    constructor(app, data) {
        super(app, data);

        this.choix = [
            new Button("Option 1", 0, [0, 0, 0], this.data.currentQuestion.reponse1||this.data.reponse1),
            new Button("Option 2", 1, [10, 0, 0], this.data.currentQuestion.reponse2||this.data.reponse2),
            new Button("Option 3", 2, [20, 0, 0], this.data.currentQuestion.reponse3||this.data.reponse3),
            new Button("Option 4", 3, [30, 0, 0], this.data.currentQuestion.reponse4||this.data.reponse4),
          
        ];
    }

    getType() {
        return "Question";
    }



    onclick(event) {
        if (this.data.state === "QUESTION") {
            for (let i = 0; i < this.choix.length; i++) {
                let choisis = this.choix[i]
                if (choisis.hovered) {
                    console.log(choisis.value);
                    this.app.packAction("game:question:receiveResponse", { "target": choisis.label })
                        .then((response) => {
                            if (response.data.code != 0) {
                                ErrorHandler(response);
                            }
                        }).catch(ErrorHandler);
                    break;
                }
            }
        } else if (this.data.state === "QUESTION_SPECIAL") {
            let answer = window.prompt("Entrez votre reponse:");
            this.app.packAction("game:question_special:receiveResponse", { "answer": answer })
                .then((response) => {
                    if (response.data.code != 0) {
                        ErrorHandler(response);
                    }
                })
                .catch(ErrorHandler);
        }

    }

    onmousemove(event) {

    }

    loop(time) {
        
        super.loop(time);
        let state = statesNames[this.data.state];

        this.ctx.font = "48px Lexend";
        this.ctx.textAlign = "center";
        this.ctx.fillStyle = "#000000";
        this.ctx.fillText(state.titre, this.width / 2, 80);
        this.ctx.font = "36px Lexend";
        console.log(this.data.currentQuestion.question);
        this.ctx.fillText(this.data.currentQuestion.question, this.width / 2, 140);

        if (this.data.state === "QUESTION") {
            for (let button of this.choix) {
                // console.log(button);
                button.hovered = this.isHovered(button.color);
                this.drawReponses(
                    this.verticalPlace(button.index, this.choix.length),
                    button.hovered,
                    button.color,
                    button.label,
                    this.data.selected_choice === button.index
                );
            }
        } else if (this.data.state === "QUESTION_SPECIAL") {
            this.ctx.font = "36px Lexend";
            this.ctx.textAlign = "center";
            this.ctx.fillStyle = "#000000";
            this.ctx.fillText("Cliquez pour entrer votre reponse", this.width / 2, this.height / 2);
        }
        

        // show debug information
        if ( sessionStorage.getItem( "debug" ) == "true" ) this.debugDraw();
        this.ctx.textAlign = "left"
        this.ctx.font = "50px serif";
        this.ctx.fillStyle = this.toRGB(127, 127, 127);
        this.ctx.fillText( Math.round( 10000 / this.deltaTime ) / 10 + " fps", 50, 80 );
        this.ctx.fillText( this.hover[0] + ", " + this.hover[1] + ", " + this.hover[2], 50, 160 );
        this.ctx.fillText( this.mouse.x + ", " + this.mouse.y, 50, 240 );
    }

    verticalPlace (i, size) {
        let x = Math.round(this.width / 2);
        let y = Math.round((i * 2 + 1) * (this.height - 180) / (size * 2)) + 180;
        return [ x, y ];
    }

    drawReponses(pos, hovered, hitbox, label, selected) {
        let ctx = document.querySelector("canvas").getContext("2d");
        ctx.font = "600 40px Lexend";
        ctx.textBaseline = "middle";
        ctx.strokeStyle = "#000";
        ctx.textAlign = "center";
        ctx.lineJoin = "round";
        ctx.lineCap = "round";
        ctx.lineWidth = 20;
        let txt_dimension = ctx.measureText(label);
        let w = Math.round(txt_dimension.width);
        let hw = Math.round(w / 2);
        if (selected || hovered) ctx.strokeRect(pos[0] - hw - 40, pos[1] - 40, w + 80, 80);
        // draw box
        ctx.fillStyle = "#fff";
        ctx.strokeStyle = "#fff";
        if (selected) {
            ctx.fillStyle = "#e54545";
            ctx.strokeStyle = "#e54545";
        }
        ctx.lineWidth = 12;
        ctx.strokeRect(pos[0] - hw - 40, pos[1] - 40, w + 80, 80);
        ctx.fillRect(pos[0] - hw - 40, pos[1] - 40, w + 80, 80);

        ctx.fillStyle = "#000";
        ctx.fillText(label, pos[0], pos[1]);
        ctx.textBaseline = "alphabetic";

        this.octx.fillStyle = this.toRGB(hitbox);
        this.octx.fillRect(pos[0] - hw - 48, pos[1] - 48, w + 96, 96);
    }
}