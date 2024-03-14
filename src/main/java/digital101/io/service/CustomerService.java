package digital101.io.service;

import digital101.io.domain.Customer;
import digital101.io.domain.Order;
import digital101.io.model.CustomerDto;
import digital101.io.repos.CustomerRepository;
import digital101.io.repos.OrderRepository;
import digital101.io.util.NotFoundException;
import digital101.io.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;

    public CustomerService(final CustomerRepository customerRepository,
                           final OrderRepository orderRepository) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
    }

    public List<CustomerDto> findAll() {
        final List<Customer> customerList = customerRepository.findAll(Sort.by("id"));
        return customerList.stream()
                .map(customer -> mapToDTO(customer, new CustomerDto()))
                .toList();
    }

    public CustomerDto get(final Long id) {
        return customerRepository.findById(id)
                .map(customer -> mapToDTO(customer, new CustomerDto()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final CustomerDto customerDto) {
        final Customer customer = new Customer();
        mapToEntity(customerDto, customer);
        return customerRepository.save(customer).getId();
    }

    public void update(final Long id, final CustomerDto customerDto) {
        final Customer customer = customerRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(customerDto, customer);
        customerRepository.save(customer);
    }

    public void delete(final Long id) {
        customerRepository.deleteById(id);
    }

    private CustomerDto mapToDTO(final Customer customer, final CustomerDto customerDto) {
        customerDto.setId(customer.getId());
        customerDto.setName(customer.getName());
        customerDto.setAddress(customer.getAddress());
        customerDto.setPhone(customer.getPhone());
        customerDto.setEmail(customer.getEmail());
        customerDto.setUserName(customer.getUserName());
        customerDto.setPassword(customer.getPassword());
        customerDto.setCreatedAt(customer.getCreatedAt());
        customerDto.setUpdatedAt(customer.getUpdatedAt());
        return customerDto;
    }

    private Customer mapToEntity(final CustomerDto customerDto, final Customer customer) {
        customer.setName(customerDto.getName());
        customer.setAddress(customerDto.getAddress());
        customer.setPhone(customerDto.getPhone());
        customer.setEmail(customerDto.getEmail());
        customer.setUserName(customerDto.getUserName());
        customer.setPassword(customerDto.getPassword());
        customer.setCreatedAt(customerDto.getCreatedAt());
        customer.setUpdatedAt(customerDto.getUpdatedAt());
        return customer;
    }

    public ReferencedWarning getReferencedWarning(final Long id) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Customer customer = customerRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        final Order order = orderRepository.findFirstByCustomer(customer);
        if (order != null) {
            referencedWarning.setKey("customers.orders.customer.referenced");
            referencedWarning.addParam(order.getId());
            return referencedWarning;
        }
        return null;
    }

}
