package org.viacode.library.db.model;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * VIAcode
 * Created by IVolkov on 8/6/2014.
 */

@Entity
@Table(name="Client")
public class Client {

    private Long id;
    private String fio;
    private Set<Book> books = new HashSet<Book>(0);

    public Client() {
    }

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    @Column(name="id")
    public Long getId() {
        return this.id;
    }

    @Column(name="fio")
    public String getFio() {
        return this.fio;
    }

    //FetchType.EAGER is used for accessing "books" values
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "ClientHasBook", joinColumns = @JoinColumn(name = "client_id"), inverseJoinColumns = @JoinColumn(name = "book_id"))
    public Set<Book> getBooks() {
        return books;
    }

    public void setId(Long i) {
        this.id = i;
    }

    public void setFio(String s) {
        this.fio = s;
    }

    public void setBooks(Set<Book> bookSet) {
        this.books = bookSet;
    }
}
