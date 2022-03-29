<%@ page import="jandcode.core.dbm.domain.*; jandcode.core.dbm.dbstruct.*; jandcode.core.dbm.*; jandcode.jc.*" %>
<%
  GspScript th = this

  Model model = this.args.model
  def ut = new DomainDbUtils(model)

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
    if (f.hasRef()) {
      // это ссылка
      def domainRef = ut.getDbRefDomain(f)
      if (domainRef != null) {
        return "ref[" + domainRef.dbTableName + "]"
      }
    }
    def res = f.dbDataType.name
    if (f.size > 0) {
      res = res + "[" + f.size + "]"
    }
    return res
  }

  /**
   * Вывод типа поля. Если это ссылка на домен, выводится ссылка на таблицу
   */
  th.vars.out_field_type = { Field f ->
    String s = th.vars.get_field_type(f)
    if (f.hasRef()) {
      // это ссылка
      def domainRef = ut.getDbRefDomain(f)
      if (domainRef != null) {
        s = """<a href="#${domainRef.dbTableName}">${s}</a>"""
      }
    }
    out(s)
  }
%> ы