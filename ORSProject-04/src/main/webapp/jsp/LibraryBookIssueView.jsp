<%@page import="in.co.rays.proj4.controller.LibraryBookIssueCtl"%>
<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.proj4.util.HTMLUtility"%>
<%@page import="in.co.rays.proj4.util.DataUtility"%>
<%@page import="in.co.rays.proj4.util.ServletUtility"%>

<html>
<head>
<title>Add Library Book Issue</title>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>

<body>

<form action="<%=ORSView.LIBRARY_BOOK_ISSUE_CTL%>" method="post">

	<%@ include file="Header.jsp"%>

	<jsp:useBean id="bean"
		class="in.co.rays.proj4.bean.LibraryBookIssueBean"
		scope="request"></jsp:useBean>

	<%
		/* ================= STATIC PRELOAD ================= */
		HashMap<String,String> statusMap = new HashMap<String,String>();
		statusMap.put("Issued","Issued");
		statusMap.put("Returned","Returned");
		statusMap.put("Overdue","Overdue");
	%>

	<div align="center">

		<h1 align="center" style="margin-bottom: -15; color: navy">
			<%
				if (bean != null && bean.getId() > 0) {
			%>
				Update
			<%
				} else {
			%>
				Add
			<%
				}
			%>
			Library Book Issue
		</h1>

		<div style="height: 15px; margin-bottom: 12px">
			<h3 align="center">
				<font color="red">
					<%=ServletUtility.getErrorMessage(request)%>
				</font>
			</h3>

			<h3 align="center">
				<font color="green">
					<%=ServletUtility.getSuccessMessage(request)%>
				</font>
			</h3>
		</div>

		<!-- Hidden Fields -->
		<input type="hidden" name="id" value="<%=bean.getId()%>">
		<input type="hidden" name="createdBy" value="<%=bean.getCreatedBy()%>">
		<input type="hidden" name="modifiedBy" value="<%=bean.getModifiedBy()%>">
		<input type="hidden" name="createdDatetime"
			value="<%=DataUtility.getTimestamp(bean.getCreatedDatetime())%>">
		<input type="hidden" name="modifiedDatetime"
			value="<%=DataUtility.getTimestamp(bean.getModifiedDatetime())%>">

		<table>

			<!-- Book ID -->
			<tr>
				<th align="left">Book ID<span style="color: red">*</span></th>
				<td>
					<input type="text" name="bookId"
						placeholder="Enter Book ID"
						value="<%=DataUtility.getStringData(bean.getBookId())%>">
				</td>
				<td style="position: fixed;">
					<font color="red">
						<%=ServletUtility.getErrorMessage("bookId", request)%>
					</font>
				</td>
			</tr>

			<!-- Member ID -->
			<tr>
				<th align="left">Member ID<span style="color: red">*</span></th>
				<td>
					<input type="text" name="memberId"
						placeholder="Enter Member ID"
						value="<%=DataUtility.getStringData(bean.getMemberId())%>">
				</td>
				<td style="position: fixed;">
					<font color="red">
						<%=ServletUtility.getErrorMessage("memberId", request)%>
					</font>
				</td>
			</tr>

			<!-- Issue Date -->
			<tr>
				<th align="left">Issue Date<span style="color: red">*</span></th>
				<td>
					<input type="text" id="udate" name="issueDate"
						placeholder="Select Issue Date"
						value="<%=DataUtility.getDateString(bean.getIssueDate())%>">
				</td>
				<td style="position: fixed;">
					<font color="red">
						<%=ServletUtility.getErrorMessage("issueDate", request)%>
					</font>
				</td>
			</tr>

			<!-- Return Date -->
			<tr>
				<th align="left">Return Date</th>
				<td>
					<input type="text" id="udate2" name="returnDate"
						placeholder="Select Return Date"
						value="<%=DataUtility.getDateString(bean.getReturnDate())%>">
				</td>
				<td style="position: fixed;">
					<font color="red">
						<%=ServletUtility.getErrorMessage("returnDate", request)%>
					</font>
				</td>
			</tr>

			<!-- Fine -->
			<tr>
				<th align="left">Fine Amount</th>
				<td>
					<input type="text" name="fineAmount"
						placeholder="Enter Fine Amount"
						value="<%=DataUtility.getStringData(bean.getFineAmount())%>">
				</td>
				<td></td>
			</tr>

			<!-- Issued By -->
			<tr>
				<th align="left">Issued By<span style="color: red">*</span></th>
				<td>
					<input type="text" name="issuedBy"
						placeholder="Enter Issuer Name"
						value="<%=DataUtility.getStringData(bean.getIssuedBy())%>">
				</td>
				<td style="position: fixed;">
					<font color="red">
						<%=ServletUtility.getErrorMessage("issuedBy", request)%>
					</font>
				</td>
			</tr>

			<!-- Status -->
			<tr>
				<th align="left">Status<span style="color: red">*</span></th>
				<td>
					<%
						String htmlList = HTMLUtility.getList(
								"status",
								bean.getStatus(),
								statusMap);
					%>
					<%=htmlList%>
				</td>
				<td style="position: fixed;">
					<font color="red">
						<%=ServletUtility.getErrorMessage("status", request)%>
					</font>
				</td>
			</tr>

			<!-- Buttons -->
			<tr>
				<th></th>

				<%
					if (bean != null && bean.getId() > 0) {
				%>
				<td align="left" colspan="2">
					<input type="submit" name="operation"
						value="<%=LibraryBookIssueCtl.OP_UPDATE%>">

					<input type="submit" name="operation"
						value="<%=LibraryBookIssueCtl.OP_CANCEL%>">
				</td>
				<%
					} else {
				%>
				<td align="left" colspan="2">
					<input type="submit" name="operation"
						value="<%=LibraryBookIssueCtl.OP_SAVE%>">

					<input type="submit" name="operation"
						value="<%=LibraryBookIssueCtl.OP_RESET%>">
				</td>
				<%
					}
				%>
			</tr>

		</table>
	</div>

</form>

<%@ include file="Footer.jsp"%>

</body>
</html>