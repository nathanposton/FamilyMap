package ServerClasses.DataAccess;

import Model.Person;

import java.sql.*;
import java.util.ArrayList;

/**
 * Person Data Access Object
 */
public class PersonDao {
    private final Connection conn;

    public PersonDao(Connection conn) {this.conn = conn;}


    /**
     *
     * Returns the Person object with the specified ID.
     * @param personID is the unique identifier associated with the user being retrieved
     * @return the only Person object associated with the ID.
     */
    public Person findOne(String personID) throws DataAccessException {
        Person person;
        ResultSet rs = null;
        String sql = "SELECT * FROM Persons WHERE id = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                person = new Person(rs.getString("id"), rs.getString("AssociatedUsername"),
                        rs.getString("firstName"), rs.getString("lastName"),
                        rs.getString("gender"), rs.getString("fatherID"),
                        rs.getString("motherID"), rs.getString("spouseID"));
                return person;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person");
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
     * Returns ALL family members of the current user.
     * @return List<Person> is a list of ancestors associated with the specified user
     */
    public ArrayList<Person> findAll() throws DataAccessException {
        ArrayList<Person> person = new ArrayList<>();
        ResultSet rs = null;
        try (Statement stmt = conn.createStatement()) {
            String sql = "SELECT * FROM Persons;";
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                person.add(new Person(rs.getString("id"), rs.getString("AssociatedUsername"),
                        rs.getString("firstName"), rs.getString("lastName"),
                        rs.getString("gender"), rs.getString("fatherID"),
                        rs.getString("motherID"), rs.getString("spouseID")));
            }
            if (person.size() > 0) {
                return person;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person");
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
     * Returns ALL family members of the current user.
     * @return List<Person> is a list of ancestors associated with the specified user
     */
    public ArrayList<Person> findAllForUser(String username) throws DataAccessException {
        ArrayList<Person> person = new ArrayList<>();
        ResultSet rs = null;
        String sql = "SELECT * FROM Persons WHERE AssociatedUsername = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while (rs.next()) {
                person.add(new Person(rs.getString("id"), rs.getString("AssociatedUsername"),
                        rs.getString("firstName"), rs.getString("lastName"),
                        rs.getString("gender"), rs.getString("fatherID"),
                        rs.getString("motherID"), rs.getString("spouseID")));
            }
            if (person.size() > 0) {
                return person;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person");
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
     * inserts Person object data into the database
     * @param person contains information for a unique object to be stored in the database
     */
    public void insert(Person person) throws DataAccessException {
        String sql = "INSERT INTO Persons (id, AssociatedUsername, firstName, lastName, gender, " +
                "fatherID, motherID, spouseID) VALUES(?,?,?,?,?,?,?,?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getAssociatedUsername());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFatherID());
            stmt.setString(7, person.getMotherID());
            stmt.setString(8, person.getSpouseID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    public void clear() throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM Persons";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }

    public void clear(String personID) throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM Persons WHERE id = \'" + personID + "\'";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }

    public void clearByUsername(String username) throws DataAccessException {
        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM Persons WHERE associatedUsername = \'" + username + "\'";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }
    }
}
