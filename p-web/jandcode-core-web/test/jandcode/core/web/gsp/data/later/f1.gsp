<%@ page import="jandcode.core.web.gsp.*" %>
hello
<%
  out({ Gsp g ->
    out("1")
  } as IGspLaterHandler)
  out({ Gsp g ->
    out("2")
  } as IGspLaterHandler)
%>end