package com.capgemini.billingservice.web;

import com.capgemini.billingservice.entities.Bill;
import com.capgemini.billingservice.feign.CustomerRestClient;
import com.capgemini.billingservice.feign.ProductItemRestClient;
import com.capgemini.billingservice.model.Customer;
import com.capgemini.billingservice.model.Product;
import com.capgemini.billingservice.repository.BillRepository;
import com.capgemini.billingservice.repository.ProductItemRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillingRestController {

    private BillRepository billRepository;
    private ProductItemRepository productItemRepository;
    private CustomerRestClient customerRestClient;
    private ProductItemRestClient productItemRestClient;

    public BillingRestController(BillRepository billRepository,
                                 ProductItemRepository productItemRepository,
                                 CustomerRestClient customerRestClient,
                                 ProductItemRestClient productItemRestClient) {
        this.billRepository = billRepository;
        this.productItemRepository = productItemRepository;
        this.customerRestClient = customerRestClient;
        this.productItemRestClient = productItemRestClient;
    }

    @GetMapping(path = "/fullBill/{id}")
    public Bill getBill(@PathVariable(name = "id") Long id){
        Bill bill = billRepository.findById(id).get();
        Customer customer = customerRestClient.getCustomerById(id);
        bill.setCustomer(customer);
        bill.getProductItems().forEach(pi->{
            Product product = productItemRestClient.getProductById(pi.getId());
            pi.setProduct(product);
        });

        return bill;
    }
}
