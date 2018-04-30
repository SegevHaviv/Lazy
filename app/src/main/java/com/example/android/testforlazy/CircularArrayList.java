package com.example.android.testforlazy;

import java.util.ArrayList;

public class CircularArrayList<E> extends ArrayList<E> {

    private static final String TAG = CircularArrayList.class.getSimpleName();

    @Override
    public E get(int index) {

        int result = index % size();
        result = Math.abs(result);
        return super.get(result);
    }
}
