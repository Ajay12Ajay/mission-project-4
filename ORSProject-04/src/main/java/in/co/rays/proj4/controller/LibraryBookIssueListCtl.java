/**
 * @Author: Ajay Pratap Kerketta
 * @Description: LibraryBookIssueListCtl handles listing, searching,
 * pagination, and deletion of LibraryBookIssue records.
 *
 * @Creation Date: 27-Feb-2026
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
import in.co.rays.proj4.bean.LibraryBookIssueBean;
import in.co.rays.proj4.exception.ApplicationException;
import in.co.rays.proj4.model.LibraryBookIssueModel;
import in.co.rays.proj4.util.DataUtility;
import in.co.rays.proj4.util.PropertyReader;
import in.co.rays.proj4.util.ServletUtility;
import in.co.rays.proj4.controller.ORSView;

@WebServlet(name = "LibraryBookIssueListCtl", urlPatterns = { "/ctl/LibraryBookIssueListCtl" })
public class LibraryBookIssueListCtl extends BaseCtl {

    Logger log = Logger.getLogger(LibraryBookIssueListCtl.class);

    /**
     * Dynamic preload of Status values from DB
     */
    @Override
    protected void preload(HttpServletRequest request) {

        log.info("LibraryBookIssueListCtl preload Started");

        LibraryBookIssueModel model = new LibraryBookIssueModel();

        try {
            Iterator<?> it = model.list().iterator();
            HashMap<String, String> statusMap = new HashMap<>();

            while (it.hasNext()) {
                LibraryBookIssueBean bean =
                        (LibraryBookIssueBean) it.next();

                if (bean.getStatus() != null) {
                    statusMap.put(bean.getStatus(),
                                  bean.getStatus());
                }
            }

            request.setAttribute("statusMap", statusMap);

        } catch (ApplicationException e) {
            e.printStackTrace();
        }

        log.info("LibraryBookIssueListCtl preload Ended");
    }

    /**
     * Populate Bean from request
     */
    @Override
    protected BaseBean populateBean(HttpServletRequest request) {

        LibraryBookIssueBean bean =
                new LibraryBookIssueBean();

        bean.setId(DataUtility.getLong(request.getParameter("id")));
        bean.setBookId(DataUtility.getLong(request.getParameter("bookId")));
        bean.setMemberId(DataUtility.getLong(request.getParameter("memberId")));
        bean.setIssueDate(DataUtility.getDate(request.getParameter("issueDate")));
        bean.setReturnDate(DataUtility.getDate(request.getParameter("returnDate")));
        bean.setIssuedBy(DataUtility.getString(request.getParameter("issuedBy")));
        bean.setStatus(DataUtility.getString(request.getParameter("status")));

        return bean;
    }

    /**
     * Handle GET request
     */
    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp)
            throws ServletException, IOException {

        log.info("LibraryBookIssueListCtl doGet Started");

        int pageNo = 1;
        int pageSize =
                DataUtility.getInt(PropertyReader.getValue("page.size"));

        LibraryBookIssueBean bean =
                (LibraryBookIssueBean) populateBean(req);

        LibraryBookIssueModel model =
                new LibraryBookIssueModel();

        try {

            List<LibraryBookIssueBean> list =
                    model.search(bean, pageNo, pageSize);

            List<LibraryBookIssueBean> next =
                    model.search(bean, pageNo + 1, pageSize);

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
            return;
        }

        log.info("LibraryBookIssueListCtl doGet Ended");
    }

    /**
     * Handle POST request
     */
    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws ServletException, IOException {

        log.info("LibraryBookIssueListCtl doPost Started");

        List<?> list = null;
        List<?> next = null;

        int pageNo =
                DataUtility.getInt(req.getParameter("pageNo"));

        int pageSize =
                DataUtility.getInt(PropertyReader.getValue("page.size"));

        pageNo = (pageNo == 0) ? 1 : pageNo;

        LibraryBookIssueBean bean =
                (LibraryBookIssueBean) populateBean(req);

        LibraryBookIssueModel model =
                new LibraryBookIssueModel();

        String op =
                DataUtility.getString(req.getParameter("operation"));

        String[] ids =
                req.getParameterValues("ids");

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
                ServletUtility.redirect(
                        ORSView.LIBRARY_BOOK_ISSUE_CTL,
                        req, resp);
                return;

            } else if (OP_DELETE.equalsIgnoreCase(op)) {

                pageNo = 1;

                if (ids != null && ids.length > 0) {

                    for (String id : ids) {

                        model.delete(
                                DataUtility.getLong(id));
                    }

                    ServletUtility.setSuccessMessage(
                            "Record deleted successfully", req);

                } else {
                    ServletUtility.setErrorMessage(
                            "Select at least one record", req);
                }

            } else if (OP_RESET.equalsIgnoreCase(op)
                    || OP_BACK.equalsIgnoreCase(op)) {

                ServletUtility.redirect(
                        ORSView.LIBRARY_BOOK_ISSUE_LIST_CTL,
                        req, resp);
                return;
            }

            list = model.search(bean, pageNo, pageSize);
            next = model.search(bean, pageNo + 1, pageSize);

            if (list == null || list.size() == 0) {
                ServletUtility.setErrorMessage(
                        "No record found", req);
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

        log.info("LibraryBookIssueListCtl doPost Ended");

        ServletUtility.forward(getView(), req, resp);
    }

    /**
     * Return JSP View
     */
    @Override
    protected String getView() {
        return ORSView.LIBRARY_BOOK_ISSUE_LIST_VIEW;
    }
}