import LobbyHome from "./LobbyHome";
import Loup from "./Loup";
import Uno from "./Uno";
import Question from "./Question";

export default function useGames() {
    return {
        LobbyHome: LobbyHome,
        Loup: Loup,
        Uno: Uno,
        Question: Question
    }
};