<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.proj4.bean.CourceBean"%>
<%@page import="in.co.rays.proj4.controller.CourceListCtl"%>
<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.util.HTMLUtility"%>
<%@page import="in.co.rays.proj4.util.DataUtility"%>
<%@page import="in.co.rays.proj4.util.ServletUtility"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>

<html>
<head>
<title>Course List</title>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>

<body>
<%@include file="Header.jsp"%>

<jsp:useBean id="bean" class="in.co.rays.proj4.bean.CourceBean"
	scope="request"></jsp:useBean>

<div align="center">

	<h1 align="center" style="margin-bottom: -15; color: navy;">
		Course List
	</h1>

	<div style="height: 15px; margin-bottom: 12px">
		<h3>
			<font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
		</h3>
		<h3>
			<font color="green"><%=ServletUtility.getSuccessMessage(request)%></font>
		</h3>
	</div>

	<form action="<%=ORSView.COURCE_LIST_CTL%>" method="post">

	<%
		int pageNo = ServletUtility.getPageNo(request);
		int pageSize = ServletUtility.getPageSize(request);
		int index = ((pageNo - 1) * pageSize) + 1;
		int nextListSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());

		HashMap<String,String> courseCodeMap =
			(HashMap<String,String>) request.getAttribute("courseCodeMap");

		HashMap<String,String> courseNameMap =
			(HashMap<String,String>) request.getAttribute("courseNameMap");

		List<CourceBean> list =
			(List<CourceBean>) ServletUtility.getList(request);

		Iterator<CourceBean> it = list.iterator();

		if (list.size() != 0) {
	%>

	<input type="hidden" name="pageNo" value="<%=pageNo%>">
	<input type="hidden" name="pageSize" value="<%=pageSize%>">

	<!-- ================= SEARCH FILTER ================= -->
	<table style="width: 100%">
	<tr>
		<td align="center">

			<label><b>Course Code :</b></label>
			<%=HTMLUtility.getList("courseCode",
				String.valueOf(bean.getCourseCode()), courseCodeMap)%>
			&emsp;

			<label><b>Course Name :</b></label>
			<%=HTMLUtility.getList("courseName",
				String.valueOf(bean.getCourseName()), courseNameMap)%>
			&emsp;

			<input type="submit" name="operation"
				value="<%=CourceListCtl.OP_SEARCH%>">

			<input type="submit" name="operation"
				value="<%=CourceListCtl.OP_RESET%>">

		</td>
	</tr>
	</table>

	<br>

	<!-- ================= LIST TABLE ================= -->
	<table border="1" style="width: 100%; border: groove;">
	<tr style="background-color: #e1e6f1e3;">
		<th width="5%"><input type="checkbox" id="selectall" /></th>
		<th width="5%">S.No</th>
		<th width="15%">Course Code</th>
		<th width="20%">Course Name</th>
		<th width="15%">Duration</th>
		<th width="10%">Course Fee</th>
		<th width="5%">Edit</th>
	</tr>

	<%
		while (it.hasNext()) {
			bean = (CourceBean) it.next();
	%>

	<tr>
		<td style="text-align: center;">
			<input type="checkbox" class="case"
				name="ids" value="<%=bean.getId()%>">
		</td>

		<td><%=index++%></td>
		<td><%=bean.getCourseCode()%></td>
		<td><%=bean.getCourseName()%></td>
		<td><%=bean.getDuration()%></td>
		<td><%=bean.getCourseFee()%></td>

		<td style="text-align: center;">
			<a href="CourceCtl?id=<%=bean.getId()%>">Edit</a>
		</td>
	</tr>

	<%
		}
	%>
	</table>

	<!-- ================= PAGINATION ================= -->
	<table style="width: 100%">
	<tr>
		<td style="width: 25%">
			<input type="submit" name="operation"
				value="<%=CourceListCtl.OP_PREVIOUS%>"
				<%=pageNo > 1 ? "" : "disabled"%>>
		</td>

		<td align="center" style="width: 25%">
			<input type="submit" name="operation"
				value="<%=CourceListCtl.OP_NEW%>">
		</td>

		<td align="center" style="width: 25%">
			<input type="submit" name="operation"
				value="<%=CourceListCtl.OP_DELETE%>">
		</td>

		<td style="width: 25%" align="right">
			<input type="submit" name="operation"
				value="<%=CourceListCtl.OP_NEXT%>"
				<%=nextListSize != 0 ? "" : "disabled"%>>
		</td>
	</tr>
	</table>

	<%
		} else {
	%>

	<table>
	<tr>
		<td align="right">
			<input type="submit" name="operation"
				value="<%=CourceListCtl.OP_BACK%>">
		</td>
	</tr>
	</table>

	<%
		}
	%>

	</form>
</div>

<%@ include file="Footer.jsp"%>

</body>
</html>