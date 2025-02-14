import icon0 from '/vite.svg';
import icon1 from './assets/react.svg'

const icons = [
    icon0,
    icon1
];

export default function get_player_icon(icon_id) {
    return icons[icon_id];
}