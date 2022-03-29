<%@ page import="jandcode.jc.GspScript" %>
<%
  GspScript th = this
  println "args in body: ${th.args}"
  th.vars.func1 = { a ->
    out("A=${a}, args=${th.args}")
  }
%>
BAD-BAD

