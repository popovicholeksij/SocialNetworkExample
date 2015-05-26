package com.socialnetwork.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Repository;

import com.socialnetwork.entities.Album;
import com.socialnetwork.entities.Image;

@Repository
public class GalleryDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void saveAlbum(Album album) {

        String sql = "INSERT INTO albums SET owner_id = ?,  creation_date = ?, photos_count = ?, title = ?";
        
        jdbcTemplate.execute(sql, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setInt(1, album.getOwnerId());
                ps.setLong(2, album.getCreationDate());
                ps.setInt(3, album.getPhotosCount());
                ps.setString(4, album.getTitle());

                return ps.execute();
            }
        });
    }

    public void saveImage(Image image) {

        String sql = "INSERT INTO images SET album_id = ?, owner_id = ?,  creation_date = ?, filename = ?, title = ?";

        jdbcTemplate.execute(sql, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setInt(1, image.getAlbumId());
                ps.setInt(2, image.getOwnerId());
                ps.setLong(3, image.getCreationDate());
                ps.setString(4, image.getFilename());
                ps.setString(5, image.getTitle());

                return ps.execute();
            }
        });
    }

    public List<Album> getAlbumsList(int userId) {
        String sql = "SELECT * FROM albums WHERE owner_id = " + userId + " ORDER BY creation_date";

        List<Album> albumsList = new ArrayList<>();

        jdbcTemplate.query(sql, new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Album album = new Album();

                album.setId(rs.getInt("id"));
                album.setOwnerId(rs.getInt("owner_id"));
                album.setCreationDate(rs.getLong("creation_date"));
                album.setTitle(rs.getString("title"));
                album.setPhotosCount(rs.getInt("photos_count"));

                albumsList.add(album);
            }
        });

        return albumsList;
    }

    public List<Image> getImagesList(int albumId, int index) {
        String sql = "SELECT * FROM images WHERE album_id = " + albumId + " ORDER BY creation_date LIMIT " + (index - 1) * 3 + ", 3";

        List<Image> imagesList = new ArrayList<>();

        jdbcTemplate.query(sql, new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {
                Image image = new Image();

                image.setId(rs.getInt("id"));
                image.setOwnerId(rs.getInt("owner_id"));
                image.setAlbumId(albumId);
                image.setCreationDate(rs.getLong("creation_date"));
                image.setFilename(rs.getString("filename"));
                image.setTitle(rs.getString("title"));

                imagesList.add(image);
            }
        });

        return imagesList;
    }

    public Album getAlbum(int albumId) {
        String sql = "SELECT * FROM albums WHERE id = " + albumId;

        Album album = new Album();

        jdbcTemplate.query(sql, new RowCallbackHandler() {

            @Override
            public void processRow(ResultSet rs) throws SQLException {

                album.setId(albumId);
                album.setOwnerId(rs.getInt("owner_id"));
                album.setCreationDate(rs.getLong("creation_date"));
                album.setTitle(rs.getString("title"));
                album.setPhotosCount(rs.getInt("photos_count"));
            }
        });

        return album;
    }

    public void updateAlbumPhotosCount(Album album) {
        String sql = "UPDATE albums SET photos_count = " + album.getPhotosCount() + " WHERE id = " + album.getId();
        jdbcTemplate.execute(sql);
    }

    public void deleteImage(int albumId, String filename) {
        String sql = "DELETE FROM images WHERE album_id = ? AND filename = ?";
        
        jdbcTemplate.execute(sql, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setInt(1, albumId);
                ps.setString(2, filename);

                return ps.execute();
            }
        });
    
    
    }

    public void deleteAlbum(int albumId) {
        String sql = "DELETE FROM albums WHERE id = " + albumId;
        jdbcTemplate.execute(sql);
    }

    public void renameAlbum(int albumId, String newFolderName) {
        String sql = "UPDATE albums SET title = ? WHERE id = ?";
        
        jdbcTemplate.execute(sql, new PreparedStatementCallback<Boolean>() {
            @Override
            public Boolean doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setString(1, newFolderName);
                ps.setInt(2, albumId);

                return ps.execute();
            }
        });
    }

	public Image getImage(int imageId) {

        String sql = "SELECT * FROM images WHERE id = ?";

        return jdbcTemplate.execute(sql, new PreparedStatementCallback<Image>() {
            @Override
            public Image doInPreparedStatement(PreparedStatement ps) throws SQLException, DataAccessException {
                ps.setInt(1, imageId);
                ResultSet resultSet = ps.executeQuery();
                resultSet.next();
                Image image = new Image();
                
                image.setId(resultSet.getInt("id"));
                image.setOwnerId(resultSet.getInt("owner_id"));
                image.setAlbumId(resultSet.getInt("album_id"));
                image.setCreationDate(resultSet.getLong("creation_date"));
                image.setFilename(resultSet.getString("filename"));
                image.setTitle(resultSet.getString("title"));

                return image;
            }
        });
    
	}

}
