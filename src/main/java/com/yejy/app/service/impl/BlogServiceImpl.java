package com.yejy.app.service.impl;

import com.yejy.app.data.BlogMapper;
import com.yejy.app.model.Blog;
import com.yejy.app.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    BlogMapper blogMapper;

    @Override
    public Blog selectBlog(int id) {
        return blogMapper.selectBlog(id);
    }

    @Override
    public Integer sumBlog() {
        return blogMapper.sumBlog();
    }

    @Override
    public Integer addBlog(Blog blog) {
        return blogMapper.addBlog(blog);
    }

    @Override
    public List<Blog> listBlogs() {
        return blogMapper.list();
    }
}
