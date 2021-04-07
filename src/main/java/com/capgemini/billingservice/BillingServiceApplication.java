package com.capgemini.billingservice;

import com.capgemini.billingservice.entities.Bill;
import com.capgemini.billingservice.entities.ProductItem;
import com.capgemini.billingservice.feign.CustomerRestClient;
import com.capgemini.billingservice.feign.ProductItemRestClient;
import com.capgemini.billingservice.model.Customer;
import com.capgemini.billingservice.model.Product;
import com.capgemini.billingservice.repository.BillRepository;
import com.capgemini.billingservice.repository.ProductItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.PagedModel;

import java.util.Date;
import java.util.Random;

@SpringBootApplication
@EnableFeignClients
public class BillingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillingServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner start(BillRepository billRepository,
							ProductItemRepository productItemRepository,
							CustomerRestClient customerRestClient,
							ProductItemRestClient productItemRestClient){
		return args -> {
			Customer customer = customerRestClient.getCustomerById(1L);
			Bill bill = billRepository.save(new Bill(null,new Date(),null, customer.getId(), null));
			PagedModel<Product> productPagedModel = productItemRestClient.pageProducts(1,5);
		    productPagedModel.forEach(p->{
				ProductItem productItem = new ProductItem();
				productItem.setPrice(p.getPrice());
				productItem.setQuantity(1 + new Random().nextInt());
				productItem.setBill(bill);
				productItem.setProductId(p.getId());
				productItemRepository.save(productItem);
			});
		};
	}

}
