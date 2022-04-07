<%@ page import="jandcode.core.dbm.dbstruct.*; " %>
<%

  // генерация foreign key универсальная

  def ut = new DomainDbUtils(this.model)

  for (def d : ut.domains) {
    for (def f : d.fields) {
      def refInfo = ut.domainGroup.getRefInfo(f)
      if (refInfo == null) continue
      def gname = ut.makeShortIdn("fk_${d.dbTableName}_${f.name}")
      def refCascade = f.refCascade ? " on delete cascade" : ""
%>
alter table ${d.name} add constraint ${gname}
foreign key(${f.name}) references ${refInfo.refDomain.dbTableName}(id)${refCascade}
~~
<%
    }
  }
%>