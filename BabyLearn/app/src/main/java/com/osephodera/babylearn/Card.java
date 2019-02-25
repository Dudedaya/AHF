package com.osephodera.babylearn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Card {
    public static final int COLOR_BLANK = R.color.blank;
    public static final int COLOR_GREEN = R.color.green;
    public static final int COLOR_YELLOW = R.color.yellow;
    public static final int COLOR_ORANGE = R.color.orange;
    public static final int COLOR_BLUE = R.color.blue;
    public static final int COLOR_PINK = R.color.pink;
    public static final int COLOR_BLACK = R.color.black;
    public static final int COLOR_GREY = R.color.grey;
    public static final int COLOR_BROWN = R.color.brown;
    public static final int COLOR_VIOLET = R.color.violet;
    public static final int COLOR_RED = R.color.red;

    public static final int SHAPE_BLANK = R.drawable.blank;
    public static final int SHAPE_OVAL = R.drawable.oval;
    public static final int SHAPE_TRIANGLE = R.drawable.triangle;
    public static final int SHAPE_SQUARE = R.drawable.square;
    public static final int SHAPE_RECTANGLE = R.drawable.rectangle;
    public static final int SHAPE_DIAMOND = R.drawable.diamond;
    public static final int SHAPE_PENTAGON = R.drawable.pentagon;
    public static final int SHAPE_STAR = R.drawable.star;
    public static final int SHAPE_CRESCENT = R.drawable.crescent;
    public static final int SHAPE_HEART = R.drawable.heart;
    public static final int SHAPE_CIRCLE = R.drawable.circle;

    private int color;
    private int shape;

    public int getColor() {
        return color;
    }

    public int getShape() {
        return shape;
    }

    private void setShape(int shape) {
        this.shape = shape;
    }

    private Card(int color, int shape) {
        this.color = color;
        this.shape = shape;
    }

    public int getShapeNameId(int shape) {
        switch (shape) {
            case SHAPE_OVAL:
                return R.string.shape_oval;
            case SHAPE_TRIANGLE:
                return R.string.shape_triangle;
            case SHAPE_SQUARE:
                return R.string.shape_square;
            case SHAPE_RECTANGLE:
                return R.string.shape_rectangle;
            case SHAPE_DIAMOND:
                return R.string.shape_diamond;
            case SHAPE_PENTAGON:
                return R.string.shape_pentagon;
            case SHAPE_STAR:
                return R.string.shape_star;
            case SHAPE_CRESCENT:
                return R.string.shape_crescent;
            case SHAPE_HEART:
                return R.string.shape_heart;
            case SHAPE_CIRCLE:
                return R.string.shape_circle;
        }
        return R.string.error;
    }

    public int getColorNameId(int color) {
        switch (color) {
            case COLOR_GREEN:
                return R.string.color_green;
            case COLOR_YELLOW:
                return R.string.color_yellow;
            case COLOR_ORANGE:
                return R.string.color_orange;
            case COLOR_BLUE:
                return R.string.color_blue;
            case COLOR_PINK:
                return R.string.color_pink;
            case COLOR_BLACK:
                return R.string.color_black;
            case COLOR_GREY:
                return R.string.color_grey;
            case COLOR_BROWN:
                return R.string.color_brown;
            case COLOR_VIOLET:
                return R.string.color_violet;
            case COLOR_RED:
                return R.string.color_red;
        }
        return R.string.error;
    }


    public static List<Card> getColorCards() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(COLOR_GREEN, SHAPE_BLANK));
        cards.add(new Card(COLOR_YELLOW, SHAPE_BLANK));
        cards.add(new Card(COLOR_ORANGE, SHAPE_BLANK));
        cards.add(new Card(COLOR_BLUE, SHAPE_BLANK));
        cards.add(new Card(COLOR_PINK, SHAPE_BLANK));
        cards.add(new Card(COLOR_BLACK, SHAPE_BLANK));
        cards.add(new Card(COLOR_GREY, SHAPE_BLANK));
        cards.add(new Card(COLOR_BROWN, SHAPE_BLANK));
        cards.add(new Card(COLOR_VIOLET, SHAPE_BLANK));
        cards.add(new Card(COLOR_RED, SHAPE_BLANK));
        Collections.shuffle(cards);
        return cards;
    }

    public static List<Card> getShapeCards() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(COLOR_BLANK, SHAPE_OVAL));
        cards.add(new Card(COLOR_BLANK, SHAPE_TRIANGLE));
        cards.add(new Card(COLOR_BLANK, SHAPE_SQUARE));
        cards.add(new Card(COLOR_BLANK, SHAPE_RECTANGLE));
        cards.add(new Card(COLOR_BLANK, SHAPE_DIAMOND));
        cards.add(new Card(COLOR_BLANK, SHAPE_PENTAGON));
        cards.add(new Card(COLOR_BLANK, SHAPE_STAR));
        cards.add(new Card(COLOR_BLANK, SHAPE_CRESCENT));
        cards.add(new Card(COLOR_BLANK, SHAPE_HEART));
        cards.add(new Card(COLOR_BLANK, SHAPE_CIRCLE));
        Collections.shuffle(cards);
        return cards;
    }

    public static List<Card> getColorShapeCards() {
        List<Card> cards = new ArrayList<>();
        cards.add(new Card(COLOR_GREEN, SHAPE_BLANK));
        cards.add(new Card(COLOR_YELLOW, SHAPE_BLANK));
        cards.add(new Card(COLOR_ORANGE, SHAPE_BLANK));
        cards.add(new Card(COLOR_BLUE, SHAPE_BLANK));
        cards.add(new Card(COLOR_PINK, SHAPE_BLANK));
        cards.add(new Card(COLOR_BLACK, SHAPE_BLANK));
        cards.add(new Card(COLOR_GREY, SHAPE_BLANK));
        cards.add(new Card(COLOR_BROWN, SHAPE_BLANK));
        cards.add(new Card(COLOR_VIOLET, SHAPE_BLANK));
        cards.add(new Card(COLOR_RED, SHAPE_BLANK));
        Collections.shuffle(cards);
        cards.get(0).setShape(SHAPE_OVAL);
        cards.get(1).setShape(SHAPE_TRIANGLE);
        cards.get(2).setShape(SHAPE_SQUARE);
        cards.get(3).setShape(SHAPE_RECTANGLE);
        cards.get(4).setShape(SHAPE_DIAMOND);
        cards.get(5).setShape(SHAPE_PENTAGON);
        cards.get(6).setShape(SHAPE_STAR);
        cards.get(7).setShape(SHAPE_CRESCENT);
        cards.get(8).setShape(SHAPE_HEART);
        cards.get(9).setShape(SHAPE_CIRCLE);
        Collections.shuffle(cards);
        return cards;
    }
}
