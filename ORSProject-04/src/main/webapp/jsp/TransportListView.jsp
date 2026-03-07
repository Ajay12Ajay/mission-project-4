<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.proj4.bean.TransportBean"%>
<%@page import="in.co.rays.proj4.controller.TransportListCtl"%>
<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.util.HTMLUtility"%>
<%@page import="in.co.rays.proj4.util.DataUtility"%>
<%@page import="in.co.rays.proj4.util.ServletUtility"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>

<html>
<head>
<title>Transport List</title>

<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />

</head>

<body>

	<%@include file="Header.jsp"%>

	<jsp:useBean id="bean" class="in.co.rays.proj4.bean.TransportBean"
		scope="request"></jsp:useBean>

	<div align="center">

		<h1 align="center" style="margin-bottom: -15; color: navy;">
			Transport List</h1>

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

		<form action="<%=ORSView.TRANSPORT_LIST_CTL%>" method="post">

			<%
			int pageNo = ServletUtility.getPageNo(request);
			int pageSize = ServletUtility.getPageSize(request);
			int index = ((pageNo - 1) * pageSize) + 1;

			int nextListSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());

			HashMap<String, String> transportIdMap = (HashMap<String, String>) request.getAttribute("transportIdMap");

			HashMap<String, String> vehicleTypeMap = (HashMap<String, String>) request.getAttribute("vehicleTypeMap");

			HashMap<String, String> driverNameMap = (HashMap<String, String>) request.getAttribute("driverNameMap");

			List<TransportBean> list = (List<TransportBean>) ServletUtility.getList(request);

			Iterator<TransportBean> it = list.iterator();

			if (list.size() != 0) {
			%>

			<input type="hidden" name="pageNo" value="<%=pageNo%>"> <input
				type="hidden" name="pageSize" value="<%=pageSize%>">

			<!-- Search Filters -->

			<table style="width: 100%">

				<tr>

					<td align="center"><label><b>Transport ID :</b></label> <%=HTMLUtility.getList("transportId", ServletUtility.getParameter("transportId", request), transportIdMap)%> &emsp; <label><b>Vehicle Type :</b></label> <%=HTMLUtility.getList("vehicleType", ServletUtility.getParameter("vehicleType", request), vehicleTypeMap)%> &emsp; <label><b>Driver Name :</b></label> <%=HTMLUtility.getList("driverName", ServletUtility.getParameter("driverName", request), driverNameMap)%> &emsp; <input type="submit" name="operation"
						value="<%=TransportListCtl.OP_SEARCH%>"> <input
						type="submit" name="operation"
						value="<%=TransportListCtl.OP_RESET%>"></td>

				</tr>

			</table>

			<br>

			<!-- Data Table -->

			<table border="1" style="width: 100%; border: groove;">

				<tr style="background-color: #e1e6f1e3;">

					<th width="5%"><input type="checkbox" id="selectall" /></th>

					<th width="5%">S.No</th>

					<th width="15%">Transport ID</th>

					<th width="20%">Vehicle Type</th>

					<th width="20%">Driver Name</th>

					<th width="15%">Charges</th>

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

					<td><%=bean.getTransportId()%></td>

					<td><%=bean.getVehicleType()%></td>

					<td><%=bean.getDriverName()%></td>

					<td><%=bean.getCharges()%></td>

					<td align="center"><a href="TransportCtl?id=<%=bean.getId()%>">Edit</a>

					</td>

				</tr>

				<%
				}
				%>

			</table>

			<!-- Pagination -->

			<table style="width: 100%">

				<tr>

					<td style="width: 25%"><input type="submit" name="operation"
						value="<%=TransportListCtl.OP_PREVIOUS%>"
						<%=pageNo > 1 ? "" : "disabled"%>></td>

					<td align="center" style="width: 25%"><input type="submit"
						name="operation" value="<%=TransportListCtl.OP_NEW%>"></td>

					<td align="center" style="width: 25%"><input type="submit"
						name="operation" value="<%=TransportListCtl.OP_DELETE%>">

					</td>

					<td align="right" style="width: 25%"><input type="submit"
						name="operation" value="<%=TransportListCtl.OP_NEXT%>"
						<%=nextListSize != 0 ? "" : "disabled"%>></td>

				</tr>

			</table>

			<%
			} else {
			%>

			<table>

				<tr>

					<td align="right"><input type="submit" name="operation"
						value="<%=TransportListCtl.OP_BACK%>"></td>

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