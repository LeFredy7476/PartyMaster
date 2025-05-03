import { useEffect, useState } from "react";


export default function FullscreenButton() {

    const [fullscreen, setfullscreen] = useState(false);

    function toggleFullScreen() {
        if (!document.fullscreenElement) {
            document.documentElement.requestFullscreen();
            setfullscreen(true);
        } else if (document.exitFullscreen) {
            document.exitFullscreen();
            setfullscreen(false);
        }
    }

    useEffect(() => {
        if (document.fullscreenElement) {
            setfullscreen(true);
        } else {
            setfullscreen(false);
        }
        document.onfullscreenchange = () => {
            if (document.fullscreenElement) {
                setfullscreen(true);
            } else {
                setfullscreen(false);
            }
        }
    }, [])

    return (
        <button className="micon" id="fullscreen" onClick={toggleFullScreen}>{fullscreen ? "fullscreen_exit" : "fullscreen"}</button>
    )
}