<%@ page import="jandcode.core.web.gsp.*; jandcode.utils.*; jandcode.core.*; jandcode.core.web.*; jandcode.core.web.gsp.*;" %>
<%
  // настройка на среду исполнения
  BaseGsp th = this
%>
<script>
    Jc.cfg.baseUrl = '${th.ref('/')}';
    <% if (th.app.debug) { %>
    Jc.cfg.debug = true;
    <% } %>
</script>
