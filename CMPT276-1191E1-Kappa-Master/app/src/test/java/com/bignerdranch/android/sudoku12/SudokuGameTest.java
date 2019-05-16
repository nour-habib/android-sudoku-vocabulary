package com.bignerdranch.android.sudoku12;

import android.arch.lifecycle.ViewModelProvider;

import org.junit.Test;

import static org.junit.Assert.*;

public class SudokuGameTest {

    @Test
    public void testSudokuGame()
    {
        SudokuGame sudokuGameTestObject = new SudokuGame(9,6);
        assertEquals(9,sudokuGameTestObject.getN());
        assertEquals(6,sudokuGameTestObject.getK());


    }

    @Test
    public void testgetMat() {
        int N = 9;
        int k = 10;
        SudokuGame sudokuGameTestObject = new SudokuGame(N,k);
        String[] stringArrayTestObject = sudokuGameTestObject.getMat();
        assertEquals(N*N, stringArrayTestObject.length);

    }

    @Test
    public void testfillValues() {
        int N = 9;
        int k = 10;
        SudokuGame sudokuGameTestObject = new SudokuGame(N,k);
        sudokuGameTestObject.fillValues();
        String[] gameArray = sudokuGameTestObject.getMat();

        //count number of empty spaces in gamearray, check to see if equal to k;
    }



}