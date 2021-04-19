package io.labforward.api.entities;

import io.labforward.api.utils.Constant;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Created by: Robert Wilson
 * Date:  19/04/2021
 * Project: api
 * Package: io.labforward.api.entities
 * Class: ItemValue
 */
@Data
@Entity
@Table( name = Constant.ITEM_VALUE )
public class ItemValue implements Serializable
{
    @Transient
    private static final long serialVersionUID = 4L;

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private long id;

    @ManyToOne
    private Item item;

    @ManyToOne
    private Attribute attribute;

    private String value;

    @CreationTimestamp
    @Column( name = Constant.CREATED_DATE )
    private Date createdDate;

    @UpdateTimestamp
    @Column( name = Constant.UPDATED_DATE)
    private Date updatedDate;
}
