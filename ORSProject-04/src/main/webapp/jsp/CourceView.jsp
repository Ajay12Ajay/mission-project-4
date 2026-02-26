<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.proj4.util.HTMLUtility"%>
<%@page import="in.co.rays.proj4.controller.CourceCtl"%>
<%@page import="in.co.rays.proj4.bean.CourceBean"%>
<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.util.DataUtility"%>
<%@page import="in.co.rays.proj4.util.ServletUtility"%>

<html>
<head>
<title>Add Course</title>
<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />
</head>

<body>

<form action="<%=ORSView.COURCE_CTL%>" method="post">

	<%@ include file="Header.jsp"%>

	<jsp:useBean id="bean" class="in.co.rays.proj4.bean.CourceBean"
		scope="request"></jsp:useBean>

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
			Course
		</h1>

		<div style="height: 15px; margin-bottom: 12px">

			<H3 align="center">
				<font color="red">
					<%=ServletUtility.getErrorMessage(request)%>
				</font>
			</H3>

			<H3 align="center">
				<font color="green">
					<%=ServletUtility.getSuccessMessage(request)%>
				</font>
			</H3>

		</div>

		<input type="hidden" name="id" value="<%=bean.getId()%>">
		<input type="hidden" name="createdBy" value="<%=bean.getCreatedBy()%>">
		<input type="hidden" name="modifiedBy" value="<%=bean.getModifiedBy()%>">
		<input type="hidden" name="createdDatetime"
			value="<%=DataUtility.getTimestamp(bean.getCreatedDatetime())%>">
		<input type="hidden" name="modifiedDatetime"
			value="<%=DataUtility.getTimestamp(bean.getModifiedDatetime())%>">

		<table>

			<tr>
				<th align="left">Course Code<span style="color: red">*</span></th>
				<td>
					<input type="text" name="courseCode"
						placeholder="Enter Course Code"
						value="<%=DataUtility.getStringData(bean.getCourseCode())%>">
				</td>
				<td style="position: fixed;">
					<font color="red">
						<%=ServletUtility.getErrorMessage("courseCode", request)%>
					</font>
				</td>
			</tr>

			<tr>
				<th align="left">Course Name<span style="color: red">*</span></th>
				<td>
					<input type="text" name="courseName"
						placeholder="Enter Course Name"
						value="<%=DataUtility.getStringData(bean.getCourseName())%>">
				</td>
				<td style="position: fixed;">
					<font color="red">
						<%=ServletUtility.getErrorMessage("courseName", request)%>
					</font>
				</td>
			</tr>

			<%
				// ===== STATIC PRELOAD FOR DURATION =====
				HashMap<String,String> durationMap = new HashMap<>();
				durationMap.put("3 Month", "3 Month");
				durationMap.put("6 Month", "6 Month");
				durationMap.put("12 Months", "12 Months");
			%>

			<tr>
				<th align="left">Duration<span style="color: red">*</span></th>
				<td>
					<%=HTMLUtility.getList("duration",
							String.valueOf(bean.getDuration()),
							durationMap)%>
				</td>
				<td style="position: fixed;">
					<font color="red">
						<%=ServletUtility.getErrorMessage("duration", request)%>
					</font>
				</td>
			</tr>

			<tr>
				<th align="left">Course Fee<span style="color: red">*</span></th>
				<td>
					<input type="text" name="courseFee"
						placeholder="Enter Course Fee"
						value="<%=DataUtility.getStringData(bean.getCourseFee())%>">
				</td>
				<td style="position: fixed;">
					<font color="red">
						<%=ServletUtility.getErrorMessage("courseFee", request)%>
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
						value="<%=CourceCtl.OP_UPDATE%>">
					<input type="submit" name="operation"
						value="<%=CourceCtl.OP_CANCEL%>">
				</td>

				<%
					} else {
				%>

				<td align="left" colspan="2">
					<input type="submit" name="operation"
						value="<%=CourceCtl.OP_SAVE%>">
					<input type="submit" name="operation"
						value="<%=CourceCtl.OP_RESET%>">
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