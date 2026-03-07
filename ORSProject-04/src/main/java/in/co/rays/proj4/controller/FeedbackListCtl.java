/**
 * @Author: Ajay Pratap Kerketta
 * @Description: FeedbackListCtl is a Servlet controller responsible for handling
 * operations related to listing, searching, deleting, and paginating Feedback entities.
 * It extends BaseCtl to leverage common controller functionalities.
 *
 * @Creation Date: 28-Feb-2026
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
import in.co.rays.proj4.bean.FeedbackBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.model.FeedbackModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;

@WebServlet(name = "FeedbackListCtl", urlPatterns = { "/ctl/FeedbackListCtl" })
public class FeedbackListCtl extends BaseCtl {

    Logger log = Logger.getLogger(FeedbackListCtl.class);

    /**
     * Preload dynamic filters for feedbackCode, feedbackDate, and rating.
     */
    @Override
    protected void preload(HttpServletRequest request) {

        log.info("FeedbackListCtl preload method started");

        FeedbackModel model = new FeedbackModel();

        try {
            Iterator<FeedbackBean> it = model.list().iterator();

            HashMap<String, String> codeMap = new HashMap<>();
            HashMap<String, String> dateMap = new HashMap<>();
            //HashMap<Integer, String> ratingMap = new HashMap<>();

            while (it.hasNext()) {
                FeedbackBean bean = it.next();

                if (bean.getFeedbackCode() != null) {
                    codeMap.put(bean.getFeedbackCode(), bean.getFeedbackCode());
                }

                if (bean.getFeedbackDate() != null) {
                    String date = DataUtility.getDateString(bean.getFeedbackDate());
                    dateMap.put(date, date);
                }

				/*
				 * if (bean.getRating() != null) { ratingMap.put(bean.getRating(),
				 * String.valueOf(bean.getRating())); }
				 */
            }

            request.setAttribute("codeMap", codeMap);
            request.setAttribute("dateMap", dateMap);
          //  request.setAttribute("ratingMap", ratingMap);

        } catch (ApplicationException e) {
            e.printStackTrace();
        }

        log.info("FeedbackListCtl preload method ended");
    }

    /**
     * Populate bean for search filters.
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {

        log.info("FeedbackListCtl populateBean started");

        FeedbackBean bean = new FeedbackBean();

        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setFeedbackCode(DataUtility.getString(request.getParameter("feedbackCode")));
        bean.setFeedbackDate(DataUtility.getDate(request.getParameter("feedbackDate")));
        bean.setRating(DataUtility.getInt(request.getParameter("rating")));

        log.info("FeedbackListCtl populateBean ended");

        return bean;
    }

    /**
     * Handles GET request (initial load).
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        log.info("FeedbackListCtl doGet started");

        int pageNo = 1;
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

        FeedbackBean bean = (FeedbackBean) populateBean(req);
        FeedbackModel model = new FeedbackModel();

        try {
            List<FeedbackBean> list = model.search(bean, pageNo, pageSize);
            List<FeedbackBean> next = model.search(bean, pageNo + 1, pageSize);

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

        log.info("FeedbackListCtl doGet ended");
    }

    /**
     * Handles POST operations (search, pagination, delete).
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        log.info("FeedbackListCtl doPost started");

        int pageNo = DataUtility.getInt(req.getParameter("pageNo"));
        int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

        pageNo = (pageNo == 0) ? 1 : pageNo;

        FeedbackBean bean = (FeedbackBean) populateBean(req);
        FeedbackModel model = new FeedbackModel();

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
                ServletUtility.redirect(ORSView.FEEDBACK_CTL, req, resp);
                return;

            } else if (OP_DELETE.equalsIgnoreCase(op)) {

                pageNo = 1;

                if (ids != null && ids.length > 0) {
                    for (String id : ids) {
                        model.delete(DataUtility.getLong(id));
                    }
                    ServletUtility.setSuccessMessage("Feedback deleted successfully", req);
                } else {
                    ServletUtility.setErrorMessage("Select at least 1 record.", req);
                }

            } else if (OP_RESET.equalsIgnoreCase(op) || OP_BACK.equalsIgnoreCase(op)) {
                ServletUtility.redirect(ORSView.FEEDBACK_LIST_CTL, req, resp);
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

        log.info("FeedbackListCtl doPost ended");
        ServletUtility.forward(getView(), req, resp);
    }

    /**
     * Returns Feedback List View page.
     */
    @Override
    protected String getView() {
        return ORSView.FEEDBACK_LIST_VIEW;
    }
}