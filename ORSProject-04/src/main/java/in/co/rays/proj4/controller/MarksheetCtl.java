package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.MarksheetBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.MarksheetModel;
import in.co.rays.proj4.model.StudentModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "MarksheetCtl", urlPatterns = { "/MarksheetCtl" })
public class MarksheetCtl extends BaseCtl {

	@Override
	protected void preload(HttpServletRequest request) {
		StudentModel model = new StudentModel();
		try {

			List studentList = model.list();
			request.setAttribute("studentList", studentList);
		} catch (ApplicationException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	@Override
	protected boolean validate(HttpServletRequest request) {
		// TODO Auto-generated method stub

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("studentId"))) {
			request.setAttribute("studentId", PropertyReader.getValue("error.require", "Student Name"));
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("rollNo"))) {
			request.setAttribute("rollNo", PropertyReader.getValue("error.require", "Roll Number"));
			pass = false;
		} else if (!DataValidator.isRollNo(request.getParameter("rollNo"))) {
			request.setAttribute("rollNo", "Roll No is invalid");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("physics"))) {
			request.setAttribute("physics", PropertyReader.getValue("error.require", "Marks"));
			pass = false;
		} else if (DataValidator.isNotNull(request.getParameter("physics"))
				&& !DataValidator.isInteger(request.getParameter("physics"))) {
			request.setAttribute("physics", PropertyReader.getValue("error.integer", "Marks"));
			pass = false;
		} else if (DataUtility.getInt(request.getParameter("physics")) > 100
				|| DataUtility.getInt(request.getParameter("physics")) < 0) {
			request.setAttribute("physics", "Marks should be in 0 to 100");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("chemistry"))) {
			request.setAttribute("chemistry", PropertyReader.getValue("error.require", "Marks"));
			pass = false;
		} else if (DataValidator.isNotNull(request.getParameter("chemistry"))
				&& !DataValidator.isInteger(request.getParameter("chemistry"))) {
			request.setAttribute("chemistry", PropertyReader.getValue("error.integer", "Marks"));
			pass = false;
		} else if (DataUtility.getInt(request.getParameter("chemistry")) > 100
				|| DataUtility.getInt(request.getParameter("chemistry")) < 0) {
			request.setAttribute("chemistry", "Marks should be in 0 to 100");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("maths"))) {
			request.setAttribute("maths", PropertyReader.getValue("error.require", "Marks"));
			pass = false;
		} else if (DataValidator.isNotNull(request.getParameter("maths"))
				&& !DataValidator.isInteger(request.getParameter("maths"))) {
			request.setAttribute("maths", PropertyReader.getValue("error.integer", "Marks"));
			pass = false;
		} else if (DataUtility.getInt(request.getParameter("maths")) > 100
				|| DataUtility.getInt(request.getParameter("maths")) < 0) {
			request.setAttribute("maths", "Marks should be in 0 to 100");
			pass = false;
		}

		return pass;

	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		MarksheetBean bean = new MarksheetBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setRollNo(DataUtility.getString(request.getParameter("rollNo")));
		bean.setName(DataUtility.getString(request.getParameter("name")));
		bean.setStudentId(DataUtility.getLong(request.getParameter("studentId")));

		if (request.getParameter("physics") != null && request.getParameter("physics").length() != 0) {
			bean.setPhysics(DataUtility.getInt(request.getParameter("physics")));
		}
		if (request.getParameter("chemistry") != null && request.getParameter("chemistry").length() != 0) {
			bean.setChemistry(DataUtility.getInt(request.getParameter("chemistry")));
		}
		if (request.getParameter("maths") != null && request.getParameter("maths").length() != 0) {
			bean.setMaths(DataUtility.getInt(request.getParameter("maths")));
		}

		populateDTO(bean, request);

		return bean;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		long id = DataUtility.getLong(req.getParameter("id"));

		MarksheetModel model = new MarksheetModel();

		if (id > 0) {
			try {
				MarksheetBean bean = model.findByPk(id);
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
		// TODO Auto-generated method stub
		String op = DataUtility.getString(req.getParameter("operation"));
		long id = DataUtility.getLong(req.getParameter("id"));
		MarksheetModel model = new MarksheetModel();

		if (OP_SAVE.equalsIgnoreCase(op)) {
			MarksheetBean bean = (MarksheetBean) populateBean(req);

			try {
				System.out.println(bean);
				model.add(bean);
				ServletUtility.setBean(bean, req);
				ServletUtility.setSuccessMessage("Marksheet added succesfully", req);

			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, req);
				ServletUtility.setErrorMessage("Marksheet name already exist", req);
			} catch (ApplicationException e) {
				// TODO: handle exception
				e.printStackTrace();
				ServletUtility.handleException(e, req, resp);
				return;
			}

		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.MARKSHEET_CTL, req, resp);
			return;
		} else if (OP_UPDATE.equalsIgnoreCase(op)) {
			MarksheetBean bean = (MarksheetBean) populateBean(req);
			try {
				if (id > 0) {
					model.update(bean);
				}
				ServletUtility.setBean(bean, req);
				ServletUtility.setSuccessMessage("Marksheet updated successfully", req);
			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, req);
				ServletUtility.setErrorMessage("Roll No already exists", req);
			} catch (ApplicationException e) {
				e.printStackTrace();
				ServletUtility.handleException(e, req, resp);
				return;
			}
		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.MARKSHEET_LIST_CTL, req, resp);
			return;
		}

		ServletUtility.forward(getView(), req, resp);
	}

	@Override
	protected String getView() {
		// TODO Auto-generated method stub
		return ORSView.MARKSHEET_VIEW;
	}

}
