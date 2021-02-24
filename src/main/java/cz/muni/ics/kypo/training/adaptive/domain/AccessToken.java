package cz.muni.ics.kypo.training.adaptive.domain;

import javax.persistence.*;
import java.util.Objects;

/**
 * Class representing access token needed by trainee to start a Training run.
 * Access tokens are associated with Training instances.
 */
@Entity
@Table(name = "access_token")
@NamedQueries({
        @NamedQuery(
                name = "AccessToken.findOneByAccessToken",
                query = "SELECT at FROM AccessToken at WHERE at.accessToken = :accessToken"
        ),
})
public class AccessToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accessTokenGenerator")
    @SequenceGenerator(name = "accessTokenGenerator", sequenceName = "access_token_seq")
    @Column(name = "access_token_id", nullable = false, unique = true)
    private Long id;
    @Column(name = "access_token", nullable = false, unique = true)
    private String accessToken;

    /**
     * Instantiates a new Access token
     */
    public AccessToken() {
    }

    /**
     * Instantiates a new Access token
     *
     * @param id          unique identification number of access token
     * @param accessToken string representing token that trainee needs to know to access Training run
     */
    public AccessToken(Long id, String accessToken) {
        this.accessToken = accessToken;
        this.id = id;
    }

    /**
     * Gets unique identification number of access token
     *
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets unique identification number of access token
     *
     * @param id the id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets string representing token that trainee needs to know to access Training run
     *
     * @return the access token
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * Sets string representing token that trainee needs to know to access Training run
     *
     * @param accessToken the access token
     */
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof AccessToken))
            return false;
        AccessToken accessToken = (AccessToken) o;
        return Objects.equals(this.getId(), accessToken.getId()) && Objects.equals(this.accessToken, accessToken.getAccessToken());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(), accessToken);
    }

    @Override
    public String toString() {
        return "AccessToken{" +
                "id=" + id +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }
}
