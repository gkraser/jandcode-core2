<%@ page import="jandcode.core.dbm.genid.*;jandcode.core.dbm.genid.std.*;" %>
<%

  // генерация генераторов

  GenIdDriver drv = this.context
  def ut = new GenIdDbUtils(this.model)

  for (def g : ut.getGenIds(drv)) {
%>
insert into genid_data(name,val,startId,stepId)
       values('${g.name.toLowerCase()}', ${g.start}, ${g.start}, ${g.step})
~~
<%
  }
%>