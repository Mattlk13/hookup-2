package rs.hookupspring.springweb.services;

import com.google.gson.Gson;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import com.ning.http.client.multipart.Part;
import com.ning.http.client.multipart.StringPart;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.stereotype.Service;
import rs.hookupspring.entity.User;
import rs.hookupspring.springweb.dto.FcmJsonBody;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
* Created by Bandjur on 8/20/2016.
*/
@Service
public class FirebaseNotificationService {

    private final String PROJECT_KEY = "AIzaSyAbgfNI9_awcBn2g59hqlYXmEr-tbf78eQ";

    public void sendHookupNotification(User a, User b) {
        HttpClient httpClient = HttpClientBuilder.create().build(); //Use this instead

//        String userToken ="c1w-Q5Sh_NU:APA91bHDP7pDXQuRLg2ZMa2D5LcTup-c5bKhpcT-1VpeJ2Z-cnhtvb0JT7tQmZEsXJvuLqgfb6I4eOah7IGBu4tZnLHPeqvaPNKMR_uOgjItZdX7o8n0CBSrVsZjIzm1O89k5ZA1jgVM";

        FcmJsonBody fcmData = new FcmJsonBody();
        fcmData.setTo(a.getFirebaseInstaceToken());
        Map<String, String> data = new HashMap<String, String>();
        data.put("personName", b.getFirstname() + " " + b.getLastname());
        fcmData.setData(data);

        try {
            HttpPost request = new HttpPost("https://fcm.googleapis.com/fcm/send");
            String jsonExample = new Gson().toJson(fcmData);
            StringEntity params = new StringEntity(new Gson().toJson(fcmData));
            request.addHeader("content-type", "application/json");
            request.addHeader("authorization", "key="+PROJECT_KEY);
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);

        }catch (Exception ex) {
            // handle exception here
        } finally {
            httpClient.getConnectionManager().shutdown(); //Deprecated
        }
    }


    public void httpApachePostExample() {
        HttpClient httpClient = HttpClientBuilder.create().build(); //Use this instead

        String userToken ="c1w-Q5Sh_NU:APA91bHDP7pDXQuRLg2ZMa2D5LcTup-c5bKhpcT-1VpeJ2Z-cnhtvb0JT7tQmZEsXJvuLqgfb6I4eOah7IGBu4tZnLHPeqvaPNKMR_uOgjItZdX7o8n0CBSrVsZjIzm1O89k5ZA1jgVM";

        FcmJsonBody fcmData = new FcmJsonBody();
        fcmData.setTo(userToken);
        Map<String, String> data = new HashMap<String, String>();
        data.put("message","Message tebra");
        data.put("personName","Ime tebra");
        fcmData.setData(data);

        try {
            HttpPost request = new HttpPost("https://fcm.googleapis.com/fcm/send");
//            StringEntity params =new StringEntity("details={\"name\":\"myname\",\"age\":\"20\"} ");
            String jsonExample = new Gson().toJson(fcmData);
            StringEntity params = new StringEntity(new Gson().toJson(fcmData));
            request.addHeader("content-type", "application/json");
            request.addHeader("authorization", "key="+PROJECT_KEY);
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);

//            response.sta

            // handle response here...
        }catch (Exception ex) {
            // handle exception here
        } finally {
            httpClient.getConnectionManager().shutdown(); //Deprecated
        }
    }

    public void asyncHttpTest() throws UnsupportedEncodingException {
        String url = "https://fcm.googleapis.com/fcm/send";

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        AsyncHttpClient.BoundRequestBuilder builder = asyncHttpClient.preparePost(url);

//        boundRequestBuilder.addBodyPart(new StringPart("to","TOKEN FROM DB"));

        String userToken ="c1w-Q5Sh_NU:APA91bHDP7pDXQuRLg2ZMa2D5LcTup-c5bKhpcT-1VpeJ2Z-cnhtvb0JT7tQmZEsXJvuLqgfb6I4eOah7IGBu4tZnLHPeqvaPNKMR_uOgjItZdX7o8n0CBSrVsZjIzm1O89k5ZA1jgVM";


        FcmJsonBody fcmData = new FcmJsonBody();
        fcmData.setTo(userToken);
        Map<String, String> data = new HashMap<String, String>();
        data.put("message","Message tebra");
        data.put("personName","Ime tebra");
        fcmData.setData(data);

//        StringEntity entity = new StringEntity(new Gson().toJson(fcmData));
        builder.addBodyPart(new StringPart("to",userToken));
        builder.addBodyPart(new StringPart("priority", "high"));

//        asyncHttpClient.post(this, url, entity, "application/json",
//                responseHandler);

//        boundRequestBuilder.addBodyPart(new StringPart("notification", new StringPart()));


        builder.setHeader("Content-Type", "application/json");
        builder.setHeader("Authorization:key", PROJECT_KEY);

        builder.execute(new AsyncCompletionHandler<Void>() {
            @Override
            public Void onCompleted(Response response) throws Exception {

                return null;
            }

            @Override
            public void onThrowable(Throwable t) {
//                LOG.error("Got exception while posting {}", elem, t);
//                latch.countDown();
            }
        });

    }

//    public void test(){
//        String host = "fcm.googleapis.com";
//        String requestURI = "/fcm/send";
//        String CLIENT_TOKEN = "ASK_YOUR_MOBILE_CLIENT_DEV"; // https://developers.google.com/instance-id/
//
//        CompletableFuture<String> fut = new CompletableFuture<String>();
//        JsonObject body = new JsonObject();
//        // JsonArray registration_ids = new JsonArray();
//        // body.put("registration_ids", registration_ids);
//        body.put("to", CLIENT_TOKEN);
//        body.put("priority", "high");
//        // body.put("dry_run", true);
//
//        JsonObject notification = new JsonObject();
//        notification.put("body", "body string here");
//        notification.put("title", "title string here");
//        // notification.put("icon", "myicon");
//
//        JsonObject data = new JsonObject();
//        data.put("key1", "value1");
//        data.put("key2", "value2");
//
//        body.put("notification", notification);
//        body.put("data", data);
//
//        HttpClientRequest hcr = httpsClient.post(443, host, requestURI).handler(response -> {
//            response.bodyHandler(buffer -> {
//                logger.debug("FcmTest1 rcvd: {}, {}", response.statusCode(), buffer.toString());
//                if (response.statusCode() == 200) {
//                    fut.complete(buffer.toString());
//                } else {
//                    fut.completeExceptionally(new RuntimeException(buffer.toString()));
//                }
//            });
//        });
//        hcr.putHeader("Authorization", "key=" + Utils.FIREBASE_SERVER_KEY)
//                .putHeader("content-type", "application/json").end(body.encode());
//        return fut;
//
//    }

}
