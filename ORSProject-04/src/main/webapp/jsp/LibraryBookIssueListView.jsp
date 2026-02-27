<%@page import="in.co.rays.proj4.controller.LibraryBookIssueListCtl"%>
<%@page import="in.co.rays.proj4.bean.LibraryBookIssueBean"%>
<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.util.HTMLUtility"%>
<%@page import="in.co.rays.proj4.util.DataUtility"%>
<%@page import="in.co.rays.proj4.util.ServletUtility"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>

<html>
<head>
<title>Library Book Issue List</title>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>

<body>
<%@include file="Header.jsp"%>

<jsp:useBean id="bean"
	class="in.co.rays.proj4.bean.LibraryBookIssueBean"
	scope="request"></jsp:useBean>

<div align="center">

	<h1 align="center" style="margin-bottom: -15; color: navy;">
		Library Book Issue List
	</h1>

	<div style="height: 15px; margin-bottom: 12px">
		<h3>
			<font color="red"><%=ServletUtility.getErrorMessage(request)%></font>
		</h3>
		<h3>
			<font color="green"><%=ServletUtility.getSuccessMessage(request)%></font>
		</h3>
	</div>

	<form action="<%=ORSView.LIBRARY_BOOK_ISSUE_LIST_CTL%>" method="post">

	<%
		int pageNo = ServletUtility.getPageNo(request);
		int pageSize = ServletUtility.getPageSize(request);
		int index = ((pageNo - 1) * pageSize) + 1;
		int nextListSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());

		HashMap<String,String> statusMap =
			(HashMap<String,String>) request.getAttribute("statusMap");

		List<LibraryBookIssueBean> list =
			(List<LibraryBookIssueBean>) ServletUtility.getList(request);

		Iterator<LibraryBookIssueBean> it = list.iterator();
	%>

	<input type="hidden" name="pageNo" value="<%=pageNo%>">
	<input type="hidden" name="pageSize" value="<%=pageSize%>">

	<!-- ================= SEARCH PANEL ================= -->

	<table style="width: 100%">
	<tr>
	<td align="center">

	<label><b>Book ID :</b></label>
	<input type="text" name="bookId"
		value="<%=ServletUtility.getParameter("bookId", request)%>"
		placeholder="Enter Book ID">&emsp;

	<label><b>Member ID :</b></label>
	<input type="text" name="memberId"
		value="<%=ServletUtility.getParameter("memberId", request)%>"
		placeholder="Enter Member ID">&emsp;

	<label><b>Issue Date :</b></label>
	<input type="text" id="udate" name="issueDate"
		value="<%=ServletUtility.getParameter("issueDate", request)%>"
		placeholder="Enter Issue Date">&emsp;

	<label><b>Status :</b></label>
	<%=HTMLUtility.getList("status",
			ServletUtility.getParameter("status", request),
			statusMap)%>&emsp;

	<input type="submit" name="operation"
		value="<%=LibraryBookIssueListCtl.OP_SEARCH%>">

	<input type="submit" name="operation"
		value="<%=LibraryBookIssueListCtl.OP_RESET%>">

	</td>
	</tr>
	</table>

	<br>

	<!-- ================= RESULT TABLE ================= -->

	<table border="1" style="width: 100%; border: groove;">
	<tr style="background-color: #e1e6f1e3;">
		<th width="5%"><input type="checkbox" id="selectall"/></th>
		<th width="5%">S.No</th>
		<th>Book ID</th>
		<th>Member ID</th>
		<th>Issue Date</th>
		<th>Return Date</th>
		<th>Fine</th>
		<th>Issued By</th>
		<th>Status</th>
		<th>Edit</th>
	</tr>

	<%
	if (list != null && list.size() > 0) {
		while (it.hasNext()) {
			bean = it.next();
	%>

	<tr>
		<td align="center">
			<input type="checkbox" class="case"
				name="ids" value="<%=bean.getId()%>">
		</td>

		<td><%=index++%></td>
		<td><%=bean.getBookId()%></td>
		<td><%=bean.getMemberId()%></td>
		<td><%=bean.getIssueDate()%></td>
		<td><%=bean.getReturnDate()%></td>
		<td><%=bean.getFineAmount()%></td>
		<td><%=bean.getIssuedBy()%></td>
		<td><%=bean.getStatus()%></td>

		<td align="center">
			<a href="LibraryBookIssueCtl?id=<%=bean.getId()%>">Edit</a>
		</td>
	</tr>

	<%
		}
	%>
	</table>

	<!-- ================= PAGINATION ================= -->

	<table style="width: 100%">
	<tr>
	<td width="25%">
		<input type="submit" name="operation"
			value="<%=LibraryBookIssueListCtl.OP_PREVIOUS%>"
			<%=pageNo > 1 ? "" : "disabled"%>>
	</td>

	<td align="center" width="25%">
		<input type="submit" name="operation"
			value="<%=LibraryBookIssueListCtl.OP_NEW%>">
	</td>

	<td align="center" width="25%">
		<input type="submit" name="operation"
			value="<%=LibraryBookIssueListCtl.OP_DELETE%>">
	</td>

	<td align="right" width="25%">
		<input type="submit" name="operation"
			value="<%=LibraryBookIssueListCtl.OP_NEXT%>"
			<%=nextListSize != 0 ? "" : "disabled"%>>
	</td>
	</tr>
	</table>

	<%
	} else {
	%>

	<table>
	<tr>
		<td align="center">
			<input type="submit" name="operation"
				value="<%=LibraryBookIssueListCtl.OP_BACK%>">
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