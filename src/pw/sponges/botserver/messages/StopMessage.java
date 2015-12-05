package pw.sponges.botserver.messages;

import org.json.JSONObject;
import pw.sponges.botserver.Client;
import pw.sponges.botserver.util.JSONBuilder;

public class StopMessage extends Message {

    public StopMessage(Client client) {
        super(client, "STOP");
    }

    @Override
    public JSONObject toJson() {
        return JSONBuilder.create(this).build();
    }

}