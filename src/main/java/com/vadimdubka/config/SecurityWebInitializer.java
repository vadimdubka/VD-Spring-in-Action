package com.vadimdubka.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * 9.1.2	Filtering web requests
 *
 * DelegatingFilterProxy - the main Spring Security Filter -
 * DelegatingFilterProxy - is a special servlet filter that will intercept requests coming into the application
 * and delegate them to a bean whose ID is springSecurityFilterChain.
 * DelegatingFilterProxy - just a proxy that delegates all its logic to the springSecurityFilterChain bean.
 *
 * As for the springSecurityFilterChain bean itself, it’s another special filter known as FilterChainProxy.
 * It’s a single filter that chains together one or more additional filters.
 * Spring Security relies on several servlet filters to provide different security features,
 * but you should almost never need to know these details, as you likely won’t need to explicitly declare the 'springSecurityFilterChain' bean
 * or any of the filters it chains together. Those filters will be created when you enable web security.
 *
 * If you'd rather configure DelegatingFilterProxy in Java with a WebApplicationInitializer,
 * then all you need to do is create a new class that extends AbstractSecurityWebApplicationInitializer
 * - it implements WebApplicationInitializer, so it will be discovered by Spring and be used to register DelegatingFilterProxy with the web container.
 * Although you can override its appendFilters() or insertFilters() methods to register filters of your own choosing,
 * you need not override anything to register DelegatingFilterProxy.
 *
 */
public class SecurityWebInitializer extends AbstractSecurityWebApplicationInitializer {
}
