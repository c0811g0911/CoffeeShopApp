package digital101.io.controller;

import digital101.io.domain.Menu;
import digital101.io.domain.Queue;
import digital101.io.model.ShopDto;
import digital101.io.repos.MenuRepository;
import digital101.io.repos.QueueRepository;
import digital101.io.service.ShopService;
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
@RequestMapping("/shops")
public class ShopController {

    private final ShopService shopsService;
    private final MenuRepository menuRepository;
    private final QueueRepository queuesRepository;

    public ShopController(final ShopService shopsService, final MenuRepository menuRepository,
                          final QueueRepository queuesRepository) {
        this.shopsService = shopsService;
        this.menuRepository = menuRepository;
        this.queuesRepository = queuesRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("menuValues", menuRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Menu::getId, Menu::getId)));
        model.addAttribute("queueValues", queuesRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Queue::getId, Queue::getName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("shops", shopsService.findAll());
        return "shops/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("shops") final ShopDto shopDto) {
        return "shops/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("shops") @Valid final ShopDto shopDto,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "shops/add";
        }
        shopsService.create(shopDto);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("shops.create.success"));
        return "redirect:/shops";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("shops", shopsService.get(id));
        return "shops/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("shops") @Valid final ShopDto shopDto,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "shops/edit";
        }
        shopsService.update(id, shopDto);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("shops.update.success"));
        return "redirect:/shops";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        shopsService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("shops.delete.success"));
        return "redirect:/shops";
    }

}
