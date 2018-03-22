package com.yejy.app.service;

import com.yejy.app.model.Blog;

public interface BlogService {

    Blog selectBlog(int id);

    Integer sumBlog();

    Integer addBlog(Blog blog);
}
