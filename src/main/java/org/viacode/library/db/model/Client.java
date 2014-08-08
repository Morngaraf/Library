package org.viacode.library.db.model;

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
    private String firstName, secondName;
    private Set<Book> books = new HashSet<Book>(0);

    public Client() {
    }

    public Client(String clientFirstName, String clientSecondName) {
        this.firstName = clientFirstName;
        this.secondName = clientSecondName;
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Column(name = "secondName")
    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
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
        if (cj == null || cj.getFirstName().equals("") || cj.getSecondName().equals(""))
            return null;
        return new Client(cj.getFirstName(), cj.getSecondName());
    }
}
