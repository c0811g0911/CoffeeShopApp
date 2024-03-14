package digital101.io.controller;

import digital101.io.domain.Menu;
import digital101.io.model.MenuItemDto;
import digital101.io.repos.MenuRepository;
import digital101.io.service.MenuItemService;
import digital101.io.util.CustomCollectors;
import digital101.io.util.ReferencedWarning;
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
@RequestMapping("/menuItems")
public class MenuItemController {

    private final MenuItemService menuItemService;
    private final MenuRepository menuRepository;

    public MenuItemController(final MenuItemService menuItemService,
                              final MenuRepository menuRepository) {
        this.menuItemService = menuItemService;
        this.menuRepository = menuRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("menuValues", menuRepository.findAll(Sort.by("id"))
                .stream()
                .collect(CustomCollectors.toSortedMap(Menu::getId, Menu::getId)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("menuItems", menuItemService.findAll());
        return "menuItems/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("menuItems") final MenuItemDto menuItemDto) {
        return "menuItems/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("menuItems") @Valid final MenuItemDto menuItemDto,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "menuItems/add";
        }
        menuItemService.create(menuItemDto);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("menuItems.create.success"));
        return "redirect:/menuItems";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("menuItems", menuItemService.get(id));
        return "menuItems/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("menuItems") @Valid final MenuItemDto menuItemDto,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "menuItems/edit";
        }
        menuItemService.update(id, menuItemDto);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("menuItems.update.success"));
        return "redirect:/menuItems";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = menuItemService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            menuItemService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("menuItems.delete.success"));
        }
        return "redirect:/menuItems";
    }

}
