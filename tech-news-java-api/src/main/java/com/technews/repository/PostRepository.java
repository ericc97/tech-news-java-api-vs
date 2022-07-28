package com.technews.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.technews.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer>{
    List<Post> findAllPostsByUserId(Integer id) throws Exception;
}
