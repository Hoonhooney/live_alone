package com.example.kante.live_alone;
//피드(게시글) 속성
public class Item {
    int image;
    String title;
    String detail;

    int getImage() {
        return this.image;
    }
    String getTitle() {
        return this.title;
    }
    String getDetail() {
        return this.detail;
    }

    Item(int image, String title, String detail) {
        this.image = image;
        this.title = title;
        this.detail = detail;
    }
}
