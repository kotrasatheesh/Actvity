package com.example.asutosh.aebug.bean;

/**
 * Created by Asutosh on 06-09-2017.
 */

import android.graphics.Bitmap;

import java.io.Serializable;

/**
 * Created by Asutosh on 22-07-2017.
 */

public class recylcer_data_model implements Serializable {  // Getter and Setter model for recycler view items
    String title;
    Bitmap image;
    String imageslide;

    public String getImageURl() {
        return imageURl;
    }

    public void setImageURl(String imageURl) {
        this.imageURl = imageURl;
    }

    String imageURl;
    public String getImageslide() {
        return imageslide;
    }

    public void setImageslide(String imageslide) {
        this.imageslide = imageslide;
    }




    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }



    public Bitmap getImage() {
        return image;
    }
}