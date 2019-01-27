package com.keepthinker.wavemessaging.nosql;

import java.util.List;

/**
 * Created by keepthinker on 2019/1/27.
 */
public interface ScanHandler {
    void handle(List<String> values);
}
