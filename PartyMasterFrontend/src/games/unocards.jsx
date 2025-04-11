function url(_url) {
    return `${_url}`.replaceAll(" ", "%20")
}

const _unocards = {
    "backface": url("/uno card/backface.svg"),
    "color": {
        "color": url("/uno card/color.svg"),
        "p4": url("/uno card/p4.svg")
    },
    "red": {
        "0": url("/uno card/red 0.svg"),
        "1": url("/uno card/red 1.svg"),
        "2": url("/uno card/red 2.svg"),
        "3": url("/uno card/red 3.svg"),
        "4": url("/uno card/red 4.svg"),
        "5": url("/uno card/red 5.svg"),
        "6": url("/uno card/red 6.svg"),
        "7": url("/uno card/red 7.svg"),
        "8": url("/uno card/red 8.svg"),
        "9": url("/uno card/red 9.svg"),
        "pass": url("/uno card/red pass.svg"),
        "flip": url("/uno card/red flip.svg"),
        "p2": url("/uno card/red p2.svg")
    },
    "yellow": {
        "0": url("/uno card/yellow 0.svg"),
        "1": url("/uno card/yellow 1.svg"),
        "2": url("/uno card/yellow 2.svg"),
        "3": url("/uno card/yellow 3.svg"),
        "4": url("/uno card/yellow 4.svg"),
        "5": url("/uno card/yellow 5.svg"),
        "6": url("/uno card/yellow 6.svg"),
        "7": url("/uno card/yellow 7.svg"),
        "8": url("/uno card/yellow 8.svg"),
        "9": url("/uno card/yellow 9.svg"),
        "pass": url("/uno card/yellow pass.svg"),
        "flip": url("/uno card/yellow flip.svg"),
        "p2": url("/uno card/yellow p2.svg")
    },
    "green": {
        "0": url("/uno card/green 0.svg"),
        "1": url("/uno card/green 1.svg"),
        "2": url("/uno card/green 2.svg"),
        "3": url("/uno card/green 3.svg"),
        "4": url("/uno card/green 4.svg"),
        "5": url("/uno card/green 5.svg"),
        "6": url("/uno card/green 6.svg"),
        "7": url("/uno card/green 7.svg"),
        "8": url("/uno card/green 8.svg"),
        "9": url("/uno card/green 9.svg"),
        "pass": url("/uno card/green pass.svg"),
        "flip": url("/uno card/green flip.svg"),
        "p2": url("/uno card/green p2.svg")
    },
    "blue": {
        "0": url("/uno card/blue 0.svg"),
        "1": url("/uno card/blue 1.svg"),
        "2": url("/uno card/blue 2.svg"),
        "3": url("/uno card/blue 3.svg"),
        "4": url("/uno card/blue 4.svg"),
        "5": url("/uno card/blue 5.svg"),
        "6": url("/uno card/blue 6.svg"),
        "7": url("/uno card/blue 7.svg"),
        "8": url("/uno card/blue 8.svg"),
        "9": url("/uno card/blue 9.svg"),
        "pass": url("/uno card/blue pass.svg"),
        "flip": url("/uno card/blue flip.svg"),
        "p2": url("/uno card/blue p2.svg")
    }
}

export default function unocards() {
    return _unocards;
}