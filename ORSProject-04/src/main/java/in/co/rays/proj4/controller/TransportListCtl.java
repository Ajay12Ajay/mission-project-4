/**
 * @Author: Ajay Pratap Kerketta
 * @Description: TransportListCtl is a Servlet controller responsible for handling
 * operations related to listing, searching, deleting, and paginating Transport entities.
 * It extends BaseCtl to leverage common controller functionalities.
 *
 * @Creation Date: 10-Mar-2026
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
import in.co.rays.proj4.bean.TransportBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.model.TransportModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "TransportListCtl", urlPatterns = { "/ctl/TransportListCtl" })

public class TransportListCtl extends BaseCtl {

	Logger log = Logger.getLogger(TransportListCtl.class);

	/**
	 * Preload dynamic filters for transportId, vehicleType, and driverName.
	 */
	@Override
	protected void preload(HttpServletRequest request) {

		log.info("TransportListCtl preload method started");

		TransportModel model = new TransportModel();

		try {

			Iterator<TransportBean> it = model.list().iterator();

			HashMap<String, String> transportIdMap = new HashMap<>();
			HashMap<String, String> vehicleTypeMap = new HashMap<>();
			HashMap<String, String> driverNameMap = new HashMap<>();

			while (it.hasNext()) {

				TransportBean bean = it.next();

				if (bean.getTransportId() != null) {
					transportIdMap.put(bean.getTransportId(), bean.getTransportId());
				}

				if (bean.getVehicleType() != null) {
					vehicleTypeMap.put(bean.getVehicleType(), bean.getVehicleType());
				}

				if (bean.getDriverName() != null) {
					driverNameMap.put(bean.getDriverName(), bean.getDriverName());
				}
			}

			request.setAttribute("transportIdMap", transportIdMap);
			request.setAttribute("vehicleTypeMap", vehicleTypeMap);
			request.setAttribute("driverNameMap", driverNameMap);

		} catch (ApplicationException e) {
			e.printStackTrace();
		}

		log.info("TransportListCtl preload method ended");
	}

	/**
	 * Populate bean for search filters.
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		log.info("TransportListCtl populateBean started");

		TransportBean bean = new TransportBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setTransportId(DataUtility.getString(request.getParameter("transportId")));
		bean.setVehicleType(DataUtility.getString(request.getParameter("vehicleType")));
		bean.setDriverName(DataUtility.getString(request.getParameter("driverName")));

		log.info("TransportListCtl populateBean ended");

		return bean;
	}

	/**
	 * Handles GET request (initial load).
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		log.info("TransportListCtl doGet started");

		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		TransportBean bean = (TransportBean) populateBean(req);
		TransportModel model = new TransportModel();

		try {

			List<TransportBean> list = model.search(bean, pageNo, pageSize);
			List<TransportBean> next = model.search(bean, pageNo + 1, pageSize);

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

		log.info("TransportListCtl doGet ended");
	}

	/**
	 * Handles POST operations (search, pagination, delete).
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		log.info("TransportListCtl doPost started");

		int pageNo = DataUtility.getInt(req.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		pageNo = (pageNo == 0) ? 1 : pageNo;

		TransportBean bean = (TransportBean) populateBean(req);
		TransportModel model = new TransportModel();

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

				ServletUtility.redirect(ORSView.TRANSPORT_CTL, req, resp);
				return;

			} else if (OP_DELETE.equalsIgnoreCase(op)) {

				pageNo = 1;

				if (ids != null && ids.length > 0) {

					for (String id : ids) {

						model.delete(DataUtility.getLong(id));
					}

					ServletUtility.setSuccessMessage("Transport deleted successfully", req);

				} else {

					ServletUtility.setErrorMessage("Select at least 1 record.", req);
				}

			} else if (OP_RESET.equalsIgnoreCase(op) || OP_BACK.equalsIgnoreCase(op)) {

				ServletUtility.redirect(ORSView.TRANSPORT_LIST_CTL, req, resp);
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

		log.info("TransportListCtl doPost ended");

		ServletUtility.forward(getView(), req, resp);
	}

	/**
	 * Returns Transport List View page.
	 */
	@Override
	protected String getView() {

		return ORSView.TRANSPORT_LIST_VIEW;
	}
}