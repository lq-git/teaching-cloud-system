export default class Popups {
    type = ''
    visible = false
    title = ''

    constructor(title = '', type = '', visible = false) {
        this.type = type;
        this.visible = visible;
        this.title = title;
    }

}