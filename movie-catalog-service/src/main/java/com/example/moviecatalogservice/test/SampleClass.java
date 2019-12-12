package com.example.moviecatalogservice.test;

import com.netflix.hystrix.HystrixCommand;

public class SampleClass extends HystrixCommand<String> {
    protected SampleClass(Setter setter) {
        super(setter);
    }

    @Override
    protected String run() throws Exception {
        CallBack callback = new CallBack();

        someMethod(callback);
        // someMethod is an async call and it returns immediately. The async call has callback functionality. callback is a diffferent thread. hence i need a new object for each hystrix command.
        // callback will have exceptions which can't be handled here. run() method should not wait for callback.

        return null;
    }

    private String someMethod(CallBack callback) {
        System.out.println("i'm somemethod");
        System.out.println("i'm failig with runtime exveption");
        callback.callback(new RuntimeException("some method is failing"));
        return "somemethod is succeded";
    }

    @Override
    protected String getFallback() {
        // fallback logic goes here.
        return "fallback";
    }

    public class CallBack {
        void callback(Exception e) {
            if (e != null) {
                getFallback();  // call getFallback() explicitly.
            }
        }
    }
}
