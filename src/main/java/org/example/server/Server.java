package org.example.server;


import org.example.StudentResponse;
import org.example.StudentServceGrpc;
import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@GRpcService
public class Server extends StudentServceGrpc.StudentServceImplBase {

    Logger logger = LoggerFactory.getLogger(Server.class);

    public void getStudent(org.example.StudentRequest request,
                           io.grpc.stub.StreamObserver<org.example.StudentResponse> responseObserver) {

        logger.info("got request = "+ request.getId());
        StudentResponse studentResponse =  StudentResponse.newBuilder().setAge(25).setName("Akshit Kumar").build();
        responseObserver.onNext(studentResponse);
        responseObserver.onCompleted();
    }
}
