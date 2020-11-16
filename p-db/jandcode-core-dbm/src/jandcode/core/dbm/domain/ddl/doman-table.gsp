<%@ page import="jandcode.core.dbm.domain.db.DomainDbUtils; jandcode.core.dbm.domain.db.FieldDb; jandcode.core.dbm.domain.db.DomainDbUtils; jandcode.core.dbm.domain.db.FieldDb; jandcode.core.dbm.domain.db.*" %>
<%

  // генерация create table универсальная

  def ut = new jandcode.core.dbm.domain.db.DomainDbUtils(th)

  for (def d : ut.getDomainsMy()) {
%>
--@${d.dbTableName}
create table ${d.dbTableName} (
<%
    for (def f : d.fields) {
      def fd = f.bean(jandcode.core.dbm.domain.db.FieldDb);
      String notNull = "";
      if ("id".equals(f.getName())) {
        notNull = " not null";
      }
      def zpt = f == d.fields.last() ? '' : ','
      out("  ${f.name} ${fd.sqlType}${notNull}${zpt}\n")
    }
%>
)
~~
<%
  }
%>