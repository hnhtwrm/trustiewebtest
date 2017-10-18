package service;

import bean.UrlAndAnswer;
import bean.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class TestService {


    //记录当前用户名和密码的下标
    private int currentIndexOfUsers=0;

    private ArrayList<User> users;

    private ArrayList<UrlAndAnswer> urlAndAnswers;

    //单例模式
    private volatile static TestService testService;

    public static TestService getInstance() {
        if (testService == null) {
            synchronized (TestService.class) {
                if (testService == null) {
                    testService = new TestService();
                }
            }
        }
        return testService;
    }


    private TestService(){
        try{

            //初始化users集合
            Reader reader = new InputStreamReader(TestService.class.getResourceAsStream("/userandpwd.json"), "UTF-8");
            Type type = new TypeToken<ArrayList<User>>(){}.getType();
            Gson gson = new GsonBuilder().create();
            this.users = gson.fromJson(reader,type);

            //初始化urlandAnswers集合
            reader = new InputStreamReader(TestService.class.getResourceAsStream("/urlandanswer.json"), "UTF-8");
            type = new TypeToken<ArrayList<UrlAndAnswer>>(){}.getType();
            this.urlAndAnswers = gson.fromJson(reader,type);

        }catch (Exception e){
            throw new RuntimeException("userandpwd.json或者urlandanswer.json可能已经丢失了,请把文件放到resources目录下");
        }

    }

    //如果队列中还有用户则返回用户，否则返回null
    public synchronized User getUser(){
        if(currentIndexOfUsers<users.size()){
            return users.get(currentIndexOfUsers++);
        }
        return null;
    }


    public String getAnswer(String url){
        for(UrlAndAnswer urlAndAnswer:urlAndAnswers){
            if(url!=null && url.equals(urlAndAnswer.getUrl())){
                return urlAndAnswer.getAnswer();
            }
        }
        return null;
    }

    public String getUrl(int index){
        if(index>=0 && index<urlAndAnswers.size()){
            return urlAndAnswers.get(index).getUrl();
        }
        return null;
    }

}
