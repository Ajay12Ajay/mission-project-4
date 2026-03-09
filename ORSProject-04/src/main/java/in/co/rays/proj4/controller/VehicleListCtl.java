/**
 * @Author: Ajay Pratap Kerketta
 * @Description: VehicleListCtl is a Servlet controller responsible for handling
 * operations related to listing, searching, deleting, and paginating Vehicle entities.
 * It extends BaseCtl to leverage common controller functionalities.
 *
 * @Creation Date: 09-Mar-2026
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
import in.co.rays.proj4.bean.VehicleBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.model.VehicleModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "VehicleListCtl", urlPatterns = { "/ctl/VehicleListCtl" })

public class VehicleListCtl extends BaseCtl {

	Logger log = Logger.getLogger(VehicleListCtl.class);

	/**
	 * Preload dynamic filters for vehicleNumber, ownerName, serviceDate.
	 */
	@Override
	protected void preload(HttpServletRequest request) {

		log.info("VehicleListCtl preload method started");

		VehicleModel model = new VehicleModel();

		try {

			Iterator<VehicleBean> it = model.list().iterator();

			HashMap<String, String> vehicleNumberMap = new HashMap<>();
			HashMap<String, String> ownerNameMap = new HashMap<>();
			HashMap<String, String> serviceDateMap = new HashMap<>();

			while (it.hasNext()) {

				VehicleBean bean = it.next();

				if (bean.getVehicleNumber() != null) {
					vehicleNumberMap.put(bean.getVehicleNumber(), bean.getVehicleNumber());
				}

				if (bean.getOwnerName() != null) {
					ownerNameMap.put(bean.getOwnerName(), bean.getOwnerName());
				}

				if (bean.getServiceDate() != null) {
					String date = DataUtility.getDateString(bean.getServiceDate());
					serviceDateMap.put(date, date);
				}
			}

			request.setAttribute("vehicleNumberMap", vehicleNumberMap);
			request.setAttribute("ownerNameMap", ownerNameMap);
			request.setAttribute("serviceDateMap", serviceDateMap);

		} catch (ApplicationException e) {
			e.printStackTrace();
		}

		log.info("VehicleListCtl preload method ended");
	}

	/**
	 * Populate bean for search filters.
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {

		log.info("VehicleListCtl populateBean started");

		VehicleBean bean = new VehicleBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setVehicleNumber(DataUtility.getString(request.getParameter("vehicleNumber")));
		bean.setOwnerName(DataUtility.getString(request.getParameter("ownerName")));
		bean.setServiceDate(DataUtility.getDate(request.getParameter("serviceDate")));

		log.info("VehicleListCtl populateBean ended");

		return bean;
	}

	/**
	 * Handles GET request (initial load).
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		log.info("VehicleListCtl doGet started");

		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		VehicleBean bean = (VehicleBean) populateBean(req);
		VehicleModel model = new VehicleModel();

		try {

			List<VehicleBean> list = model.search(bean, pageNo, pageSize);
			List<VehicleBean> next = model.search(bean, pageNo + 1, pageSize);

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

		log.info("VehicleListCtl doGet ended");
	}

	/**
	 * Handles POST operations (search, pagination, delete).
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		log.info("VehicleListCtl doPost started");

		int pageNo = DataUtility.getInt(req.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		pageNo = (pageNo == 0) ? 1 : pageNo;

		VehicleBean bean = (VehicleBean) populateBean(req);
		VehicleModel model = new VehicleModel();

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

				ServletUtility.redirect(ORSView.VEHICLE_CTL, req, resp);
				return;

			} else if (OP_DELETE.equalsIgnoreCase(op)) {

				pageNo = 1;

				if (ids != null && ids.length > 0) {

					for (String id : ids) {

						model.delete(DataUtility.getLong(id));
					}

					ServletUtility.setSuccessMessage("Vehicle deleted successfully", req);

				} else {

					ServletUtility.setErrorMessage("Select at least 1 record.", req);
				}

			} else if (OP_RESET.equalsIgnoreCase(op) || OP_BACK.equalsIgnoreCase(op)) {

				ServletUtility.redirect(ORSView.VEHICLE_LIST_CTL, req, resp);
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

		log.info("VehicleListCtl doPost ended");

		ServletUtility.forward(getView(), req, resp);
	}

	/**
	 * Returns Vehicle List View page.
	 */
	@Override
	protected String getView() {

		return ORSView.VEHICLE_LIST_VIEW;
	}
}