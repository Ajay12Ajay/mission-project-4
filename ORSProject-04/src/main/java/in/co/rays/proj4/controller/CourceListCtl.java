/**
 * @Author: Ajay Pratap Kerketta
 * @Description: CourceListCtl is a Servlet controller responsible for handling
 * operations related to listing, searching, deleting, and paginating Cource entities.
 * It extends BaseCtl to leverage common controller functionalities.
 * 
 * @Creation Date: 26-Feb-2026
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
import in.co.rays.proj4.bean.CourceBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.model.CourceModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "CourceListCtl", urlPatterns = { "/ctl/CourceListCtl" })
public class CourceListCtl extends BaseCtl {

    Logger log = Logger.getLogger(CourceListCtl.class);

    // ===================== PRELOAD =====================
    @Override
    protected void preload(HttpServletRequest request) {

        log.info("CourceListCtl preload Method Started");

        CourceModel model = new CourceModel();

        try {
            Iterator it = model.list().iterator();

            HashMap<String, String> courseCodeMap = new HashMap<>();
            HashMap<String, String> courseNameMap = new HashMap<>();

            while (it.hasNext()) {
                CourceBean bean = (CourceBean) it.next();

                courseCodeMap.put(bean.getCourseCode(), bean.getCourseCode());
                courseNameMap.put(bean.getCourseName(), bean.getCourseName());
            }

            request.setAttribute("courseCodeMap", courseCodeMap);
            request.setAttribute("courseNameMap", courseNameMap);

        } catch (ApplicationException e) {
            e.printStackTrace();
        }

        log.info("CourceListCtl preload Method Ended");
    }

    // ===================== POPULATE BEAN =====================
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {

        log.info("CourceListCtl populateBean Method Started");

        CourceBean bean = new CourceBean();

        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setCourseCode(DataUtility.getString(request.getParameter("courseCode")));
        bean.setCourseName(DataUtility.getString(request.getParameter("courseName")));
        bean.setDuration(DataUtility.getString(request.getParameter("duration")));
        bean.setCourseFee(DataUtility.getString(request.getParameter("courseFee")));

        log.info("CourceListCtl populateBean Method Ended");

        return bean;
    }

    // ===================== DO GET =====================
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        log.info("CourceListCtl doGet Method Started");

        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

        CourceBean bean = (CourceBean) populateBean(req);
        CourceModel model = new CourceModel();

        try {
            List<CourceBean> list = model.search(bean, pageNo, pageSize);
            List<CourceBean> next = model.search(bean, pageNo + 1, pageSize);

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

        log.info("CourceListCtl doGet Method Ended");
    }

    // ===================== DO POST =====================
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        log.info("CourceListCtl doPost Method Started");

        int pageNo = DataUtility.getInt(req.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(PropertyReader.getValue("pageSize"));

        pageNo = (pageNo == 0) ? 1 : pageNo;
        pageSize = (pageSize == 0)
                ? DataUtility.getInt(PropertyReader.getValue("page.size"))
                : pageSize;

        CourceBean bean = (CourceBean) populateBean(req);
        CourceModel model = new CourceModel();

        String op = DataUtility.getString(req.getParameter("operation"));
        String[] ids = req.getParameterValues("ids");

        try {

            if (OP_SEARCH.equalsIgnoreCase(op)
                    || OP_NEXT.equalsIgnoreCase(op)
                    || OP_PREVIOUS.equalsIgnoreCase(op)) {

                if (OP_SEARCH.equalsIgnoreCase(op)) {
                    pageNo = 1;
                } else if (OP_NEXT.equalsIgnoreCase(op)) {
                    pageNo++;
                } else if (OP_PREVIOUS.equalsIgnoreCase(op)) {
                    pageNo--;
                }

            } else if (OP_NEW.equalsIgnoreCase(op)) {

                ServletUtility.redirect(ORSView.COURCE_CTL, req, resp);
                return;

            } else if (OP_DELETE.equalsIgnoreCase(op)) {

                pageNo = 1;

                if (ids != null && ids.length > 0) {

                    CourceBean deleteBean = new CourceBean();

                    for (String id : ids) {
                        deleteBean.setId(DataUtility.getLong(id));
                        model.delete(deleteBean.getId());
                    }

                    ServletUtility.setSuccessMessage("Course deleted successfully", req);

                } else {
                    ServletUtility.setErrorMessage("Select at least 1 id.", req);
                }

            } else if (OP_RESET.equalsIgnoreCase(op)
                    || OP_BACK.equalsIgnoreCase(op)) {

                ServletUtility.redirect(ORSView.COURCE_LIST_CTL, req, resp);
                return;
            }

            List list = model.search(bean, pageNo, pageSize);
            List next = model.search(bean, pageNo + 1, pageSize);

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

        log.info("CourceListCtl doPost Method Ended");
        ServletUtility.forward(getView(), req, resp);
    }

    // ===================== VIEW =====================
    @Override
    protected String getView() {
        return ORSView.COURCE_LIST_VIEW;
    }
}