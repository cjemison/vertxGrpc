package com.cjemison.vertx;

import com.cjemison.protobuf.gen.CustomerGrpc;
import com.cjemison.protobuf.gen.HelloWorld;

import io.grpc.ManagedChannel;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.grpc.VertxChannelBuilder;

public class ClientVerticle extends AbstractVerticle {

  @Override
  public void start() throws Exception {
    final ManagedChannel channel = VertxChannelBuilder
          .forAddress(vertx, "localhost", 8080)
          .usePlaintext(true)
          .build();

    final CustomerGrpc.CustomerVertxStub stub = CustomerGrpc.newVertxStub(channel);
    stub.createCustomer(HelloWorld.CustomerRequest.newBuilder()
          .build(), event -> {
      System.out.println(String.format("CustomerResponse: %s", event
            .result().toString()));
      System.exit(1);
    });
  }

  public static void main(String[] args) {
    final Vertx vertx = Vertx.vertx();
    vertx.deployVerticle(new ClientVerticle());
  }
}
