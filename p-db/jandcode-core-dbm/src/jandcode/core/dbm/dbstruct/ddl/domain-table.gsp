<%@ page import="jandcode.core.dbm.dbstruct.*; " %>
<%

  // генерация create table универсальная

  def ut = new DomainDbUtils(this.model)

  for (def d : ut.domains) {
%>
create table ${d.dbTableName} (
<%
    for (def f : d.fields) {
      String notNull = ""
      if ("id" == f.name || f.notNull) {
        notNull = " not null"
      }
      def zpt = f == d.fields.last() ? '' : ','
      out("  ${f.name} ${f.sqlType}${notNull}${zpt}\n")
    }
%>
)
~~
<%
  }
%>