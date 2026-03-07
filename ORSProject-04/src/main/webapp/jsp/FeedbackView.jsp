<%@page import="in.co.rays.proj4.controller.FeedbackCtl"%>
<%@page import="in.co.rays.proj4.bean.FeedbackBean"%>
<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.util.HTMLUtility"%>
<%@page import="in.co.rays.proj4.util.DataUtility"%>
<%@page import="in.co.rays.proj4.util.ServletUtility"%>
<%@page import="java.util.HashMap"%>

<html>
<head>
<title>Add Feedback</title>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>
<body>

<form action="<%=ORSView.FEEDBACK_CTL%>" method="post">

<%@ include file="Header.jsp"%>

<jsp:useBean id="bean" class="in.co.rays.proj4.bean.FeedbackBean"
	scope="request"></jsp:useBean>

<%
	// Static Rating Map (1 to 5)
	HashMap<String, String> ratingMap = new HashMap<>();
	ratingMap.put("1", "1");
	ratingMap.put("2", "2");
	ratingMap.put("3", "3");
	ratingMap.put("4", "4");
	ratingMap.put("5", "5");
%>

<div align="center">

	<h1 align="center" style="margin-bottom: -15; color: navy">
	<%
		if (bean != null && bean.getId() > 0) {
	%>Update<%
		} else {
	%>Add<%
		}
	%>
	Feedback
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

		<tr>
			<th align="left">Feedback Code<span style="color: red">*</span></th>
			<td>
				<input type="text" name="feedbackCode"
					placeholder="Enter Feedback Code"
					value="<%=DataUtility.getStringData(bean.getFeedbackCode())%>">
			</td>
			<td style="position: fixed;">
				<font color="red">
					<%=ServletUtility.getErrorMessage("feedbackCode", request)%>
				</font>
			</td>
		</tr>

		<tr>
			<th align="left">User Name<span style="color: red">*</span></th>
			<td>
				<input type="text" name="userName"
					placeholder="Enter User Name"
					value="<%=DataUtility.getStringData(bean.getUserName())%>">
			</td>
			<td style="position: fixed;">
				<font color="red">
					<%=ServletUtility.getErrorMessage("userName", request)%>
				</font>
			</td>
		</tr>

		<tr>
			<th align="left">Comments<span style="color: red">*</span></th>
			<td>
				<textarea name="comments" rows="4" cols="30"
					placeholder="Enter Comments"><%=DataUtility.getStringData(bean.getComments())%></textarea>
			</td>
			<td style="position: fixed;">
				<font color="red">
					<%=ServletUtility.getErrorMessage("comments", request)%>
				</font>
			</td>
		</tr>

		<tr>
			<th align="left">Rating<span style="color: red">*</span></th>
			<td>
				<%
					String ratingList = HTMLUtility.getList(
						"rating",
						String.valueOf(bean.getRating()),
						ratingMap);
				%>
				<%=ratingList%>
			</td>
			<td style="position: fixed;">
				<font color="red">
					<%=ServletUtility.getErrorMessage("rating", request)%>
				</font>
			</td>
		</tr>

		<tr>
			<th align="left">Feedback Date<span style="color: red">*</span></th>
			<td>
				<input type="text" id="udate" name="feedbackDate"
					placeholder="Select Feedback Date"
					value="<%=DataUtility.getDateString(bean.getFeedbackDate())%>">
			</td>
			<td style="position: fixed;">
				<font color="red">
					<%=ServletUtility.getErrorMessage("feedbackDate", request)%>
				</font>
			</td>
		</tr>

		<tr>
			<th></th>

			<%
				if (bean != null && bean.getId() > 0) {
			%>
			<td align="left" colspan="2">
				<input type="submit" name="operation"
					value="<%=FeedbackCtl.OP_UPDATE%>">
				<input type="submit" name="operation"
					value="<%=FeedbackCtl.OP_CANCEL%>">
			<%
				} else {
			%>
			<td align="left" colspan="2">
				<input type="submit" name="operation"
					value="<%=FeedbackCtl.OP_SAVE%>">
				<input type="submit" name="operation"
					value="<%=FeedbackCtl.OP_RESET%>">
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
