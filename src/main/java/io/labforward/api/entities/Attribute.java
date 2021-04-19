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
 * Class: Attribute
 */
@Data
@Entity
@Table( name = Constant.ATTRIBUTES )
public class Attribute implements Serializable
{
    @Transient
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue( strategy = GenerationType.AUTO )
    private long id;

    private String key;

    private String label;

    @Column( name = Constant.VALUE_TYPE )
    private String valueType;

    @CreationTimestamp
    @Column( name = Constant.CREATED_DATE )
    private Date createdDate;

    @UpdateTimestamp
    @Column( name = Constant.UPDATED_DATE)
    private Date updatedDate;
}
