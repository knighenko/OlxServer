package pushy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SendPush {
    public static void sendSamplePush() {
        // Prepare list of target device tokens
        List<String> deviceTokens = new ArrayList<>();

        // Add your device tokens here
        deviceTokens.add("2e4f84e963d35d05eeb354");

        // Convert to String[] array
        String[] to = deviceTokens.toArray(new String[deviceTokens.size()]);

        // Optionally, send to a publish/subscribe topic instead
        // String to = '/topics/news';

        // Set payload (any object, it will be serialized to JSON)
        Map<String, String> payload = new HashMap<>();

        // Add "message" parameter to payload
        payload.put("message", "Hello World!");

        // iOS notification fields
        Map<String, Object> notification = new HashMap<>();

        notification.put("badge", 1);
        notification.put("sound", "ping.aiff");
        notification.put("body", "Hello World \u270c");

        // Prepare the push request
        PushyAPI.PushyPushRequest push = new PushyAPI.PushyPushRequest(payload, to, notification);

        try {
            // Try sending the push notification
            PushyAPI.sendPush(push);
        }
        catch (Exception exc) {
            // Error, print to console
            System.out.println(exc.toString());
        }
    }
}
