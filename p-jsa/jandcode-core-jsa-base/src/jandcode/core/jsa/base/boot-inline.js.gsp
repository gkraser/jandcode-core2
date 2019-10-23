<%@ page import="jandcode.core.web.gsp.*; jandcode.core.jsa.jsmodule.*; jandcode.commons.*; jandcode.core.*; jandcode.core.web.*;" %>
<%
  BaseGsp th = this
  def svc = th.app.bean(JsModuleService)
  def env = svc.getModule("jandcode/core/jsa/base/js/boot/env.js")
%>
${env.text}

