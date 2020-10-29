/* Рыба-текст для тестирования
----------------------------------------------------------------------------- */

export default {
    data() {
        return {
            loremEn: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat',
            loremRu: "Повседневная практика показывает, что постоянный количественный рост и сфера нашей активности требует определения и уточнения новых принципов формирования материально-технической и кадровой базы. Но перспективное планирование предоставляет",
        }
    },
    computed: {
        loremLg() {
            return this.lorem(300);
        },
        loremMd() {
            return this.lorem(80);
        },
        loremSm() {
            return this.lorem(50);
        }
    },
    methods: {
        lorem(length) {
            let cnt = Math.floor(length / 2)
            return this.loremRu.substr(0, cnt) + ". " + this.loremEn.substring(0, cnt) + ".";
        }
    }
}
