package com.socialnetwork.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.socialnetwork.entities.Message;
import com.socialnetwork.entities.NewsMessagePrototype;
import com.socialnetwork.services.UserService;

@Repository
public class MessageDao {

    @Autowired
    private UserService userService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void saveMessage(Message message) {
        Date date = message.getCreationDate();
        long creationDate = date.getTime();

        String sql = "INSERT INTO messages SET sender_id = ?, receiver_id = ?, creation_date = ?, message = ?, is_readed = ?";

        jdbcTemplate.execute(sql, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setInt(1, message.getSender().getId());
                ps.setInt(2, message.getReceiver().getId());
                ps.setLong(3, creationDate);
                ps.setString(4, message.getMessage());
                ps.setBoolean(5, message.isReaded());

                return ps.execute();
            }
        });
    }

    public List<Message> getMessagesList(int ownerId) {
        String sql = "SELECT * FROM messages WHERE sender_id = " + ownerId + " OR receiver_id = " + ownerId + " ORDER BY creation_date";

        List<Message> messagesList = new ArrayList<>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Message message = new Message();

                message.setId(rs.getInt("id"));
                message.setSender(userService.getUser(rs.getInt("sender_id")));
                message.setReceiver(userService.getUser(rs.getInt("receiver_id")));

                Date date = new Date(rs.getLong("creation_date"));
                message.setCreationDate(date);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                message.setFormatedDate(simpleDateFormat.format(date));

                message.setMessage(rs.getString("message"));
                message.setReaded(rs.getBoolean("is_readed"));
                messagesList.add(message);
            }
        });

        return messagesList;
    }

    public List<Message> getMessagesGroupList(int ownerId) {
        String sql = "SELECT * FROM messages WHERE sender_id = " + ownerId + " OR receiver_id = " + ownerId + " GROUP BY sender_id, receiver_id";

        List<Message> messagesGroupList = new ArrayList<>();

        jdbcTemplate.query(sql, new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Message message = new Message();

                message.setId(rs.getInt("id"));
                message.setSender(userService.getUser(rs.getInt("sender_id")));
                message.setReceiver(userService.getUser(rs.getInt("receiver_id")));

                Date date = new Date(rs.getLong("creation_date"));
                message.setCreationDate(date);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                message.setFormatedDate(simpleDateFormat.format(date));

                message.setMessage(rs.getString("message"));
                message.setReaded(rs.getBoolean("is_readed"));
                messagesGroupList.add(message);
            }
        });

        return messagesGroupList;
    }

    public void setMessagesReaded(int userId) {
        String sql = "UPDATE messages SET is_readed = true WHERE receiver_id = " + userId;

        jdbcTemplate.execute(sql);
    }

    public void saveWallMessage(Message wallMessage) {
        Date date = wallMessage.getCreationDate();
        long creationDate = date.getTime();

        String sql = "INSERT INTO wall_messages SET sender_id = ?, receiver_id = ?, creation_date = ?, message = ?";

        jdbcTemplate.execute(sql, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setInt(1, wallMessage.getSender().getId());
                ps.setInt(2, wallMessage.getReceiver().getId());
                ps.setLong(3, creationDate);
                ps.setString(4, wallMessage.getMessage());

                return ps.execute();
            }
        });
    }

    public int getWallMessageId(Message wallMessage) {
        String sql = "SELECT * FROM wall_messages WHERE sender_id = ? AND receiver_id = ? AND message = ? AND creation_date = ?";

        return jdbcTemplate.execute(sql, new PreparedStatementCallback<Integer>() {
            @Override
            public Integer doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setInt(1, wallMessage.getReceiver().getId());
                ps.setInt(2, wallMessage.getSender().getId());
                ps.setString(3, wallMessage.getMessage());
                ps.setLong(4, wallMessage.getCreationDate().getTime());
                ResultSet resultSet = ps.executeQuery();
                resultSet.next();
                return resultSet.getInt("id");
            }
        });
    }

    public List<Message> getWallMessagesList(int ownerId) {
        String sql = "SELECT * FROM wall_messages WHERE receiver_id = " + ownerId + " ORDER BY creation_date DESC";

        List<Message> wallMessagesList = new ArrayList<>();

        jdbcTemplate.query(sql, new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {

                Message wallMessage = new Message();

                wallMessage.setId(rs.getInt("id"));
                wallMessage.setSender(userService.getUser(rs.getInt("sender_id")));
                wallMessage.setReceiver(userService.getUser(rs.getInt("receiver_id")));

                Date date = new Date(rs.getLong("creation_date"));
                wallMessage.setCreationDate(date);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                wallMessage.setFormatedDate(simpleDateFormat.format(date));

                wallMessage.setMessage(rs.getString("message"));

                wallMessagesList.add(wallMessage);

            }
        });

        return wallMessagesList;
    }

    public List<Message> getDialogList(int userId1, int userId2) {
        String sql = "SELECT * FROM messages WHERE (sender_id=" + userId1 + " AND receiver_id=" + userId2 + ") OR (sender_id=" + userId2 + " AND receiver_id="
                + userId1 + ") ORDER BY creation_date DESC";

        List<Message> dialogList = new ArrayList<>();

        jdbcTemplate.query(sql, new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {

                Message dialogMessage = new Message();

                dialogMessage.setId(rs.getInt("id"));
                dialogMessage.setSender(userService.getUser(rs.getInt("sender_id")));
                dialogMessage.setReceiver(userService.getUser(rs.getInt("receiver_id")));

                Date date = new Date(rs.getLong("creation_date"));
                dialogMessage.setCreationDate(date);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
                dialogMessage.setFormatedDate(simpleDateFormat.format(date));

                dialogMessage.setMessage(rs.getString("message"));
                dialogMessage.setReaded(rs.getBoolean("is_readed"));
                dialogList.add(dialogMessage);
            }
        });

        return dialogList;
    }

    public void addNews(NewsMessagePrototype newsMessagePrototype) {
        String sql = "INSERT INTO news SET sender_id = ?, message_type = ?, object_id = ?, creation_date = ?";

        jdbcTemplate.execute(sql, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setInt(1, newsMessagePrototype.getSender().getId());
                ps.setInt(2, newsMessagePrototype.getMessageType());
                ps.setInt(3, newsMessagePrototype.getObjectId());
                ps.setLong(4, newsMessagePrototype.getCreationDate());

                return ps.execute();
            }
        });
    }

}
