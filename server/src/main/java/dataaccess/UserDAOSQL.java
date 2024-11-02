package dataaccess;

import model.UserData;
import service.DatabaseManager;

import java.sql.Connection;

public class UserDAOSQL implements UserDAO {

    public UserData getUser(String username) throws DataAccessException {
        return null;
    }

    public void addUser(UserData newUser) throws DataAccessException {
        //do something
    }

    public void clear() throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "TRUNCATE TABLE users";
            try(var preparedStatement = conn.prepareStatement(statement) ) {
                preparedStatement.executeUpdate();
            }

        } catch (Exception ex){
            throw new DataAccessException(ex.getMessage());
        }
    }

    public int getNumUsers() throws DataAccessException{
        try (Connection conn = DatabaseManager.getConnection()) {
            String statement = "SELECT COUNT(0) AS count FROM users";
            try(var preparedStatement = conn.prepareStatement(statement) ) {
                try(var rs = preparedStatement.executeQuery()){
                    if(rs.next()){
                        return rs.getInt("count");
                    } else {
                        return 0;
                    }
                }
            }
        } catch (Exception ex){
            throw new DataAccessException(ex.getMessage());
        }
    }
}
