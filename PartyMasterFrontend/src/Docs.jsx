import React from 'react'
import { useNavigate, useParams } from "react-router-dom";

function DocLoupgaroux() {
    return (
        <>
            <h1>Aide pour le loup garou</h1>
            <h2>Règle du jeu</h2>
            <h3>Le concept du jeu du loup-garou est que, lorsque la nuit est tombée, les personnages possédant des capacités uniques, les utiliseront ou pour portée aide au village ou pour leur propre intérêt. À la suite de cela, les loups-garous se réveillent, et décident à l'unisson de manger un des villageois . Au matin, grâce au pouvoir de nos villageois, ils doivent dénicher les loups-garous et les pendre. </h3>
            <h2>Role </h2>
            <div className='grid'>
                <div>
                    <img width="100" height="150" src="/loupgarou/villageois.svg" alt="" />
                    <h3>Villageois</h3>
                    <p>simple gars qui doit deviner qui est les loups ,les tués , se faire dévorer par les loups ou traire des vaches</p>
                </div>
                <div> <img width="100" height="150" src="/loupgarou/loup.svg" alt="" />
                <h3>loup garoux</h3>
                <p>rester discret , dévorer des villageois sans vous faire prendre ou sinon la corde vous attends </p>
                </div>
               <div> <img width="100" height="150" src="/loupgarou/voyante.svg" alt="" />
               <h3>Voyante</h3>
                <p>issue d'une famille de magicien roux, a la tombée de la nuit elle peut savoir le role d'un autre joueur lors de la partie</p>
               </div>
               
                <div><img width="100" height="150" src="/loupgarou/traitre.svg" alt="" />
                <h3>Traitre</h3>
                <p>ayant pour idole le comte de talleyrand et judas, lors de la nuit, il prend la carte d'une personne au choix sans en prendre connaissance et l'envoie a un joueur aléatoire dans la partie</p>
                </div>
                
                <div>
                    <img width="100" height="150" src="/loupgarou/cupidon.svg" alt="" />
                    <h3>Cupidon</h3>
                    <p>N'ayant jamais trouvé l'amour grace a son charme, cupidon a pour capacité de rendre deux personne amoureuse lors de la première journée ayant pour effet que si l'un des tourteraux meurt, son amant le suis dans le trépas</p>
                </div>
                <div>
                    <img width="100" height="150" src="/loupgarou/guardien.svg" alt="" />
                    <h3>Gardien</h3>
                    <p>Une des rares personne avec un peu de bonne intention dans ce village, le gardien a pour capacité de protéger quelqu'un des loups garoux lors de la nuit,attention il ne peut pas proteger une personne deux fois de suite</p>
                </div>
                
                <div>
                    <img width="100" height="150" src="/loupgarou/chasseur.svg" alt="" />
                    <h3>Chasseur</h3>
                    <p>Un homme extremement rancunier et doté d'un fusil,le chasseur lorsque qu'il est pendu ou dévoré, choisis une personne qui mourreras avec lui </p>
                </div>
                <div>
                   <img width="100" height="150" src="/loupgarou/loupblanc.svg" alt="" /> 
                   <h3>Loup blanc</h3>
                    <p>Agent double détenant une belle fourrure blanche, le loup blanc a pour mission de tué le village puis les loups afin de gagner en étant seul</p>
                </div>
                

            </div>
        </>
    )
}
function DocQuestion(){
    return(
    <>
        <h1>Aide pour le questionnaire</h1>
        <h2>Le jeux consiste a ce que chaque personne repond a une question de culture generale sur les sujets suivant: question de culture generale,
       question sur la geographie et question sur la programmation. A la fin de chaque tour une question bonus ayant plusieurs de diffuclte pouvant aller jusqu'a trois seras poser a chaque personne presente.
        Il peut choisir de prendre la question ou de l'ignorer, dans le cas ou la personne repond bien a la question, il recevras un nombre de point correspondant au numero de diffuclte de cette derniere,
        par contre, si il a le malheur de ne pas bien repondre il perdrat ce nombre de point associer a la question.</h2>
    </>
    )
}
function DocUno(){
    return(
        <>
            <h1>Aide pour le uno</h1>
            <h2>Placer des cartes avec la bonne couleur ou le bon chiffre pour poser une carte ou une carte avec differents effets. Le premiere
            a ne plus avoir de carte remporte la victoire.
            </h2>
        </>
    )
}

export default function Docs() {
    const { game } = useParams();
    console.log(game)

    var doc = <></>;
    if (game == "loupgaroux") {
        doc = <DocLoupgaroux />
    if(game =="question"){
        doc = <DocQuestion/>
    }
    if(game=="uno"){
        doc = <DocUno/>
    }

    }


    return (
        <main className='docs'>
            {doc}
        </main>
    )

}
