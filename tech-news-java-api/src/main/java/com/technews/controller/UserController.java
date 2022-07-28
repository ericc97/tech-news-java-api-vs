package com.technews.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.technews.model.Post;
import com.technews.model.User;
import com.technews.repository.UserRepository;
import com.technews.repository.VoteRepository;


@RestController
public class UserController {
    // @AutoWired annotation = tells spring to only instiantiate each object as needed by the program
    @Autowired
    UserRepository repository;

    @Autowired
    VoteRepository voteRepository;

    // Add CRUD Logic
    @GetMapping("/api/users") // Declare the route and the type of HTTP method used (GET)
    public List<User> getAllUsers() {
        // Set return type to List<User> because we want it to return a list of users
        List<User> userList = repository.findAll(); // Calling findAll() on repository object to get a list of users and assign it to userList variable
        for (User u : userList) {
            List<Post> postList = u.getPosts(); // Call getPosts() on every user in u inside userList.
            for (Post p : postList) { // Iterate over post p in postList calling setVoteCount() passing in countVotesById(). Then using getId() to obtain the id of the post
                p.setVoteCount(voteRepository.countVotesByPostId(p.getId()));
            }
        }
        return userList; 
    }

    @GetMapping("/api/users/{id}")
    public User getUserById(@PathVariable Integer id) {
        User returnUser = repository.getReferenceById(id);
        List<Post> postList = returnUser.getPosts();
        for (Post p : postList) {
            p.setVoteCount(voteRepository.countVotesByPostId(p.getId()));
        }
        return returnUser; // Instead of returning a list the getUserById() will only return a single user
    }

    @PostMapping("/api/users")
    public User addUser(@RequestBody User user) {
        // Encrypt password
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        repository.save(user);
        return user;
    }

    @PutMapping("/api/users/{id}") 
    public User updateUser(@PathVariable int id, @RequestBody User user) {
        User tempUser = repository.getReferenceById(id);

        if (!tempUser.equals(null)) {
            user.setId(tempUser.getId());
            repository.save(user);
        }

        return user;
    }

    @DeleteMapping("/api/users/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable int id) {
        repository.deleteById(id);
    }

}
