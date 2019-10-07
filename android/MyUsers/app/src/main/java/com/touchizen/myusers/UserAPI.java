package com.touchizen.myusers;

import android.os.Bundle;
import android.os.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserAPI {
    public static final String LIST_USERS_URL = "http://13.125.254.189/users";
    public static final String CREATE_USER_URL = LIST_USERS_URL;
    public static final String USER_URL = "http://13.125.254.189/users/";
    public static int nPage = -1;
    public static int nCount = -1;

    public static ArrayList<UserItem> getUserList() {
        String result = get(LIST_USERS_URL);
        ArrayList<UserItem> users = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(result);
            nCount = (int) jsonObject.get("count");
            nPage = (int) jsonObject.get("page");
            String jStr = (String)jsonObject.get("users");
            JSONArray jsonArray = new JSONArray(jStr);

            for(int i=0; i<jsonArray.length();i++) {
                JSONObject jone = (JSONObject) jsonArray.get(i);
                int id = (int) jone.get("id");
                String sName = (String) jone.get("username");
                String sEmail = (String) jone.get("useremail");
                String sPhone = (String) jone.get("userphone");
                String sDesc = (String) jone.get("userdesc");
                int nViews = (int) jone.get("views");

                UserItem oneItem = new UserItem();
                oneItem.setUserId(id);
                oneItem.setUserName(sName);
                oneItem.setUserEmail(sEmail);
                oneItem.setUserPhone(sPhone);
                oneItem.setUserDesc(sDesc);
                oneItem.setViews(nViews);

                users.add( oneItem);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return users;
    }

    public static void createUser(UserItem userItem ) {
        post(CREATE_USER_URL, userItem.toJSON().toString() );
    }

    public static void updateUser(UserItem userItem) {
        put(USER_URL+userItem.getUserId(), userItem.toJSON().toString() );
    }

    public static void deleteUser(int id ) {
        delete(USER_URL+ Integer.toString(id) );
    }


    public static String get(String requestURL) {
        String message = null;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(requestURL)
                    .build(); //GET Request

            //동기 처리시 execute함수 사용
            Response response = client.newCall(request).execute();

            //출력
            message = response.body().string();
            System.out.println(message);

        } catch (Exception e) {
            System.err.println(e.toString());
        }
        return message;
    }

    public static void getAsync(String requestURL) {

        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(requestURL)
                    .build();

            //비동기 처리 (enqueue 사용)
            client.newCall(request).enqueue(new Callback() {
                //비동기 처리를 위해 Callback 구현
                @Override
                public void onFailure(Call call, IOException e) {
                    System.out.println("error + Connect Server Error is " + e.toString());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    System.out.println("Response Body is " + response.body().string());
                }
            });

        } catch (Exception e){
            System.err.println(e.toString());
        }
    }


    public static String post(String requestURL, String jsonMessage) {
        String message = null;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(requestURL)
                    .post(RequestBody.create(MediaType.parse("application/json"), jsonMessage)) //POST로 전달할 내용 설정
                    .build();

            //동기 처리시 execute함수 사용
            Response response = client.newCall(request).execute();

            //출력
            message = response.body().string();
            System.out.println(message);

        } catch (Exception e) {
            System.err.println(e.toString());
        }

        return message;
    }

    public static String put(String requestURL, String jsonMessage) {
        String message = null;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(requestURL)
                    .put(RequestBody.create(MediaType.parse("application/json"), jsonMessage)) //PUT로 전달할 내용 설정
                    .build();

            //동기 처리시 execute함수 사용
            Response response = client.newCall(request).execute();

            //출력
            message = response.body().string();
            System.out.println(message);

        } catch (Exception e) {
            System.err.println(e.toString());
        }

        return message;
    }

    public static String delete(String requestURL) {
        String message = null;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(requestURL)
                    .delete() //DELETE로 전달할 내용 설정
                    .build();

            //동기 처리시 execute함수 사용
            Response response = client.newCall(request).execute();

            //출력
            message = response.body().string();
            System.out.println(message);

        } catch (Exception e) {
            System.err.println(e.toString());
        }

        return message;
    }
}
