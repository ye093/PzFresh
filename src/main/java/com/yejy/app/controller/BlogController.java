package com.yejy.app.controller;

import com.yejy.app.model.BaseModel;
import com.yejy.app.model.Blog;
import com.yejy.app.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BlogController {
    @Autowired
    BlogService blogService;

    @RequestMapping(value = "blog/{id}", method = RequestMethod.GET)
    public ResponseEntity<BaseModel> getBlogById(@PathVariable("id") Integer id) {
        Blog data = blogService.selectBlog(id);
        return ResponseEntity.ok(new BaseModel(0, "ok", data));
    }

    @RequestMapping(value = "blog/count", method = RequestMethod.GET)
    public ResponseEntity<BaseModel> count() {
        return ResponseEntity.ok(new BaseModel(0, "ok", blogService.sumBlog()));
    }

    @RequestMapping(value = "blog/list", method = RequestMethod.GET)
    public ResponseEntity<BaseModel> list() {
        return ResponseEntity.ok(new BaseModel(0, "ok", blogService.listBlogs()));
    }
}
