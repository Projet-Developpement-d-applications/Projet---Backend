package projet.conquerants.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import projet.conquerants.Serializer.RoleSerializer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity(name = "utilisateur")
public class Utilisateur implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String prenom;
    private String nom;
    private String pseudo;
    @Column(name = "mot_passe")
    @JsonIgnore
    private String motPasse;
    @ManyToOne
    @JoinColumn(name = "id_role")
    private Role role;
    @OneToMany(mappedBy = "utilisateur")
    private List<Prediction> predictions = new ArrayList<>();

    public Utilisateur() {

    }

    public Utilisateur(String prenom, String nom, String pseudo, String mot_passe, Role role) {
        this.prenom = prenom;
        this.nom = nom;
        this.pseudo = pseudo;
        this.motPasse = mot_passe;
        this.role = role;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setMotPasse(String motPasse) {
        this.motPasse = motPasse;
    }

    public String getNom() {
        return nom;
    }

    public String getMot_passe() {
        return motPasse;
    }

    public String getPseudo() {
        return pseudo;
    }

    public String getRole() {
        return role.getNom().toUpperCase();
    }

    @Override
    public String toString() {
        return "Utilisateur{" +
                "id=" + id +
                ", prenom='" + prenom + '\'' +
                ", nom='" + nom + '\'' +
                ", pseudo='" + pseudo + '\'' +
                ", mot_passe='" + motPasse + '\'' +
                ", role=" + role +
                '}';
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getNom().toUpperCase()));
    }

    @Override
    public String getPassword() {
        return motPasse;
    }

    @Override
    public String getUsername() {
        return pseudo;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
