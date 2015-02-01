package com.aarestu;

import com.aarestu.inject.HttpProxy;

public class Main {

    public static void main(String[] args) {
        HttpProxy httpProxy = new HttpProxy();
        httpProxy.setListenPort(6789);
        httpProxy.run();
    }
}
