package com.example.pizza;

public class Pizza {
    private String name;
    private int imageResourceId;

    public static final Pizza[] pizzas = {
            new Pizza ("Diavolo", R.drawable.diavolo),
            new Pizza ("Funghi", R.drawable.funghi),
            new Pizza ("Diavolo", R.drawable.diavolo),
            new Pizza ("Funghi", R.drawable.funghi),
            new Pizza ("Diavolo", R.drawable.diavolo),
            new Pizza ("Funghi", R.drawable.funghi),
            new Pizza ("Diavolo", R.drawable.diavolo),
            new Pizza ("Funghi", R.drawable.funghi),
            new Pizza ("Diavolo", R.drawable.diavolo),
            new Pizza ("Funghi", R.drawable.funghi),
            new Pizza ("Diavolo", R.drawable.diavolo),
            new Pizza ("Funghi", R.drawable.funghi),
            new Pizza ("Diavolo", R.drawable.diavolo),
            new Pizza ("Funghi", R.drawable.funghi)
    };

    public String getName() {
        return name;
    }

    public int getImageResourceId() {
        return imageResourceId;
    }

    private Pizza(String name, int imageResourceId) {
        this.name = name;
        this.imageResourceId = imageResourceId;
    }

}
