<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
<title>已达发票信息查询</title>
<meta name="decorator" content="default" />
<script type="text/javascript">
	$(document).ready(function() {
		$("#btnImport").click(function() {
			$.jBox($("#importBox").html(), {
				title : "发票打印",
				buttons : {
					"关闭" : true
				},
				bottomText : "导入文件不能超过5M，仅允许导入“xls”或“xlsx”格式文件！"
			});
		});
		//$("#a2").attr("checked","checked");
		//$("#a3").attr("checked","checked");
	});
	function page(n, s) {
		$("#pageNo").val(n);
		$("#pageSize").val(s);
		$("#searchForm").submit();
		return false;
	}
</script>
</head>
<body>

	<ul class="nav nav-tabs">
		<li class="active"><a href="${ctx}/invoice/info/invoiceinfo">发票信息列表</a></li>
	</ul>
	<form:form id="searchForm" action="${ctx}/invoice/info/invoiceprintinfo"
		method="post" class="breadcrumb form-search ">
		<input id="pageNo" name="pageNo" type="hidden" value="${page.pageNo}" />
		<input id="pageSize" name="pageSize" type="hidden"
			value="${page.pageSize}" />
		<sys:tableSort id="orderBy" name="orderBy" value="${page.orderBy}"
			callback="page();" />
		<ul class="ul-form">
			<p>
				<label>日期范围：&nbsp;</label><input id="startDate"
					name="printstartDate" type="text" readonly="readonly"
					maxlength="20" class="input-mini Wdate" value="${sdate}"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
				&nbsp;--&nbsp;&nbsp;&nbsp;<input id="endDate" name="printendDate"
					type="text" readonly="readonly" maxlength="20"
					class="input-mini Wdate" value="${edate}"
					onclick="WdatePicker({dateFmt:'yyyy-MM-dd',isShowClear:false});" />
				<label>户号：</label><input name="aac001" maxlength="50"
					class="input-medium" value="${aac001}" /> <label>姓&nbsp;&nbsp;&nbsp;名：</label>
				<input name="aac002" maxlength="50" class="input-medium"
					value="${aac002}" /> <input id="exception" name="exception"
					type="checkbox" value="1" ${exception==1?"checked":""} />只查询自己收费
			</p>
			<p></p>
			<p>
			<li class="btns"><input id="btnSubmit" class="btn btn-primary"
				type="submit" value="查询" onclick="return page();" /></li>
			<li class="clearfix"></li>
			</p>



		</ul>
	</form:form>
	<li>已收总金额：${money}</li>
	<sys:message content="${message}" />
	<table id="contentTable"
		class="table table-striped table-bordered table-condensed">
		<thead>
			<tr>
				<th>户号</th>
				<th>用户姓名</th>
				<th>起码</th>
				<th>止码</th>
				<th>电量</th>
				<th>缴费年月</th>>
				<th>应收金额</th>
				<th>实收金额</th>
				<th>缴费状态</th>
				<th>操作员</th>
				<th>导入日期</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${page.list}" var="invoicesum">
				<tr>
					<td>${invoicesum.aac001}</td>
					<td>${invoicesum.aac002}</td>
					<td>${invoicesum.aac003}</td>
					<td>${invoicesum.aac004}</td>
					<td>${invoicesum.aac013}</td>
					<td>${invoicesum.aac009}</td>
					<td>${invoicesum.sum}</td>
					<td>${invoicesum.aac006}</td>
					<td>${invoicesum.flag==2?"未缴费":"已缴费"}</td>
					<td>${invoicesum.name}</td>
					<td>${invoicesum.impdate}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	<div class="pagination">${page}</div>
</body>
</html>