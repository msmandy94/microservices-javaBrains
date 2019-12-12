package com.example.moviecatalogservice.test;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import rx.Observable;

public class MyCommand extends HystrixObservableCommand {
    protected MyCommand(HystrixCommandGroupKey group) {
        super(group);
    }

    protected MyCommand(Setter setter) {
        super(setter);
    }

    @Override
    protected Observable construct() {

        return null;
    }
}
