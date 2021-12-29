<%@ page import="jandcode.core.dbm.dbstruct.*; " %>
<%

  // генерация foreign key универсальная

  def ut = new DomainDbUtils(this.model)

  for (def d : ut.dbTables) {
    for (def f : d.fields) {
      def refDomain = ut.getDbRefDomain(f)
      if (refDomain == null) continue
      def gname = ut.makeShortIdn("fk_${d.dbTableName}_${f.name}")
      def refCascade = f.refCascade ? " on delete cascade" : ""
%>
--@${gname}
alter table ${d.name} add constraint ${gname}
foreign key(${f.name}) references ${refDomain.dbTableName}(id)${refCascade}
~~
<%
    }
  }
%>