package org.example.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.StudentRequest;
import org.example.StudentResponse;
import org.example.StudentServceGrpc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Scanner;

public class Client {

    static Logger logger = LoggerFactory.getLogger(Client.class);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter student ID: ");
        int studentId = scanner.nextInt();
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost", 6565).usePlaintext().build();

        StudentServceGrpc.StudentServceBlockingStub blockingStub = StudentServceGrpc.newBlockingStub(managedChannel);


        StudentResponse studentResponse = blockingStub.getStudent(StudentRequest.newBuilder().setId(studentId).build());

        logger.info("response = "+ studentResponse.getName() +" " + studentResponse.getAge());


    }
}
