import JcDecor from './JcDecor'

/**
 * Базовый предок для декораторов фреймов.
 */
export default {

    extends: JcDecor,

    props: {
        bodyClass: {
            type: [Array, String, Object],
        },
        bodyStyle: {
            type: [Array, String, Object],
        }
    },

    computed: {}

}
