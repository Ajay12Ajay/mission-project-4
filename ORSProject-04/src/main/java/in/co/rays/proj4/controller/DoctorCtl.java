package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.DoctorBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.DoctorModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "DoctorCtl", urlPatterns = { "/ctl/DoctorCtl" })
public class DoctorCtl extends BaseCtl {
	
	@Override
	protected void preload(HttpServletRequest request) {
		// TODO Auto-generated method stub
		HashMap<String, String> experticeMap = new HashMap<String, String>();
		experticeMap.put("Cardiology", "Cardiology");
		experticeMap.put("Neurology", "Neurology");
		experticeMap.put("Pulmonology", "Pulmonology");
		experticeMap.put("Gastroenterology", "Gastroenterology");
		experticeMap.put("Endocrinology", "Endocrinology");
		experticeMap.put("Dermatology", "Dermatology");
		experticeMap.put("Pediatrics", "Pediatrics");
		experticeMap.put("Oncology", "Oncology");
		experticeMap.put("Psychiatry", "Psychiatry");
		experticeMap.put("Orthopedics", "Orthopedics");
	
	  request.setAttribute("experticeMap", experticeMap);

		
		
	}

	@Override
	protected boolean validate(HttpServletRequest request) {

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("name"))) {
			request.setAttribute("name", PropertyReader.getValue("error.require", "Name"));
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("name"))) {
			request.setAttribute("name", "Invalid Name");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("dob"))) {
			request.setAttribute("dob", PropertyReader.getValue("error.require", "Date of Birth"));
			pass = false;
		} else if (!DataValidator.isDate(request.getParameter("dob"))) {
			request.setAttribute("dob", PropertyReader.getValue("error.date", "Date of Birth"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("mobile"))) {
			request.setAttribute("mobile", PropertyReader.getValue("error.require", "MobileNo"));
			pass = false;
		} else if (!DataValidator.isPhoneLength(request.getParameter("mobile"))) {
			request.setAttribute("mobile", "Mobile No must have 10 digits");
			pass = false;
		} else if (!DataValidator.isPhoneNo(request.getParameter("mobile"))) {
			request.setAttribute("mobile", "Invalid Mobile No");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("expertise"))) {
			request.setAttribute("expertise", PropertyReader.getValue("error.require", "Expertise"));
			pass = false;
		}

		return pass;

	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		// TODO Auto-generated method stub
		DoctorBean bean = new DoctorBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setName(DataUtility.getString(request.getParameter("name")));
		bean.setDob(DataUtility.getDate(request.getParameter("dob")));
		bean.setMobile(DataUtility.getString(request.getParameter("mobile")));
		bean.setExpertise(DataUtility.getString(request.getParameter("expertise")));

		populateDTO(bean, request);

		return bean;

	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		long id = DataUtility.getLong(req.getParameter("id"));

		DoctorModel model = new DoctorModel();

		if (id > 0) {
			try {
				DoctorBean bean = model.findByPk(id);
				ServletUtility.setBean(bean, req);
			} catch (ApplicationException e) {
				e.printStackTrace();
				ServletUtility.handleException(e, req, resp);
				return;
			}
		}
		// TODO Auto-generated method stub
		ServletUtility.forward(getView(), req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String op = DataUtility.getString(req.getParameter("operation"));
		
		long id = DataUtility.getLong(req.getParameter("id"));
		
		DoctorModel model = new DoctorModel();

		if (OP_SAVE.equalsIgnoreCase(op)) {
			DoctorBean bean = (DoctorBean) populateBean(req);

			try {
				model.add(bean);
				ServletUtility.setBean(bean, req);
				ServletUtility.setSuccessMessage("Doctor added successfully", req);
			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, req);
				ServletUtility.setErrorMessage("Doctor already exists", req);
				// TODO Auto-generated catch block

			} catch (ApplicationException e) {
				e.printStackTrace();
				ServletUtility.handleException(e, req, resp);
				return;
				// TODO: handle exception
			}

		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.DOCTOR_CTL, req, resp);
			return;
		} else if (OP_UPDATE.equalsIgnoreCase(op)) {
			DoctorBean bean = (DoctorBean) populateBean(req);
			try {
				if (id > 0) {
					model.update(bean);
				}
				ServletUtility.setBean(bean, req);
				ServletUtility.setSuccessMessage("Doctor updated successfully", req);
			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, req);
				ServletUtility.setErrorMessage("Doctor already exists", req);
			} catch (ApplicationException e) {
				e.printStackTrace();
				ServletUtility.handleException(e, req, resp);
				return;
			}
		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.DOCTOR_LIST_CTL, req, resp);
			return;
		}

		// TODO Auto-generated method stub
		ServletUtility.forward(getView(), req, resp);
	}

	@Override
	protected String getView() {
		// TODO Auto-generated method stub
		return ORSView.DOCTOR_VIEW;
	}

}
