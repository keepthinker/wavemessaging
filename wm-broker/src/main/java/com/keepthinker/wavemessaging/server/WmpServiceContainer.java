package com.keepthinker.wavemessaging.server;

import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.proto.WmpMessage;
import com.keepthinker.wavemessaging.proto.WmpMessageMethod;
import com.keepthinker.wavemessaging.server.proto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class WmpServiceContainer {

    private final Map<WmpMessageMethod, ProtocolService<? extends WmpMessage>> services = new HashMap<>();

    @Autowired
    private ApplicationContext context;

    public void put(WmpMessageMethod type, ProtocolService<WmpMessage> service) {
        services.put(type, service);
    }

    @SuppressWarnings("unchecked")
    public ProtocolService<WmpMessage> get(WmpMessageMethod type) {
        return (ProtocolService<WmpMessage>) services.get(type);
    }

    @PostConstruct
    public void init() {
        services.put(WmpMessageMethod.PINGREQ, context.getBean(PingReqService.class));
        services.put(WmpMessageMethod.CONNECT, context.getBean(ConnectService.class));
        services.put(WmpMessageMethod.CONNACK, context.getBean(ConnAckService.class));
        services.put(WmpMessageMethod.PUBLISH, context.getBean(PublishService.class));
        services.put(WmpMessageMethod.PUBACK, context.getBean(PubAckService.class));
        services.put(WmpMessageMethod.SUBSCRIBE, context.getBean(SubscribeService.class));
        services.put(WmpMessageMethod.SUBACK, context.getBean(SubAckService.class));
    }

}
