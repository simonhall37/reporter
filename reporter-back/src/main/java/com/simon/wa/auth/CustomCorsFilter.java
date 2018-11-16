package com.simon.wa.auth;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UrlPathHelper;

@Component
@Order(-1)
public class CustomCorsFilter implements Filter {

	private final static Logger logger = LoggerFactory.getLogger(CustomCorsFilter.class);
	
	public CustomCorsFilter() {
		super();
	}

	@Override
	public final void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
			throws IOException, ServletException {
		final HttpServletResponse response = (HttpServletResponse) res;

		response.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Methods", "POST, PUT, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers",
				"X-Requested-With, Authorization, Origin, Content-Type, Version");
		response.setHeader("Access-Control-Expose-Headers", "X-Requested-With, Authorization, Origin, Content-Type");

		final HttpServletRequest request = (HttpServletRequest) req;
		String path = new UrlPathHelper().getPathWithinApplication(request);
		boolean authenticated = SecurityContextHolder.getContext().getAuthentication()!=null;
		logger.info(request.getRemoteAddr() + " accessing: " + path + " with " + request.getMethod() + " and auth status " + authenticated );

		if (!request.getMethod().equals("OPTIONS")) {
			chain.doFilter(req, res);
		}
	}

	@Override
	public void destroy() {

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}
}
