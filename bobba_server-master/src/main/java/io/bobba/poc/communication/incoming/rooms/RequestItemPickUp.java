package io.bobba.poc.communication.incoming.rooms;

import io.bobba.poc.communication.incoming.IIncomingEvent;
import io.bobba.poc.communication.protocol.ClientMessage;
import io.bobba.poc.core.gameclients.GameClient;
import io.bobba.poc.core.rooms.users.RoomUser;

public class RequestItemPickUp implements IIncomingEvent {
    @Override
    public void handle(GameClient client, ClientMessage request) {
        int itemId = request.popInt();

        RoomUser user = client.getUser().getCurrentRoomUser();
        if (user != null){
        	user.furniaddInventory(itemId);
            user.furniRemove(itemId);
        }
    }
}