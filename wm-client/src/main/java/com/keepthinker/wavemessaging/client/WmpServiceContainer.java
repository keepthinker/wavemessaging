package com.keepthinker.wavemessaging.client;

import com.keepthinker.wavemessaging.proto.WmpMessage;
import com.keepthinker.wavemessaging.proto.WmpMessageMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class WmpServiceContainer {

    private final Map<WmpMessageMethod, ProtocolService<WmpMessage>> services = new HashMap<>();
    @Autowired
    private PingRespService pingRespService;

    public void put(WmpMessageMethod type, ProtocolService<WmpMessage> service) {
        services.put(type, service);
    }

    public ProtocolService<WmpMessage> get(WmpMessageMethod type) {
        return services.get(type);
    }

    @PostConstruct
    public void init() {
        services.put(WmpMessageMethod.PINGRESP, pingRespService);
    }

}
