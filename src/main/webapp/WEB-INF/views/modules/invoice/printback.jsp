
<%@page import="com.thinkgem.jeesite.common.web.Servlets"%>
<%@page contentType="text/html;charset=UTF-8" isErrorPage="true"%>
<%@include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>打印</title>
<%@include file="/WEB-INF/views/include/head.jsp"%>
<script type="text/javascript">
	var idTmr = "";
	$(document)
			.ready(
					function() {

						var src = "http://" + window.location.host
								+ "/dlsite/exltemple/${filename}";
						var xlsApp = null;
						try {
							xlsApp = new ActiveXObject('Excel.Application');
						} catch (e) {
							alert(e
									+ ', 原因分析: 浏览器安全级别较高导致不能创建Excel对象或者客户端没有安装Excel软件');
							return;
						}
						var xlBook = xlsApp.Workbooks.Open(src);
						var xlsheet = xlBook.Worksheets(1);
						xlsApp.Application.Visible = false;
						xlsApp.visible = false;
						xlsheet.PageSetup.PaperSize=37;
						//xlsheet.PrintPreview()
						xlsheet.Printout;

						xlsApp.Quit();
						xlsApp = null;
						idTmr = window.setInterval("Cleanup();", 1000);

					})
	function Cleanup() {
		window.clearInterval(idTmr);
		CollectGarbage();
	}
</script>
</head>
<body>
	<div class="container-fluid">
		<div class="page-header">
			<h1>打印完成.....</h1>
		</div>
		<div>
			<a href="javascript:" onclick="history.go(-1);" class="btn">返回上一页</a>
		</div>
		<script>
			try {
				top.$.jBox.closeTip();
			} catch (e) {
			}
		</script>
	</div>
</body>
</html>
