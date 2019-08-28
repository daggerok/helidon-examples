package com.github.daggerok;

import io.helidon.grpc.server.GrpcRouting;
import io.helidon.grpc.server.GrpcServer;
import io.helidon.grpc.server.GrpcServerConfiguration;
import io.helidon.grpc.server.GrpcService;
import io.helidon.grpc.server.ServiceDescriptor.Rules;
import io.vavr.control.Try;
import lombok.extern.log4j.Log4j2;

import java.util.concurrent.TimeUnit;

@Log4j2
public class Main {

    public static void main(String[] args) {

        Try.of(() -> GrpcServer.create(GrpcServerConfiguration.builder()
                                                              .port(8080)
                                                              .build(),
                                       GrpcRouting.builder()
                                                  .register(new HelloService())
                                                  .build())
                               .start()
                               .toCompletableFuture()
                               .get(10, TimeUnit.SECONDS))
           .onSuccess(grpcServer -> log.info("gRPC Server started on {} port!", grpcServer.port()))
           .onFailure(throwable -> log.error("oops! {}", throwable.getLocalizedMessage()));
    }

    static class HelloService implements GrpcService {

        @Override
        public void update(Rules rules) {
            rules.unary("SayHello", ((req, res) -> complete(res, "Hello " + req)));
        }
    }
}
