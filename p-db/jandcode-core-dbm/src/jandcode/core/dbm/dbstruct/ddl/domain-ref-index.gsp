<%@ page import="jandcode.core.dbm.dbstruct.*; " %>
<%

  // генерация индексов для foreign key универсальная

  def ut = new DomainDbUtils(this.model)

  for (def d : ut.domains) {
    for (def f : d.fields) {
      def refInfo = ut.domainGroup.getRefInfo(f)
      if (refInfo == null) continue
      if (!f.refIndex) continue
      def gname = ut.makeShortIdn("fki_${d.dbTableName}_${f.name}")
%>
create index ${gname} on ${d.dbTableName}(${f.name})
~~
<%
    }
  }
%>