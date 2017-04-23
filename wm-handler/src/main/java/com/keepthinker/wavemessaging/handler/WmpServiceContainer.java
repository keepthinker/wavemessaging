package com.keepthinker.wavemessaging.handler;

import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.handler.proto.*;
import com.keepthinker.wavemessaging.proto.WmpMessage;
import com.keepthinker.wavemessaging.proto.WmpMessageMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class WmpServiceContainer {

    private final Map<WmpMessageMethod, ProtocolService<? extends WmpMessage>> services = new HashMap<>();
//    @Autowired
//    private PingRespService pingRespService;

    @Autowired
    private ApplicationContext context;

    public void put(WmpMessageMethod type, ProtocolService<WmpMessage> service) {
        services.put(type, service);
    }

    @SuppressWarnings("unchecked")
    public ProtocolService<WmpMessage> get(WmpMessageMethod type) {
        return (ProtocolService<WmpMessage>)services.get(type);
    }

    @PostConstruct
    public void init() {
        services.put(WmpMessageMethod.CONNECT, context.getBean(ConnectService.class));
        services.put(WmpMessageMethod.CONNACK, context.getBean(ConnAckService.class));
        services.put(WmpMessageMethod.DISCONNECT, context.getBean((DisconnectService.class)));
        services.put(WmpMessageMethod.PINGRESP, context.getBean(PingRespService.class));
        services.put(WmpMessageMethod.PUBLISH, context.getBean(PublishService.class));
        services.put(WmpMessageMethod.PUBACK, context.getBean(PubAckService.class));
    }

}
