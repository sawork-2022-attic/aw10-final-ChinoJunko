package com.micropos.cart.model;

import lombok.Data;
import lombok.Generated;
import lombok.experimental.Accessors;
import org.springframework.web.context.annotation.SessionScope;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Data
@Accessors(fluent = true, chain = true)
public class Cart implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer id;

    @OneToMany(fetch = FetchType.EAGER)
    private List<Item> items;
}
