package rs.hookupspring.springweb.services;

import com.google.gson.Gson;
import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Response;
import com.ning.http.client.multipart.StringPart;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.hookupspring.common.enums.Enums;
import rs.hookupspring.common.utils.DateUtils;
import rs.hookupspring.dao.HookupRepository;
import rs.hookupspring.entity.Hookup;
import rs.hookupspring.entity.User;
import rs.hookupspring.springweb.dto.FcmJsonBody;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
* Created by Bandjur on 8/20/2016.
*/
@Service
public class FirebaseNotificationService {

    @Autowired
    private UserHookupService userHookupService;

    @Autowired
    private HookupRepository hookupRepository;

    private final String PROJECT_KEY = "AIzaSyAbgfNI9_awcBn2g59hqlYXmEr-tbf78eQ";

    public void sendHookupNotification(User a, User b) {
        HttpClient httpClient = HttpClientBuilder.create().build(); //Use this instead

        if(!isHookupPairValidToBeNotified(a, b)) {
            return;
        }

        String test = Boolean.toString(userHookupService.findHookupPair(a,b).isRecommended());

        FcmJsonBody fcmData = new FcmJsonBody();
        fcmData.setTo(a.getFirebaseInstaceToken());
        Map<String, String> data = new HashMap<String, String>();
        data.put("personName", b.getFirstname() + " " + b.getLastname());
        data.put("hookupUserUID", b.getFirebaseUID());
        data.put("recommended", Boolean.toString(userHookupService.findHookupPair(a,b).isRecommended()));
        fcmData.setData(data);

        try {
            HttpPost request = new HttpPost("https://fcm.googleapis.com/fcm/send");
            String jsonExample = new Gson().toJson(fcmData);
            StringEntity params = new StringEntity(new Gson().toJson(fcmData));
            request.addHeader("content-type", "application/json");
            request.addHeader("authorization", "key=" + PROJECT_KEY);
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);

        }catch (Exception ex) {
            // handle exception here
        } finally {
            httpClient.getConnectionManager().shutdown(); //Deprecated
        }
    }

    private boolean isHookupPairValidToBeNotified(User a, User b) {
        Hookup hookupPair = userHookupService.findHookupPair(a, b);
        int hookupRequestSentCount = hookupPair.getHookupRequestSentCount();
        if(hookupPair.getHookupRequestDate() != null) {
            long dateDifferenceInMinutes = DateUtils.getDatetimeDifference(new Date(),
                    hookupPair.getHookupRequestDate(), Enums.TimeUnit.Minute);

//            if(hookupRequestSentCount >= 2) { // == 2 treba
//                return false;
//            }

//            if(dateDifferenceInMinutes < 5) {
//            if(dateDifferenceInMinutes < 5 || hookupRequestSentCount == 2 ) {
            if( hookupRequestSentCount >=2 && dateDifferenceInMinutes < 5) { // == 2 treba
                return false;
            }
            else {
                // reset counters and send new request again
                if(hookupRequestSentCount>=2) {
                    hookupRequestSentCount = 0;
                    hookupPair.setHookupRequestSentCount(hookupRequestSentCount);
                    hookupPair.setHookupPositiveResponseCount(0);
                }

                hookupPair.setHookupRequestSentCount(hookupRequestSentCount+1);
                hookupPair.setHookupRequestDate(new Date());
                hookupRepository.save(hookupPair);
            }
        }
        else {
            hookupPair.setHookupRequestDate(new Date());
            hookupPair.setHookupRequestSentCount(1);
            hookupRepository.save(hookupPair);
            return true;
        }

        return true;
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

    public void sendFcmDataTest(User a, User b, boolean recommended) {
        HttpClient httpClient = HttpClientBuilder.create().build();

        FcmJsonBody fcmData = new FcmJsonBody();
        fcmData.setTo(a.getFirebaseInstaceToken());
        Map<String, String> data = new HashMap<String, String>();
        data.put("personName", b.getFirstname() + " " + b.getLastname());
        data.put("hookupUserUID", b.getFirebaseUID());
        data.put("recommended", Boolean.toString(recommended));
//        data.put("recommended", Boolean.toString(userHookupService.findHookupPair(a, b).isRecommended()));
        fcmData.setData(data);

        try {
            HttpPost request = new HttpPost("https://fcm.googleapis.com/fcm/send");
            String jsonExample = new Gson().toJson(fcmData);
            StringEntity params = new StringEntity(new Gson().toJson(fcmData));
            request.addHeader("content-type", "application/json");
            request.addHeader("authorization", "key=" + PROJECT_KEY);
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);

        }catch (Exception ex) {
            // handle exception here
        } finally {
            httpClient.getConnectionManager().shutdown(); //Deprecated
        }
    }

    public void sendFriendsNotification(User a, User b) {
        HttpClient httpClient = HttpClientBuilder.create().build();

        FcmJsonBody fcmData = new FcmJsonBody();
        fcmData.setTo(a.getFirebaseInstaceToken());
        Map<String, String> data = new HashMap<String, String>();
        data.put("personName", b.getFirstname() + " " + b.getLastname());
        data.put("hookupUserUID", b.getFirebaseUID());
        data.put("success_paired", "true");
        fcmData.setData(data);

        try {
            HttpPost request = new HttpPost("https://fcm.googleapis.com/fcm/send");
            String jsonExample = new Gson().toJson(fcmData);
            StringEntity params = new StringEntity(new Gson().toJson(fcmData));
            request.addHeader("content-type", "application/json");
            request.addHeader("authorization", "key=" + PROJECT_KEY);
            request.setEntity(params);
            HttpResponse response = httpClient.execute(request);

        }catch (Exception ex) {
            // handle exception here
        } finally {
            httpClient.getConnectionManager().shutdown(); //Deprecated
        }
    }

}
