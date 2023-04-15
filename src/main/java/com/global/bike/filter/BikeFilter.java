package com.global.bike.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class BikeFilter extends OncePerRequestFilter{

	private static final Logger LOGGER = LoggerFactory.getLogger(BikeFilter.class);
	public BikeFilter() {
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		ContentCachingRequestWrapper cachingRequestWrapper = new ContentCachingRequestWrapper(request);
		ContentCachingResponseWrapper contentCachingResponseWrapper = new ContentCachingResponseWrapper(response);
		filterChain.doFilter(cachingRequestWrapper, contentCachingResponseWrapper);
		String requestBody = new String(cachingRequestWrapper.getContentAsByteArray(),0,cachingRequestWrapper.getContentAsByteArray().length,cachingRequestWrapper.getCharacterEncoding());
		String responseBody = new String(contentCachingResponseWrapper.getContentAsByteArray(),0,contentCachingResponseWrapper.getContentAsByteArray().length,contentCachingResponseWrapper.getCharacterEncoding());
		LOGGER.info("method:{} uri:{} request{} response{}",request.getMethod(),request.getRequestURI(),requestBody,responseBody);

		contentCachingResponseWrapper.copyBodyToResponse();

//		filterChain.doFilter(request, response);
//		LOGGER.info("method:{} uri:{} request{} response{}",request.getMethod(),request.getRequestURI(),request.getLocale(),response.getContentType());
		// TODO Auto-generated method stub

	}

}
