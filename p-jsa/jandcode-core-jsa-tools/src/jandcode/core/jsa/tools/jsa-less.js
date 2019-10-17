/*

    Поддержка less

 */

const jsaSupport = require('./jsa-support');
const path = require('path');

let tmpFileCounter = 0

/**
 * Поддержка import '~xxx'
 */
let JsaLessFileManager = function() {
}

let less = require('less')
let LessFileManager = less.FileManager

JsaLessFileManager.prototype = new LessFileManager();
JsaLessFileManager.prototype.loadFile = function(filename, currentDirectory, options, environment, callback) {

    //
    filename = jsaSupport.resolveAlias(filename)

    //
    if (filename.indexOf('*') !== -1) {
        let files = jsaSupport.expandPath(filename, currentDirectory, false)
        let fstr = ''
        for (let f of files) {
            fstr += '@import "' + f + '";\n';
        }
        let res = {}
        res.contents = fstr
        tmpFileCounter++
        res.filename = path.resolve(currentDirectory, "TMP-IMPORT--" + tmpFileCounter + ".less")
        callback(null, res)
        return
    }

    //
    return LessFileManager.prototype.loadFile.call(this, filename, currentDirectory, options, environment, callback)
}

////// jsa preprocessor

let JsaLessPreProcessor = function() {}
JsaLessPreProcessor.prototype = {
    process: function(src, extra) {
        let s = handleJcDirectives(src, extra)
        if (!s) {
            return src;
        }
        return s;
    }
}

////// plugin

function JsaLessPlugin(options) {
    this.options = options;
}

JsaLessPlugin.prototype = {
    install: function(less, pluginManager) {
        pluginManager.addFileManager(new JsaLessFileManager(this.options));
    }
}

module.exports = {
    JsaLessPlugin,
}

