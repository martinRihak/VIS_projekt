package com.moravianwine.app.repository;

import com.moravianwine.app.model.Users;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {
    private Connection connection;
    public UserMapper(Connection connection) {
        this.connection = connection;
    }
    public void insertUser(Users user) throws SQLException {
        String sql = "INSERT INTO users (username, first_name, last_name, email, password_hash, role, create_at, address, country,ZIP) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getFirstName());
            ps.setString(3, user.getLastName());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPasswordHash());
            ps.setString(6, String.valueOf(user.getRole()));
            ps.setTimestamp(7, user.getCreateAt());
            ps.setString(8, user.getAddress().getAddress());
            ps.setString(9, user.getAddress().getCountry());
            ps.setString(10,user.getAddress().getZIP());
            ps.executeUpdate();

            // Získání automaticky generovaného ID
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    user.setUserId(rs.getInt(1));
                }
            }
        }

        // Specifické vložení podle role
        if (user instanceof Users.SysAdmin) {
            insertSysAdmin(connection, (Users.SysAdmin) user);
        } else if (user instanceof Users.RegUser) {
            insertRegUser(connection, (Users.RegUser) user);
        }
    }
    public List<Users> findAllUsers() throws SQLException {
        List<Users> users = new ArrayList<>();
        String sql = "SELECT * FROM users";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    if(rs.getString("role").equals("0")) {
                        users.add(this.findAdminUserById(rs.getInt("user_id")));
                    } else if (rs.getString("role").equals("2")) {
                        users.add(this.findRegUserById(rs.getInt("user_id")));
                    }
                }
            }
        }
        return users;
    }
    private void insertSysAdmin(Connection connection, Users.SysAdmin sysAdmin) throws SQLException {
        String sql = "INSERT INTO sys_admins (user_id, oddeleni) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, sysAdmin.getUserId());
            ps.setString(2, sysAdmin.getOddeleni());
            ps.executeUpdate();
        }
    }

    private void insertRegUser(Connection connection, Users.RegUser regUser) throws SQLException {
        String sql = "INSERT INTO reg_users (user_id, loyalty_points) VALUES (?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, regUser.getUserId());
            ps.setInt(2, regUser.getLoyalty_points());
            ps.executeUpdate();
        }
    }

    public Users.RegUser findRegUserById(int userId) throws SQLException {
        String sql = "SELECT u.* ,r.loyalty_points " +
                "FROM users u JOIN reg_users r ON r.user_id = u.user_id WHERE u.user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Users.Address add = new Users.Address(
                            resultSet.getString("address"),
                            resultSet.getString("country"),
                            resultSet.getString("ZIP")
                    );
                    Users.RegUser user = new Users.RegUser(resultSet);
                    user.setAddress(add);
                    return user;
                }
            }
        }
        return null;
    }
    public void updateUser(Connection connection, Users user) throws SQLException {
        String sql = "UPDATE users SET username = ?, first_name = ?, last_name = ?, email = ?, password_hash = ?, " +
                "role = ?, create_at = ?, address = ?, country = ?,ZIP = ? WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getFirstName());
            ps.setString(3, user.getLastName());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPasswordHash());
            ps.setString(6, String.valueOf(user.getRole()));
            ps.setTimestamp(7, user.getCreateAt());
            ps.setString(8, user.getAddress().getAddress());
            ps.setString(9, user.getAddress().getCountry());
            ps.setString(10,user.getAddress().getZIP());
            ps.setInt(11, user.getUserId());
            ps.executeUpdate();
        }

        // Specifická aktualizace podle role
        if (user instanceof Users.SysAdmin) {
            updateSysAdmin(connection, (Users.SysAdmin) user);
        } else if (user instanceof Users.RegUser) {
            updateRegUser(connection, (Users.RegUser) user);
        }
    }

    private void updateSysAdmin(Connection connection, Users.SysAdmin sysAdmin) throws SQLException {
        String sql = "UPDATE sys_admins SET oddeleni = ? WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, sysAdmin.getOddeleni());
            ps.setInt(2, sysAdmin.getUserId());
            ps.executeUpdate();
        }
    }

    private void updateRegUser(Connection connection, Users.RegUser regUser) throws SQLException {
        String sql = "UPDATE reg_users SET loyalty_points = ? WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, regUser.getLoyalty_points());
            ps.setInt(2, regUser.getUserId());
            ps.executeUpdate();
        }
    }
    public void deleteUser(Connection connection, int userId, char role) throws SQLException {
        // Smazání z příslušné podtabulky
        if (role == '0') {
            deleteSysAdmin(connection, userId);
        } else if (role == '2') {
            deleteRegUser(connection, userId);
        }

        // Smazání z tabulky Users
        String sql = "DELETE FROM users WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    private void deleteSysAdmin(Connection connection, int userId) throws SQLException {
        String sql = "DELETE FROM sys_admins WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    private void deleteRegUser(Connection connection, int userId) throws SQLException {
        String sql = "DELETE FROM reg_users WHERE user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        }
    }

    private Users.SysAdmin findAdminUserById(int userId) throws SQLException {
        String sql = "SELECT u.*, r.oddeleni " +
                "FROM users u " +
                "JOIN sys_admins r ON u.user_id = r.user_id " +
                "WHERE u.user_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Users.Address add = new Users.Address(
                            resultSet.getString("address"),
                            resultSet.getString("country"),
                            resultSet.getString("ZIP")
                    );
                    Users.SysAdmin user = new Users.SysAdmin(resultSet);
                    user.setAddress(add);
                    return user;
                }
            }
        }
        return null;
    }
    public Users findById(int userId) throws SQLException {
        String sql = "SELECT * FROM users " +
                "LEFT JOIN sys_admins ON users.user_id = sys_admins.user_id " +
                "LEFT JOIN reg_users ON users.user_id = reg_users.user_id " +
                "WHERE users.user_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    char role = rs.getString("role").charAt(0);
                    if (role == '0') {
                        return new Users.SysAdmin(rs); // Vytvoří SysAdmin
                    } else if (role == '1') {
                        return new Users.RegUser(rs); // Vytvoří RegUser
                    } else {
                        return new Users(rs); // Vytvoří obecného uživatele
                    }
                }
            }
        }
        return null; // Uživatel nenalezen
    }
    public Users findByUsername(String userId) throws SQLException {
        String sql = "SELECT * FROM users " +
                "LEFT JOIN sys_admins ON users.user_id = sys_admins.user_id " +
                "LEFT JOIN reg_users ON users.user_id = reg_users.user_id " +
                "WHERE users.username = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    char role = rs.getString("role").charAt(0);
                    if (role == '0') {
                        return new Users.SysAdmin(rs); // Vytvoří SysAdmin
                    } else if (role == '2') {
                        return new Users.RegUser(rs); // Vytvoří RegUser
                    } else {
                        return new Users(rs);
                    }
                }
            }
        }
        return null; // Uživatel nenalezen
    }
}