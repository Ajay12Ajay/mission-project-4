<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.proj4.bean.FreelancerBean"%>
<%@page import="in.co.rays.proj4.controller.FreelancerListCtl"%>
<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.util.HTMLUtility"%>
<%@page import="in.co.rays.proj4.util.DataUtility"%>
<%@page import="in.co.rays.proj4.util.ServletUtility"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>

<html>
<head>
<title>Freelancer List</title>

<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />

</head>

<body>

	<%@include file="Header.jsp"%>

	<jsp:useBean id="bean" class="in.co.rays.proj4.bean.FreelancerBean"
		scope="request"></jsp:useBean>

	<div align="center">

		<h1 align="center" style="margin-bottom: -15; color: navy;">
			Freelancer List</h1>

		<div style="height: 15px; margin-bottom: 12px">

			<h3>
				<font color="red"> <%=ServletUtility.getErrorMessage(request)%>
				</font>
			</h3>

			<h3>
				<font color="green"> <%=ServletUtility.getSuccessMessage(request)%>
				</font>
			</h3>

		</div>

		<form action="<%=ORSView.FREELANCER_LIST_CTL%>" method="post">

			<%
			int pageNo = ServletUtility.getPageNo(request);
			int pageSize = ServletUtility.getPageSize(request);

			int index = ((pageNo - 1) * pageSize) + 1;

			int nextListSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());

			HashMap<String, String> freelancerNameMap = (HashMap<String, String>) request.getAttribute("freelancerNameMap");

			HashMap<String, String> projectNameMap = (HashMap<String, String>) request.getAttribute("projectNameMap");

			HashMap<String, String> deadlineMap = (HashMap<String, String>) request.getAttribute("deadlineMap");

			List<FreelancerBean> list = (List<FreelancerBean>) ServletUtility.getList(request);

			Iterator<FreelancerBean> it = list.iterator();

			if (list.size() != 0) {
			%>

			<input type="hidden" name="pageNo" value="<%=pageNo%>"> <input
				type="hidden" name="pageSize" value="<%=pageSize%>">

			<!-- Search Filters -->

			<table style="width: 100%">

				<tr>

					<td align="center"><label><b>Freelancer Name :</b></label> <%=HTMLUtility.getList("freelancerName", ServletUtility.getParameter("freelancerName", request),
		freelancerNameMap)%> &emsp; <label><b>Project Name :</b></label> <%=HTMLUtility.getList("projectName", ServletUtility.getParameter("projectName", request), projectNameMap)%> &emsp; <label><b>Deadline :</b></label> <%=HTMLUtility.getList("deadline", ServletUtility.getParameter("deadline", request), deadlineMap)%> &emsp; <input type="submit" name="operation"
						value="<%=FreelancerListCtl.OP_SEARCH%>"> <input
						type="submit" name="operation"
						value="<%=FreelancerListCtl.OP_RESET%>"></td>

				</tr>

			</table>

			<br>

			<!-- Data Table -->

			<table border="1" style="width: 100%; border: groove;">

				<tr style="background-color: #e1e6f1e3;">

					<th width="5%"><input type="checkbox" id="selectall" /></th>

					<th width="5%">S.No</th>

					<th width="20%">Freelancer Name</th>

					<th width="20%">Project Name</th>

					<th width="20%">Deadline</th>

					<th width="15%">Payment Amount</th>

					<th width="5%">Edit</th>

				</tr>

				<%
				while (it.hasNext()) {

					bean = it.next();
				%>

				<tr>

					<td align="center"><input type="checkbox" class="case"
						name="ids" value="<%=bean.getId()%>"></td>

					<td><%=index++%></td>

					<td><%=bean.getFreelancerName()%></td>

					<td><%=bean.getProjectName()%></td>

					<td><%=DataUtility.getDateString(bean.getDeadline())%></td>

					<td><%=bean.getPaymentAmount()%></td>

					<td align="center"><a
						href="FreelancerCtl?id=<%=bean.getId()%>">Edit</a></td>

				</tr>

				<%
				}
				%>

			</table>

			<!-- Pagination -->

			<table style="width: 100%">

				<tr>

					<td style="width: 25%"><input type="submit" name="operation"
						value="<%=FreelancerListCtl.OP_PREVIOUS%>"
						<%=pageNo > 1 ? "" : "disabled"%>></td>

					<td align="center" style="width: 25%"><input type="submit"
						name="operation" value="<%=FreelancerListCtl.OP_NEW%>"></td>

					<td align="center" style="width: 25%"><input type="submit"
						name="operation" value="<%=FreelancerListCtl.OP_DELETE%>">
					</td>

					<td align="right" style="width: 25%"><input type="submit"
						name="operation" value="<%=FreelancerListCtl.OP_NEXT%>"
						<%=nextListSize != 0 ? "" : "disabled"%>></td>

				</tr>

			</table>

			<%
			} else {
			%>

			<table>

				<tr>

					<td align="right"><input type="submit" name="operation"
						value="<%=FreelancerListCtl.OP_BACK%>"></td>

				</tr>

			</table>

			<%
			}
			%>

		</form>

	</div>

	<%@include file="Footer.jsp"%>

</body>
</html>