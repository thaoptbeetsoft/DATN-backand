package com.codegym.demo.controller;

import com.codegym.demo.dto.response.PostResponse;
import com.codegym.demo.dto.response.Response;
import com.codegym.demo.model.Company;
import com.codegym.demo.model.Post;
import com.codegym.demo.service.category.ICategoryService;
import com.codegym.demo.service.company.ICompanyService;
import com.codegym.demo.service.post.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.codegym.demo.dto.response.ResponseBody;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("posts")
@CrossOrigin("*")
public class PostController {
    @Autowired
    private IPostService iPostService;
    @Autowired
    private ICompanyService iCompanyService;
    @Autowired
    private ICategoryService iCategoryService;

    @GetMapping
    public ResponseEntity<Iterable<Post>> findAll() {
        List<Post> posts = (List<Post>) iPostService.findAll();
        if (posts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseBody> create(@Validated @RequestBody Post post) {
        try {
            if (post.getQuantity() == 0) {
                return new ResponseEntity<>(new ResponseBody(Response.OBJECT_INVALID, null), HttpStatus.BAD_REQUEST);
            }
            Company company = iCompanyService.findById(post.getCompany().getId()).get();
            post.setCode("CODE" + company.getCompanyCode() + post.getCategory().getId());
            post.setStatus(true);
            return new ResponseEntity<>(new ResponseBody(Response.SUCCESS, iPostService.save(post)), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseBody(Response.SYSTEM_ERROR, null), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Post> findById(@PathVariable Long id) {
        Optional<Post> postOptional = iPostService.findById(id);

        if (!postOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(postOptional.get(), HttpStatus.OK);
    }

    @GetMapping("/companies/{idCompany}")
    public ResponseEntity<Iterable<Post>> findAllByCompany(@PathVariable Long idCompany) {
        List<Post> posts = (List<Post>) iPostService.findAllByIdCompany(idCompany);
        if (posts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @GetMapping("/get-top-5-companies")
    public ResponseEntity<Iterable<Post>> findTop5Company() {
        List<Post> posts = (List<Post>) iPostService.findTop5Company();
        if (posts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseBody> editPost(@PathVariable Long id, @RequestBody Post post) {
        Optional<Post> postOptional = iPostService.findById(id);
        post.setCode(postOptional.get().getCode());
        post.setStatus(postOptional.get().getStatus());
        post.setId(id);
        if (!postOptional.isPresent()) {
            return new ResponseEntity<>(new ResponseBody(Response.OBJECT_INVALID, null), HttpStatus.BAD_REQUEST);
        }
        if (post.getQuantity() == 0) {
            return new ResponseEntity<>(new ResponseBody(Response.OBJECT_INVALID, null), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseBody(Response.SUCCESS, iPostService.save(post)), HttpStatus.CREATED);
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<ResponseBody> editStatus(@PathVariable Long id) {
        Optional<Post> postOptional = iPostService.findById(id);

        if (!postOptional.isPresent()) {
            return new ResponseEntity<>(new ResponseBody(Response.OBJECT_INVALID, null), HttpStatus.BAD_REQUEST);
        }
        if (postOptional.get().getQuantity() == 0) {
            return new ResponseEntity<>(new ResponseBody(Response.OBJECT_INVALID, null), HttpStatus.BAD_REQUEST);
        }
        if (postOptional.get().getStatus() == true) {
            postOptional.get().setStatus(false);
        } else {
            postOptional.get().setStatus(true);
        }
        Post post = postOptional.get();
        return new ResponseEntity<>(new ResponseBody(Response.SUCCESS, iPostService.save(post)), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Post> delete(@PathVariable Long id) {
        Optional<Post> productOptional = iPostService.findById(id);
        if (!productOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        iPostService.remove(id);
        return new ResponseEntity<>(productOptional.get(), HttpStatus.NO_CONTENT);
    }

    @GetMapping("/position")
    public ResponseEntity<Iterable<Post>> findAllByPositionContaining(String position) {
        List<Post> postList = (List<Post>) iPostService.findAllByPositionContaining(position);
        if (postList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(postList, HttpStatus.OK);
    }

    @GetMapping("/title")
    public ResponseEntity<Iterable<Post>> findAllByTitleContaining(String title) {
        List<Post> postList = (List<Post>) iPostService.findAllByTitleContaining(title);
        if (postList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(postList, HttpStatus.OK);
    }

    @GetMapping("/address")
    public ResponseEntity<Iterable<Post>> findAllByAddressContaining(String address) {
        List<Post> postList = (List<Post>) iPostService.findAllByAddressContaining(address);
        if (postList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(postList, HttpStatus.OK);
    }

    @GetMapping("/salary")
    public ResponseEntity<Iterable<Post>> findAllBySalaryBetween(String salary) {
        List<Post> postList = (List<Post>) iPostService.findAllBySalaryContaining(Double.parseDouble(salary));
        if (postList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(postList, HttpStatus.OK);
    }

//    @GetMapping("/find")
//    public ResponseEntity<Iterable<Post>> searchAdvanced(String title, Integer salary, String exp, String address) {
//        if (salary == null) {
//            salary = 0;
//        }
//        List<Post> postList = (List<Post>) iPostService.searchAdvanced(title, salary, exp, address);
//        if (postList.isEmpty()) {
//            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//        }
//        return new ResponseEntity<>(postList, HttpStatus.OK);
//    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    private ResponseEntity<?> findJob(@RequestParam(name = "keyword") String keyword, @RequestParam(name = "cat_id") Long cat_id, @RequestParam(name = "city_id") Long city_id, @RequestParam(name = "salary1") double salary1, @RequestParam(name = "salary2") double salary2) {
        List<Post> posts;
        posts = iPostService.searchJob(keyword, cat_id, city_id, salary1, salary2);
        System.out.println(keyword);
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }

    @RequestMapping(value = "/count", method = RequestMethod.GET)
    private ResponseEntity<?> countPosts() {
        List<Post> postList = (List<Post>) iPostService.findAll();
        return new ResponseEntity<>(postList.size(), HttpStatus.OK);
    }
    @GetMapping("/{id}/{cat_id}")
    public ResponseEntity<?> findAllByCategory(@PathVariable(name = "id") Long id, @PathVariable(name = "cat_id") Long cat_id){
        System.out.println("wtf");
        System.out.println(cat_id);
        List<Post> posts = iPostService.findByCategory(cat_id,id);
        if (posts.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(posts, HttpStatus.OK);
    }
    @GetMapping("/new")
    private ResponseEntity<?> getnewPosts() {
        return new ResponseEntity<>(iPostService.find20PostsOrderByIdDesc(), HttpStatus.OK);
    }

}
