export default function ShareButton() {
    return (
        <button className="micon" id="fullscreen" onClick={function() {
            //https://medium.com/@cdtech1628/next-js-referenceerror-navigator-not-defined-4e21cdcbbd4b#:~:text=To%20avoid%20this%20issue%2C%20you,non%2Dbrowser%20environment%20(Next.
            if (typeof window !== "undefined") {
                navigator.clipboard.writeText(window.location.href);
            }
        }}>link</button>
    )
}