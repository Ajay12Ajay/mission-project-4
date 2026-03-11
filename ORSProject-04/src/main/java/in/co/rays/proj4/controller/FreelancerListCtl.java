/**
 * @Author: Ajay Pratap Kerketta
 * @Description: FreelancerListCtl is a Servlet controller responsible for handling
 * operations related to listing, searching, deleting, and paginating Freelancer entities.
 * It extends BaseCtl to leverage common controller functionalities.
 *
 * @Creation Date: 11-Mar-2026
 * @Version: 1.0
 */

package in.co.rays.proj4.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.proj4.bean.BaseBean;
import in.co.rays.proj4.bean.FreelancerBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.model.FreelancerModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "FreelancerListCtl", urlPatterns = { "/ctl/FreelancerListCtl" })

public class FreelancerListCtl extends BaseCtl {

	Logger log = Logger.getLogger(FreelancerListCtl.class);

	/**
	 * Preload dynamic filters for freelancerName, projectName and deadline.
	 */
	@Override
	protected void preload(HttpServletRequest request) {

		log.info("FreelancerListCtl preload method started");

		FreelancerModel model = new FreelancerModel();

		try {

			Iterator<FreelancerBean> it = model.list().iterator();

			HashMap<String, String> freelancerNameMap = new HashMap<>();
			HashMap<String, String> projectNameMap = new HashMap<>();
			HashMap<String, String> deadlineMap = new HashMap<>();

			while (it.hasNext()) {

				FreelancerBean bean = it.next();

				if (bean.getFreelancerName() != null) {
					freelancerNameMap.put(bean.getFreelancerName(), bean.getFreelancerName());
				}

				if (bean.getProjectName() != null) {
					projectNameMap.put(bean.getProjectName(), bean.getProjectName());
				}

				if (bean.getDeadline() != null) {

					String d = DataUtility.getDateString(bean.getDeadline());

					deadlineMap.put(d, d);
				}
			}

			request.setAttribute("freelancerNameMap", freelancerNameMap);
			request.setAttribute("projectNameMap", projectNameMap);
			request.setAttribute("deadlineMap", deadlineMap);

		} catch (ApplicationException e) {

			e.printStackTrace();
		}

		log.info("FreelancerListCtl preload method ended");
	}

	/**
	 * Populate bean for search filters.
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		log.info("FreelancerListCtl populateBean started");

		FreelancerBean bean = new FreelancerBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setFreelancerName(DataUtility.getString(request.getParameter("freelancerName")));
		bean.setProjectName(DataUtility.getString(request.getParameter("projectName")));
		bean.setDeadline(DataUtility.getDate(request.getParameter("deadline")));

		log.info("FreelancerListCtl populateBean ended");

		return bean;
	}

	/**
	 * Handles GET request (initial load).
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		log.info("FreelancerListCtl doGet started");

		int pageNo = 1;

		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		FreelancerBean bean = (FreelancerBean) populateBean(req);

		FreelancerModel model = new FreelancerModel();

		try {

			List<FreelancerBean> list = model.search(bean, pageNo, pageSize);

			List<FreelancerBean> next = model.search(bean, pageNo + 1, pageSize);

			if (list == null || list.isEmpty()) {

				ServletUtility.setErrorMessage("No record found", req);
			}

			ServletUtility.setList(list, req);

			ServletUtility.setPageNo(pageNo, req);

			ServletUtility.setPageSize(pageSize, req);

			ServletUtility.setBean(bean, req);

			req.setAttribute("nextListSize", next.size());

			ServletUtility.forward(getView(), req, resp);

		} catch (ApplicationException e) {

			e.printStackTrace();

			ServletUtility.handleException(e, req, resp);
		}

		log.info("FreelancerListCtl doGet ended");
	}

	/**
	 * Handles POST operations (search, pagination, delete).
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		log.info("FreelancerListCtl doPost started");

		int pageNo = DataUtility.getInt(req.getParameter("pageNo"));

		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		pageNo = (pageNo == 0) ? 1 : pageNo;

		FreelancerBean bean = (FreelancerBean) populateBean(req);

		FreelancerModel model = new FreelancerModel();

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

				ServletUtility.redirect(ORSView.FREELANCER_CTL, req, resp);

				return;

			} else if (OP_DELETE.equalsIgnoreCase(op)) {

				pageNo = 1;

				if (ids != null && ids.length > 0) {

					for (String id : ids) {

						model.delete(DataUtility.getLong(id));
					}

					ServletUtility.setSuccessMessage("Freelancer record deleted successfully", req);

				} else {

					ServletUtility.setErrorMessage("Select at least 1 record.", req);
				}

			} else if (OP_RESET.equalsIgnoreCase(op) || OP_BACK.equalsIgnoreCase(op)) {

				ServletUtility.redirect(ORSView.FREELANCER_LIST_CTL, req, resp);

				return;
			}

			List list = model.search(bean, pageNo, pageSize);

			List next = model.search(bean, pageNo + 1, pageSize);

			if (list == null || list.isEmpty()) {

				ServletUtility.setErrorMessage("No record found", req);
			}

			ServletUtility.setBean(bean, req);

			ServletUtility.setList(list, req);

			ServletUtility.setPageNo(pageNo, req);

			ServletUtility.setPageSize(pageSize, req);

			req.setAttribute("nextListSize", next.size());

		} catch (ApplicationException e) {

			e.printStackTrace();

			ServletUtility.handleException(e, req, resp);

			return;
		}

		log.info("FreelancerListCtl doPost ended");

		ServletUtility.forward(getView(), req, resp);
	}

	/**
	 * Returns Freelancer List View page.
	 */
	@Override
	protected String getView() {

		return ORSView.FREELANCER_LIST_VIEW;
	}
}