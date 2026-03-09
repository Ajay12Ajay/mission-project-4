<%@page import="java.util.HashMap"%>
<%@page import="in.co.rays.proj4.bean.VehicleBean"%>
<%@page import="in.co.rays.proj4.controller.VehicleListCtl"%>
<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.util.HTMLUtility"%>
<%@page import="in.co.rays.proj4.util.DataUtility"%>
<%@page import="in.co.rays.proj4.util.ServletUtility"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>

<html>
<head>
<title>Vehicle List</title>

<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />

</head>

<body>

	<%@include file="Header.jsp"%>

	<jsp:useBean id="bean" class="in.co.rays.proj4.bean.VehicleBean"
		scope="request"></jsp:useBean>

	<div align="center">

		<h1 align="center" style="margin-bottom: -15; color: navy;">
			Vehicle List</h1>

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

		<form action="<%=ORSView.VEHICLE_LIST_CTL%>" method="post">

			<%
			int pageNo = ServletUtility.getPageNo(request);
			int pageSize = ServletUtility.getPageSize(request);
			int index = ((pageNo - 1) * pageSize) + 1;

			int nextListSize = DataUtility.getInt(request.getAttribute("nextListSize").toString());

			HashMap<String, String> vehicleNumberMap = (HashMap<String, String>) request.getAttribute("vehicleNumberMap");

			HashMap<String, String> ownerNameMap = (HashMap<String, String>) request.getAttribute("ownerNameMap");

			HashMap<String, String> serviceDateMap = (HashMap<String, String>) request.getAttribute("serviceDateMap");

			List<VehicleBean> list = (List<VehicleBean>) ServletUtility.getList(request);

			Iterator<VehicleBean> it = list.iterator();

			if (list.size() != 0) {
			%>

			<input type="hidden" name="pageNo" value="<%=pageNo%>"> <input
				type="hidden" name="pageSize" value="<%=pageSize%>">

			<!-- Search Filters -->

			<table style="width: 100%">

				<tr>

					<td align="center"><label><b>Vehicle Number :</b></label> <%=HTMLUtility.getList("vehicleNumber", ServletUtility.getParameter("vehicleNumber", request), vehicleNumberMap)%> &emsp;
									   <label><b>Owner Name :</b></label> <%=HTMLUtility.getList("ownerName", ServletUtility.getParameter("ownerName", request), ownerNameMap)%> &emsp;
									   <%-- <label><b>Service Date :</b></label> <%=HTMLUtility.getList("serviceDate", ServletUtility.getParameter("serviceDate", request), serviceDateMap)%> &emsp; --%> 
									   <input type="submit" name="operation"
						value="<%=VehicleListCtl.OP_SEARCH%>"> <input
						type="submit" name="operation"
						value="<%=VehicleListCtl.OP_RESET%>"></td>

				</tr>

			</table>

			<br>

			<!-- Data Table -->

			<table border="1" style="width: 100%; border: groove;">

				<tr style="background-color: #e1e6f1e3;">

					<th width="5%"><input type="checkbox" id="selectall" /></th>

					<th width="5%">S.No</th>

					<th width="15%">Vehicle Number</th>

					<th width="20%">Owner Name</th>

					<th width="15%">Service Date</th>

					<th width="15%">Service Type</th>

					<th width="15%">Cost</th>

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

					<td><%=bean.getVehicleNumber()%></td>

					<td><%=bean.getOwnerName()%></td>

					<td><%=DataUtility.getDateString(bean.getServiceDate())%></td>

					<td><%=bean.getServiceType()%></td>

					<td><%=bean.getCost()%></td>

					<td align="center"><a href="VehicleCtl?id=<%=bean.getId()%>">Edit</a>
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
						value="<%=VehicleListCtl.OP_PREVIOUS%>"
						<%=pageNo > 1 ? "" : "disabled"%>></td>

					<td align="center" style="width: 25%"><input type="submit"
						name="operation" value="<%=VehicleListCtl.OP_NEW%>"></td>

					<td align="center" style="width: 25%"><input type="submit"
						name="operation" value="<%=VehicleListCtl.OP_DELETE%>"></td>

					<td align="right" style="width: 25%"><input type="submit"
						name="operation" value="<%=VehicleListCtl.OP_NEXT%>"
						<%=nextListSize != 0 ? "" : "disabled"%>></td>

				</tr>

			</table>

			<%
			} else {
			%>

			<table>

				<tr>

					<td align="right"><input type="submit" name="operation"
						value="<%=VehicleListCtl.OP_BACK%>"></td>

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