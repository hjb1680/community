package com.lanying.community.demo.provider;

import com.alibaba.fastjson.JSON;
import com.lanying.community.demo.dto.AccessTokenDto;
import com.lanying.community.demo.dto.GitHubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Component
public class GitHubProvider {

    public String getAccessToken(AccessTokenDto dto) {
        MediaType JSON1
                = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        String json = JSON.toJSONString(dto);

            RequestBody body = RequestBody.create(json, JSON1);
            Request request = new Request.Builder()
                    .url("https://github.com/login/oauth/access_token")
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                String string = response.body().string();

                System.out.println(string);
                String s = string.split("&")[0].split("=" )[1];
                return s;
            } catch (IOException e) {
                e.printStackTrace();
            }
        return  null;
    }

    public GitHubUser getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            GitHubUser gitHubUser = JSON.parseObject(response.body().string(), GitHubUser.class);
            return gitHubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
