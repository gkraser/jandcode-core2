/*

Глобальное пространство имен Jc

----------------------------------------------------------------------------- */

import cfg from './cfg'

// глобальный экземпляр пространства имен
let globalNs = window.Jc = window.Jc || {}

// настраиваем
globalNs.cfg = cfg;

export default globalNs;
