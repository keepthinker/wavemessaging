package com.keepthinker.wavemessaging.proto;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by keepthinker on 2017/3/12.
 */
public enum WmpMessageMethod {
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

    private static Map<Integer, WmpMessageMethod> map = new HashMap<>();

    static {
        WmpMessageMethod[] wmpMethods = WmpMessageMethod.values();
        for(int i = 0; i < wmpMethods.length; i++) {
            map.put(wmpMethods[i].code, wmpMethods[i]);
        }
    }


    private int code;

    WmpMessageMethod(int code){
        this.code = code;
    }

    public int getCode(){
        return code;
    }

    public static WmpMessageMethod toEnum(int code){
        return map.get(code);
    }
}
