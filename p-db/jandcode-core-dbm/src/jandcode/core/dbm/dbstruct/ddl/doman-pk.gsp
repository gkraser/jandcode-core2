<%@ page import="jandcode.core.dbm.dbstruct.*; " %>
<%

  // генерация primary key универсальная

  def ut = new DomainDbUtils(this.model)

  for (def d : ut.dbTables) {
    if (d.findField("id") == null) continue; // нет id
    def gname = ut.makeShortIdn("pk_${d.dbTableName}")
%>
--@${gname}
alter table ${d.dbTableName} add constraint ${gname} primary key (id)
~~
<%
  }
%>