package com.cjemison.vertx;

import com.cjemison.protobuf.gen.CustomerGrpc;
import com.cjemison.protobuf.gen.HelloWorld;

import java.util.Random;

import io.grpc.stub.StreamObserver;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.grpc.VertxServer;
import io.vertx.grpc.VertxServerBuilder;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    final VertxServer server = VertxServerBuilder.forAddress(vertx, "localhost", 8080).addService(new CustomerGrpc.CustomerImplBase () {
      @Override
      public void createCustomer(final HelloWorld.CustomerRequest request, final StreamObserver<HelloWorld.CustomerResponse> responseObserver) {
        responseObserver.onNext(HelloWorld.CustomerResponse.newBuilder()
              .setId(new Random().nextInt()%10000)
              .setSuccess(true)
              .build());
        responseObserver.onCompleted();
      }
    }).build();

    server.start(ar ->{
      if (ar.succeeded()) {
        System.out.println("gRPC service started");
      } else {
        System.out.println("Could not start server " + ar.cause().getMessage());
      }
    });
  }

  public static void main(String[] args) {
    final Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new MainVerticle());
  }
}
