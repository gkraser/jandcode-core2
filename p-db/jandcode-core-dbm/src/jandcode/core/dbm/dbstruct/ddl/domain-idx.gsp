<%@ page import="jandcode.core.dbm.dbstruct.*; " %>
<%

  // генерация indexes универсальная

  def ut = new DomainDbUtils(this.model)

  for (def d : ut.domains) {
    def dd = d.bean(DomainDb)
    for (def idx : dd.indexes) {
      def unique = idx.unique ? "unique " : ""
      def gname = ut.makeShortIdn("i_${d.dbTableName}_${idx.name}")
      def flds = idx.sqlFields
%>
create ${unique}index ${gname} on ${d.dbTableName}(${flds})
~~
<%
    }
  }
%>