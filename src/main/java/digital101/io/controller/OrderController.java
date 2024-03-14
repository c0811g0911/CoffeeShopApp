package digital101.io.controller;

import digital101.io.domain.Customer;
import digital101.io.domain.MenuItem;
import digital101.io.model.OrderDto;
import digital101.io.repos.CustomerRepository;
import digital101.io.repos.MenuItemRepository;
import digital101.io.service.OrderService;
import digital101.io.util.CustomCollectors;
import digital101.io.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService ordersService;
    private final MenuItemRepository menuItemRepository;
    private final CustomerRepository customerRepository;

    public OrderController(final OrderService ordersService,
                           final MenuItemRepository menuItemRepository,
                           final CustomerRepository customerRepository) {
        this.ordersService = ordersService;
        this.menuItemRepository = menuItemRepository;
        this.customerRepository = customerRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("menuItemValues", menuItemRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(MenuItem::getId, MenuItem::getName)));
        model.addAttribute("customerValues", customerRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Customer::getId, Customer::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("orders", ordersService.findAll());
        return "orders/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("orders") final OrderDto orderDto) {
        return "orders/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("orders") @Valid final OrderDto orderDto,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "orders/add";
        }
        ordersService.create(orderDto);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("orders.create.success"));
        return "redirect:/orders";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("orders", ordersService.get(id));
        return "orders/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("orders") @Valid final OrderDto orderDto,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "orders/edit";
        }
        ordersService.update(id, orderDto);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("orders.update.success"));
        return "redirect:/orders";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        ordersService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("orders.delete.success"));
        return "redirect:/orders";
    }

}
