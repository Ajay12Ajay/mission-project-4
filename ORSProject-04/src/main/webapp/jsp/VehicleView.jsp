<%@page import="in.co.rays.proj4.controller.VehicleCtl"%>
<%@page import="in.co.rays.proj4.bean.VehicleBean"%>
<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.util.HTMLUtility"%>
<%@page import="in.co.rays.proj4.util.DataUtility"%>
<%@page import="in.co.rays.proj4.util.ServletUtility"%>
<%@page import="java.util.HashMap"%>

<html>
<head>
<title>Add Vehicle</title>

<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />

</head>

<body>

	<form action="<%=ORSView.VEHICLE_CTL%>" method="post">

		<%@ include file="Header.jsp"%>

		<jsp:useBean id="bean" class="in.co.rays.proj4.bean.VehicleBean"
			scope="request"></jsp:useBean>

		<%
		/* Static Service Type Map */
		HashMap<String, String> serviceTypeMap = new HashMap<>();

		serviceTypeMap.put("Oil Change", "Oil Change");
		serviceTypeMap.put("General Service", "General Service");
		serviceTypeMap.put("Engine Repair", "Engine Repair");
		serviceTypeMap.put("Brake Service", "Brake Service");
		serviceTypeMap.put("Full Service", "Full Service");
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

				Vehicle
			</h1>

			<div style="height: 15px; margin-bottom: 12px">

				<h3 align="center">
					<font color="red"> <%=ServletUtility.getErrorMessage(request)%>
					</font>
				</h3>

				<h3 align="center">
					<font color="green"> <%=ServletUtility.getSuccessMessage(request)%>
					</font>
				</h3>

			</div>

			<!-- Hidden Fields -->

			<input type="hidden" name="id" value="<%=bean.getId()%>"> <input
				type="hidden" name="createdBy" value="<%=bean.getCreatedBy()%>">

			<input type="hidden" name="modifiedBy"
				value="<%=bean.getModifiedBy()%>"> <input type="hidden"
				name="createdDatetime"
				value="<%=DataUtility.getTimestamp(bean.getCreatedDatetime())%>">

			<input type="hidden" name="modifiedDatetime"
				value="<%=DataUtility.getTimestamp(bean.getModifiedDatetime())%>">

			<table>

				<tr>

					<th align="left">Vehicle Number<span style="color: red">*</span></th>

					<td><input type="text" name="vehicleNumber"
						placeholder="Enter Vehicle Number"
						value="<%=DataUtility.getStringData(bean.getVehicleNumber())%>">

					</td>

					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("vehicleNumber", request)%>
					</font></td>

				</tr>


				<tr>

					<th align="left">Owner Name<span style="color: red">*</span></th>

					<td><input type="text" name="ownerName"
						placeholder="Enter Owner Name"
						value="<%=DataUtility.getStringData(bean.getOwnerName())%>">

					</td>

					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("ownerName", request)%>
					</font></td>

				</tr>


				<tr>

					<th align="left">Service Date<span style="color: red">*</span></th>

					<td><input type="text" id="udatee" name="serviceDate"
						placeholder="dd/mm/yyyy"
						value="<%=DataUtility.getDateString(bean.getServiceDate())%>">

					</td>

					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("serviceDate", request)%>
					</font></td>

				</tr>


				<tr>

					<th align="left">Service Type<span style="color: red">*</span></th>

					<td>
						<%
						String serviceTypeList = HTMLUtility.getList("serviceType", bean.getServiceType(), serviceTypeMap);
						%> <%=serviceTypeList%>

					</td>

					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("serviceType", request)%>
					</font></td>

				</tr>


				<tr>

					<th align="left">Cost<span style="color: red">*</span></th>

					<td><input type="text" name="cost"
						placeholder="Enter Service Cost" value="<%=bean.getCost()%>">

					</td>

					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("cost", request)%>
					</font></td>

				</tr>


				<tr>

					<th></th>

					<%
					if (bean != null && bean.getId() > 0) {
					%>

					<td align="left" colspan="2"><input type="submit"
						name="operation" value="<%=VehicleCtl.OP_UPDATE%>"> <input
						type="submit" name="operation" value="<%=VehicleCtl.OP_CANCEL%>">

						<%
						} else {
						%>
					<td align="left" colspan="2"><input type="submit"
						name="operation" value="<%=VehicleCtl.OP_SAVE%>"> <input
						type="submit" name="operation" value="<%=VehicleCtl.OP_RESET%>">

						<%
						}
						%></td>

				</tr>

			</table>

		</div>

	</form>

	<%@ include file="Footer.jsp"%>

</body>
</html>