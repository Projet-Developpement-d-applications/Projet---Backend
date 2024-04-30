package projet.conquerants.Controller;

import projet.conquerants.Model.RegisterRequest;
import projet.conquerants.Model.Utilisateur;

import java.sql.*;

import static projet.conquerants.Util.PasswordHashUtil.hashPassword;

public class DatabaseController {

    private final String url = "jdbc:mysql://mysql-bd-projet-dev-app.a.aivencloud.com:21790/projetbd";
    private final String user = "projetadmin";
    private final String password = "AVNS_py8jL8Jck7oibOhFn42";

    public DatabaseController() {

    }

    public Utilisateur doesUserExist(String username) {
        String sql = "SELECT * FROM utilisateur WHERE pseudo = ?";
        Utilisateur userInfo = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);

            PreparedStatement dec = connection.prepareStatement(sql);
            dec.setString(1, username);

            ResultSet resultSet = dec.executeQuery();

            if (resultSet.next()) {
                userInfo = new Utilisateur(
                        resultSet.getInt("id"),
                        resultSet.getString("prenom"),
                        resultSet.getString("nom"),
                        resultSet.getString("pseudo"),
                        resultSet.getString("mot_passe"),
                        resultSet.getInt("id_role")
                );
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return userInfo;
    }

    public boolean createUser(RegisterRequest registerRequest) {
        int defaultRole = 1;
        String sql = "INSERT INTO utilisateur (prenom, nom, pseudo, mot_passe, id_role) VALUES (?, ?, ?, ?, ?)";
        boolean result = false;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);

            String hashPassword = hashPassword(registerRequest.getMot_passe());

            PreparedStatement dec = connection.prepareStatement(sql);
            dec.setString(1, registerRequest.getPrenom());
            dec.setString(2, registerRequest.getNom());
            dec.setString(3, registerRequest.getPseudo());
            dec.setString(4, hashPassword);
            dec.setInt(5, defaultRole);

            int lineAdded = dec.executeUpdate();

            result = lineAdded > 0;

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    /* EXAMPLE
    public List<Utilisateur> getAllUsers() {
        String sql = "SELECT * FROM utilisateur";
        List<Utilisateur> utilisateurs = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);

            PreparedStatement dec = connection.prepareStatement(sql);
            ResultSet result = dec.executeQuery();

            while (result.next()) {
                Utilisateur tempUser = new Utilisateur(
                        result.getInt("id"),
                        result.getString("prenom"),
                        result.getString("nom"),
                        result.getString("pseudo"),
                        result.getString("mot_passe")
                );

                utilisateurs.add(tempUser);
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

        return utilisateurs;
    }*/
}
