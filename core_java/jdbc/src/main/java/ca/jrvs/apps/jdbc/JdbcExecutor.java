package ca.jrvs.apps.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcExecutor{
    public static void main(String[] args) {
        DatabaseConnectionManager dcm = new DatabaseConnectionManager("localhost", "hplussport",
                "postgres", "postgres");

        try{
            Connection connection = dcm.getConnection();
            OrderDAO orderDAO = new OrderDAO(connection);
            Order order = orderDAO.findById(1000);
            System.out.println(order);
            //CustomerDAO customerDAO = new CustomerDAO(connection);
            //Creating a new row in the database
            /*
            Customer customer = new Customer();
            customer.setFirstName("George");
            customer.setLastName("Washington");
            customer.setEmail("george.washignton@gmail.com");
            customer.setAddress("123 Main St. West");
            customer.setPhone("(555)-908-989");
            customer.setCity("Mount Vernon");
            customer.setState("VA");
            customer.setZipCode("221221");

            customerDAO.create(customer);

             */
            //Finding customer by id
            /*
            Customer customer = customerDAO.findById(1001);
            System.out.println(customer.getFirstName() + " " + customer.getLastName());

             */

            //Updating customer
            /*
            Customer customer = customerDAO.findById(10000);
            System.out.println(customer.getFirstName() + " " + customer.getLastName() +" " + customer.getEmail());
            customer.setEmail("geowashington@whitehouse.gov");
            customer = customerDAO.update(customer);
            System.out.println(customer.getFirstName() + " " + customer.getLastName() +" " + customer.getEmail());

             */

            //Deleting customer/
            /*
            Customer customer = new Customer();
            customer.setFirstName("John");
            customer.setLastName("Adams");
            customer.setEmail("john.adams@whitehouse.gov");
            customer.setPhone("(666) 768 890");
            customer.setAddress("121 St. George St.West");
            customer.setCity("Arlington");
            customer.setState("VA");
            customer.setZipCode("01234");



            Customer dbCustomer = customerDAO.create(customer);
            System.out.println(dbCustomer);
            dbCustomer = customerDAO.findById(dbCustomer.getId());
            System.out.println(dbCustomer);
            dbCustomer.setEmail("adamns.john@whitehouse.gov");
            dbCustomer = customerDAO.update(dbCustomer);
            System.out.println(dbCustomer);
            customerDAO.delete(dbCustomer.getId());

             */

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }
}