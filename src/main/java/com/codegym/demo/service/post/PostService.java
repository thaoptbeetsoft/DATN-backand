package com.codegym.demo.service.post;

import com.codegym.demo.model.Company;
import com.codegym.demo.model.Post;
import com.codegym.demo.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class PostService implements IPostService{
    @Autowired
    private PostRepository postRepository;
    @Override
    public Iterable<Post> findAll() {
        return postRepository.findAll();
    }

    @Override
    public Optional<Post> findById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public Post save(Post post) {
        return postRepository.save(post);
    }

    @Override
    public void remove(Long id) {
        postRepository.deleteById(id);
    }

    @Override
    public Iterable<Post> findAllByIdCompany(Long id) {
        return postRepository.findAllByIdCompany(id);
    }

    @Override
    public Iterable<Post> findTop5Company() {
        return postRepository.findTop5Company();
    }
}
