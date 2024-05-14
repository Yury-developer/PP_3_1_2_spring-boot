package academy.kata.controller;

import academy.kata.model.User;
import academy.kata.model.entry.EmailEntry;
import academy.kata.model.entry.PhoneEntry;
import academy.kata.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;


/**
 * @Author: Yury Lapitski
 * 2024-05-14
 */
@Controller
@RequestMapping(value = "/users")
public class UserController {

    private final UserService userService;

    private static Logger LOGGER;

    static {
        try {
            Resource resource = new ClassPathResource("userController_loggerConfig.properties");
            InputStream ins = resource.getInputStream();
            LogManager.getLogManager().readConfiguration(ins);
            LOGGER = Logger.getLogger(UserController.class.getName());
            LOGGER.setLevel(Level.ALL);
        } catch (IOException e) {
            e.printStackTrace();
            LOGGER = null; // Или другое действие по умолчанию
        }
    }


    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }



    // просто возвращает страницу приветствия (с нее можно сгенерировать тестовые данные во все таблицы)
    @RequestMapping(method = RequestMethod.HEAD)
    public String ping() {
        LOGGER.fine("UserController: ping");
        return "redirect:/";
    }



    @GetMapping("/create")
    public String showCreateUserForm(Model model) {
        LOGGER.fine("UserController: showCreateUserForm");
        model.addAttribute("createdUser", new User());
        return "create-user";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute("createdUser") User user) {
        LOGGER.fine("UserController: addUser, user = " + user);
        userService.saveUser(user);
        return "redirect:/users";
    }



    @GetMapping("/view")
    public String showUserDetails(@RequestParam(defaultValue = "0", required = false, name = "user_id") Long userId,
                                  Model model) {
        LOGGER.fine("UserController: showUserDetails, user_id = " + userId);
        User user = userService.findById(userId);
        model.addAttribute("viewUser", user);
        return "view-user";
    }



    @GetMapping()
    public String showAllUsers(Model model) {
        LOGGER.fine("UserController: showAllUsers");
        List<User> userList = userService.findAll();
        model.addAttribute("viewAllUsers", userList);
        return "all-users";
    }



    @GetMapping("/edit")
    public String showEditUserForm(@RequestParam(name = "user_id", required = true) Long userId,
                                   Model model) {
        LOGGER.fine("UserController: showEditUserForm, user_id = " + userId);
        User user = userService.findById(userId);
        model.addAttribute("editUser", user);
        return "edit-user";
    }

    @PostMapping("/edit")
    public String editUser(@RequestParam(name = "id", required = true) Long userId,
                           @ModelAttribute("editUser") User user) {
        LOGGER.fine("UserController: editUser, user_id = " + userId + "\n user = " + user);
        userService.update(user);
        return "redirect:/users";
    }



    @GetMapping("/addPhone")
    public String addPhone(@RequestParam(name = "user_id") Long userId,
                           Model model) {
        LOGGER.fine("UserController: addPhone, \n\tuser_id = " + userId);
        model.addAttribute("user_id", userId);
        model.addAttribute("phone_entry", new PhoneEntry());
        return "hepler-pages/add-phone-page";
    }

    @PostMapping("/savePhone")
    public String savePhone(@RequestParam(name = "user_id", required = true) Long userId,
                            @ModelAttribute("phone_entry") PhoneEntry phoneEntry) {
        LOGGER.fine("UserController: savePhone, user_id = " + userId + "\n phone_entry = " + phoneEntry);
        User user = userService.findById(userId);
        user.getPhones().add(phoneEntry);
        userService.update(user);
        return "redirect:/users";
    }

    @GetMapping("/deletePhone")
    public String deletePhone(@RequestParam(name = "user_id") Long userId,
                              @RequestParam(name = "phone_id") Long phoneId,
                              Model model) {
        LOGGER.fine("UserController: deletePhone, \tuser_id = " + userId + "\tphone_id = " + phoneId);
        User user = userService.findById(userId);
        user.getPhones().remove(user.getPhones().get(Math.toIntExact(phoneId)));
        if (user.getPhones().isEmpty()) {
            user.getPhones().add(new PhoneEntry("", ""));
        }
        userService.update(user);
        model.addAttribute("editUser", user);
        return "redirect:/users/edit?user_id=" + userId;
    }



    @GetMapping("/addEmail")
    public String addEmail(@RequestParam(name = "user_id") Long userId,
                           Model model) {
        LOGGER.fine("UserController: addPhone, \tuser_id = " + userId);
        model.addAttribute("user_id", userId);
        model.addAttribute("email_entry", new EmailEntry());
        return "hepler-pages/add-email-page";
    }

    @PostMapping("/saveEmail")
    public String saveEmail(@RequestParam(name = "user_id", required = true) Long userId,
                            @ModelAttribute("email_entry") EmailEntry emailEntry) {
        LOGGER.fine("UserController: saveEmail, user_id = " + userId + " email_entry = " + emailEntry);
        User user = userService.findById(userId);
        user.getEmails().add(emailEntry);
        userService.update(user);
        return "redirect:/users";
    }

    @GetMapping("/deleteEmail")
    public String deleteEmail(@RequestParam(name = "user_id") Long userId,
                              @RequestParam(name = "email_id") Long emailId,
                              Model model) {
        LOGGER.fine("UserController: deleteEmail, \tuser_id = " + userId + "\tphone_id = " + emailId);
        User user = userService.findById(userId);
        user.getEmails().remove(user.getEmails().get(Math.toIntExact(emailId)));
        if (user.getEmails().isEmpty()) {
            user.getEmails().add(new EmailEntry("", ""));
        }
        userService.update(user);
        model.addAttribute("editUser", user);
        return "redirect:/users/edit?user_id=" + userId;
    }



    @GetMapping("/delete")
    public String deleteUser(@RequestParam(name = "user_id") Long userId) {
        LOGGER.fine("UserController: deleteUser, user_id = " + userId);
        userService.deleteById(userId);
        return "redirect:/users";
    }
}
