<%@ page import="jandcode.core.jsa.jsmodule.JsModuleService; jandcode.core.web.gsp.*; jandcode.core.jsa.jsmodule.*; jandcode.commons.*; jandcode.core.*; jandcode.core.web.*; jandcode.core.web.gsp.*;" %>
<%
  BaseGsp th = this
  def svc = th.app.bean(jandcode.core.jsa.jsmodule.JsModuleService)
  def bh = svc.getModule("jandcode/core/jsa/base/js/boot/babelHelpers.js")
  def mc = svc.getModule("jandcode/core/jsa/base/js/boot/module-core.js")
%>
${bh.text}
${mc.text}


