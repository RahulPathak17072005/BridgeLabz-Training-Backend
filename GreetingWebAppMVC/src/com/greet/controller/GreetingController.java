package com.greet.controller;
import com.greet.model.Greeting;
import com.greet.service.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
/**
 * GreetingController — Spring MVC Controller for the Greeting Application.
 *
 * Replaces the old GreetingServlet entirely.
 *
 * Key differences from GreetingServlet:
 * 1. Annotated with @Controller — Spring registers this as a web controller
 * 2. No need to extend HttpServlet
 * 3. No manual Spring context loading in init()
 * 4. GreetingService is @Autowired — Spring injects it automatically
 * 5. URL mapping via @GetMapping/@PostMapping — no web.xml changes needed
 * 6. Returns view name as String — ViewResolver finds the JSP
 * 7. Model object replaces request.setAttribute()
 */
@Controller
@RequestMapping("/greet")
/*
* @Controller:
* Marks this class as a Spring MVC controller.
SpringMVCImplementationPlanGreetingApplication.md 2026-06-21
7 / 19
* Registered as a bean by component-scan in dispatcher-servlet.xml.
* Unlike @Service or @Component, @Controller also enables request handling.
*
* @RequestMapping("/greet"):
* All methods in this controller handle requests to /greet.
* Specific GET or POST is defined per-method with @GetMapping/@PostMapping.
*/
public class GreetingController {
    /**
     * @Autowired:
     * Spring automatically injects the GreetingService bean declared in
     * dispatcher-servlet.xml (or found via component scan if @Service is used).
     * No need for: context.getBean("greetingService") like in the old Servlet.
     */
    @Autowired
    private GreetingService greetingService;   //
    // ──────────────────────────────────────────────────────────────
    // GET /GreetingWebApp/greet — Show the input form
    // ──────────────────────────────────────────────────────────────
    /**
     * Handles GET request to /greet.
     * Shows the greeting form (index.jsp).
     *
     * @param model — Spring's Model object. Attributes added here are
     * available in the JSP as ${attributeName}.
     * @return "index" — ViewResolver resolves this to /WEB-INF/views/index.jsp
     */
    @GetMapping
    public String showForm(Model model) {
        System.out.println("=== GreetingController.showForm() — GET /greet ===");
        // Optionally add default data to the model
        model.addAttribute("pageTitle", "Spring Greeting App");
        // Return view name — ViewResolver maps "index" → /WEB-INF/views/index.jsp
        return "index";
    }
    // ──────────────────────────────────────────────────────────────
    // POST /GreetingWebApp/greet — Process the form and show result
    // ──────────────────────────────────────────────────────────────
    /**
     * Handles POST request to /greet.
     * Reads the "userName" form field, generates a greeting, passes it to JSP.
     *
     * @param userName — Spring automatically reads the "userName" request parameter.
     * Equivalent to: request.getParameter("userName")
     * @RequestParam maps the HTTP form field name to this parameter.
     * @param model — Spring's Model: add attributes here to pass to JSP.
     * Equivalent to: request.setAttribute("greeting", greetObj)
     * @return "greeting" — ViewResolver maps this to /WEB-INF/views/greeting.jsp
     */
    @PostMapping
    public String handleGreet(
            @RequestParam("userName") String userName,
            Model model) {
        System.out.println("=== GreetingController.handleGreet() — POST /greet ===");
        System.out.println(" Received userName: " + userName);
        // Call the Spring-managed service bean
        Greeting greeting = greetingService.greet(userName);
        System.out.println(" Greeting generated: " + greeting);
        // Add greeting to model — available in JSP as ${greeting}
        model.addAttribute("greeting", greeting);
        // Return view name — ViewResolver maps "greeting" → /WEB-INF/views/greeting.jsp
        return "greeting";
    }
}
