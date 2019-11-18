import * as apex from 'jandcode.core.apex.jsa'
import * as jsaBase from 'jandcode.core.jsa.base'

export {utils} from 'jandcode.core.apex.jsa'

let dao = new apex.DaoService()

dao.register({
})

export {
    apex,
    jsaBase,
    dao,
}


