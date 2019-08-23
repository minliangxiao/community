package club.hl.community.community.provider;

import club.hl.community.community.dto.AccessTokenDTO;
import club.hl.community.community.dto.GithubUser;


import com.alibaba.fastjson.JSON;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;
//import okhttp3.MediaType;

/*
* 这个包是负责第三方的提供者
* */
@Component//这个注解将类初始化到spring的上下文（就是对象池）
public class GithubProvider {
    /*
    * 这个类主要提供了gitHub的登陆验证*/
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
       // MediaType JSON= MediaType.get("application/json; charset=utf-8");
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
//            切分返回的access_token
            String token= string.split("&")[0].split("=")[1];
            /*System.out.println(string);*/
           // System.out.println(token);
            return token;
        } catch (Exception e) {
            e.printStackTrace();

        }

        return null;//4e7fd8491e061d2750bf4940617a1b326d8a3d3f
    }
    public GithubUser getUser(String accessToken) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accessToken)
                .build();
        try {
            Response response=client.newCall(request).execute();
            String string = response.body().string();//这儿是string不是toString
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);
            return githubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    }
