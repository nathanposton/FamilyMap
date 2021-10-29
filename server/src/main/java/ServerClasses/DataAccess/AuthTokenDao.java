package ServerClasses.DataAccess;

import Model.AuthToken;

import java.sql.*;

/**
 * Authorization Token Data Access Object
 */
public class AuthTokenDao {
    private final Connection conn;

    public AuthTokenDao(Connection conn) { this.conn = conn; }
    /**
     * @param authToken contains a unique token associated with a username, which this method stores in the database.
     */
    public void insert(AuthToken authToken) throws DataAccessException {
        String sql = "INSERT INTO Auth_Tokens (authtoken, username) VALUES(?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, authToken.getAuthtoken());
            stmt.setString(2, authToken.getUsername());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     *
     * @param username references a single unique username associated with an auth token within the database
     * @return the unique auth token associated with the username string
     */
    public AuthToken find(String username) throws DataAccessException {
        AuthToken authToken;
        ResultSet rs = null;
        String sql = "SELECT * FROM Auth_Tokens WHERE Username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                authToken = new AuthToken(rs.getString("authtoken"), rs.getString("username"));

                return authToken;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding authToken");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    /**
     *
     * @param token references a single unique username associated with an auth token within the database
     * @return the unique auth token associated with the username string
     */
    public AuthToken verify(String token) throws DataAccessException {
        AuthToken authToken;
        ResultSet rs = null;
        String sql = "SELECT * FROM Auth_Tokens WHERE AuthToken = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token);
            rs = stmt.executeQuery();
            if (rs.next()) {
                authToken = new AuthToken(rs.getString("authtoken"), rs.getString("username"));

                return authToken;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding authToken");
        } finally {
            if(rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }
        return null;
    }

    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM Auth_Tokens";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }

    public void clear(String username) throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM Auth_Tokens WHERE username = \'" + username + "\'";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }
}
