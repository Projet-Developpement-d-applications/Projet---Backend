package projet.conquerants.Controller;

import projet.conquerants.Model.Equipe;
import projet.conquerants.Model.Joueur;
import projet.conquerants.Model.RegisterRequest;
import projet.conquerants.Model.Utilisateur;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    public List<Equipe> getTeamByGame(Equipe teamRequest) {
        String sql = "SELECT * FROM equipe WHERE id_jeu = ? AND id_saison = ?";
        List<Equipe> result = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);

            PreparedStatement dec = connection.prepareStatement(sql);
            dec.setInt(1, teamRequest.getId_jeu());
            dec.setInt(2, teamRequest.getId_saison());

            ResultSet resultSet = dec.executeQuery();
            while (resultSet.next()) {
                result.add(new Equipe(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getInt("division"),
                        resultSet.getInt("id_jeu"),
                        resultSet.getInt("id_saison")
                ));
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public Equipe getTeamByName(Equipe teamRequest) {
        String sql = "SELECT * FROM equipe WHERE nom = ? AND id_jeu = ? AND id_saison = ?";
        Equipe result = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);

            PreparedStatement dec = connection.prepareStatement(sql);
            dec.setString(1, teamRequest.getNom());
            dec.setInt(2, teamRequest.getId_jeu());
            dec.setInt(3, teamRequest.getId_saison());

            ResultSet resultSet = dec.executeQuery();
            while (resultSet.next()) {
                result = new Equipe(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getInt("division"),
                        resultSet.getInt("id_jeu"),
                        resultSet.getInt("id_saison")
                );
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public boolean createTeam(Equipe teamRequest) {
        String sql = "INSERT INTO equipe (nom, division, id_jeu, id_saison) VALUES (?, ?, ?, ?)";
        boolean result = false;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);

            PreparedStatement dec = connection.prepareStatement(sql);
            dec.setString(1, teamRequest.getNom());
            dec.setInt(2, teamRequest.getDivision());
            dec.setInt(3, teamRequest.getId_jeu());
            dec.setInt(4, teamRequest.getId_saison());

            int lineAdded = dec.executeUpdate();

            result = lineAdded > 0;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException er) {
            er.printStackTrace();
        }

        return result;
    }

    public boolean modifyTeam(Equipe teamRequest) {
        String sql = "UPDATE equipe SET nom = ?, division = ? WHERE id = ?";
        boolean result = false;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);

            PreparedStatement dec = connection.prepareStatement(sql);
            dec.setString(1, teamRequest.getNom());
            dec.setInt(2, teamRequest.getDivision());
            dec.setInt(3, teamRequest.getId());

            int lineModified = dec.executeUpdate();

            result = lineModified > 0;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException er) {
            er.printStackTrace();
        }

        return result;
    }

    public Joueur getPlayerByName(Joueur playerRequest) {
        String sql = "SELECT * FROM joueur WHERE pseudo = ? AND id_jeu = ? AND id_saison = ?";
        Joueur result = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);

            PreparedStatement dec = connection.prepareStatement(sql);
            dec.setString(1, playerRequest.getPseudo());
            dec.setInt(2, playerRequest.getId_jeu());
            dec.setInt(3, playerRequest.getId_saison());

            ResultSet resultSet = dec.executeQuery();
            while (resultSet.next()) {
                result = new Joueur(
                        resultSet.getInt("id"),
                        resultSet.getString("prenom"),
                        resultSet.getString("nom"),
                        resultSet.getString("pseudo"),
                        resultSet.getString("date_naissance"),
                        resultSet.getInt("id_position"),
                        resultSet.getInt("id_equipe"),
                        resultSet.getInt("id_jeu"),
                        resultSet.getInt("id_saison")
                );
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public List<Joueur> getPlayerByTeam(Joueur playerRequest) {
        String sql = "SELECT * FROM joueur WHERE id_equipe = ?";
        List<Joueur> result = new ArrayList<>();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);

            PreparedStatement dec = connection.prepareStatement(sql);
            dec.setInt(1, playerRequest.getId_equipe());

            ResultSet resultSet = dec.executeQuery();
            while (resultSet.next()) {
                result.add(new Joueur(
                        resultSet.getInt("id"),
                        resultSet.getString("prenom"),
                        resultSet.getString("nom"),
                        resultSet.getString("pseudo"),
                        resultSet.getString("date_naissance"),
                        resultSet.getInt("id_position"),
                        resultSet.getInt("id_equipe"),
                        resultSet.getInt("id_jeu"),
                        resultSet.getInt("id_saison")
                ));
            }

        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public boolean createPlayer(Joueur playerRequest) {
        String sql = "INSERT INTO joueur (prenom, nom, pseudo, date_naissance, id_position, id_equipe, id_jeu, id_saison) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        boolean result = false;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);

            PreparedStatement dec = connection.prepareStatement(sql);
            dec.setString(1, playerRequest.getPrenom());
            dec.setString(2, playerRequest.getNom());
            dec.setString(3, playerRequest.getPseudo());
            dec.setString(4, playerRequest.getDate_naissance());
            dec.setInt(5, playerRequest.getId_position());
            dec.setInt(6, playerRequest.getId_equipe());
            dec.setInt(7, playerRequest.getId_jeu());
            dec.setInt(8, playerRequest.getId_saison());

            int lineAdded = dec.executeUpdate();

            result = lineAdded > 0;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException er) {
            er.printStackTrace();
        }

        return result;
    }

    public int createPlayers(List<Joueur> playerRequest) {
        int result = 0;
        for (Joueur playerToAdd : playerRequest) {
            if (createPlayer(playerToAdd)) {
                result ++;
            }
        }

        return result;
    }

    public boolean modifyPlayer(Joueur playerRequest) {
        String sql = "UPDATE joueur SET prenom = ?, nom = ?, pseudo = ?, date_naissance = ?, id_position = ?, id_equipe = ?, id_jeu = ?, id_saison = ? WHERE id = ?";
        boolean result = false;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, user, password);

            PreparedStatement dec = connection.prepareStatement(sql);
            dec.setString(1, playerRequest.getPrenom());
            dec.setString(2, playerRequest.getNom());
            dec.setString(3, playerRequest.getPseudo());
            dec.setString(4, playerRequest.getDate_naissance());
            dec.setInt(5, playerRequest.getId_position());
            dec.setInt(6, playerRequest.getId_equipe());
            dec.setInt(7, playerRequest.getId_jeu());
            dec.setInt(8, playerRequest.getId_saison());
            dec.setInt(9, playerRequest.getId());

            int lineAdded = dec.executeUpdate();

            result = lineAdded > 0;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException er) {
            er.printStackTrace();
        }

        return result;
    }

    public int modifyPlayers(List<Joueur> playerRequest) {
        int result = 0;
        for (Joueur playerToModify : playerRequest) {
            if (modifyPlayer(playerToModify)) {
                result ++;
            }
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
