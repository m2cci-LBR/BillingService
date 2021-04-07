package com.capgemini.billingservice.entities;

import com.capgemini.billingservice.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Bill {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date billingDate;

    @OneToMany(mappedBy = "bill")
    private Collection<ProductItem> productItems;

    private Long customerId;

    @Transient
    private Customer customer;

}
