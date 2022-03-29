<%@ page import="jandcode.core.dbm.domain.*; jandcode.core.dbm.dbstruct.*; jandcode.core.dbm.*; jandcode.jc.*" %>
<%
  GspScript th = this

  DomainDbUtils dbUtils = this.args.dbUtils

  /**
   * Вывод title, если он есть. Иначе выводится имя с указанием, что title не указан
   */
  th.vars.out_title = { a ->
    if (a.title) {
      out("<span>${a.title}</span>")
    } else {
      out("<span>${a.name} (заголовок не указан)</span>")
    }
  }

  /**
   * Вывод коментария, если он есть.
   * Коментарий берется из conf.
   */
  th.vars.out_comment = { a ->
    String rem = a.conf['comment']
    if (!rem) {
      return
    }
    out("""<div class="comment">${rem}</div>""")
  }

  /**
   * Возвращает строку с типом поля
   */
  th.vars.get_field_type = { Field f ->
    def res = [
        text   : "?",       // текст типа
        refInfo: null       // информация о ссылке, если не null
    ]
    // это ссылка?
    def refInfo = dbUtils.domainGroup.getRefInfo(f, true)
    if (refInfo != null) {
      def s = refInfo.ref
      if (refInfo.refDomain) {
        s = refInfo.refDomain.dbTableName
      }
      res['text'] = "ref[" + s + "]"
      res['refInfo'] = refInfo
    } else {
      res['text'] = f.dbDataType.name

      if (f.size > 0) {
        res['text'] = res['text'] + "[" + f.size + "]"
      }
    }
    return res
  }

  /**
   * Вывод типа поля. Если это ссылка на домен, выводится ссылка на таблицу
   */
  th.vars.out_field_type = { Field f ->
    def ft = th.vars.get_field_type(f)
    def s = ft.text
    if (ft.refInfo) {
      // это ссылка
      if (ft.refInfo.refDomain != null) {
        s = """<a href="#${ft.refInfo.refDomain.dbTableName}">${s}</a>"""
      } else {
        s = """<span class="bad-link">${s}</span>"""
      }
    }
    out(s)
  }
%>