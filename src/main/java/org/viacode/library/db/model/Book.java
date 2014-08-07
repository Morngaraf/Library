package org.viacode.library.db.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * VIAcode
 * Created by IVolkov on 8/6/2014.
 */

@Entity
@Table(name="Book")
public class Book {

    private Long id;
    private String title, author;
    private Integer quantity;

    public Book() {
    }

    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    @Column(name="id")
    public Long getId() {
        return this.id;
    }

    @Column(name="title")
    public String getTitle() {
        return this.title;
    }

    @Column(name="author")
    public String getAuthor() {
        return this.author;
    }

    @Column(name="quantity")
    public Integer getQuantity() {
        return this.quantity;
    }

    public void setId(Long i) {
        this.id = i;
    }

    public void setTitle(String s) {
        this.title = s;
    }

    public void setAuthor(String s) {
        this.author = s;
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

}
