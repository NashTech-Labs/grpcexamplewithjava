package org.example;

import io.grpc.ManagedChannel;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.stub.StreamObserver;
import io.grpc.testing.GrpcCleanupRule;
import org.example.client.Client;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.junit.Assert.assertEquals;

public class ClientTest {

    private static final Logger logger = LoggerFactory.getLogger(Client.class);
    private StudentServceGrpc.StudentServceBlockingStub blockingStub;
    private ManagedChannel managedChannel;

    @Before
    public void setup() {
        // Create a mock ManagedChannel
        managedChannel = Mockito.mock(ManagedChannel.class);
        // Create a stub using the mock ManagedChannel
        blockingStub = StudentServceGrpc.newBlockingStub(managedChannel);
    }

    @Rule
    public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

    /**
     * To test the server, make calls with a real stub using the in-process channel, and verify
     * behaviors or state changes from the client side.
     */
    @Test
    public void greeterImpl_replyMessage() throws Exception {
        // Generate a unique in-process server name.
        String serverName = InProcessServerBuilder.generateName();

        // Create a server, add service, start, and register for automatic graceful shutdown.
        grpcCleanup.register(InProcessServerBuilder
                .forName(serverName).directExecutor().addService(new StudentServceGrpc.StudentServceImplBase() {
                    @Override
                    public void getStudent(StudentRequest request, StreamObserver<StudentResponse> responseObserver) {
                        // Create a mock response
                        StudentResponse response = StudentResponse.newBuilder().setName("John Doe").build();
                        // Send the response
                        responseObserver.onNext(response);
                        responseObserver.onCompleted();
                    }
                }).build().start());

        // Create a client channel and register for automatic graceful shutdown.
        blockingStub = StudentServceGrpc.newBlockingStub(
                grpcCleanup.register(InProcessChannelBuilder.forName(serverName).directExecutor().build()));

        // Create a request
        StudentRequest request = StudentRequest.newBuilder().setId(1).build();

        // Make the call and get the response
        StudentResponse response = blockingStub.getStudent(request);

        // Verify the response
        assertEquals("John Doe", response.getName());
    }
}
