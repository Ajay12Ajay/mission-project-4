<%@page import="in.co.rays.proj4.controller.TransportCtl"%>
<%@page import="in.co.rays.proj4.bean.TransportBean"%>
<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.util.HTMLUtility"%>
<%@page import="in.co.rays.proj4.util.DataUtility"%>
<%@page import="in.co.rays.proj4.util.ServletUtility"%>
<%@page import="java.util.HashMap"%>

<html>
<head>
<title>Add Transport</title>

<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />

</head>

<body>

	<form action="<%=ORSView.TRANSPORT_CTL%>" method="post">

		<%@ include file="Header.jsp"%>

		<jsp:useBean id="bean" class="in.co.rays.proj4.bean.TransportBean"
			scope="request"></jsp:useBean>

		<%
		/* Static Vehicle Type Map */
		HashMap<String, String> vehicleTypeMap = new HashMap<>();

		vehicleTypeMap.put("Motorcycles", "Motorcycles");
		vehicleTypeMap.put("Cars", "Cars");
		vehicleTypeMap.put("Buses", "Buses");
		vehicleTypeMap.put("Trucks/Lorries", "Trucks/Lorries");
		vehicleTypeMap.put("Auto Rickshaws", "Auto Rickshaws");
		vehicleTypeMap.put("Ambulances", "Ambulances");
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

				Transport
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

					<th align="left">Transport ID<span style="color: red">*</span>
					</th>

					<td><input type="text" name="transportId"
						placeholder="Enter Transport ID"
						value="<%=DataUtility.getStringData(bean.getTransportId())%>">

					</td>

					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("transportId", request)%>

					</font></td>

				</tr>


				<tr>

					<th align="left">Vehicle Type<span style="color: red">*</span>
					</th>

					<td>
						<%
						String vehicleTypeList = HTMLUtility.getList("vehicleType", bean.getVehicleType(), vehicleTypeMap);
						%> <%=vehicleTypeList%>

					</td>

					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("vehicleType", request)%>

					</font></td>

				</tr>


				<tr>

					<th align="left">Driver Name<span style="color: red">*</span>
					</th>

					<td><input type="text" name="driverName"
						placeholder="Enter Driver Name"
						value="<%=DataUtility.getStringData(bean.getDriverName())%>">

					</td>

					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("driverName", request)%>

					</font></td>

				</tr>


				<tr>

					<th align="left">Charges<span style="color: red">*</span>
					</th>

					<td><input type="text" name="charges"
						placeholder="Enter Charges" value="<%=bean.getCharges()%>">

					</td>

					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("charges", request)%>

					</font></td>

				</tr>


				<tr>

					<th></th>

					<%
					if (bean != null && bean.getId() > 0) {
					%>

					<td align="left" colspan="2"><input type="submit"
						name="operation" value="<%=TransportCtl.OP_UPDATE%>"> <input
						type="submit" name="operation" value="<%=TransportCtl.OP_CANCEL%>">

						<%
						} else {
						%>
					<td align="left" colspan="2"><input type="submit"
						name="operation" value="<%=TransportCtl.OP_SAVE%>"> <input
						type="submit" name="operation" value="<%=TransportCtl.OP_RESET%>">

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