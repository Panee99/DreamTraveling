/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oiog.dreamtraveling.filters;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import oiog.dreamtraveling.dtos.UserDTO;
import org.apache.log4j.Logger;

/**
 *
 * @author hoang
 */
public class MainController implements Filter {

    private static final Logger LOGGER = Logger.getLogger(MainController.class);
    private static final boolean debug = true;
    private static final String ERROR_P = "error.jsp";
    private static final String ERROR401_P = "error-401.jsp";
    private static Map<String, List<String>> validRole = new HashMap();

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured. 
    private FilterConfig filterConfig = null;

    public MainController() {
    }

    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("MainController:DoBeforeProcessing");
        }

        // Write code here to process the request and/or response before
        // the rest of the filter chain is invoked.
        // For example, a logging filter might log items on the request object,
        // such as the parameters.
        /*
	for (Enumeration en = request.getParameterNames(); en.hasMoreElements(); ) {
	    String name = (String)en.nextElement();
	    String values[] = request.getParameterValues(name);
	    int n = values.length;
	    StringBuffer buf = new StringBuffer();
	    buf.append(name);
	    buf.append("=");
	    for(int i=0; i < n; i++) {
	        buf.append(values[i]);
	        if (i < n-1)
	            buf.append(",");
	    }
	    log(buf.toString());
	}
         */
    }

    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("MainController:DoAfterProcessing");
        }

        // Write code here to process the request and/or response after
        // the rest of the filter chain is invoked.
        // For example, a logging filter might log the attributes on the
        // request object after the request has been processed. 
        /*
	for (Enumeration en = request.getAttributeNames(); en.hasMoreElements(); ) {
	    String name = (String)en.nextElement();
	    Object value = request.getAttribute(name);
	    log("attribute: " + name + "=" + value.toString());

	}
         */
        // For example, a filter might append something to the response.
        /*
	PrintWriter respOut = new PrintWriter(response.getWriter());
	respOut.println("<P><B>This has been appended by an intrusive filter.</B>");
         */
    }

    /**
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     * @param chain The filter chain we are processing
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet error occurs
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        String url = ERROR_P;
        HttpSession session = ((HttpServletRequest) request).getSession();
        UserDTO user = (UserDTO) session.getAttribute("user");
        try {
            if (uri.endsWith(".css") || uri.endsWith(".js") || uri.endsWith(".jpg") || uri.endsWith(".png") || uri.endsWith(".tff") || uri.endsWith(".woff") || uri.endsWith(".woff2") || uri.endsWith(".map")) {
                chain.doFilter(request, response);
                return;
            } else {
                String resource;
                if (uri.endsWith("/") && !uri.endsWith("/admin/")) {
                    resource = "LoadHome";
                    if (user != null) {
                        if ("admin".equals(user.getRole())) {
                            resource = "LoadAdmin";
                        }
                    }
                } else if (uri.endsWith("/admin") || uri.endsWith("/admin/")) {
                    resource = "LoadAdmin";
                } else {
                    int lastIndex = uri.lastIndexOf("/");
                    resource = uri.substring(lastIndex + 1);
                    resource = resource.split("\\?")[0];
                }
                List<String> requireRole = authenticate(resource, user);
                if (requireRole == null) {
                    url = resource.substring(0, 1).toUpperCase() + resource.substring(1) + "Controller";
                } else {
                    boolean isAjax = "XMLHttpRequest".equals(
                            req.getHeader("X-Requested-With"));
                    if (isAjax) {
                        res.sendError(401);
                        res.getWriter().flush();
                        return;
                    }
                    request.setAttribute("error", "Please login as [" + requireRole.get(0) + "] to do this action");
                    url = ERROR401_P;
                }
            }
            request.getRequestDispatcher(url).forward(request, response);
        } catch (Exception e) {
            url = "error.jsp";
            request.setAttribute("error", "Server error. Please check log file for more infomation");
            LOGGER.error(e.getMessage());
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    /*=== Return reuired role to do the action ===*/
    private List<String> authenticate(String action, UserDTO user) {
        List<String> requireRole = null;
        if (validRole.containsKey(action)) {
            requireRole = validRole.get(action);
            if (user != null) {
                if (requireRole.contains(user.getRole())) {
                    requireRole = null;
                }
            } else {
                if (requireRole.contains(null)) {
                    requireRole = null;
                }
            }
        }
        return requireRole;
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {
                log("MainController:Initializing filter");
            }
        }
        /*=== config valid role ===*/
        validRole.put("LoadAdmin", new ArrayList(Arrays.asList("admin")));
        validRole.put("AddTour", new ArrayList(Arrays.asList("admin")));
        validRole.put("NewTour", new ArrayList(Arrays.asList("admin")));
        validRole.put("UpdateTour", new ArrayList(Arrays.asList("admin")));
        validRole.put("DeactiveTour", new ArrayList(Arrays.asList("admin")));
        validRole.put("LoadHome", new ArrayList(Arrays.asList("user", null)));
        validRole.put("BookTour", new ArrayList(Arrays.asList("user")));
        validRole.put("ViewCart", new ArrayList(Arrays.asList("user")));
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("MainController()");
        }
        StringBuffer sb = new StringBuffer("MainController(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }

    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);

        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                response.setContentType("text/html");
                PrintStream ps = new PrintStream(response.getOutputStream());
                PrintWriter pw = new PrintWriter(ps);
                pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n"); //NOI18N

                // PENDING! Localize this for next official release
                pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                pw.print(stackTrace);
                pw.print("</pre></body>\n</html>"); //NOI18N
                pw.close();
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        } else {
            try {
                PrintStream ps = new PrintStream(response.getOutputStream());
                t.printStackTrace(ps);
                ps.close();
                response.getOutputStream().close();
            } catch (Exception ex) {
            }
        }
    }

    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }

    public void log(String msg) {
        filterConfig.getServletContext().log(msg);
    }

}
