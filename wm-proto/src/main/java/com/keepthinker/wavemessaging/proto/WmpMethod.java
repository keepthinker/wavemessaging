package com.keepthinker.wavemessaging.proto;

/**
 * Created by keepthinker on 2017/3/12.
 */
public enum WmpMethod {
    RESERVED(0),
    /**
     Client to Server
     Client request to connect to Server
     */
    CONNECT(1),
    /**
     Server to Client
     Connect acknowledgment
     */
    CONNACK(2),
    /**
     Client to Server
     or
     Server to Client
     Publish message
     */
    PUBLISH(3),
    /**
     Client to Server
     or
     Server to Client
     Publish acknowledgment
     */
    PUBACK(4),
    /**
     Client to Server
     Client subscribe request
     */
    SUBSCRIBE(5),
    /**
     Server to Client
     Subscribe acknowledgment
     */
    SUBACK(6),
    /**
     Client to Server
     Unsubscribe request
     */
    UNSUBSCRIBE(7),
    /**
     Server to Client
     Unsubscribe acknowledgment
     */
    UNSUBACK(8),
    /**
     Client to Server
     PING request
     */
    PINGREQ(9),
    /**
     Server to Client
     PING response
     */
    PINGRESP(10),

    DISCONNECT(11);

    private int code;
    WmpMethod(int code){
        this.code = code;
    }

    public int getCode(){
        return code;
    }
}
