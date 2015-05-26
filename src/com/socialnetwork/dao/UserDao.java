package com.socialnetwork.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementCreatorFactory;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.socialnetwork.entities.Country;
import com.socialnetwork.entities.NewsMessagePrototype;
import com.socialnetwork.entities.User;
import com.socialnetwork.services.UserService;

@Repository
public class UserDao {

    @Autowired
    private UserService userService;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Component
    protected static final class UserMapper implements RowMapper<User> {

        private static UserDao userDao;

        @Autowired
        private UserDao userDao2;

        @PostConstruct
        public void init() {
            UserMapper.userDao = userDao2;
        }

        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setFirstName(rs.getString("first_name"));
            user.setSecondName(rs.getString("second_name"));
            user.setGender(rs.getInt("gender"));
            Country country = userDao.getCountry(rs.getInt("country_id"));
            user.setCountry(country);
            user.setPhoneNumber(rs.getString("phone_number"));
            user.setEmail(rs.getString("email"));
            user.setSchool(rs.getString("school"));
            user.setUniversity(rs.getString("university"));
            user.setPassword(rs.getString("password"));
            user.setDateOfBirth(rs.getString("date_of_birth"));
            user.setCity(rs.getString("city"));
            user.setAvatarPath(rs.getString("avatar_path"));
            return user;
        }
    }

    public List<Country> getCountriesList() {
        String sql = "SELECT * FROM countries";
        List<Country> countriesList = new ArrayList<>();

        jdbcTemplate.query(sql, new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Country country = new Country();

                int id = rs.getInt("id");
                country.setId(id);

                String name = rs.getString("name");
                country.setName(name);

                countriesList.add(country);
            }
        });

        return countriesList;
    }

    public void saveUser(User user) {
        String query = "INSERT INTO users SET first_name = ?, second_name = ?, gender = ?, date_of_birth = ?, country_id = ?, phone_number = ?, "
                + "email = ?, school = ?, university = ?, password = ?, avatar_path = ?";

        jdbcTemplate.execute(query, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setString(1, user.getFirstName());
                ps.setString(2, user.getSecondName());
                ps.setInt(3, user.getGender());
                ps.setString(4, user.getDateOfBirth());
                ps.setInt(5, user.getCountry().getId());
                ps.setString(6, user.getPhoneNumber());
                ps.setString(7, user.getEmail());
                ps.setString(8, user.getSchool());
                ps.setString(9, user.getUniversity());
                ps.setString(10, user.getPassword());
                ps.setString(11, user.getAvatarPath());

                return ps.execute();
            }
        });
    }

    public User getUser(int userId) {

        String SELECT = "SELECT * FROM users WHERE id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource("id", userId);

        return (User) namedParameterJdbcTemplate.queryForObject(SELECT, namedParameters, new UserMapper());

    }

    private Country getCountry(int countryId) {
        String sql = "SELECT * FROM countries WHERE id = " + countryId;
        Country country = new Country();

        jdbcTemplate.query(sql, new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {

                country.setId(countryId);

                String countryName = rs.getString("name");
                country.setName(countryName);
            }
        });

        return country;
    }

    public boolean isAuthorized(String email, String password) {
        String sql = "SELECT COUNT(*) AS is_authorized FROM users WHERE email = ? AND password = ?";

        return jdbcTemplate.execute(sql, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setString(1, email);
                ps.setString(2, password);
                return ps.execute();
            }
        });
    }

    public User getUser(String hashCookie) {
        String SELECT = "SELECT * FROM users WHERE hash = :hashCookie";
        SqlParameterSource namedParameters = new MapSqlParameterSource("hashCookie", hashCookie);

        User user = null;
        try {
            user = (User) namedParameterJdbcTemplate.queryForObject(SELECT, namedParameters, new UserMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

        return user;
    }

    public void setHash(String hash, String email, String password) {
        String sql = "UPDATE users SET hash = ? WHERE email = ? AND password = ?";

        jdbcTemplate.execute(sql, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setString(1, hash);
                ps.setString(2, email);
                ps.setString(3, password);

                return ps.execute();
            }
        });
    }

    public void deleteHash(String hashCookie) {
        String sql = "UPDATE users SET hash = '' WHERE hash = ?";

        jdbcTemplate.execute(sql, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setString(1, hashCookie);

                return ps.execute();
            }
        });

    }

    public List<User> search(User user) {
        List<SqlParameter> typesList = new ArrayList<>();
        List<Object> parametersList = new ArrayList<>();

        String sql = "SELECT * FROM users WHERE";
        StringBuilder stringBuilder = new StringBuilder(sql);
        boolean isFirst = true;

        if (user.getFirstName() != null) {
            typesList.add(new SqlParameter(Types.VARCHAR));
            if (user.getFirstName().length() >= 3) {
                parametersList.add(user.getFirstName() + "*");
                stringBuilder.append(" MATCH (first_name) AGAINST (? IN BOOLEAN MODE)");
            } else {
                parametersList.add(user.getFirstName() + "%");
                stringBuilder.append(" first_name LIKE ?");

            }
            isFirst = false;
        }

        if (user.getSecondName() != null) {
            typesList.add(new SqlParameter(Types.VARCHAR));
            if (!isFirst) {
                stringBuilder.append(" AND");
            }
            if (user.getSecondName().length() >= 3) {
                parametersList.add(user.getSecondName() + "*");
                stringBuilder.append(" MATCH (second_name) AGAINST (? IN BOOLEAN MODE)");
            } else {
                parametersList.add(user.getSecondName() + "%");
                stringBuilder.append(" second_name LIKE  ?");
            }
            isFirst = false;
        }

        if (user.getGender() != 0) {
            if (!isFirst) {
                stringBuilder.append(" AND");
            }
            stringBuilder.append(" gender = " + user.getGender());
            isFirst = false;
        }

        if (user.getCountry() != null) {
            if (!isFirst) {
                stringBuilder.append(" AND");
            }
            stringBuilder.append(" country_id = " + user.getCountry().getId());
            isFirst = false;
        }

        if (user.getPhoneNumber() != null) {
            typesList.add(new SqlParameter(Types.VARCHAR));
            if (!isFirst) {
                stringBuilder.append(" AND");
            }
            if (user.getPhoneNumber().length() >= 3) {
                parametersList.add(user.getPhoneNumber() + "*");
                stringBuilder.append(" MATCH (phone_number) AGAINST (? IN BOOLEAN MODE)");
                isFirst = false;
            } else {
                parametersList.add(user.getPhoneNumber() + "%");
                stringBuilder.append(" phone_number LIKE ?");
                isFirst = false;
            }
        }

        if (user.getEmail() != null) {
            typesList.add(new SqlParameter(Types.VARCHAR));
            if (!isFirst) {
                stringBuilder.append(" AND");
            }
            if (user.getEmail().length() >= 3) {
                parametersList.add(user.getEmail() + "*");
                stringBuilder.append(" MATCH (email) AGAINST (? IN BOOLEAN MODE)");
                isFirst = false;
            } else {
                parametersList.add(user.getEmail() + "%");
                stringBuilder.append(" email LIKE ?");
                isFirst = false;
            }
        }

        if (user.getSchool() != null) {
            typesList.add(new SqlParameter(Types.VARCHAR));
            if (!isFirst) {
                stringBuilder.append(" AND");
            }
            if (user.getSchool().length() >= 3) {
                parametersList.add(user.getSchool() + "*");
                stringBuilder.append(" MATCH (school) AGAINST (? IN BOOLEAN MODE)");
                isFirst = false;
            } else {
                parametersList.add(user.getSchool() + "%");
                stringBuilder.append(" school LIKE ?");
                isFirst = false;
            }
        }

        if (user.getUniversity() != null) {
            typesList.add(new SqlParameter(Types.VARCHAR));
            if (!isFirst) {
                stringBuilder.append(" AND");
            }
            if (user.getUniversity().length() >= 3) {
                parametersList.add(user.getUniversity() + "*");
                stringBuilder.append(" MATCH (university) AGAINST (? IN BOOLEAN MODE)");
                isFirst = false;
            } else {
                parametersList.add(user.getUniversity() + "%");
                stringBuilder.append(" university LIKE ?");
                isFirst = false;
            }
        }

        if (user.getCity() != null) {
            typesList.add(new SqlParameter(Types.VARCHAR));
            if (!isFirst) {
                stringBuilder.append(" AND");
            }
            if (user.getCity().length() >= 3) {
                parametersList.add(user.getCity() + "*");
                stringBuilder.append(" MATCH (city) AGAINST (? IN BOOLEAN MODE)");
                isFirst = false;
            } else {
                parametersList.add(user.getCity() + "%");
                stringBuilder.append(" city LIKE ?");
                isFirst = false;
            }
        }

        sql = stringBuilder.toString();
        if (user.getFirstName() == null && user.getSecondName() == null && user.getGender() == 0 && user.getPhoneNumber() == null && user.getEmail() == null
                && user.getSchool() == null && user.getUniversity() == null && user.getCity() == null && user.getCountry() == null) {
            sql = "SELECT * FROM users";
        }

        PreparedStatementCreatorFactory pscf = new PreparedStatementCreatorFactory(sql, typesList);
        PreparedStatementCreator psc = pscf.newPreparedStatementCreator(parametersList);

        List<User> usersList = new ArrayList<>();
        try {
            usersList = jdbcTemplate.query(psc, new UserMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

        return usersList;
    }

    public void updateUser(User user) {
        String sql = "UPDATE users SET first_name = ?, second_name = ?, gender = ?, country_id = ?, phone_number = ?, "
                + "email = ?, school= ?, university = ?, city = ?, date_of_birth = ? WHERE id = ?";

        jdbcTemplate.execute(sql, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setString(1, user.getFirstName());
                ps.setString(2, user.getSecondName());
                ps.setInt(3, user.getGender());
                ps.setInt(4, user.getCountry().getId());
                ps.setString(5, user.getPhoneNumber());
                ps.setString(6, user.getEmail());
                ps.setString(7, user.getSchool());
                ps.setString(8, user.getUniversity());
                ps.setString(9, user.getCity());
                ps.setString(10, user.getDateOfBirth());
                ps.setInt(11, user.getId());
                return ps.execute();
            }
        });

    }

    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = :mail";
        SqlParameterSource namedParameters = new MapSqlParameterSource("mail", email);
        User user = null;
        try {
            user = (User) namedParameterJdbcTemplate.queryForObject(sql, namedParameters, new UserMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }

        return user;
    }

    public void addToFriends(int userId, int friendId) {
        String sql = "INSERT INTO friends SET owner_id = " + userId + ", edited_user_id = " + friendId + ", status = 0";

        jdbcTemplate.execute(sql);

    }

    public List<Integer> getFriendsIdsList(int userId) {
        String sql = "SELECT owner_id FROM friends WHERE edited_user_id = " + userId
                + " AND status = 1 UNION ALL SELECT edited_user_id FROM friends WHERE owner_id = " + userId + " AND status = 1;";

        List<Integer> friendsIdsList = new ArrayList<>();
        jdbcTemplate.query(sql, new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {
                int id = rs.getInt("owner_id");
                friendsIdsList.add(id);
            }
        });

        return friendsIdsList;
    }

    public List<User> getFriendsList(int userId) {
        String sql = "SELECT * FROM users WHERE id IN (SELECT owner_id FROM friends WHERE edited_user_id = ? AND status = 1 "
                + "UNION ALL SELECT edited_user_id FROM friends WHERE owner_id = ? AND status = 1);";

        return jdbcTemplate.query(sql, new UserMapper(), new Object[] { userId, userId });
    }

    public void deleteFromFriends(int userId, int friendId) {
        String sql = "DELETE FROM friends WHERE (owner_id = " + userId + " AND edited_user_id = " + friendId + " AND status = 1) OR (owner_id = " + friendId
                + " AND edited_user_id = " + userId + " AND status = 1);";
        jdbcTemplate.execute(sql);

    }

    public List<User> getIgnoreList(int userId) {
        String sql = "SELECT * FROM users WHERE id IN ( SELECT edited_user_id FROM friends WHERE owner_id = ? AND status = 2)";
        return jdbcTemplate.query(sql, new UserMapper(), new Object[] { userId });
    }

    public void addToIgnore(int userId, int ignoreUserId) {
        String sql = "INSERT INTO friends SET owner_id = " + userId + ", edited_user_id = " + ignoreUserId + ", status = 2";
        jdbcTemplate.execute(sql);
    }

    public void addFriendToIgnore(int userId, int ignoreUserId) {
        userService.deleteFromFriends(userId, ignoreUserId);
        String sql = "INSERT INTO friends SET owner_id = " + userId + ", edited_user_id = " + ignoreUserId + ", status = 2";
        jdbcTemplate.execute(sql);
    }

    public List<Integer> getIgnoreIdsList(int userId) {
        String sql = "SELECT edited_user_id FROM friends WHERE owner_id = " + userId + " AND status = 2";
        List<Integer> ignoreIdsList = new ArrayList<>();

        jdbcTemplate.query(sql, new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {
                int id = rs.getInt("edited_user_id");
                ignoreIdsList.add(id);
            }
        });

        return ignoreIdsList;
    }

    public void deleteFromIgnore(int userId, int userIgnoreId) {
        String sql = "DELETE FROM friends WHERE owner_id = " + userId + " AND edited_user_id = " + userIgnoreId + " AND status = 2;";
        jdbcTemplate.execute(sql);

    }

    public void updateAvatarPath(int userId, String avatarPath) {
        String sql = "UPDATE users SET avatar_path = ? WHERE id = ?";

        jdbcTemplate.execute(sql, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setString(1, avatarPath);
                ps.setInt(2, userId);
                return ps.execute();
            }
        });
    }

    public List<NewsMessagePrototype> getUserNews(int userId) {
        String sql = "SELECT * FROM news WHERE sender_id IN (SELECT owner_id FROM friends WHERE edited_user_id = " + userId
                + " AND status = 1 UNION ALL SELECT edited_user_id FROM friends WHERE owner_id = " + userId + " AND status = 1) ORDER BY creation_date DESC";

        List<NewsMessagePrototype> userNews = new ArrayList<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm");
        jdbcTemplate.query(sql, new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {

                NewsMessagePrototype newsMessagePrototype = new NewsMessagePrototype();

                newsMessagePrototype.setId(rs.getInt("id"));
                newsMessagePrototype.setSender(userService.getUser(rs.getInt("sender_id")));
                newsMessagePrototype.setMessageType(rs.getInt("message_type"));
                newsMessagePrototype.setObjectId(rs.getInt("object_id"));
                newsMessagePrototype.setCreationDate(rs.getLong("creation_date"));
                newsMessagePrototype.setFormattedCreationDate(simpleDateFormat.format(new Date(rs.getLong("creation_date"))));

                userNews.add(newsMessagePrototype);
            }
        });

        return userNews;
    }

    public List<User> getRequestFriendshipList(int userId) {
        String sql = "SELECT * FROM users WHERE id IN (SELECT owner_id FROM friends WHERE edited_user_id = ? AND status = 0)";
        return jdbcTemplate.query(sql, new UserMapper(), new Object[] { userId });

    }

    public List<User> getSendRequestFriendshipList(int userId) {
        String sql = "SELECT * FROM users WHERE id IN ( SELECT edited_user_id FROM friends WHERE owner_id = ? AND status = 0)";
        return jdbcTemplate.query(sql, new UserMapper(), new Object[] { userId });

    }

    public void acceptFrienship(int userId, int friendId) {
        String sql = "UPDATE friends SET status = 1 WHERE (owner_id = " + userId + " AND edited_user_id = " + friendId + " AND status = 0) OR (owner_id = "
                + friendId + " AND edited_user_id = " + userId + " AND status = 0);";
        jdbcTemplate.execute(sql);
    }

    public void declineFriendship(int userId, int friendId) {
        String sql = "DELETE FROM friends WHERE (owner_id = " + userId + " AND edited_user_id = " + friendId + " AND status = 0) OR (owner_id = " + friendId
                + " AND edited_user_id = " + userId + " AND status = 0);";

        jdbcTemplate.execute(sql);

    }

    public List<Integer> getRequestFriendshipIdsList(int userId) {
        String sql = "SELECT owner_id FROM friends WHERE edited_user_id = " + userId + " AND status = 0";
        ;
        List<Integer> requestFriendshipIdsList = new ArrayList<>();

        jdbcTemplate.query(sql, new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {
                int id = rs.getInt("owner_id");
                requestFriendshipIdsList.add(id);
            }
        });

        return requestFriendshipIdsList;
    }

    public List<Integer> getSendRequestFriendshipIdsList(int userId) {
        String sql = "SELECT edited_user_id FROM friends WHERE owner_id = " + userId + " AND status = 0";
        List<Integer> sendRequestFriendshipIdsLists = new ArrayList<>();

        jdbcTemplate.query(sql, new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {
                int id = rs.getInt("edited_user_id");
                sendRequestFriendshipIdsLists.add(id);
            }
        });

        return sendRequestFriendshipIdsLists;

    }
}
