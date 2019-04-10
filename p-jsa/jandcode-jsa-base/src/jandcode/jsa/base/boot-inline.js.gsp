<%@ page import="jandcode.jsa.jsmodule.*; jandcode.commons.*; jandcode.core.*; jandcode.web.*; jandcode.web.gsp.*;" %>
<%
  BaseGsp th = this
  def svc = th.app.bean(JsModuleService)
  def env = svc.getModule("jandcode/jsa/base/js/boot/env.js")
%>
${env.text}

