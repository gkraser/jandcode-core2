<%@ page import="jandcode.core.dbm.domain.*; jandcode.core.dbm.dbstruct.*; " %>
<%

  // генерация primary key универсальная

  def ut = new DomainDbUtils(this.model)

  for (def d : ut.domains) {
    if (d.findField("id") == null) continue; // нет id
    def gname = ut.makeShortIdn("pk_${d.dbTableName}")
%>
alter table ${d.dbTableName} add constraint ${gname} primary key (id)
~~
<%
  }
%>