<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>发票打印</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
	function drop(){
		var ret = confirm("确认要撤销吗?");
		if(ret){
			$("#searchForm").submit();
		}
	}
</script>
</head>
<body>

	<form:form id="searchForm" action="${ctx}/invoice/info/invoicestatus" method="post"
		class="breadcrumb form-search">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		
		<div style="margin-top: 8px;">
			<label>户号：&nbsp;</label><input name="aac001"
					 maxlength="50" class="input-medium" />
			<label>户名：&nbsp;</label><input name="aac002"
					 maxlength="50" class="input-medium" />
			<label>日期范围：&nbsp;</label><input id="aac009" name="aac009"
				type="text" readonly="readonly" maxlength="20"
				class="input-mini Wdate"
				onclick="WdatePicker({dateFmt:'yyyyMM',isShowClear:false});" />						
			&nbsp;&nbsp;&nbsp;<input id="btnSubmit" class="btn btn-primary"
				type="submit" value="查询" />&nbsp;&nbsp;
		</div>
	</form:form>
	<sys:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>户号</th>
				<th>户名</th>
				<th>起码</th>
				<th>止码</th>
				<th>档位</th>
				<th>欠费金额</th>
				<th>打印操作员</th>
				<th>打印时间</th>
				<th>操作</th>
		</thead>
		<tbody>
			<%
			    request.setAttribute("strEnter", "\n");
			    request.setAttribute("strTab", "\t");
			%>
			<c:forEach items="${page.list}" var="invoice">
				<tr>
					<td>${invoice.aac001}</td>
					<td>${invoice.aac002}</td>
					<td>${invoice.aac003}</td>
					<td>${invoice.aac004}</td>
					<td>${invoice.aac006}</td>
					<td>${invoice.aac010}</td>
					<td>${invoice.aac008}</td>
					<td><a href="${ctx}/invoice/info/invoiceupdatestatus?aac001=${invoice.aac001}&aac007=2" onclick="">撤销打印</a></td>
				</tr>
				
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>