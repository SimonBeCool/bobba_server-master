package io.bobba.poc.core.gameclients;

import io.bobba.poc.BobbaEnvironment;
import io.bobba.poc.communication.protocol.ClientMessage;
import io.bobba.poc.communication.protocol.ServerMessage;
import io.bobba.poc.core.users.User;
import io.bobba.poc.misc.logging.LogLevel;
import io.bobba.poc.misc.logging.Logging;
import io.bobba.poc.net.Connection;

public class GameClient {
    private Connection connection;
    private User user;

    public GameClient(int id, Connection connection) {
        this.connection = connection;
        this.user = null;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
    
    public void settingMotto(String newmotto) {
    	this.user.setMotto(newmotto);
    }

    public void handleMessage(String rawMessage) {
        Logging.getInstance().writeLine("Received: '" + rawMessage + "'", LogLevel.SuperDebug, this.getClass());
        ClientMessage message = new ClientMessage(rawMessage);
        BobbaEnvironment.getInstance().getGame().getGameClientManager().getSharedMessageHandler().handleMessage(this, message);
    }

    public void stop() {

        if (user != null) {
            user.onDisconnect();
        }
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

    public void sendMessage(ServerMessage message) {
        Logging.getInstance().writeLine("Sent: '" + message.toString() + "'", LogLevel.SuperDebug, this.getClass());
        Logging.getInstance().writeLine("Composed by: " + message.getClass().getName(), LogLevel.Debug, this.getClass());
        if (this.connection != null && this.connection.isOpen()) {
            this.connection.send(message.toString());
        } else {
            stop();
        }
    }

}
