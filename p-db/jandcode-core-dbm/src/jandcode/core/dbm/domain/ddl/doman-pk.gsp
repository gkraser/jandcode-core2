<%@ page import="jandcode.core.dbm.domain.db.DomainDbUtils; jandcode.core.dbm.domain.db.DomainDbUtils; jandcode.core.dbm.domain.db.*" %>
<%

  // генерация primary key универсальная

  def ut = new jandcode.core.dbm.domain.db.DomainDbUtils(th)

  for (def d : ut.getDomainsMy()) {
    if (d.findField("id") == null) continue; // нет id
    def gname = ut.makeShortIdn("pk_${d.dbTableName}")
%>
--@${gname}
alter table ${d.dbTableName} add constraint ${gname} primary key (id)
~~
<%
  }
%>