package com.ua.hrv.controllers;

import com.ua.hrv.dao.CustomerDAO;
import com.ua.hrv.models.Customer;
import com.ua.hrv.service.CustomerService;
import com.ua.hrv.util.CustomerEditor;
import com.ua.hrv.util.CustomerValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.UUID;

@Controller
@PropertySource("classpath:validation.properties")
public class MainController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerEditor customerEditor;
    @Autowired
    private CustomerValidator customerValidator;
    @Autowired
    private Environment environment;
    @Autowired
    private JavaMailSender sender;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/ok")
    public String ok() {
        return "ok";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/save")
    public String save(Customer customer, BindingResult result, Model model) throws MessagingException {
        customerValidator.validate(customer, result);
        String errorMessage = "";
        if (result.hasErrors()) {
            List<ObjectError> allErrors = result.getAllErrors();
            for (ObjectError errors : allErrors) {
                errorMessage += " " + environment.getProperty(errors.getCode());
            }
            model.addAttribute("error", errorMessage);
            return "index";
        }
        customerEditor.setValue(customer);
        String activationCode = UUID.randomUUID().toString();
        customer.setActivationCode(activationCode);
        customerService.save(customer);
        sendMail(customer.getEmail(), activationCode);
        return "resp";
    }

    private void sendMail(String email, String code) throws MessagingException {
        MimeMessage mimeMessage = sender.createMimeMessage();
        MimeMessageHelper message = new MimeMessageHelper(mimeMessage, true);
        message.setSubject("Activate login");
        message.setText("Welcome to Opel Club. Please click this link: http://localhost:8080/activate/" + code, true);
        message.setTo(email);
        sender.send(mimeMessage);
    }

    @GetMapping("activate/{code}")
    public String activate(@PathVariable String code, Customer customer) {
        boolean activateCustomer = customerService.activateCustomer(code);
        if (activateCustomer) {
            customerService.flush(customer);
        }
        return "login";
    }

}
