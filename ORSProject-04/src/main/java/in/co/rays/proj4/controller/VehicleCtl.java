/**
 * @Author: Ajay Pratap Kerketta
 * @Description: VehicleCtl is a Servlet controller responsible for handling
 * operations related to Vehicle management such as adding, updating,
 * and validating vehicle data. It extends BaseCtl to inherit common
 * controller functionalities like validation and bean population.
 *
 * @Creation Date: 09-Mar-2026
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
import in.co.rays.proj4.bean.VehicleBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.VehicleModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "VehicleCtl", urlPatterns = { "/ctl/VehicleCtl" })

public class VehicleCtl extends BaseCtl {

	Logger log = Logger.getLogger(VehicleCtl.class);

	/**
	 * Preload method
	 */
	@Override
	protected void preload(HttpServletRequest request) {
	}

	/**
	 * Validate Vehicle form
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {

		log.info("VehicleCtl validate method started");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("vehicleNumber"))) {

			request.setAttribute("vehicleNumber",
					PropertyReader.getValue("error.require", "Vehicle Number"));

			pass = false;

		} else if (!DataValidator.isModuleCode(request.getParameter("vehicleNumber"))) {

			request.setAttribute("vehicleNumber",
					"Vehicle Number must be in format UPPERCASELETTERS-DIGITS (e.g., VH-001)");

			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("ownerName"))) {

			request.setAttribute("ownerName",
					PropertyReader.getValue("error.require", "Owner Name"));

			pass = false;

		} else if (!DataValidator.isName(request.getParameter("ownerName"))) {

			request.setAttribute("ownerName", "Invalid Owner Name");

			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("serviceDate"))) {

			request.setAttribute("serviceDate",
					PropertyReader.getValue("error.require", "Service Date"));

			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("serviceType"))) {

			request.setAttribute("serviceType",
					PropertyReader.getValue("error.require", "Service Type"));

			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("cost"))) {

			request.setAttribute("cost",
					PropertyReader.getValue("error.require", "Cost"));

			pass = false;

		} else if (!DataValidator.isDouble(request.getParameter("cost"))) {

			request.setAttribute("cost", "Cost must be a valid number");

			pass = false;
		}

		log.info("VehicleCtl validate method ended");

		return pass;
	}

	/**
	 * Populate VehicleBean from request
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		log.info("VehicleCtl populateBean method started");

		VehicleBean bean = new VehicleBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setVehicleNumber(DataUtility.getString(request.getParameter("vehicleNumber")));
		bean.setOwnerName(DataUtility.getString(request.getParameter("ownerName")));
		bean.setServiceDate(DataUtility.getDate(request.getParameter("serviceDate")));
		bean.setServiceType(DataUtility.getString(request.getParameter("serviceType")));
		bean.setCost(DataUtility.getDouble(request.getParameter("cost")));

		populateDTO(bean, request);

		log.info("VehicleCtl populateBean method ended");

		return bean;
	}

	/**
	 * Handles GET request (Edit case)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		log.info("VehicleCtl doGet started");

		long id = DataUtility.getLong(req.getParameter("id"));

		VehicleModel model = new VehicleModel();

		if (id > 0) {

			try {

				VehicleBean bean = model.findByPk(id);

				ServletUtility.setBean(bean, req);

			} catch (ApplicationException e) {

				e.printStackTrace();

				ServletUtility.handleException(e, req, resp);

				return;
			}
		}

		log.info("VehicleCtl doGet ended");

		ServletUtility.forward(getView(), req, resp);
	}

	/**
	 * Handles POST request
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		log.info("VehicleCtl doPost started");

		String op = DataUtility.getString(req.getParameter("operation"));

		long id = DataUtility.getLong(req.getParameter("id"));

		VehicleModel model = new VehicleModel();

		if (OP_SAVE.equalsIgnoreCase(op)) {

			VehicleBean bean = (VehicleBean) populateBean(req);

			try {

				model.add(bean);

				ServletUtility.setBean(bean, req);

				ServletUtility.setSuccessMessage("Vehicle added successfully", req);

			} catch (DuplicateRecordException e) {

				ServletUtility.setBean(bean, req);

				ServletUtility.setErrorMessage("Vehicle Number already exists", req);

			} catch (ApplicationException e) {

				e.printStackTrace();

				ServletUtility.handleException(e, req, resp);

				return;
			}

		} else if (OP_UPDATE.equalsIgnoreCase(op)) {

			VehicleBean bean = (VehicleBean) populateBean(req);

			try {

				if (id > 0) {

					model.update(bean);
				}

				ServletUtility.setBean(bean, req);

				ServletUtility.setSuccessMessage("Vehicle updated successfully", req);

			} catch (DuplicateRecordException e) {

				ServletUtility.setBean(bean, req);

				ServletUtility.setErrorMessage("Vehicle Number already exists", req);

			} catch (ApplicationException e) {

				e.printStackTrace();

				ServletUtility.handleException(e, req, resp);

				return;
			}

		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.VEHICLE_CTL, req, resp);

			return;

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.VEHICLE_LIST_CTL, req, resp);

			return;
		}

		log.info("VehicleCtl doPost ended");

		ServletUtility.forward(getView(), req, resp);
	}

	/**
	 * Returns Vehicle View page
	 */
	@Override
	protected String getView() {

		return ORSView.VEHICLE_VIEW;
	}
}