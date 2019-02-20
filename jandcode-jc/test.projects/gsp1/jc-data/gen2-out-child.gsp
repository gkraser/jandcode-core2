<%@ page import="jandcode.jc.GspScript" %>
<%
  GspScript th = this
  GspScript ch = th.create(th.scriptDir + "/child1.gsp")

  th.args['temp1'] = 'ttt'
%>
Owner : ${th.args}
${ch}
