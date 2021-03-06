package ca.jrvs.apps.jdbc;

import ca.jrvs.apps.jdbc.util.DataAccessObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class CustomerDAO extends DataAccessObject<Customer> {
    private static final String INSERT = "INSERT INTO customer (first_name, last_name," +
            "email, phone, address, city, state, zipcode) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String GET_ONE = "SELECT customer_id, first_name, last_name, email, phone, address, " +
            "city, state, zipcode FROM customer WHERE customer_id = ?";

    private static final String UPDATE = "Update customer SET first_name = ?, last_name = ?, email = ?, " +
            "phone = ?, address = ?, city = ?, state = ?, zipcode = ? WHERE customer_id = ?";

    private static String DELETE = "DELETE FROM customer WHERE customer_id = ?";

    public CustomerDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Customer findById(long id) {
        Customer customer = new Customer();
        try (PreparedStatement statement = this.connection.prepareStatement(GET_ONE);) {
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                customer.setId(rs.getLong("customer_id"));
                customer.setFirstName(rs.getString("first_name"));
                customer.setLastName(rs.getString("last_name"));
                customer.setEmail(rs.getString("email"));
                customer.setCity(rs.getString("city"));
                customer.setState(rs.getString("state"));
                customer.setAddress(rs.getString("address"));
                customer.setZipCode(rs.getString("zipcode"));

            }
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return customer;
    }

    @Override
    public List<Customer> findAll() {
        return null;
    }

    @Override
    public Customer update(Customer dto) {
        Customer customer = null;
        try {
            this.connection.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try (PreparedStatement statement = this.connection.prepareStatement(UPDATE);) {
            statement.setString(1, dto.getFirstName());
            statement.setString(2, dto.getLastName());
            statement.setString(3, dto.getEmail());
            statement.setString(4, dto.getPhone());
            statement.setString(5, dto.getAddress());
            statement.setString(6, dto.getCity());
            statement.setString(7, dto.getState());
            statement.setString(8, dto.getZipCode());
            statement.setLong(9, dto.getId());
            statement.execute();
            this.connection.commit();
            customer = this.findById(dto.getId());
        } catch (SQLException ex) {
            try {
                this.connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            throw new RuntimeException(ex);
        }

        return customer;
    }

    @Override
    public Customer create(Customer dto) {
        try (PreparedStatement statement = this.connection.prepareStatement(INSERT);) {
            statement.setString(1, dto.getFirstName());
            statement.setString(2, dto.getLastName());
            statement.setString(3, dto.getEmail());
            statement.setString(4, dto.getPhone());
            statement.setString(5, dto.getAddress());
            statement.setString(6, dto.getCity());
            statement.setString(7, dto.getState());
            statement.setString(8, dto.getZipCode());
            statement.execute();
            int id = this.getLastVal(CUSTOMER_SEQUENCE);
            return this.findById(id);
        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }

    @Override
    public void delete(long id) {
        try (PreparedStatement statement = this.connection.prepareStatement(INSERT);) {
            statement.setLong(1, id);
            statement.execute();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

    }
}
