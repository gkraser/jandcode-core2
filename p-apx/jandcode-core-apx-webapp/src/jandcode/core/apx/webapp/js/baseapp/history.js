/* history api для фреймов только
----------------------------------------------------------------------------- */

export class FrameHistory {

    /**
     * Обновить hash
     * @param s {String} hash
     */
    updateHash(s) {
        if (s == null || s === '') {
            // пусто, убираем hash
            const url = new URL(window.location);
            url.hash = '';
            window.history.replaceState(null, document.title, url.toString());
            return
        }
        //
        if (!s.startsWith('#')) {
            s = '#' + s
        }
        window.history.replaceState(null, document.title, s);
    }

}