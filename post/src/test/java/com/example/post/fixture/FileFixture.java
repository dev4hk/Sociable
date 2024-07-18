package com.example.post.fixture;

import com.example.post.model.FileInfo;

public class FileFixture {
    public static FileInfo get() {
        return new FileInfo("AABB", "TYPE");
    }
}
