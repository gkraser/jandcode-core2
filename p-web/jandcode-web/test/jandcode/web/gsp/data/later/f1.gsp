<%@ page import="jandcode.web.gsp.*" %>
hello
<%
  out({ Gsp g ->
    out("1")
  } as IGspLaterHandler)
  out({ Gsp g ->
    out("2")
  } as IGspLaterHandler)
%>end