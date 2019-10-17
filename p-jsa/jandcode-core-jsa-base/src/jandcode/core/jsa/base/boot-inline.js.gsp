<%@ page import="jandcode.core.jsa.jsmodule.JsModuleService; jandcode.core.web.gsp.*; jandcode.core.jsa.jsmodule.*; jandcode.commons.*; jandcode.core.*; jandcode.core.web.*; jandcode.core.web.gsp.*;" %>
<%
  BaseGsp th = this
  def svc = th.app.bean(jandcode.core.jsa.jsmodule.JsModuleService)
  def env = svc.getModule("jandcode/core/jsa/base/js/boot/env.js")
%>
${env.text}

