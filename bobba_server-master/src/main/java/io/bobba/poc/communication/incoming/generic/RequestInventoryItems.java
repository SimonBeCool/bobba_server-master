package io.bobba.poc.communication.incoming.generic;

import io.bobba.poc.BobbaEnvironment;
import io.bobba.poc.communication.incoming.IIncomingEvent;
import io.bobba.poc.communication.outgoing.InventarComposer;
import io.bobba.poc.communication.protocol.ClientMessage;
import io.bobba.poc.core.Game;
import io.bobba.poc.core.gameclients.GameClient;
import io.bobba.poc.core.rooms.*;
import io.bobba.poc.core.rooms.items.RoomItem;


public class RequestInventoryItems implements IIncomingEvent {
    @Override
    public void handle(GameClient client, ClientMessage request) {
    	Game.Inventar();
    	return;
    }
}
