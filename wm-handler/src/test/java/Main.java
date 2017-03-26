import com.keepthinker.wavemessaging.core.utils.WmpActionLogger;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Main {

    public static void main(String[] args) {
        Map<String, String> map = new ConcurrentHashMap<>();
        map.put("key1", "value1");
        Iterator<String> str = map.values().iterator();

        System.out.println(map);

        WmpActionLogger.connect("123", 123);
    }

}
