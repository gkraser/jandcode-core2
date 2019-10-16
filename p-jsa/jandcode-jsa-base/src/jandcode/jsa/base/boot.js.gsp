<%@ page import="jandcode.core.web.gsp.*; jandcode.jsa.jsmodule.*; jandcode.commons.*; jandcode.core.*; jandcode.core.web.*; jandcode.core.web.gsp.*;" %>
<%
  BaseGsp th = this
  def svc = th.app.bean(JsModuleService)
  def bh = svc.getModule("jandcode/jsa/base/js/boot/babelHelpers.js")
  def mc = svc.getModule("jandcode/jsa/base/js/boot/module-core.js")
%>
${bh.text}
${mc.text}


