package br.com.nexus.plugin.storage.database;

import br.com.nexus.plugin.model.PlayerObject;
import br.com.nexus.plugin.storage.HikaridConnect;
import br.com.nexus.plugin.util.ListUtil;
import com.sun.org.apache.regexp.internal.RE;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

@RequiredArgsConstructor
public class DatabaseMethod {

    private final HikaridConnect hikaridConnect;
    private final ListUtil listUtil;

    @SneakyThrows
    public Boolean hasProxiedPlayer(ProxiedPlayer proxiedPlayer) {
        PreparedStatement preparedStatement = null;
        Connection connection = hikaridConnect.hikariDataSource.getConnection();

        preparedStatement = connection.prepareStatement("SELECT * FROM `NexusAmigos` WHERE `Player` = ?;");
        preparedStatement.setString(1, proxiedPlayer.getName());
        ResultSet rs = preparedStatement.executeQuery();
        return rs.next();
    }

    @SneakyThrows
    public Boolean hasProxiedPlayer(String proxiedPlayer) {
        PreparedStatement preparedStatement = null;
        Connection connection = hikaridConnect.hikariDataSource.getConnection();

        preparedStatement = connection.prepareStatement("SELECT * FROM `NexusAmigos` WHERE `Player` = ?;");
        preparedStatement.setString(1, proxiedPlayer);
        ResultSet rs = preparedStatement.executeQuery();
        return rs.next();
    }

    @SneakyThrows
    public void updatePlayerObject(PlayerObject playerObject) {
        PreparedStatement preparedStatement = null;
        Connection connection = hikaridConnect.hikariDataSource.getConnection();

        preparedStatement = connection.prepareStatement("UPDATE `NexusAmigos` SET `FriendList` = ? WHERE `Player` = ?");
        preparedStatement.setString(1, listUtil.convertArrayListInString(playerObject.getFriendList()));
        preparedStatement.setString(2, playerObject.getProxiedPlayer().getName());
        preparedStatement.executeUpdate();
    }

    @SneakyThrows
    public void updateFrindList(String name, ArrayList<String> amigos) {
        PreparedStatement preparedStatement = null;
        Connection connection = hikaridConnect.hikariDataSource.getConnection();

        preparedStatement = connection.prepareStatement("UPDATE `NexusAmigos` SET `FriendList` = ? WHERE `Player` = ?");
        preparedStatement.setString(1, listUtil.convertArrayListInString(amigos));
        preparedStatement.setString(2, name);
        preparedStatement.executeUpdate();
    }

    @SneakyThrows
    public void setProxiedPlayer(ProxiedPlayer proxiedPlayer) {
        PreparedStatement preparedStatement = null;
        Connection connection = hikaridConnect.hikariDataSource.getConnection();

        preparedStatement = connection.prepareStatement("INSERT INTO `NexusAmigos`(`Player`,`FriendList`, `Notification`) VALUES (?, ?, ?)");
        preparedStatement.setString(1, proxiedPlayer.getName());
        preparedStatement.setString(2, "");
        preparedStatement.setBoolean(3, true);
        preparedStatement.executeUpdate();
    }

    @SneakyThrows
    public ArrayList<String> getListFriendByName(String name) {
        if (!hasProxiedPlayer(name)) new ArrayList<String>();

        PreparedStatement preparedStatement = null;
        Connection connection = hikaridConnect.hikariDataSource.getConnection();

        preparedStatement = connection.prepareStatement("SELECT * FROM `NexusAmigos` WHERE `Player` = ?;");
        preparedStatement.setString(1, name);
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        return listUtil.convertStringInArrayList(rs.getString("FriendList"));
    }

    @SneakyThrows
    public PlayerObject getPlayerObjectByPorxiedPlayer(ProxiedPlayer proxiedPlayer) {
        if (!hasProxiedPlayer(proxiedPlayer)) setProxiedPlayer(proxiedPlayer);

        PreparedStatement preparedStatement = null;
        Connection connection = hikaridConnect.hikariDataSource.getConnection();

        preparedStatement = connection.prepareStatement("SELECT * FROM `NexusAmigos` WHERE `Player` = ?;");
        preparedStatement.setString(1, proxiedPlayer.getName());
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        return new PlayerObject(proxiedPlayer, rs.getBoolean("Notification"), new ArrayList<>(), listUtil.convertStringInArrayList(rs.getString("FriendList")));
    }

    @SneakyThrows
    public void createTable() {
        PreparedStatement preparedStatement = null;
        Connection connection = hikaridConnect.hikariDataSource.getConnection();

        preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `NexusAmigos`(`Player` VARCHAR(24), `FriendList` LONGTEXT, `Notification` BOOLEAN)");
        preparedStatement.executeUpdate();
    }

}
