package com.badlogic.tankStars;


import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class EnvironmentGenerator {
    public int RandomIntGeneratorX(){
        int max=1920;
        int min=-1920;
        Random x = new Random();
        return x.nextInt(max-min)+min;
    }

    public int RandomIntGeneratorY(){
        int max=700;
        int min=100;
        Random y = new Random();
        return y.nextInt(max-min)+min;
    }


}
