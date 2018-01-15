<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>发票信息导入</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
		$("#btnImport").click(function() {
			$.jBox($("#importBox").html(), {
				title : "导入数据",
				buttons : {
					"关闭" : true
				},
				bottomText : "导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！"
			});
		});
	});
</script>
</head>
<body>
	<div id="importBox">
		<form id="importForm" action="${ctx}/invoice/imp/import" method="post"
			enctype="multipart/form-data" class="form-search"
			style="padding-left: 20px; text-align: center;"
			onsubmit="loading('正在导入，请稍等...');">
			<li><input type="radio" name="imptype" value="1" checked=checked />信息总表<input
				type="radio" name="imptype" value="2" />欠费表</li> <br /> <input
				id="uploadFile" name="file" type="file" style="width: 600px" /><br />
			<br /> <input id="btnImportSubmit" class="btn btn-primary"
				type="submit" value="   导    入   " />
		</form>
	</div>
	<li>错误信息</li>
	<sys:message content="${message}" />
</body>
</html>