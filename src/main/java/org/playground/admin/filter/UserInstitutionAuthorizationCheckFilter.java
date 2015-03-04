package org.playground.admin.filter;

import org.playground.admin.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * Created by wsantasiero on 9/25/14.
 */
@Component
public class UserInstitutionAuthorizationCheckFilter implements Filter{

    @Autowired
    private AuthorityService authorityService;

    private ServletContext context;

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        this.context = fConfig.getServletContext();
        this.context.log("UserInstitutionAuthorizationCheckFilter initialized");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletResponse httpServletResponse = (HttpServletResponse) resp;
        Map<String,String[]> params = req.getParameterMap();
        if (!params.isEmpty() && params.containsKey("institutionId") && params.containsKey("userId")) {
            String institutionId = params.get("institutionId")[0];
            String userId = params.get("userId")[0];

            if(authorityService.findAdminAuthorityByUserIdAndInstitutionId(userId, institutionId) == null){
                httpServletResponse.sendRedirect("http://apps:80804/app.html#/log-in");
            }else{
                chain.doFilter(req, resp);
            }

        }
    }

    @Override
    public void destroy() {}
}
