package com.example.f15.fiftashki;


import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by rybin on 19.12.17.
 */

public class GameLogic {

    private int [][] field;
    private int fieldX;
    private int fieldY;

    private int zeroX;
    private int zeroY;

    public final int UP = 0;
    public final int RIGHT = 1;
    public final int DOWN = 2;
    public final int LEFT = 3;

    private int turnCount = 0;
    private boolean moveW = false;

    public GameLogic(int x, int y) {
        fieldX = x;
        fieldY = y;
        field = new int[fieldY][fieldX];
//        List <Integer> list2 = shlist();
        List <Integer> list2 = new ArrayList<>(fieldX * fieldY);
        for (int i = 0; i < fieldX * fieldY; i++) {
            list2.add(i);
        }
        Collections.shuffle(list2);
        int damnit = 10;
        while (fieldX == fieldY && !isSolvable(list2) && damnit > 0) {
            Collections.shuffle(list2);
            damnit--;
        }
        for (int i = 0; i < fieldY; i++) {
            for (int j = 0; j < fieldX; j++) {
//                field[i][j] = i*fieldX + j;
                field[i][j] = list2.get(i*fieldX + j);
            }
        }

        // Find zero - this will be empty movable square
        for (int i = 0; i < fieldY; i++) {
            for (int j = 0; j < fieldX; j++) {
                if (field[i][j] == 0) {
                    zeroX = j;
                    zeroY = i;
                }
            }
        }
    }

    private List <Integer> shlist () {
        List<Integer> list2 = new ArrayList<>(fieldX * fieldY);
        for (int i = 0; i < fieldX * fieldY; i++) {
            list2.add(i);
        }
        Collections.shuffle(list2);
        int damnit = 10;
        while (fieldX == fieldY && !isSolvable(list2) && damnit > 0) {
            Collections.shuffle(list2);
            damnit--;
        }
        return list2;
    }

    public void move(int direction) {
        turnCount++;
        int newZero;
        switch (direction) {
            case UP:
                newZero = moveW ? zeroY - 1 : zeroY + 1;
                if (newZero < 0 || newZero >= fieldY) { break; }
                field[zeroY][zeroX] = field[newZero][zeroX];
                field[newZero][zeroX] = 0;
                zeroY = newZero;
                break;
            case RIGHT:
                newZero = moveW ? zeroX + 1 : zeroX - 1;
                if (newZero < 0 || newZero >= fieldY) { break; }
                field[zeroY][zeroX] = field[zeroY][newZero];
                field[zeroY][newZero] = 0;
                zeroX = newZero;
                break;
            case DOWN:
                newZero = moveW ? zeroY + 1 : zeroY - 1;
                if (newZero < 0 || newZero >= fieldY) { break; }
                field[zeroY][zeroX] = field[newZero][zeroX];
                field[newZero][zeroX] = 0;
                zeroY = newZero;
                break;
            case LEFT:
                newZero = moveW ? zeroX - 1 : zeroX + 1;
                if (newZero < 0 || newZero >= fieldY) { break; }
                field[zeroY][zeroX] = field[zeroY][newZero];
                field[zeroY][newZero] = 0;
                zeroX = newZero;
                break;
            default:
                break;
        }
    }

    public int getTurnCount() {
        return  turnCount;
    }

    public int getF(int i, int j) {
        return field[i][j];
    }

    public int getFieldX() {
        return fieldX;
    }

    public int getFieldY() {
        return fieldY;
    }

    public int getZeroX() {
        return zeroX;
    }

    public int getZeroY() {
        return zeroY;
    }

    public void changeMode() { moveW = !moveW; }

    public boolean isSolvable(List<Integer> puzzle)
    {
        int parity = 0;
        int gridWidth = (int) Math.sqrt(puzzle.size());
        int row = 0; // the current row we are on
        int blankRow = 0; // the row with the blank tile

        for (int i = 0; i < puzzle.size(); i++) {
            if (i % gridWidth == 0) { // advance to next row
                row++;
            }
            if (puzzle.get(i) == 0) { // the blank tile
                blankRow = row; // save the row on which encountered
                continue;
            }
            for (int j = i + 1; j < puzzle.size(); j++) {
                if (puzzle.get(i) > puzzle.get(j) && puzzle.get(j) != 0) {
                    parity++;
                }
            }
        }

        if (gridWidth % 2 == 0) { // even grid
            if (blankRow % 2 == 0) { // blank on odd row; counting from bottom
                return parity % 2 == 0;
            } else { // blank on even row; counting from bottom
                return parity % 2 != 0;
            }
        } else { // odd grid
            return parity % 2 == 0;
        }
    }

    private void logField() {
        for (int i = 0; i < this.fieldY; i++) {
            for (int j = 0; j < this.fieldX; j++) {
                Log.d("№ " + i + "," + j, Integer.toString(field[i][j]));
            }
        }
    }
}
