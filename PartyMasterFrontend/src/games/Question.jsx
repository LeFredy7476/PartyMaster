import useAssets from "../useAssets";
import CanvasHandler from "./CanvasHandler";
import ErrorHandler from "./ErrorHandler";

const assets = useAssets();

class Button {
    constructor ( value, index, color, label, hovered ) {
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

    QUESTION_SPECIAL:
{ titre: "Question Speciale",

         confirm: false, 

        confirmText: "envoie"
     },
 
    REVELATIONQSBM: { 
        titre: "voila la bonne reponse",

         confirm: false,

          confirmText: "" }
};

export default class Question extends CanvasHandler {
    constructor(app, data) {
        super(app, data);
        this.choix = [
            new Button("Option 1", 0, [150, 0, 0], this.data.reponse1),

            new Button("Option 2", 1, [150, 0, 0], this.data.reponse2),

            new Button("Option 3", 2, [150, 0, 0], this.data.reponse3),

            new Button("Option 4", 3, [150, 0, 0], this.data.reponse4)
        ];
        
    }

    getType() {
        return "Question";
    }

   

    onclick(event) {
        if (this.data.state === "QUESTION") {
            for (let i = 0; i < this.choix.length; i++) {
                let choisis =this.games[i]
                if (choisis.hovered) {
                    console.log(this.games.value);
                    this.app.packAction("game:question:receiveResponse", { choice: button.index })
                        .then((response) => {
                            if (response.data.code != 0) {
                                ErrorHandler(response);
                            }
                        })
                        .catch(ErrorHandler);
                    break;
                }
            }
        } else if (this.data.state === "QUESTION_SPECIAL") {
            let answer = window.prompt("Entrez votre reponse:");
            this.app.packAction("game:question_special:receiveResponse", { answer: answer })
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
        this.ctx.clearRect(0, 0, this.width, this.height);
        this.octx.clearRect(0, 0, this.width, this.height);
        let state = statesNames[this.data.state] || statesNames.QUESTION;

        this.ctx.font = "48px Lexend";
        this.ctx.textAlign = "center";
        this.ctx.fillStyle = "#000000";
        this.ctx.fillText(state.titre, this.width / 2, 80);
        this.ctx.font = "36px Lexend";
        this.ctx.fillText(this.data.question || "Question exemple?", this.width / 2, 140);

        if (this.data.state === "QUESTION") {
            for (let button of this.games) {
                button.hovered = this.isHovered(button.color);
                this.drawTitles(
                    this.verticalPlace(button.index, this.games.length),
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

       


        
        if (sessionStorage.getItem("debug") === "true") {
            this.debugDraw();
        }
    }

    
}