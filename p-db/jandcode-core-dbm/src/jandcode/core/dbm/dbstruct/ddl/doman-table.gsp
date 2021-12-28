<%@ page import="jandcode.core.dbm.dbstruct.*; " %>
<%

  // генерация create table универсальная

  def ut = new DomainDbUtils(this.model)

  for (def d : ut.dbTables) {
%>
--@${d.dbTableName}
create table ${d.dbTableName} (
<%
    for (def f : d.fields) {
      def fd = f.bean(FieldDb)
      String notNull = ""
      if ("id" == f.name) {
        notNull = " not null"
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