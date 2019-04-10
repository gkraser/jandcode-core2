<%@ page import="jandcode.jc.GspScript" %>
<%
  GspScript th = this

  th.vars.func1 = { a ->
    out("A=${a}")
  }
%>
BAD-BAD

