package org.viacode.library.db.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.viacode.library.db.json.ClientJson;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * VIAcode
 * Created by IVolkov on 8/6/2014.
 */

@Entity
@Table(name = "Client")
public class Client {

    private Long id;
    private String firstName, lastName;
    private Set<Book> books = new HashSet<Book>(0);

    public Client() {
    }

    public Client(String clientFirstName, String clientLastName) {
        this.firstName = clientFirstName;
        this.lastName = clientLastName;
    }

    @Id
    @GeneratedValue(generator = "increment")
    @GenericGenerator(name = "increment", strategy = "increment")
    @Column(name = "id")
    public Long getId() {
        return this.id;
    }

    public void setId(Long i) {
        this.id = i;
    }

    @Column(name = "firstName")
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String fName) {
        this.firstName = fName;
    }

    @Column(name = "lastName")
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lName) {
        this.lastName = lName;
    }

    //FetchType.EAGER is used for accessing "books" values
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "ClientHasBook", joinColumns = @JoinColumn(name = "client_id"), inverseJoinColumns = @JoinColumn(name = "book_id"))
    public Set<Book> getBooks() {
        return books;
    }

    public void setBooks(Set<Book> bookSet) {
        this.books = bookSet;
    }

    public static Client fromJSON(ClientJson cj) {
        if (cj == null || cj.getFirstName().equals("") || cj.getLastName().equals(""))
            return null;
        return new Client(cj.getFirstName(), cj.getLastName());
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Client)) {
            return false;
        }
        Client client = (Client) object;
        return new EqualsBuilder().append(this.id, client.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(-354333345, -1492094871).append(this.id).toHashCode();
    }

}
