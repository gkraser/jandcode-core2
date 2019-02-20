<%@ page import="jandcode.jc.GspScript" %>
<%
  GspScript th = this
%>
<%
  for (i in 1..5) {
    th.changeFile("t1/t2/f-${i}.txt")
    out(i)
    th.changeFile("f-${i}.txt")
    out(i)
%>

file: ${th.currentFile}
<% th.writer.indentInc() %>
dir:  ${th.outDir}
<% th.writer.indentDec() %>
1
<%
  }
%>