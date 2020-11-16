<%@ page import="jandcode.core.dbm.domain.db.DomainDbUtils; jandcode.core.dbm.domain.db.DomainDbUtils; jandcode.core.dbm.domain.db.*" %>
<%

  // генерация индексов для foreign key для oracle

  def ut = new jandcode.core.dbm.domain.db.DomainDbUtils(th)

  for (def d : ut.getDomainsMy()) {
    for (def f : d.fields) {
      def refDomain = ut.getDbRefDomain(f)
      if (refDomain == null) continue
      def gname = ut.makeShortIdn("fki_${d.dbTableName}_${f.name}")
%>
--@${gname}
create index ${gname} on ${d.dbTableName}(${f.name})
~~
<%
    }
  }
%>