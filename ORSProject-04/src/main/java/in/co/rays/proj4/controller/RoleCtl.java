package in.co.rays.proj4.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.RoleBean;
import in.co.rays.proj4.bean.UserBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.exception.DuplicateRecordException;
import in.co.rays.proj4.model.RoleModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.DataValidator;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "RoleCtl", urlPatterns = { "/RoleCtl" })
public class RoleCtl extends BaseCtl {

	@Override
	protected boolean validate(HttpServletRequest request) {

		boolean pass = true;

		if (DataValidator.isNull(request.getParameter("name"))) {
			request.setAttribute("name", PropertyReader.getValue("error.require", "Name is reqired"));
			pass = false;
		} else if (!DataValidator.isName(request.getParameter("name"))) {
			request.setAttribute("name", "Invalid Name");
			pass = false;
		}

		if (DataValidator.isNull(request.getParameter("description"))) {
			request.setAttribute("description", PropertyReader.getValue("error.require", "Description is reqired"));
			pass = false;
		}

		return pass;
	}

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		RoleBean bean = new RoleBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setName(DataUtility.getString(request.getParameter("name")));
		bean.setDescription(DataUtility.getString(request.getParameter("description")));

		populateDTO(bean, request);

		return bean;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		long id = DataUtility.getLong(req.getParameter("id"));
		RoleModel model = new RoleModel();

		if (id > 0) {
			try {
				RoleBean bean = model.findByPk(id);
				ServletUtility.setBean(bean, req);
			} catch (ApplicationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ServletUtility.handleException(e, req, resp);
				return;
			}
		}

		ServletUtility.forward(getView(), req, resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		RoleModel model = new RoleModel();

		String op = DataUtility.getString(req.getParameter("operation"));

		long id = DataUtility.getLong(req.getParameter("id"));

		if (OP_SAVE.equalsIgnoreCase(op)) {
			RoleBean bean = (RoleBean) populateBean(req);

			try {
				model.add(bean);
				ServletUtility.setBean(bean, req);
				ServletUtility.setSuccessMessage("Role Added Successfully", req);
			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, req);
				ServletUtility.setErrorMessage("Role already Exists...!", req);

			} catch (ApplicationException e) {
				// TODO: handle exception
				e.printStackTrace();
				ServletUtility.handleException(e, req, resp);

			}

		} else if (OP_RESET.equalsIgnoreCase(op)) {

			ServletUtility.redirect(ORSView.ROLE_CTL, req, resp);
			return;

		} else if (OP_UPDATE.equalsIgnoreCase(op)) {
			RoleBean bean = (RoleBean) populateBean(req);
			try {
				if (id > 0) {
					model.update(bean);
				}
				ServletUtility.setBean(bean, req);
				ServletUtility.setSuccessMessage("Role updated Successfully", req);

			} catch (DuplicateRecordException e) {
				ServletUtility.setBean(bean, req);
				ServletUtility.setErrorMessage("Role already exists", req);
				// TODO: handle exception
			} catch (ApplicationException e) {
				e.printStackTrace();
				ServletUtility.handleException(e, req, resp);
				return;
				// TODO: handle exception
			}

		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.ROLE_LIST_CTL, req, resp);
			return;

		}

		ServletUtility.forward(getView(), req, resp);
	}

	@Override
	protected String getView() {

		return ORSView.ROLE_VIEW;
	}

}
