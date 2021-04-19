package io.labforward.api.entities;

import io.labforward.api.utils.Constant;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by: Robert Wilson
 * Date:  19/04/2021
 * Project: api
 * Package: io.labforward.api.entities
 * Class: Category
 */
@Data
@Entity
@Table( name = Constant.CATEGORIES )
public class Category implements Serializable
{
    @Transient
    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private long id;

    private String name;

    private String description;

    @ManyToMany(
        cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH
        },
        fetch = FetchType.EAGER
    )
    @JoinTable(
        name = Constant.CATEGORIES_ATTRIBUTES,
        joinColumns = @JoinColumn( name = Constant.CATEGORY_ID ),
        inverseJoinColumns = @JoinColumn( name = Constant.ATTRIBUTE_ID )
    )
    private Set<Attribute> attributes = new HashSet<>();

    @CreationTimestamp
    @Column( name = Constant.CREATED_DATE )
    private Date createdDate;

    @UpdateTimestamp
    @Column( name = Constant.UPDATED_DATE)
    private Date updatedDate;

    public void addAttribute( Attribute attribute )
    {
        if ( attributes == null ) attributes = new HashSet<>();

        attributes.add(attribute);
    }
}
