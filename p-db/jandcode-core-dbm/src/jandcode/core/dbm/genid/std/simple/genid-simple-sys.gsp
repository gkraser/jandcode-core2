<%@ page import="jandcode.core.dbm.dbstruct.*; " %>
<%
  def ut = new DomainDbUtils(this.model)
  def sqlLong = ut.getFieldByType("long", 0).sqlType
%>
create table genid_data (
  name    varchar(63) not null,
  val     ${sqlLong},
  startId ${sqlLong},
  stepId  ${sqlLong},
  primary key (name)
)
~~

