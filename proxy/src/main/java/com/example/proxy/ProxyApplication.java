package com.example.proxy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.event.RefreshRoutesResultEvent;
import org.springframework.cloud.gateway.filter.OrderedGatewayFilter;
import org.springframework.cloud.gateway.route.CachingRouteLocator;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class ProxyApplication {

	public int order = 0;
	public static void main(String[] args) {
		SpringApplication.run(ProxyApplication.class, args);
	}
	@Bean
	ApplicationListener<RefreshRoutesResultEvent> routesRefreshed(){
		return rre -> {
			System.out.println("routes updated");
			var crl = (CachingRouteLocator) rre.getSource();
			Flux<Route> routes = crl.getRoutes();
			routes.subscribe(System.out::println);
		};
	}



	@Bean
	RouteLocator gateway1(RouteLocatorBuilder rlb ){

			return rlb
					.routes()
					.route(routeSpec -> routeSpec.weight("group1",5).and()
							.path("/users")
								.uri("http://localhost:9001/")
					)
					.route(routeSpec -> routeSpec.weight("group1",5).and()
							.path("/users")
							.uri("http://localhost:9002/")
					)
					.route(routeSpec -> routeSpec.weight("group2",5).and()
							.path("/postput")
							.uri("http://localhost:9001/")
					)
					.route(routeSpec -> routeSpec.weight("group2",5).and()
							.path("/postput")
							.uri("http://localhost:9002/")
					)
					.build();
		}



}
