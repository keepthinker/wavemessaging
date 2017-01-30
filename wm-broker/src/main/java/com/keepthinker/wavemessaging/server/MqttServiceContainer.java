package com.keepthinker.wavemessaging.server;

import com.keepthinker.wavemessaging.core.ProtocolService;
import com.keepthinker.wavemessaging.server.proto.ConnectService;
import com.keepthinker.wavemessaging.server.proto.PingReqService;
import io.netty.handler.codec.mqtt.MqttMessage;
import io.netty.handler.codec.mqtt.MqttMessageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

@Service
public class MqttServiceContainer {

    private final Map<MqttMessageType, ProtocolService<? extends MqttMessage>> services = new HashMap<>();
    @Autowired
    private PingReqService pingReqService;
    @Autowired
    private ConnectService connectService;

    public void put(MqttMessageType type, ProtocolService<MqttMessage> service) {
        services.put(type, service);
    }

    @SuppressWarnings("unchecked")
    public ProtocolService<MqttMessage> get(MqttMessageType type) {
        return (ProtocolService<MqttMessage>) services.get(type);
    }

    @PostConstruct
    public void init() {
        services.put(MqttMessageType.PINGREQ, pingReqService);
        services.put(MqttMessageType.CONNECT, connectService);
    }

}
