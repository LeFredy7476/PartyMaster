
function url(_url) {
    return `${_url}`.replaceAll(" ", "%20")
}

const players = [
    {src: url("/players/icon-0.svg"), w: 64, h: 64, cx: 32, cy: 32, r: 32, sw: 4},
    {src: url("/players/icon-1.svg"), w: 64, h: 64, cx: 32, cy: 32, r: 32, sw: 4},
    {src: url("/players/icon-2.svg"), w: 64, h: 64, cx: 32, cy: 32, r: 32, sw: 4},
    {src: url("/players/icon-3.svg"), w: 64, h: 64, cx: 32, cy: 32, r: 32, sw: 4},
    {src: url("/players/icon-4.svg"), w: 64, h: 64, cx: 32, cy: 32, r: 32, sw: 4},
    {src: url("/players/icon-5.svg"), w: 64, h: 64, cx: 32, cy: 32, r: 32, sw: 4},
    {src: url("/players/icon-6.svg"), w: 64, h: 64, cx: 32, cy: 32, r: 32, sw: 4},
    {src: url("/players/icon-7.svg"), w: 64, h: 64, cx: 32, cy: 32, r: 32, sw: 4},
    {src: url("/players/icon-8.svg"), w: 64, h: 64, cx: 32, cy: 32, r: 32, sw: 4},
]

const unocards = {
    "backface": {src: url("/uno card/backface.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
    "color": {src: url("/uno card/color.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
    "p4": {src: url("/uno card/p4.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
    "red": [
        {src: url("/uno card/red 0.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/red 1.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/red 2.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/red 3.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/red 4.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/red 5.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/red 6.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/red 7.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/red 8.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/red 9.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/red pass.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/red flip.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/red p2.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
    ],
    "yellow": [
        {src: url("/uno card/yellow 0.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/yellow 1.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/yellow 2.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/yellow 3.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/yellow 4.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/yellow 5.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/yellow 6.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/yellow 7.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/yellow 8.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/yellow 9.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/yellow pass.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/yellow flip.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/yellow p2.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
    ],
    "green": [
        {src: url("/uno card/green 0.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/green 1.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/green 2.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/green 3.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/green 4.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/green 5.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/green 6.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/green 7.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/green 8.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/green 9.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/green pass.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/green flip.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/green p2.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
    ],
    "blue": [
        {src: url("/uno card/blue 0.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/blue 1.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/blue 2.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/blue 3.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/blue 4.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/blue 5.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/blue 6.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/blue 7.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/blue 8.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/blue 9.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/blue pass.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/blue flip.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
        {src: url("/uno card/blue p2.svg"), w: 100, h: 150, cx: 50, cy: 75, r: 50, sw: 6},
    ]
}

const loupgaroux = {
    "villageois": {src: url("/loupgarou/villageois.svg"), w: 100, h: 100, cx: 50, cy: 50, r: 32, sw: 2},
    "loup": {src: url("/loupgarou/loup.svg"), w: 100, h: 100, cx: 50, cy: 50, r: 32, sw: 2},
    "loupblanc": {src: url("/loupgarou/loupblanc.svg"), w: 100, h: 100, cx: 50, cy: 50, r: 32, sw: 2},
    "cupidon": {src: url("/loupgarou/cupidon.svg"), w: 100, h: 100, cx: 50, cy: 50, r: 32, sw: 2},
    "voyante": {src: url("/loupgarou/voyante.svg"), w: 100, h: 100, cx: 50, cy: 50, r: 32, sw: 2},
    "guardien": {src: url("/loupgarou/guardien.svg"), w: 100, h: 100, cx: 50, cy: 50, r: 32, sw: 2},
    "traitre": {src: url("/loupgarou/traitre.svg"), w: 100, h: 100, cx: 50, cy: 50, r: 32, sw: 2},
    "chasseur": {src: url("/loupgarou/chasseur.svg"), w: 100, h: 100, cx: 50, cy: 50, r: 32, sw: 2},
}

const unospinner = {src: url("/uno_spinner.svg"), w: 400, h: 400, cx: 200, cy: 200, r: 160, sw: 4};

export default function useAssets() {
    return {
        loupgaroux: loupgaroux,
        unocards: unocards,
        players: players,
        unospinner: unospinner,
    }
}