package gaian.svsa.ep.util.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import gaian.svsa.ep.model.UsuarioEP;
import lombok.extern.log4j.Log4j;

@Log4j
@WebFilter(urlPatterns = "/restrito/*")
public class filtro implements Filter {

	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession(false);

		UsuarioEP user = (session != null) ? (UsuarioEP) session.getAttribute("usuario") : null;

		if (user == null) {
			log.info("Filtro: usuário nao está logado");

			String baseUrl = req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort();
			String loginPage = baseUrl + "/svsa-ct/restricted/home/SvsaHome.xhtml";
			res.sendRedirect(loginPage);
		} else {
			log.info("Filtro: usuário logado");
			chain.doFilter(request, response);
		}
	}

	public void destroy() {

	}
}
