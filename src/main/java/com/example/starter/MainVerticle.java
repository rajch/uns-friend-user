package com.example.starter;

import java.util.HashSet;
import java.util.Set;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.CorsHandler;
import com.example.starter.DBClass;

public class MainVerticle extends AbstractVerticle{
	DBClass db = new DBClass();

	public static void main(String args[])throws Exception {
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(new MainVerticle());
	}

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
	  
	  
	  Router router = Router.router(this.vertx);
		
		//to give CORS permissions 
		Set<String> allowedHeaders = new HashSet<>();
		allowedHeaders.add("x-requested-with");
		allowedHeaders.add("Access-Control-Allow-Origin");
		allowedHeaders.add("origin");
		allowedHeaders.add("Content-Type");
		allowedHeaders.add("accept");

		Set<HttpMethod> allowedMethods = new HashSet<>();
		allowedMethods.add(HttpMethod.POST);
		allowedMethods.add(HttpMethod.GET);
		allowedMethods.add(HttpMethod.DELETE);
		
		router.route().handler(CorsHandler.create("*")
			.allowedHeaders(allowedHeaders)
          .allowedMethods(allowedMethods));
		
		router.route().handler(BodyHandler.create());
		//call to different post and get api
		router.post("/user").handler(this::signUp);
		router.post("/user/signin").handler(this::signIn);
//		router.get("/user").handler(this::getUsers);
//		router.delete("/user").handler(this::deleteUser);
			
  	String host = "IN-SaxenaPr-1";
  	int port = 8080;
      HttpServerOptions op = new HttpServerOptions();

  	op.setHost(host);
 		vertx
 			.createHttpServer(op)
 			.requestHandler(router)
 			.listen(port);
     // Deploying all the verticles 
		
	}

  
  private void signUp(RoutingContext routingContext) {
		JsonObject userObject = routingContext.getBodyAsJson();
		//calling jdbsCLient
		String name = userObject.getString("name");
		String email = userObject.getString("email");
		String password = userObject.getString("password");
		JsonObject regObj = db.registerUser(name,email,password);
		if(regObj!=null) {
			routingContext.response()
				.setStatusCode(201)
				.putHeader("content-type", "application/json")
				.end(regObj.toString());
		}
		else {
			routingContext.response()
				.setStatusCode(500)
				.end();
			}
		
	}

  private void signIn(RoutingContext routingContext) {
	JsonObject login = routingContext.getBodyAsJson();
	String email = login.getString("email");
	String password = login.getString("password");
	JsonObject user = db.authenticateUser(email, password);
	//calling sigin jdbc
	if(user!=null) {
		routingContext.response()
			.setStatusCode(200)
			.putHeader("content-type", "application/json")
			.end(user.toString());
	}
			else {
				routingContext.response()
					.setStatusCode(403)
					.end();
			}
	  
  }
//
//
//private void getUsers(RoutingContext routingContext) {
//	String token = routingContext.request().getParam("")
//	String mail = routingContext.request().getParam("mail");
//	System.out.println("mail:: " + mail);
//	JsonObject request = new JsonObject().put("mail", mail);
//	EventBus eb = vertx.eventBus();
//	//communicating with customerDetailVerticle
//	eb.send("CustomerDetailsPage", request, replyHandler -> {
//		if(replyHandler.succeeded()) {
//			routingContext.response()
//				.putHeader("content-type", "application/json")
//				.end(replyHandler.result().body().toString());
//		}
//	});
//}
//  private void deleteUser(RoutingContext routingContext) {
//	  JsonObject user = routingContext.getBodyAsJson();
//	  //some code with if
//	  
//	  if() {
//		  routingContext.response()
//		  	.setStatusCode(200)
//		  	.end();
//	  }
//	  else {
//		  routingContext.response()
//		  	.setStatusCode(403)
//		  	.end();
//	  }
//  }
  
  
}
