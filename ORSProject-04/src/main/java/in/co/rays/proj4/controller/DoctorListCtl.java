/**
 * @Author: Ajay Pratap Kerketta
 * @Description: DoctorListCtl is a Servlet controller class to manage listing, searching,
 * pagination, and deletion of Doctor entities. It extends BaseCtl to inherit common
 * controller functionality.
 * 
 * @Creation Date: 07-Oct-2025
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
import in.co.rays.proj4.bean.DoctorBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.model.DoctorModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;
import in.co.rays.proj4.controller.ORSView;

/**
 * DoctorListCtl servlet handles operations related to listing, searching,
 * pagination, and deletion of doctors.
 */
@WebServlet(name = "DoctorListCtl", urlPatterns = { "/ctl/DoctorListCtl" })
public class DoctorListCtl extends BaseCtl {
	Logger log = Logger.getLogger(DoctorListCtl.class);

    /**
     * Loads preloaded data into the request scope before displaying the list.
     * It loads all doctor expertise fields into a map.
     * 
     * @param request HttpServletRequest object
     */
    @Override
    protected void preload(HttpServletRequest request) {
    	log.info("DoctorListCtl validate Method Started");
        DoctorModel model = new DoctorModel();
        try {
            Iterator it = model.list().iterator();
            HashMap<String, String> expertiseMap = new HashMap<>();
            while (it.hasNext()) {
                DoctorBean bean = (DoctorBean) it.next();
                expertiseMap.put(bean.getExpertise(), bean.getExpertise());
            }
            request.setAttribute("expertiseMap", expertiseMap);
        } catch (ApplicationException e) {
            e.printStackTrace();
        }
        log.info("DoctorListCtl validate Method Ended");
    }

    /**
     * Populates a DoctorBean from request parameters.
     * 
     * @param request HttpServletRequest object
     * @return populated DoctorBean
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {
        DoctorBean bean = new DoctorBean();

        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setName(DataUtility.getString(request.getParameter("name")));
        bean.setDob(DataUtility.getDate(request.getParameter("dob")));
        bean.setMobile(DataUtility.getString(request.getParameter("mobile")));
        bean.setExpertise(DataUtility.getString(request.getParameter("expertise")));

        return bean;
    }

    /**
     * Handles HTTP GET requests to display the doctor list with pagination.
     * 
     * @param req  HttpServletRequest object
     * @param resp HttpServletResponse object
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	log.info("DoctorListCtl doGet Method Started");
    	int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

        DoctorBean bean = (DoctorBean) populateBean(req);
        DoctorModel model = new DoctorModel();

        try {
            List<DoctorBean> list = model.search(bean, pageNo, pageSize);
            List<DoctorBean> next = model.search(bean, pageNo + 1, pageSize);

            if (list == null || list.isEmpty()) {
                ServletUtility.setErrorMessage("No record found", req);
            }

            ServletUtility.setList(list, req);
            ServletUtility.setPageNo(pageNo, req);
            ServletUtility.setPageSize(pageSize, req);
            ServletUtility.setBean(bean, req);
            req.setAttribute("nextListSize", next.size());
            log.info("DoctorListCtl doGet Method Ended");
            ServletUtility.forward(getView(), req, resp);

        } catch (ApplicationException e) {
            e.printStackTrace();
            ServletUtility.handleException(e, req, resp);
            log.info("DoctorListCtl doGet Method Ended");
            return;
        }
       
    }

    /**
     * Handles HTTP POST requests for searching, pagination, deleting, or redirecting.
     * 
     * @param req  HttpServletRequest object
     * @param resp HttpServletResponse object
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		log.info("DoctorListCtl doPost Method Started");
    	List list = null;
        List next = null;

        int pageNo = DataUtility.getInt(req.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(PropertyReader.getValue("pageSize"));

        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

        DoctorBean bean = (DoctorBean) populateBean(req);
        DoctorModel model = new DoctorModel();

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
                ServletUtility.redirect(ORSView.DOCTOR_CTL, req, resp);
                return;

            } else if (OP_DELETE.equalsIgnoreCase(op)) {
                pageNo = 1;
                if (ids != null && ids.length > 0) {
                    DoctorBean deleteBean = new DoctorBean();
                    for (String id : ids) {
                        deleteBean.setId(DataUtility.getInt(id));
                        model.delete(deleteBean.getId());
                        ServletUtility.setSuccessMessage("Doctor deleted successfully", req);
                    }
                } else {
                    ServletUtility.setErrorMessage("Select at least one id", req);
                }

            } else if (OP_RESET.equalsIgnoreCase(op) || OP_BACK.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.DOCTOR_LIST_CTL, req, resp);
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
            req.setAttribute("nextListSize", next.size());

        } catch (ApplicationException e) {
            e.printStackTrace();
            ServletUtility.handleException(e, req, resp);
            return;
        }
        log.info("DoctorListCtl doPost Method Ended");
        ServletUtility.forward(getView(), req, resp);
    }

    /**
     * Returns the view (JSP page) for the doctor list.
     * 
     * @return JSP page path as String
     */
    @Override
    protected String getView() {
        return ORSView.DOCTOR_LIST_VIEW;
    }

}
