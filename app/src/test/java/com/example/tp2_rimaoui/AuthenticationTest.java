package com.example.tp2_rimaoui;

import android.content.Context;
import android.os.Build;

import com.android.volley.RequestQueue;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;

import myrequestt.Myrequest;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = {Build.VERSION_CODES.P})
public class AuthenticationTest {
    ServeurIP serveurIP;

    @Before
    public void setUp() {
        serveurIP = new ServeurIP(); // Initialize the serveurIP object
    }

    @Test
    public void testSignInWithLoginAndPassword() {
        Context mockContext = Mockito.mock(Context.class);
        RequestQueue mockQueue = Mockito.mock(RequestQueue.class);
        String mockIPV4_server = serveurIP.getIPV4_serveur();
        Myrequest request = new Myrequest(mockContext, mockQueue, mockIPV4_server);
        String username = "testuser";
        String password = "testpassword";

        // Mock the LoginCallback interface :
        Myrequest.LoginCallback callback = new Myrequest.LoginCallback() {
            @Override
            public void onSuccess(String id, String pseudo) {
                assertNotNull(id);
                assertNotNull(pseudo);
            }

            @Override
            public void onError(String message) {
                fail(message);
            }
        };
        request.connection(username, password, callback);
    }
}
