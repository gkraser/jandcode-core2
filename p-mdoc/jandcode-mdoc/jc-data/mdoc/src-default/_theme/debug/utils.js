/*
    Утилиты для отладки mdoc-документов
----------------------------------------------------------------------------- */

/**
 * Выполнить команду на сервере mdoc
 * @param cm команда
 * @param params параметры
 */
mdoc.execCm = function(cm, params, cb, cbError) {
    $.ajax({
        url: mdoc.baseUrl + 'mdoc/' + cm,
        data: params,
        error: function(x) {
            let s = x.responseText;
            if (s) {
                s = s.replace('ERROR_AJAX:', '')
            }
            if (cbError) {
                cbError(s)
            } else {
                mdoc.showError(s)
            }
        },
        success: function(data) {
            if (cb) {
                cb(data)
            }
        }
    })
}

mdoc.showError = function(s) {
    let msg = 'Error! ' + s
    console.error(msg);
    alert(msg);
}

mdoc.checkChangedEnable = true
mdoc.checkChangedErrors = 0
mdoc.checkChanged = function(outFile, time) {
    if (!mdoc.checkChangedEnable) {
        return;
    }
    mdoc.execCm('cmHasChangedSinceTime', {outFile: outFile, time: time}, function(res) {
        if (res == 'true') {
            location.reload()
        }
    }, function() {
        // ignore
        mdoc.checkChangedErrors++
        if (mdoc.checkChangedErrors > 3) {
            mdoc.checkChangedEnable = false;
        }
    })
}

mdoc.edit = function(sourceFile, lineNumber, sourcePos) {
    mdoc.execCm('cmEdit', {
        sourceFile: sourceFile, lineNumber: lineNumber, sourcePos: sourcePos
    });
}

mdoc.start_checkChanged = function(outFile, time, interval) {
    if (!interval) {
        interval = 1000;
    }
    setInterval(function() {
        mdoc.checkChanged(outFile, time)
    }, interval)
}

mdoc.rebuild = function() {
    mdoc.checkChangedEnable = false;
    mdoc.execCm('cmRebuild', {}, function() {
        location.reload()
    }, function(x) {
        mdoc.showError(x);
        location.reload()
    });
}

////// быстрое редактирование

mdoc._editmark_sourcePos = null;

document.addEventListener("click", function(ev) {
    let docBodyEl = ev.target.closest(".topic-body");
    let editMarkerEl = document.getElementById("edit-marker");
    if (docBodyEl) {
        document.body.appendChild(editMarkerEl);  // должен быть в body, пока так...

        let bodyRect = docBodyEl.getBoundingClientRect();
        let bodyMarkerRect = editMarkerEl.getBoundingClientRect();

        editMarkerEl.style.top = "" + (ev.pageY - bodyMarkerRect.height / 2) + "px"
        editMarkerEl.style.left = "" + (bodyRect.right + 20) + "px"
        editMarkerEl.style.display = "block";

        mdoc._editmark_sourcePos = mdoc.findSourcePos(ev.target)
        if (mdoc._editmark_sourcePos == null) {
            mdoc._editmark_sourcePos = "0-0";
        }

    } else {
        mdoc._editmark_sourcePos = null;
        editMarkerEl.style.display = "none";
    }
})

document.addEventListener("dblclick", function(ev) {
    if (mdoc._editmark_sourcePos == null) {
        return;
    }
    mdoc.edit(mdoc.sourceFile, "0", mdoc._editmark_sourcePos)
})

document.addEventListener("keydown", function(ev) {
    if (ev.key === 'e' || ev.key === 'E') {
        if (mdoc._editmark_sourcePos == null) {
            return;
        }
        mdoc.edit(mdoc.sourceFile, "0", mdoc._editmark_sourcePos)
    }
    if (ev.key === 'r' || ev.key === 'R') {
        mdoc.rebuild()
    }
})

mdoc.findSourcePos = function(from) {
    if (!from) {
        return null;
    }
    let el = from.closest("[source-pos]");
    if (el) {
        return el.getAttribute('source-pos')
    }
    let cur = from.previousSibling;
    while (cur) {
        if (cur.nodeType === 1) {
            let el = cur.closest("[source-pos]");
            if (el) {
                return el.getAttribute('source-pos')
            }
        }
        cur = cur.previousSibling;
    }
    return mdoc.findSourcePos(from.parentElement)
}