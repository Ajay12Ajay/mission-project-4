/**
 * @Author: Ajay Pratap Kerketta
 * @Description: TransportCtl is a Servlet controller responsible for handling
 * operations related to Transport management such as adding, updating,
 * and validating transport data. It extends BaseCtl to inherit common
 * controller functionalities like validation and bean population.
 *
 * @Creation Date: 07-Mar-2026
 * @Version: 1.0
 */

package in.co.rays.proj4.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.TransportBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.TransportModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "TransportCtl", urlPatterns = { "/ctl/TransportCtl" })

public class TransportCtl extends BaseCtl {

	Logger log = Logger.getLogger(TransportCtl.class);

	/**
	 * Preload method (optional)
	 */
	@Override
	protected void preload(HttpServletRequest request) {
	}

	/**
	 * Validate Transport form fields
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {

		log.info("TransportCtl validate method started");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("transportId"))) {
			request.setAttribute("transportId", PropertyReader.getValue("error.require", "Transport ID"));
			pass = false;

		} else if (!DataValidator.isModuleCode(request.getParameter("transportId"))) {

			request.setAttribute("transportId",
					"Transport ID must be in format UPPERCASELETTERS-DIGITS (e.g., TR-001)");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("vehicleType"))) {
			request.setAttribute("vehicleType", PropertyReader.getValue("error.require", "Vehicle Type"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("driverName"))) {
			request.setAttribute("driverName", PropertyReader.getValue("error.require", "Driver Name"));
			pass = false;

		} else if (!DataValidator.isName(request.getParameter("driverName"))) {

			request.setAttribute("driverName", "Invalid Driver Name");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("charges"))) {
			request.setAttribute("charges", PropertyReader.getValue("error.require", "Charges"));
			pass = false;

		} else if (!DataValidator.isDouble(request.getParameter("charges"))) {

			request.setAttribute("charges", "Charges must be a valid number");
			pass = false;
		}

		log.info("TransportCtl validate method ended");

		return pass;
	}

	/**
	 * Populate TransportBean from request
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		log.info("TransportCtl populateBean method started");

		TransportBean bean = new TransportBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setTransportId(DataUtility.getString(request.getParameter("transportId")));
		bean.setVehicleType(DataUtility.getString(request.getParameter("vehicleType")));
		bean.setDriverName(DataUtility.getString(request.getParameter("driverName")));
		bean.setCharges(DataUtility.getDouble(request.getParameter("charges")));

		populateDTO(bean, request);

		log.info("TransportCtl populateBean method ended");

		return bean;
	}

	/**
	 * Handles GET request (Edit case)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		log.info("TransportCtl doGet started");

		long id = DataUtility.getLong(req.getParameter("id"));

		TransportModel model = new TransportModel();

		if (id > 0) {

			try {

				TransportBean bean = model.findByPk(id);

				ServletUtility.setBean(bean, req);

			} catch (ApplicationException e) {

				e.printStackTrace();

				ServletUtility.handleException(e, req, resp);

				return;
			}
		}

		log.info("TransportCtl doGet ended");

		ServletUtility.forward(getView(), req, resp);
	}

	/**
	 * Handles POST request
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		log.info("TransportCtl doPost started");

		String op = DataUtility.getString(req.getParameter("operation"));

		long id = DataUtility.getLong(req.getParameter("id"));

		TransportModel model = new TransportModel();

		if (OP_SAVE.equalsIgnoreCase(op)) {

			TransportBean bean = (TransportBean) populateBean(req);

			try {

				model.add(bean);

				ServletUtility.setBean(bean, req);

				ServletUtility.setSuccessMessage("Transport added successfully", req);

			} catch (DuplicateRecordException e) {

				ServletUtility.setBean(bean, req);

				ServletUtility.setErrorMessage("Transport ID already exists", req);

			} catch (ApplicationException e) {

				e.printStackTrace();

				ServletUtility.handleException(e, req, resp);

				return;
			}

		} else if (OP_UPDATE.equalsIgnoreCase(op)) {

			TransportBean bean = (TransportBean) populateBean(req);

			try {

				if (id > 0) {

					model.update(bean);
				}

				ServletUtility.setBean(bean, req);

				ServletUtility.setSuccessMessage("Transport updated successfully", req);

			} catch (DuplicateRecordException e) {

				ServletUtility.setBean(bean, req);

				ServletUtility.setErrorMessage("Transport ID already exists", req);

			} catch (ApplicationException e) {

				e.printStackTrace();

				ServletUtility.handleException(e, req, resp);

				return;
			}

		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.TRANSPORT_CTL, req, resp);

			return;

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.TRANSPORT_LIST_CTL, req, resp);

			return;
		}

		log.info("TransportCtl doPost ended");

		ServletUtility.forward(getView(), req, resp);
	}

	/**
	 * Returns Transport View page
	 */
	@Override
	protected String getView() {

		return ORSView.TRANSPORT_VIEW;
	}
}