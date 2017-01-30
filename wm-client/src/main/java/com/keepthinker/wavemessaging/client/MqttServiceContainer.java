package com.keepthinker.wavemessaging.client;

import com.keepthinker.wavemessaging.core.ProtocolService;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class MqttServiceContainer {

    private final Map<MqttMessageType, ProtocolService<MqttMessage>> services = new HashMap<>();
    @Autowired
    private PingRespService pingRespService;

    public void put(MqttMessageType type, ProtocolService<MqttMessage> service) {
        services.put(type, service);
    }

    public ProtocolService<MqttMessage> get(MqttMessageType type) {
        return services.get(type);
    }

    @PostConstruct
    public void init() {
        services.put(MqttMessageType.PINGRESP, pingRespService);
    }

}
