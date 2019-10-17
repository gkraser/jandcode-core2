<%@ page import="jandcode.core.web.std.action.*; jandcode.commons.*; jandcode.core.web.gsp.*; jandcode.core.web.*;" %>
<%
  BaseGsp th = this
  ShowerrorInfo info = th.args.info
  out(WebConsts.ERROR_AJAX_PREFIX);
  out(info.text);
%>
