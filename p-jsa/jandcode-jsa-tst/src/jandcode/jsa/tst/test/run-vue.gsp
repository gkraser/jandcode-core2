<%@ page import="jandcode.jsa.tst.test.utils.*; jandcode.web.gsp.*" %>
<jc:page template="./template-run.gsp">
  <%
    BaseGsp th = this
    def runCtx = th.inst(TstRunContext)

    String m_run = "jandcode/jsa/tst/test/runVue.js"
  %>
  <jc:pagePart name="body">
    <jc:linkModule path="${m_run}"/>
    <script>
        require('${m_run}').runModule('${runCtx.main}')
    </script>
  </jc:pagePart>
</jc:page>
