<%@ page import="jandcode.web.std.action.*; jandcode.commons.*; jandcode.web.gsp.*; jandcode.web.*;" %>
<%
  BaseGsp th = this
  ShowerrorInfo info = th.args.info
  out(WebConsts.ERROR_AJAX_PREFIX);
  out(info.text);
%>
