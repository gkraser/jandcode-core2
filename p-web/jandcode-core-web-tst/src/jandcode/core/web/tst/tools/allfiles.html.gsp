<%@ page import="jandcode.core.web.gsp.*; jandcode.core.web.virtfile.impl.virtfs.*; jandcode.core.*; jandcode.core.web.*; jandcode.commons.*" %>
<jc:page template="page/tst-main">
  <%
    BaseGsp th = this
    //
    def mask = th.request.params.getString("mask", "**/*")
    def ps = th.webService.createDirScanner(mask)
    def lst = ps.load()
  %>
  <style>
  .sub-row {
    font-size: 7pt;
    padding-left: 20px;
    padding-top: 8px;
    color: gray;
  }

  .form-params {
    padding-bottom: 20px;
  }
  </style>
  <!-- ===================================================================== -->
  <h2 class="first">All files</h2>
  <form class="form-params">
    <label>Mask:</label>
    <input name="mask" value="${th.escapeHtml(mask)}">
    <button type="submit">Показать</button>
  </form>
  <table class="tst-table">
    <tr>
      <th>№</th>
      <th>file</th>
      <th>private<br>file</th>
      <th>type</th>
      <th>content<br>type</th>
      <th>private<br>content</th>
    </tr>
    <%
      def num = 0
      for (f in lst) {
        num++
        def cft = th.webService.findFileType(f.contentFileType)
    %>
    <tr>
      <td>${num}</td>
      <td>
        <a href="${th.ref(f.path)}">${f.path}</a>

        <div class="sub-row">${f.realPath}</div>
      </td>
      <td>${f.isPrivate()}</td>
      <td>${f.fileType}</td>
      <td>${f.contentFileType}</td>
      <td>${cft.isPrivate()}</td>
    </tr>
    <%
      }
    %>
  </table>

</jc:page>