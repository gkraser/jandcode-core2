<%@ page import="jandcode.core.web.gsp.*; jandcode.jsa.jsmodule.*; jandcode.commons.*; jandcode.core.*; jandcode.core.web.*; jandcode.core.web.gsp.*;" %>
<%
  BaseGsp th = this
  def svc = th.app.bean(JsModuleService)
  def env = svc.getModule("jandcode/jsa/base/js/boot/env.js")
%>
${env.text}

