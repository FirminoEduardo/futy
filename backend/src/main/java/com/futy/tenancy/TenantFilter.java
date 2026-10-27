package com.futy.tenancy;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.futy.league.domain.LigaRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 20)
public class TenantFilter extends OncePerRequestFilter {

	private static final Pattern LIGA_PATH = Pattern.compile("^/api/ligas/([^/]+)(/.*)?$");

	private final LigaRepository ligaRepository;

	public TenantFilter(LigaRepository ligaRepository) {
		this.ligaRepository = ligaRepository;
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();
		if (!path.startsWith("/api/ligas/")) {
			return true;
		}
		// POST /api/ligas (create) has no slug — already excluded by startsWith check above
		// when path is exactly /api/ligas. Paths like /api/ligas/{slug}/... are filtered.
		Matcher matcher = LIGA_PATH.matcher(path);
		return !matcher.matches();
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		Matcher matcher = LIGA_PATH.matcher(request.getRequestURI());
		if (!matcher.matches()) {
			filterChain.doFilter(request, response);
			return;
		}

		String slug = matcher.group(1);
		var liga = ligaRepository.findBySlug(slug);
		if (liga.isEmpty()) {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.getWriter().write("{\"message\":\"Liga nao encontrada: " + slug + "\"}");
			return;
		}

		try {
			TenantContext.set(liga.get().getId());
			filterChain.doFilter(request, response);
		}
		finally {
			TenantContext.clear();
		}
	}
}
