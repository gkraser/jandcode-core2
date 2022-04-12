<%@ page import="jandcode.core.dbm.genid.*;jandcode.core.dbm.genid.std.*;" %>
<%

  // генерация генераторов

  GenIdDriver drv = this.context
  def ut = new GenIdDbUtils(this.model)

  for (def g : ut.getGenIds(drv)) {
%>
create sequence g_${g.name} start with ${g.start} increment by ${g.step}
~~
<%
  }
%>