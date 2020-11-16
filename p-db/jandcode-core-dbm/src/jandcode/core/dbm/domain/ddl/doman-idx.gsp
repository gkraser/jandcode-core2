<%@ page import="jandcode.core.dbm.domain.db.DomainDb; jandcode.core.dbm.domain.db.DomainDbUtils; jandcode.core.dbm.domain.db.DomainDbUtils; jandcode.core.dbm.domain.db.DomainDb; jandcode.core.dbm.domain.db.*" %>
<%

  // генерация indexes универсальная

  def ut = new jandcode.core.dbm.domain.db.DomainDbUtils(th)

  for (def d : ut.getDomainsMy()) {
    def dd = d.bean(jandcode.core.dbm.domain.db.DomainDb)
    for (def idx : dd.indexes) {
      def unique = idx.unique ? "unique " : ""
      def gname = ut.makeShortIdn("i_${d.dbTableName}_${idx.name}")
      def flds = idx.sqlFields
%>
--@${gname}
create ${unique}index ${gname} on ${d.dbTableName}(${flds})
~~
<%
    }
  }
%>