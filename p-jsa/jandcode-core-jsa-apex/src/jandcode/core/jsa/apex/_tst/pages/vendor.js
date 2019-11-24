import * as apex from '../../index'
import * as jsaBase from 'jandcode.core.jsa.base'

export {utils} from '../../index'

let dao = new apex.DaoService()

dao.register({
})

export {
    apex,
    jsaBase,
    dao,
}


