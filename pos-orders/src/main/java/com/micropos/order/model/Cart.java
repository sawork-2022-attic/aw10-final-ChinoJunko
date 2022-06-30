package com.micropos.order.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "carts")
@Accessors(fluent = true, chain = true)
public class Cart implements Serializable {

    @Id
    @GeneratedValue
    @Getter
    @Setter
    private Integer id;

    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "items", joinColumns = @JoinColumn(name = "cart_id"))
    @Setter
    @Getter
    private List<Item> items = new ArrayList<>();

    public boolean addItem(Item item) {
        return items.add(item);
    }

    public boolean removeItem(Item item) {
        return items.remove(item);
    }

}
