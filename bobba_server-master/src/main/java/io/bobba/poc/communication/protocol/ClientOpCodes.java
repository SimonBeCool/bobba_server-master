package io.bobba.poc.communication.protocol;

public class ClientOpCodes {
    public final static int LOGIN = 1;
    public final static int REQUEST_MAP = 2;
    public final static int REQUEST_MOVEMENT = 7;
    public final static int REQUEST_CHAT = 9;
    public final static int REQUEST_LOOK_AT = 12;
    public final static int REQUEST_WAVE = 13;
    public final static int REQUEST_ROOM_DATA = 15;
    public final static int REQUEST_ITEM_INTERACT = 18;
    public final static int REQUEST_ITEM_MOVE = 19;
    public final static int REQUEST_ITEM_REMOVE = 20;
    public final static int REQUEST_USER_LOOK = 21;
    public final static int REQUEST_CHANGE_MOTTO = 22;
    public final static int REQUEST_INVENTORY_ITEMS = 23;
    public final static int REQUEST_ITEM_PLACE = 24;

    private ClientOpCodes() {

    }
}
