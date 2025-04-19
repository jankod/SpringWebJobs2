package hr.bioinfo.swj.rest;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class HomeController {

    @GetMapping("/")
    public String index(Model model) {
        log.info("HomeController.index() called");
        model.addAttribute("title", "Home - SpringWebJobs");
        model.addAttribute("pageTitle", "Welcome to SpringWebJobs");
        model.addAttribute("activeTab", "home");
        model.addAttribute("content", "index :: content");
        return "index";
    }

    @GetMapping("/jobs")
    public String jobs(Model model) {
        log.info("HomeController.jobs() called");
        model.addAttribute("title", "Jobs - SpringWebJobs");
        model.addAttribute("pageTitle", "Job Management");
        model.addAttribute("activeTab", "jobs");
        model.addAttribute("content", "jobs :: content");
        return "jobs";
    }

    @GetMapping("/about")
    public String about(Model model) {
       // log.info("HomeController.about() called");
        model.addAttribute("title", "About - SpringWebJobs");
        model.addAttribute("pageTitle", "About SpringWebJobs");
        model.addAttribute("activeTab", "about");
        model.addAttribute("content", "about :: content");
        return "about";
    }

    @GetMapping("/new-job")
    public String newJob(Model model) {
       // log.info("HomeController.newJob() called");
        model.addAttribute("title", "New Job - SpringWebJobs");
        model.addAttribute("pageTitle", "Create New Job");
        model.addAttribute("activeTab", "jobs");
        model.addAttribute("content", "new-job :: content");
        return "new-job";
    }
}
