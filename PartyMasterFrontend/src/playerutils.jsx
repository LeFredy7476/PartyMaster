import icon0 from '/players/icon-0.svg'
import icon1 from '/players/icon-1.svg'
import icon2 from '/players/icon-2.svg'
import icon3 from '/players/icon-3.svg'
import icon4 from '/players/icon-4.svg'
import icon5 from '/players/icon-5.svg'
import icon6 from '/players/icon-6.svg'
import icon7 from '/players/icon-7.svg'
import icon8 from '/players/icon-8.svg'

const icons = [
    icon0,
    icon1,
    icon2,
    icon3,
    icon4,
    icon5,
    icon6,
    icon7,
    icon8
];

export default function get_player_icon(icon_id) {
    return icons[icon_id];
}