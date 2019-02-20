<%@ page import="jandcode.jc.GspScript" %>
<%
  GspScript th = this

  th.ant.copy(todir: th.outDir) {
    fileset(dir: th.scriptDir) {
      include(name: '**/*.js')
      include(name: '**/*.css')
      include(name: '**/*.jpg')
    }
  }
%>
start
end
