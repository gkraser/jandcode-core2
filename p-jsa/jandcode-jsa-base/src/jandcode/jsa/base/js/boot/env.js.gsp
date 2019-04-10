<%@ page import="jandcode.utils.*; jandcode.core.*; jandcode.web.*; jandcode.web.gsp.*;" %>
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
