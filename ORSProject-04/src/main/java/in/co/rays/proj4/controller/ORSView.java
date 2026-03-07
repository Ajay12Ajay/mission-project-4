/**
 * @author 
 *     Ajay Pratap Kerketta
 * @version 
 *     1.0
 * @since 
 *     2025
 *
 * <p>
 * The {@code ORSView} interface defines a centralized collection of constants 
 * representing all the JSP page paths and Controller URLs used across the 
 * Online Result System (ORS) project.
 * </p>
 *
 * <p>
 * <b>Use Case:</b><br>
 * This interface serves as a global reference point for all views and controllers. 
 * Each controller and JSP in the project refers to these constants instead of hardcoding paths.
 * </p>
 *
 * <p>
 * <b>Advantages:</b>
 * <ul>
 *   <li>Eliminates spelling mistakes and path mismatches.</li>
 *   <li>Makes it easy to update paths from a single place.</li>
 *   <li>Improves readability and maintainability of code.</li>
 *   <li>Encourages consistency across all servlets and JSPs.</li>
 * </ul>
 * </p>
 */

package in.co.rays.proj4.controller;

public interface ORSView {

	/** Application context path */
	public String APP_CONTEXT = "/ORSProject-04";

	/** Common JSP folder path */
	public String PAGE_FOLDER = "/jsp";

	/** Welcome page and controller */
	public String WELCOME_VIEW = PAGE_FOLDER + "/Welcome.jsp";
	public String WELCOME_CTL = APP_CONTEXT + "/WelcomeCtl";

	/** User Registration page and controller */
	public String USER_REGISTRATION_VIEW = PAGE_FOLDER + "/UserRegistrationView.jsp";
	public String USER_REGISTRATION_CTL = APP_CONTEXT + "/UserRegistrationCtl";

	/** Forget Password page and controller */
	public String FORGET_PASSWORD_VIEW = PAGE_FOLDER + "/ForgetPasswordView.jsp";
	public String FORGET_PASSWORD_CTL = APP_CONTEXT + "/ForgetPasswordCtl";

	/** Login page and controller */
	public String LOGIN_VIEW = PAGE_FOLDER + "/LoginView.jsp";
	public String LOGIN_CTL = APP_CONTEXT + "/LoginCtl";

	/** My Profile page and controller */
	public String MY_PROFILE_VIEW = PAGE_FOLDER + "/MyProfileView.jsp";
	public String MY_PROFILE_CTL = APP_CONTEXT + "/ctl/MyProfileCtl";

	/** Change Password page and controller */
	public String CHANGE_PASSWORD_VIEW = PAGE_FOLDER + "/ChangePasswordView.jsp";
	public String CHANGE_PASSWORD_CTL = APP_CONTEXT + "/ctl/ChangePasswordCtl";

	/** Get Marksheet page and controller */
	public String GET_MARKSHEET_VIEW = PAGE_FOLDER + "/GetMarksheetView.jsp";
	public String GET_MARKSHEET_CTL = APP_CONTEXT + "/ctl/GetMarksheetCtl";

	/** Marksheet Merit List page and controller */
	public String MARKSHEET_MERIT_LIST_VIEW = PAGE_FOLDER + "/MarksheetMeritListView.jsp";
	public String MARKSHEET_MERIT_LIST_CTL = APP_CONTEXT + "/ctl/MarksheetMeritListCtl";

	/** User management pages and controllers */
	public String USER_VIEW = PAGE_FOLDER + "/UserView.jsp";
	public String USER_CTL = APP_CONTEXT + "/ctl/UserCtl";

	public String USER_LIST_VIEW = PAGE_FOLDER + "/UserListView.jsp";
	public String USER_LIST_CTL = APP_CONTEXT + "/ctl/UserListCtl";

	/** Role management pages and controllers */
	public String ROLE_VIEW = PAGE_FOLDER + "/RoleView.jsp";
	public String ROLE_CTL = APP_CONTEXT + "/ctl/RoleCtl";

	public String ROLE_LIST_VIEW = PAGE_FOLDER + "/RoleListView.jsp";
	public String ROLE_LIST_CTL = APP_CONTEXT + "/ctl/RoleListCtl";

	/** College management pages and controllers */
	public String COLLEGE_VIEW = PAGE_FOLDER + "/CollegeView.jsp";
	public String COLLEGE_CTL = APP_CONTEXT + "/ctl/CollegeCtl";

	public String COLLEGE_LIST_VIEW = PAGE_FOLDER + "/CollegeListView.jsp";
	public String COLLEGE_LIST_CTL = APP_CONTEXT + "/ctl/CollegeListCtl";

	/** Student management pages and controllers */
	public String STUDENT_VIEW = PAGE_FOLDER + "/StudentView.jsp";
	public String STUDENT_CTL = APP_CONTEXT + "/ctl/StudentCtl";

	public String STUDENT_LIST_VIEW = PAGE_FOLDER + "/StudentListView.jsp";
	public String STUDENT_LIST_CTL = APP_CONTEXT + "/ctl/StudentListCtl";

	/** Marksheet pages and controllers */
	public String MARKSHEET_VIEW = PAGE_FOLDER + "/MarksheetView.jsp";
	public String MARKSHEET_CTL = APP_CONTEXT + "/ctl/MarksheetCtl";

	public String MARKSHEET_LIST_VIEW = PAGE_FOLDER + "/MarksheetListView.jsp";
	public String MARKSHEET_LIST_CTL = APP_CONTEXT + "/ctl/MarksheetListCtl";

	/** Course management pages and controllers */
	public String COURSE_VIEW = PAGE_FOLDER + "/CourseView.jsp";
	public String COURSE_CTL = APP_CONTEXT + "/ctl/CourseCtl";

	public String COURSE_LIST_VIEW = PAGE_FOLDER + "/CourseListView.jsp";
	public String COURSE_LIST_CTL = APP_CONTEXT + "/ctl/CourseListCtl";

	/** Subject management pages and controllers */
	public String SUBJECT_VIEW = PAGE_FOLDER + "/SubjectView.jsp";
	public String SUBJECT_CTL = APP_CONTEXT + "/ctl/SubjectCtl";

	public String SUBJECT_LIST_VIEW = PAGE_FOLDER + "/SubjectListView.jsp";
	public String SUBJECT_LIST_CTL = APP_CONTEXT + "/ctl/SubjectListCtl";

	/** Timetable management pages and controllers */
	public String TIMETABLE_VIEW = PAGE_FOLDER + "/TimetableView.jsp";
	public String TIMETABLE_CTL = APP_CONTEXT + "/ctl/TimetableCtl";

	public String TIMETABLE_LIST_VIEW = PAGE_FOLDER + "/TimetableListView.jsp";
	public String TIMETABLE_LIST_CTL = APP_CONTEXT + "/ctl/TimetableListCtl";

	/** Faculty management pages and controllers */
	public String FACULTY_VIEW = PAGE_FOLDER + "/FacultyView.jsp";
	public String FACULTY_CTL = APP_CONTEXT + "/ctl/FacultyCtl";

	public String FACULTY_LIST_VIEW = PAGE_FOLDER + "/FacultyListView.jsp";
	public String FACULTY_LIST_CTL = APP_CONTEXT + "/ctl/FacultyListCtl";

	/** Error page and controller */
	public String ERROR_VIEW = PAGE_FOLDER + "/ErrorView.jsp";
	public String ERROR_CTL = APP_CONTEXT + "/ErrorCtl";

	/** Patient management pages and controllers (custom module) */
	public String PATIENT_VIEW = PAGE_FOLDER + "/PatientView.jsp";
	public String PATIENT_CTL = APP_CONTEXT + "/ctl/PatientCtl";

	public String PATIENT_LIST_VIEW = PAGE_FOLDER + "/PatientListView.jsp";
	public String PATIENT_LIST_CTL = APP_CONTEXT + "/ctl/PatientListCtl";

	/** Doctor management pages and controllers (custom module) */
	public String DOCTOR_VIEW = PAGE_FOLDER + "/DoctorView.jsp";
	public String DOCTOR_CTL = APP_CONTEXT + "/ctl/DoctorCtl";

	public String DOCTOR_LIST_VIEW = PAGE_FOLDER + "/DoctorListView.jsp";
	public String DOCTOR_LIST_CTL = APP_CONTEXT + "/ctl/DoctorListCtl";

	// ================== COURCE MODULE ==================
	public String COURCE_VIEW = PAGE_FOLDER + "/CourceView.jsp";
	public String COURCE_CTL = APP_CONTEXT + "/ctl/CourceCtl";

	public String COURCE_LIST_VIEW = PAGE_FOLDER + "/CourceListView.jsp";
	public String COURCE_LIST_CTL = APP_CONTEXT + "/ctl/CourceListCtl";

	// ================== LIBRARY BOOK ISSUE MODULE ==================

	public String LIBRARY_BOOK_ISSUE_VIEW = PAGE_FOLDER + "/LibraryBookIssueView.jsp";
	public String LIBRARY_BOOK_ISSUE_CTL = APP_CONTEXT + "/ctl/LibraryBookIssueCtl";

	public String LIBRARY_BOOK_ISSUE_LIST_VIEW = PAGE_FOLDER + "/LibraryBookIssueListView.jsp";
	public String LIBRARY_BOOK_ISSUE_LIST_CTL = APP_CONTEXT + "/ctl/LibraryBookIssueListCtl";

	public String FEEDBACK_VIEW = PAGE_FOLDER + "/FeedbackView.jsp";
	public String FEEDBACK_CTL = APP_CONTEXT + "/ctl/FeedbackCtl";

	public String FEEDBACK_LIST_VIEW = PAGE_FOLDER + "/FeedbackListView.jsp";
	public String FEEDBACK_LIST_CTL = APP_CONTEXT + "/ctl/FeedbackListCtl";

	// ================== TRANSPORT MODULE ==================

	public String TRANSPORT_VIEW = PAGE_FOLDER + "/TransportView.jsp";
	public String TRANSPORT_CTL = APP_CONTEXT + "/ctl/TransportCtl";

	public String TRANSPORT_LIST_VIEW = PAGE_FOLDER + "/TransportListView.jsp";
	public String TRANSPORT_LIST_CTL = APP_CONTEXT + "/ctl/TransportListCtl";

	public String JAVA_DOC = APP_CONTEXT + "/doc/index.html";

}
