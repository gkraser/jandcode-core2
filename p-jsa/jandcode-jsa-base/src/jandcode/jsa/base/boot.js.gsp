<%@ page import="jandcode.jsa.jsmodule.*; jandcode.commons.*; jandcode.core.*; jandcode.web.*; jandcode.web.gsp.*;" %>
<%
  BaseGsp th = this
  def svc = th.app.bean(JsModuleService)
  def bh = svc.getModule("jandcode/jsa/base/js/boot/babelHelpers.js")
  def mc = svc.getModule("jandcode/jsa/base/js/boot/module-core.js")
%>
${bh.text}
${mc.text}


