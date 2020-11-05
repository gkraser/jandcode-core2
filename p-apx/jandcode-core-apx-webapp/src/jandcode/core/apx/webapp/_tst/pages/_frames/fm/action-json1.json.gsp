<%@ page import="jandcode.commons.*; jandcode.core.*; jandcode.core.web.*; jandcode.core.web.gsp.*;" %>
<%
  BaseGsp th = this

  int cnt = th.request.params.getInt("cnt", 5)

  def a = []
  for (i in 1..cnt) {
    a.add([id:i, text: "text-${i}"])
  }
  out(UtJson.toJson(a))
%>
