<%@ page import="jandcode.jc.GspScript" %>
<%
  GspScript th = this
  GspScript ch = th.create(th.scriptDir + "/gen3-func-def1.gsp")
%>
start
args=${th.args}
<% ch.vars.func1(111) %>
end
