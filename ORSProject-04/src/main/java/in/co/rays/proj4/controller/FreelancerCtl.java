/**
 * @Author: Ajay Pratap Kerketta
 * @Description: FreelancerCtl is a Servlet controller responsible for handling
 * operations related to Freelancer management such as adding, updating,
 * and validating freelancer project data. It extends BaseCtl to inherit common
 * controller functionalities like validation and bean population.
 *
 * @Creation Date: 11-Mar-2026
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
import in.co.rays.proj4.bean.FreelancerBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.FreelancerModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "FreelancerCtl", urlPatterns = { "/ctl/FreelancerCtl" })

public class FreelancerCtl extends BaseCtl {

	Logger log = Logger.getLogger(FreelancerCtl.class);

	/**
	 * Preload method
	 */
	@Override
	protected void preload(HttpServletRequest request) {
	}

	/**
	 * Validate Freelancer form fields
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {

		log.info("FreelancerCtl validate method started");

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("freelancerName"))) {

			request.setAttribute("freelancerName", PropertyReader.getValue("error.require", "Freelancer Name"));
			pass = false;

		} else if (!DataValidator.isName(request.getParameter("freelancerName"))) {

			request.setAttribute("freelancerName", "Invalid Freelancer Name");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("projectName"))) {

			request.setAttribute("projectName", PropertyReader.getValue("error.require", "Project Name"));
			pass = false;
		} /*
			 * else if (!DataValidator.isModuleCode(request.getParameter("projectName"))) {
			 * 
			 * request.setAttribute("projectName",
			 * "Project Name  must be in format UPPERCASELETTERS-DIGITS (e.g., TR-001)");
			 * pass = false; }
			 */

		if (DataValidator.isNull(request.getParameter("deadline"))) {

			request.setAttribute("deadline", PropertyReader.getValue("error.require", "Deadline"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("paymentAmount"))) {

			request.setAttribute("paymentAmount", PropertyReader.getValue("error.require", "Payment Amount"));
			pass = false;

		} else if (!DataValidator.isDouble(request.getParameter("paymentAmount"))) {

			request.setAttribute("paymentAmount", "Payment Amount must be a valid number");
			pass = false;
		}

		log.info("FreelancerCtl validate method ended");

		return pass;
	}

	/**
	 * Populate FreelancerBean from request
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		log.info("FreelancerCtl populateBean method started");

		FreelancerBean bean = new FreelancerBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setFreelancerName(DataUtility.getString(request.getParameter("freelancerName")));
		bean.setProjectName(DataUtility.getString(request.getParameter("projectName")));
		bean.setDeadline(DataUtility.getDate(request.getParameter("deadline")));
		bean.setPaymentAmount(DataUtility.getDouble(request.getParameter("paymentAmount")));

		populateDTO(bean, request);

		log.info("FreelancerCtl populateBean method ended");

		return bean;
	}

	/**
	 * Handles GET request (Edit case)
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		log.info("FreelancerCtl doGet started");

		long id = DataUtility.getLong(req.getParameter("id"));

		FreelancerModel model = new FreelancerModel();

		if (id > 0) {

			try {

				FreelancerBean bean = model.findByPk(id);

				ServletUtility.setBean(bean, req);

			} catch (ApplicationException e) {

				e.printStackTrace();

				ServletUtility.handleException(e, req, resp);

				return;
			}
		}

		log.info("FreelancerCtl doGet ended");

		ServletUtility.forward(getView(), req, resp);
	}

	/**
	 * Handles POST request
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		log.info("FreelancerCtl doPost started");

		String op = DataUtility.getString(req.getParameter("operation"));

		long id = DataUtility.getLong(req.getParameter("id"));

		FreelancerModel model = new FreelancerModel();

		if (OP_SAVE.equalsIgnoreCase(op)) {

			FreelancerBean bean = (FreelancerBean) populateBean(req);

			try {

				model.add(bean);

				ServletUtility.setBean(bean, req);

				ServletUtility.setSuccessMessage("Freelancer record added successfully", req);

			} catch (DuplicateRecordException e) {

				ServletUtility.setBean(bean, req);

				ServletUtility.setErrorMessage("Project Name already exists", req);

			} catch (ApplicationException e) {

				e.printStackTrace();

				ServletUtility.handleException(e, req, resp);

				return;
			}

		} else if (OP_UPDATE.equalsIgnoreCase(op)) {

			FreelancerBean bean = (FreelancerBean) populateBean(req);

			try {

				if (id > 0) {

					model.update(bean);
				}

				ServletUtility.setBean(bean, req);

				ServletUtility.setSuccessMessage("Freelancer record updated successfully", req);

			} catch (DuplicateRecordException e) {

				ServletUtility.setBean(bean, req);

				ServletUtility.setErrorMessage("Project Name already exists", req);

			} catch (ApplicationException e) {

				e.printStackTrace();

				ServletUtility.handleException(e, req, resp);

				return;
			}

		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.FREELANCER_CTL, req, resp);
			return;

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.FREELANCER_LIST_CTL, req, resp);
			return;
		}

		log.info("FreelancerCtl doPost ended");

		ServletUtility.forward(getView(), req, resp);
	}

	/**
	 * Returns Freelancer View page
	 */
	@Override
	protected String getView() {

		return ORSView.FREELANCER_VIEW;
	}
}