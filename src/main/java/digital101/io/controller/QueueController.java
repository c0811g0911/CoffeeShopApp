package digital101.io.controller;

import digital101.io.model.QueueDto;
import digital101.io.service.QueueService;
import digital101.io.util.ReferencedWarning;
import digital101.io.util.WebUtils;
import jakarta.validation.Valid;
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
@RequestMapping("/queues")
public class QueueController {

    private final QueueService queueService;

    public QueueController(final QueueService queueService) {
        this.queueService = queueService;
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("queues", queueService.findAll());
        return "queues/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("queues") final QueueDto queueDto) {
        return "queues/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("queues") @Valid final QueueDto queueDto,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "queues/add";
        }
        queueService.create(queueDto);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("queues.create.success"));
        return "redirect:/queues";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id, final Model model) {
        model.addAttribute("queues", queueService.get(id));
        return "queues/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable(name = "id") final Long id,
            @ModelAttribute("queues") @Valid final QueueDto queueDto,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "queues/edit";
        }
        queueService.update(id, queueDto);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("queues.update.success"));
        return "redirect:/queues";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable(name = "id") final Long id,
            final RedirectAttributes redirectAttributes) {
        final ReferencedWarning referencedWarning = queueService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR,
                    WebUtils.getMessage(referencedWarning.getKey(), referencedWarning.getParams().toArray()));
        } else {
            queueService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("queues.delete.success"));
        }
        return "redirect:/queues";
    }

}
