<%@page import="in.co.rays.proj4.controller.FreelancerCtl"%>
<%@page import="in.co.rays.proj4.bean.FreelancerBean"%>
<%@page import="in.co.rays.proj4.controller.ORSView"%>
<%@page import="in.co.rays.proj4.util.DataUtility"%>
<%@page import="in.co.rays.proj4.util.ServletUtility"%>

<html>
<head>
<title>Add Freelancer</title>

<link rel="icon" type="image/png"
	href="<%=ORSView.APP_CONTEXT%>/img/logo.png" sizes="16x16" />

</head>

<body>

	<form action="<%=ORSView.FREELANCER_CTL%>" method="post">

		<%@ include file="Header.jsp"%>

		<jsp:useBean id="bean" class="in.co.rays.proj4.bean.FreelancerBean"
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

				Freelancer
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

					<th align="left">Freelancer Name<span style="color: red">*</span>
					</th>

					<td><input type="text" name="freelancerName"
						placeholder="Enter Freelancer Name"
						value="<%=DataUtility.getStringData(bean.getFreelancerName())%>">
					</td>

					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("freelancerName", request)%>
					</font></td>

				</tr>


				<tr>

					<th align="left">Project Name<span style="color: red">*</span>
					</th>

					<td><input type="text" name="projectName"
						placeholder="Enter Project Name"
						value="<%=DataUtility.getStringData(bean.getProjectName())%>">
					</td>

					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("projectName", request)%>
					</font></td>

				</tr>


				<tr>

					<th align="left">Deadline<span style="color: red">*</span>
					</th>

					<td><input type="text" name="deadline" id="udate"
						value="<%=DataUtility.getDateString(bean.getDeadline())%>">
					</td>

					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("deadline", request)%>
					</font></td>

				</tr>


				<tr>

					<th align="left">Payment Amount<span style="color: red">*</span>
					</th>

					<td><input type="text" name="paymentAmount"
						placeholder="Enter Payment Amount"
						value="<%=bean.getPaymentAmount()%>"></td>

					<td style="position: fixed;"><font color="red"> <%=ServletUtility.getErrorMessage("paymentAmount", request)%>
					</font></td>

				</tr>


				<tr>

					<th></th>

					<%
					if (bean != null && bean.getId() > 0) {
					%>

					<td align="left" colspan="2"><input type="submit"
						name="operation" value="<%=FreelancerCtl.OP_UPDATE%>"> <input
						type="submit" name="operation"
						value="<%=FreelancerCtl.OP_CANCEL%>"></td>

					<%
					} else {
					%>

					<td align="left" colspan="2"><input type="submit"
						name="operation" value="<%=FreelancerCtl.OP_SAVE%>"> <input
						type="submit" name="operation" value="<%=FreelancerCtl.OP_RESET%>">

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