package io.labforward.api.entities;

import io.labforward.api.utils.Constant;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by: Robert Wilson
 * Date:  19/04/2021
 * Project: api
 * Package: io.labforward.api.entities
 * Class: Item
 */
@Data
@Entity
@Table( name = Constant.ITEMS )
public class Item implements Serializable
{
    @Transient
    private static final long serialVersionUID = 3L;

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private long id;

    @ManyToOne
    private Category category;

    private String name;

    @OneToMany( mappedBy = Constant.ITEM, cascade = CascadeType.ALL)
    private List<ItemValue> itemValues;

    @CreationTimestamp
    @Column( name = Constant.CREATED_DATE )
    private Date createdDate;

    @UpdateTimestamp
    @Column( name = Constant.UPDATED_DATE)
    private Date updatedDate;
}
