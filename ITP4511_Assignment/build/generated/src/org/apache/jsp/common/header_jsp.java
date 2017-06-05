package org.apache.jsp.common;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class header_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List<String> _jspx_dependants;

  static {
    _jspx_dependants = new java.util.ArrayList<String>(1);
    _jspx_dependants.add("/common/../WEB-INF/tlds/icon.tld");
  }

  private org.glassfish.jsp.api.ResourceInjector _jspx_resourceInjector;

  public java.util.List<String> getDependants() {
    return _jspx_dependants;
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;

    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;
      _jspx_resourceInjector = (org.glassfish.jsp.api.ResourceInjector) application.getAttribute("com.sun.appserv.jsp.resource.injector");

      out.write("\n");
      out.write("\n");
      out.write("<!DOCTYPE html>\n");
      out.write("<html lang=\"zh-Hant-hk\">\n");
      out.write("\n");
      out.write("    <head>\n");
      out.write("        <meta charset=\"utf-8\">\n");
      out.write("        <title>C&F Dress</title>\n");
      out.write("        <meta name=\"author\" content=\"j1Lib\">\n");
      out.write("    </head>\n");
      out.write("\n");
      out.write("    <body>\n");
      out.write("        <div id=\"header\">\n");
      out.write("            ITP4511 Enterprise Systems Development\n");
      out.write("        </div>\n");
      out.write("        <div id=\"logo\">\n");
      out.write("            C&F Dresss\n");
      out.write("            <div id=\"member\">\n");
      out.write("                ");

                    // 0 = not login
                    // 1 = customer
                    // 2 = manager
                    int status = 1;
                    switch (status) {
                        case 1:
                
      out.write("\n");
      out.write("                <div>");
      if (_jspx_meth_ict_icon_0(_jspx_page_context))
        return;
      out.write("Account</div>\n");
      out.write("                ");

                        break;
                    case 2:
                        break;
                    default:
                
      out.write("\n");
      out.write("                <div><i class=\"fa fa-user-o\" aria-hidden=\"true\"></i>Login</div>\n");
      out.write("                ");

                    }
                
      out.write("\n");
      out.write("            </div>\n");
      out.write("        </div>\n");
      out.write("        <div id=\"hots\">\n");
      out.write("            <div>Trends:</div>\n");
      out.write("            <div>ItemA</div>\n");
      out.write("            <div>ItemB</div>\n");
      out.write("            <div>ItemC</div>\n");
      out.write("            <div>ItemD</div>\n");
      out.write("            <div>ItemE</div>\n");
      out.write("            <div>ItemF</div>\n");
      out.write("        </div>\n");
      out.write("        <div id=\"menu\">\n");
      out.write("            <div id=\"category\">\n");
      out.write("                <div>Category A</div>\n");
      out.write("                <div>Category B</div>\n");
      out.write("                <div>Category C</div>\n");
      out.write("                <div>Category D</div>\n");
      out.write("                <div><i class=\"fa fa-search\" aria-hidden=\"true\"></i> Search</div>\n");
      out.write("            </div>\n");
      out.write("            <div id=\"search\">\n");
      out.write("                <input type=\"text\" placeholder=\"Search\" />\n");
      out.write("            </div>\n");
      out.write("        </div>\n");
      out.write("        <div id=\"container\">\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          out.clearBuffer();
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }

  private boolean _jspx_meth_ict_icon_0(PageContext _jspx_page_context)
          throws Throwable {
    PageContext pageContext = _jspx_page_context;
    JspWriter out = _jspx_page_context.getOut();
    //  ict:icon
    ict.tag.icon _jspx_th_ict_icon_0 = (_jspx_resourceInjector != null) ? _jspx_resourceInjector.createTagHandlerInstance(ict.tag.icon.class) : new ict.tag.icon();
    _jspx_th_ict_icon_0.setJspContext(_jspx_page_context);
    _jspx_th_ict_icon_0.setName("diamond");
    _jspx_th_ict_icon_0.doTag();
    return false;
  }
}
