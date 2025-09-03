package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.MarksheetBean;
import in.co.rays.proj4.bean.SubjectBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.model.MarksheetModel;
import in.co.rays.proj4.model.SubjectModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "MarksheetListCtl", urlPatterns = { "/MarksheetListCtl" })
public class MarksheetListCtl extends BaseCtl {

	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		// TODO Auto-generated method stub
		MarksheetBean bean = new MarksheetBean();

		//bean.setId(DataUtility.getLong(request.getParameter("id")));

		bean.setName(DataUtility.getString(request.getParameter("name")));

		bean.setRollNo(DataUtility.getString(request.getParameter("rollNo")));

		return bean;
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		MarksheetBean bean = new MarksheetBean();
		MarksheetModel model = new MarksheetModel();

		try {
			List<MarksheetBean> list = model.search(bean, pageNo, pageSize);
			List<MarksheetBean> next = model.search(bean, pageNo + 1, pageSize);

			if (list == null || list.isEmpty()) {
				ServletUtility.setErrorMessage("no record Found", req);

			}

			ServletUtility.setList(list, req);
			ServletUtility.setPageNo(pageNo, req);
			ServletUtility.setPageSize(pageSize, req);
			ServletUtility.setBean(bean, req);
			req.setAttribute("nextListSize", next.size());

			ServletUtility.forward(getView(), req, resp);

		} catch (ApplicationException e) {
			// TODO: handle exception
			e.printStackTrace();
			ServletUtility.handleException(e, req, resp);
			return;
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// TODO Auto-generated method stub
		List list = null;
		List next = null;

		int pageNo = DataUtility.getInt(req.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(PropertyReader.getValue("pageSize"));

		MarksheetBean bean = (MarksheetBean) populateBean(req);
		MarksheetModel model = new MarksheetModel();

		String op = DataUtility.getString(req.getParameter("operation"));
		String[] ids = req.getParameterValues("ids");

		try {
			if (OP_SEARCH.equalsIgnoreCase(op) || OP_NEXT.equalsIgnoreCase(op) || OP_PREVIOUS.equalsIgnoreCase(op)) {

				if (OP_SEARCH.equalsIgnoreCase(op)) {
					pageNo = 1;
				} else if (OP_NEXT.equalsIgnoreCase(op)) {
					pageNo++;
				} else if (OP_PREVIOUS.equalsIgnoreCase(op)) {
					pageNo--;
				}
			} else if (OP_NEW.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.MARKSHEET_CTL, req, resp);
				return;
			} else if (OP_DELETE.equalsIgnoreCase(op)) {
				pageNo = 1;
				if (ids != null && ids.length > 0) {
					MarksheetBean deletebean = new MarksheetBean();
					for (String id : ids) {
						deletebean.setId(DataUtility.getInt(id));
						model.delete(deletebean.getId());
						ServletUtility.setSuccessMessage("Role deleted successfully", req);
					}
				} else {
					ServletUtility.setErrorMessage("select atleast 1 id..", req);

				}

			} else if (OP_RESET.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.MARKSHEET_LIST_CTL, req, resp);
				return;
			} else if (OP_BACK.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.MARKSHEET_LIST_CTL, req, resp);
				return;
			}

			list = model.search(bean, pageNo, pageSize);
			next = model.search(bean, pageNo + 1, pageSize);

			if (list == null || list.size() == 0) {
				ServletUtility.setErrorMessage("No record found", req);
			}

			ServletUtility.setBean(bean, req);
			ServletUtility.setList(list, req);
			ServletUtility.setPageNo(pageNo, req);
			ServletUtility.setPageSize(pageSize, req);
			req.setAttribute("nextListSize", req);

		} catch (ApplicationException e) {
			// TODO: handle exception
			e.printStackTrace();
			ServletUtility.handleException(e, req, resp);
			return;
		}

		// TODO Auto-generated method stub
		ServletUtility.forward(getView(), req, resp);
	}

	@Override
	protected String getView() {
		// TODO Auto-generated method stub
		return ORSView.MARKSHEET_LIST_VIEW;
	}

}
