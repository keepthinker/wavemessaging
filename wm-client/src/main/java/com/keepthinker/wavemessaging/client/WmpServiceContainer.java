package com.keepthinker.wavemessaging.client;

import com.keepthinker.wavemessaging.client.proto.ConnAckService;
import com.keepthinker.wavemessaging.client.proto.PingRespService;
import com.keepthinker.wavemessaging.client.proto.ProtocolService;
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

    @Autowired
    private ApplicationContext context;

    private final Map<WmpMessageMethod, ProtocolService<? extends WmpMessage>> services = new HashMap<>();

    public void put(WmpMessageMethod type, ProtocolService<WmpMessage> service) {
        services.put(type, service);
    }

    public ProtocolService<WmpMessage> get(WmpMessageMethod type) {
        return (ProtocolService<WmpMessage>) services.get(type);
    }

    @PostConstruct
    public void init() {
        services.put(WmpMessageMethod.PINGRESP, context.getBean(PingRespService.class));
        services.put(WmpMessageMethod.CONNACK, context.getBean(ConnAckService.class));
    }

}
