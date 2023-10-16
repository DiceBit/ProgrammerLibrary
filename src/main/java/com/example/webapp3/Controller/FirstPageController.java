package com.example.webapp3.Controller;

import com.example.webapp3.Models.Active;
import com.example.webapp3.Models.Book;
import com.example.webapp3.Models.User;
import com.example.webapp3.Repositories.BookRepository;
import com.example.webapp3.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
public class FirstPageController {

    @Value("${upload.path}")
    private String fileDirectory;

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookRepository bookRepository;

    @GetMapping
    private String getHome(Model model) {
        return "home";
    }

    @GetMapping("/file/{bookFileName}")
    private ResponseEntity<Resource> linkDownloadHandler(@PathVariable String bookFileName)
            throws MalformedURLException {

        Path filePath = Paths.get(fileDirectory).resolve(bookFileName);
        Resource resource = new UrlResource(filePath.toUri());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Book infoBook = bookRepository.findByFileName(bookFileName);
        User user = userRepository.findByUsername(auth.getName());

        user.setBalance(user.getBalance() - infoBook.getPrice());

        List<Active> activity = new ArrayList<>(user.getActivity());

        StringBuilder activityText = new StringBuilder("buy the book \"");
        activityText.append(infoBook.getBookName()).append("\" for ")
                .append(infoBook.getPrice()).append("BalanceCoin on ")
                .append(LocalDateTime.now()
                        .format(DateTimeFormatter
                                .ofPattern("dd-MM-yyyy HH:mm")));

        activity.add(new Active(activityText));
        user.setActivity(activity);

        userRepository.save(user);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    @PostMapping("/file/{bookFileName}")
    private String linkDownloadHandlerPost(@PathVariable String bookFileName) {

        return "redirect:/main";
    }
}
