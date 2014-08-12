package org.viacode.library.db.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;
import org.viacode.library.db.json.BookJson;

import javax.persistence.*;

/**
 * VIAcode
 * Created by IVolkov on 8/6/2014.
 */

@Entity
@Table(name="Book")
public class Book {

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    @Column(name="id")
    private Long id;

    @Column(name="title")
    private String title;

    @Column(name="author")
    private String author;

    @Column(name="quantity")
    private Integer quantity;

    public Book() {
    }

    public Book(String bookTitle, String bookAuthor) {
        this.title = bookTitle;
        this.author = bookAuthor;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long i) {
        this.id = i;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String s) {
        this.title = s;
    }

    public String getAuthor() {
        return this.author;
    }

    public void setAuthor(String s) {
        this.author = s;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public void setQuantity(Integer i) {
        this.quantity = i;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Book)) {
            return false;
        }
        Book book = (Book) object;
        return new EqualsBuilder().append(this.id, book.id).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(-354333345, -1492094871).append(this.id).toHashCode();
    }

    public static Book fromJSON(BookJson bj) {
        if (bj == null || bj.getAuthor().equals("") || bj.getTitle().equals(""))
            return null;
        return new Book(bj.getAuthor(), bj.getTitle());
    }
}
