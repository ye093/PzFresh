package com.yejy.app.data;

import com.yejy.app.model.Blog;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BlogMapper {
    Blog selectBlog(int id);

    Integer sumBlog();

    Integer addBlog(Blog blog);

    List<Blog> list();
}
