package in.co.rays.proj4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.CollegeBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.CollegeModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "CollegeCtl", urlPatterns = { "/CollegeCtl" })
public class CollegeCtl extends BaseCtl {

	@Override
	protected boolean validate(HttpServletRequest request) {

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("name"))) {
			request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("name"))) {
			request.setAttribute("name", "Invalid name");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("address"))) {
			request.setAttribute("address", PropertyReader.getValue("error.require", "Address"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("state"))) {
			request.setAttribute("state", PropertyReader.getValue("error.require", "State"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("city"))) {
			request.setAttribute("city", PropertyReader.getValue("error.require", "City"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("phoneNo"))) {
			request.setAttribute("phoneNo", PropertyReader.getValue("error.require", "PhoneNo"));
			pass = false;
		} else if (!DataValidator.isPhoneLength(request.getParameter("phoneNo"))) {
			request.setAttribute("phoneNo", "Phone must have 10 digits");
			pass = false;
		} else if (!DataValidator.isPhoneNo(request.getParameter("phoneNo"))) {
			request.setAttribute("phoneNo", "Invalid PhoneNo");
			pass = false;
		}
		// TODO Auto-generated method stub
		return pass;
	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		CollegeBean bean = new CollegeBean();
		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setName(DataUtility.getString(request.getParameter("name")));
		bean.setAddress(DataUtility.getString(request.getParameter("address")));
		bean.setState(DataUtility.getString(request.getParameter("state")));
		bean.setCity(DataUtility.getString(request.getParameter("city")));
		bean.setPhoneNo(DataUtility.getString(request.getParameter("phoneNo")));

		populateDTO(bean, request);

		return bean;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		long id = DataUtility.getLong(req.getParameter("id"));

		CollegeModel model = new CollegeModel();

		if (id > 0) {
			try {
				CollegeBean bean = model.findByPk(id);
				ServletUtility.setBean(bean, req);
			} catch (ApplicationException e) {
				e.printStackTrace();
				ServletUtility.handleException(e, req, resp);
				return;
			}
		}
		ServletUtility.forward(getView(), req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		String op = DataUtility.getString(req.getParameter("operation"));
		CollegeModel model = new CollegeModel();
		long id = DataUtility.getLong(req.getParameter("id"));

		if (OP_SAVE.equalsIgnoreCase(op)) {
			CollegeBean bean = (CollegeBean) populateBean(req);

			try {
				model.add(bean);
				ServletUtility.setBean(bean, req);
				ServletUtility.setSuccessMessage("College Added successfully", req);
			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, req);
				ServletUtility.setErrorMessage("College Name already exists", req);
				// TODO: handle exception
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ServletUtility.handleException(e, req, resp);
				return;
			}
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.COLLEGE_CTL, req, resp);
			return;
		} else if (OP_UPDATE.equalsIgnoreCase(op)) {
			CollegeBean bean = (CollegeBean) populateBean(req);
			try {
				if (id > 0) {
					model.update(bean);
				}
				ServletUtility.setBean(bean, req);
				ServletUtility.setSuccessMessage("Data is successfully updated", req);
			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, req);
				ServletUtility.setErrorMessage("College Name already exists", req);
			} catch (ApplicationException e) {
				e.printStackTrace();
				ServletUtility.handleException(e, req, resp);
				return;
			}
		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.COLLEGE_LIST_CTL, req, resp);
			return;
		}

		// TODO Auto-generated method stub
		ServletUtility.forward(getView(), req, resp);
	}

	@Override
	protected String getView() {
		// TODO Auto-generated method stub
		return ORSView.COLLEGE_VIEW;
	}

}
