package io.bobba.poc.communication.incoming.rooms;

import io.bobba.poc.communication.incoming.IIncomingEvent;
import io.bobba.poc.communication.outgoing.SerializeRoomUserComposer;
import io.bobba.poc.communication.protocol.ClientMessage;
import io.bobba.poc.core.gameclients.GameClient;
import io.bobba.poc.core.rooms.users.RoomUser;
import io.bobba.poc.core.rooms.Room;

public class RequestUserLook implements IIncomingEvent {
    @Override
    public void handle(GameClient client, ClientMessage request) {
        String look = request.popString();
        
        Room room = client.getUser().getCurrentRoom();
        RoomUser user = client.getUser().getCurrentRoomUser();
        
        System.out.print(look);
        
        user.setLook(look);
        room.sendMessage(new SerializeRoomUserComposer(user));
    }
}