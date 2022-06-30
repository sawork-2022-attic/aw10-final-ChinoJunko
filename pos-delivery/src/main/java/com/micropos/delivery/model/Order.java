package com.micropos.delivery.model;

import com.micropos.dto.OrderDto;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@Accessors(fluent = true, chain = true)
public class Order implements Serializable {
    @Id
    private Integer id;

    @OneToMany
    private List<Item> items;

    OrderDto.StatusEnum status;
}
