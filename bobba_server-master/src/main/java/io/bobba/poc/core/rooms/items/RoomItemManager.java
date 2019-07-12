package io.bobba.poc.core.rooms.items;

import io.bobba.poc.BobbaEnvironment;
import io.bobba.poc.communication.outgoing.FurniRemoveComposer;
import io.bobba.poc.communication.outgoing.InventarComposer;
import io.bobba.poc.communication.outgoing.InventarItemRemoveComposer;
import io.bobba.poc.communication.outgoing.SerializeFloorItemComposer;
import io.bobba.poc.communication.outgoing.SerializeWallItemComposer;
import io.bobba.poc.core.Game;
import io.bobba.poc.core.items.BaseItem;
import io.bobba.poc.core.items.BaseItemManager;
import io.bobba.poc.core.rooms.Room;
import io.bobba.poc.core.rooms.users.RoomUser;
import io.bobba.poc.misc.logging.LogLevel;
import io.bobba.poc.misc.logging.Logging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomItemManager {
    private Map<Integer, RoomItem> floorItems;
    private Map<Integer, WallItem> wallItems;
    private Room room;
    private BaseItemManager base;
	private double z;
	public RoomItemManager(Room room) {
        this.room = room;
        floorItems = new HashMap<>();
        wallItems = new HashMap<>();
    }

    public RoomItem getItem(int id) {
        if (floorItems.containsKey(id))
            return floorItems.get(id);
        if (wallItems.containsKey(id))
            return wallItems.get(id);
        return null;
    }

    public List<RoomItem> getFloorItems() {
        return new ArrayList<>(floorItems.values());
    }

    public List<WallItem> getWallItems() {
        return new ArrayList<>(wallItems.values());
    }
    
    
    // ADD ITEM TO FLOOR OR WALL
    
    public void addTo(int id, int x, int y, int rotation) {
   	 	RoomItem item = getItem(id);
   	 	if (item == null) {
   	 		if(item instanceof WallItem) {
   	 			System.out.print("WAND 1");
   	 			room.getRoomItemManager().addWallItemToRoom(id, x, y, rotation, item.getState(), item.getBaseItem());
   	 		} else {
   	 			System.out.print("BODEN 1");
   	 			room.getRoomItemManager().addFloorItemToRoom(id, x, y, item.getZ(), rotation, item.getState(), item.getBaseItem());
   	 		}
	 	}
   	 	if (item != null) {
   	 		if(item instanceof WallItem) {
   	 			System.out.print("WAND 2");
   	 			room.getRoomItemManager().InvRemove(id);
   	 			room.getRoomItemManager().addWallItemToRoom(id, x, y, rotation, item.getState(), item.getBaseItem());
	 		} else {
	 			System.out.print("BODEN 2");
	 			room.getRoomItemManager().InvRemove(id);
	 			room.getRoomItemManager().addFloorItemToRoom(id, x, y, item.getZ(), rotation, item.getState(), item.getBaseItem());
	 		}
   	 	}
    }
    
    // ADD FLOOR ITEM
    
    public void addFloorItemToRoom(int id, int x, int y, double z, int rot, int state, BaseItem baseItem) {
    	if (getItem(id) == null) {
            floorItems.put(id, new RoomItem(id, x, y, z, rot, state, room, baseItem));
        } else {
        	floorItems.put(id, new RoomItem(id, x, y, z, rot, state, room, baseItem));
        }
    	room.getGameMap().addItemToMap(floorItems.get(id));
        room.sendMessage(new SerializeFloorItemComposer(floorItems.get(id)));
        room.getRoomUserManager().updateUserStatusses();
    }
    
    // ADD WALL ITEM
    
    public void addWallItemToRoom(int id, int x, int y, int rot, int state, BaseItem baseItem) {
        if (getItem(id) == null) {
            wallItems.put(id, new WallItem(id, x, y, rot, state, room, baseItem));
            room.sendMessage(new SerializeWallItemComposer(wallItems.get(id)));
        } else {
        	wallItems.put(id, new WallItem(id, x, y, rot, state, room, baseItem));
            room.sendMessage(new SerializeWallItemComposer(wallItems.get(id)));
        }
    }
    
    // USER ON FURNI EVENT

    public void furniInteract(RoomUser user, int itemId) {
        RoomItem item = getItem(itemId);
        if (item != null) {
            item.getInteractor().onTrigger(user, true);
        }
    }
    
    // MOVE A ITEM
    
    public void moveTo(int id, int x, int y, int rotation) {
    	 RoomItem item = getItem(id);
    	 room.getRoomItemManager().addFloorItemToRoom(id, x, y, item.getZ(), rotation, item.getState(), item.getBaseItem());
    }
    
    // INVENTAR
    
    public void InventoryItem(int id, int x, int y, double z, int rot, int state, BaseItem baseItem, String type) {
    	floorItems.put(id, new RoomItem(id, x, y, z, rot, state, room, baseItem));
   	 	room.getRoomItemManager().removeItem(id);
   	 	room.sendMessage(new InventarComposer(id, baseItem.getBaseId(), 0, type));
    }
    
    // REMOVE A INVENTORY ITEM
    
    public void InvRemove(int id) {
    	RoomItem item = getItem(id);
    	if(item != null) {
    		 room.sendMessage(new InventarItemRemoveComposer(id));
    	}
    	
    }
    
    // ADD A ITEM TO INVENTAR
    
    public void addInventoryItem(int id) {
   	 	RoomItem item = getItem(id);
   	 	if(item instanceof WallItem) {
   	 		String type = "W";
   	 		room.sendMessage(new InventarComposer(id, item.getBaseItem().getBaseId(), item.getState(), type));
   	 	} else {
   	 		String type = "F";
   	 		room.sendMessage(new InventarComposer(id, item.getBaseItem().getBaseId(), item.getState(), type));
   	 	}
    }
  
    
    // REMOVE ITEMS
    
    public void removeItem(int id) {
        RoomItem item = getItem(id);
        if (item != null) {
            if (item instanceof WallItem) {
                wallItems.remove(item);
            } else {
                floorItems.remove(item);
                room.getGameMap().removeItemFromMap(item);
                room.getRoomUserManager().updateUserStatusses();
            }
            room.sendMessage(new FurniRemoveComposer(id));
        }
    }

    public void removeAllFurniture() {
        List<Integer> items = new ArrayList<>();
        items.addAll(floorItems.keySet());
        items.addAll(wallItems.keySet());
        for (int itemId : items) {
        	RoomItem iteme = getItem(itemId);
        	room.getRoomItemManager().addInventoryItem(iteme.getId());
            removeItem(itemId);
        }
    }

    public void onCycle() {
    }

	
}
