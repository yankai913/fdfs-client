package com.zoo.fdfs.support;

import java.net.Socket;

/**
 * 
 * @author yankai913@gmail.com
 * @date 2014-3-13
 */
public class DefaultFSocket implements FSocket {

    private Socket socket;


    public DefaultFSocket(Socket socket) {
        this.socket = socket;
    }


    public Socket getSocket() {
        return socket;
    }

}
