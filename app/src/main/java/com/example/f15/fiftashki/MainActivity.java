package com.example.f15.fiftashki;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "DEBUG";
    public GameLogic game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        game = new GameLogic(4,4);
        final DrawView drawView = new DrawView(this);
        drawView.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeRight() {
//                Log.d(DEBUG_TAG, "right");
                game.move(game.RIGHT);
                drawView.invalidate();
            }

            public void onSwipeLeft() {
//                Log.d(DEBUG_TAG, "left");
                game.move(game.LEFT);
                drawView.invalidate();
            }

            public void onSwipeTop() {
//                Log.d(DEBUG_TAG, "top");
                game.move(game.UP);
                drawView.invalidate();
            }

            public void onSwipeBottom() {
//                Log.d(DEBUG_TAG, "bot");
                game.move(game.DOWN);
                drawView.invalidate();
            }
        });
        setContentView(drawView);

    }

    class DrawView extends View {
        public int color = Color.GREEN;
        Paint p;
        Bitmap bm;
        Bitmap [] cr;
        int sqr;

        public DrawView(Context context) {
            super(context);
            p = new Paint();
//            Log.d("w,h", getResources().getDisplayMetrics().widthPixels + " " + getResources().getDisplayMetrics().heightPixels);
            sqr = getResources().getDisplayMetrics().widthPixels;
//            bm = BitmapFactory.decodeResource(getResources(), R.drawable.tux);
//            bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.tux), sqr, sqr, false);
            bm = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.sqrpnc), sqr, sqr, false);
//            cr = Bitmap.createBitmap(bm, 0, 0, bm.getWidth() / 2, bm.getHeight());

            Log.d(DEBUG_TAG, bm.getWidth() + " " + bm.getHeight());
            Log.d(DEBUG_TAG, sqr * 2 / game.getFieldX() + " ");
            Log.d(DEBUG_TAG, sqr * (2+1) / game.getFieldX() + " ");
            cr = new Bitmap[game.getFieldY()*game.getFieldX()];
//            cr[0] = Bitmap.createBitmap(bm, bm.getWidth()/ 2, 0, bm.getWidth() / 4, bm.getHeight());
//            cr[0] = Bitmap.createBitmap(bm, sqr * 2 / game.getFieldX(), sqr * 2 / game.getFieldY(), sqr / game.getFieldX(), sqr / game.getFieldY());

            for (int i = 0; i < game.getFieldY(); i++) {
                for (int j = 0; j < game.getFieldX(); j++) {
                    cr[i*game.getFieldX()+j] = Bitmap.createBitmap(bm, sqr * j / game.getFieldX(), sqr * i / game.getFieldY(), sqr / game.getFieldX(), sqr / game.getFieldY());
                }
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(Color.WHITE);
//            canvas.drawColor(color);
            p.setTextSize(canvas.getHeight() / 10);
            canvas.drawText(Integer.toString(game.getTurnCount()), canvas.getWidth() / 2, canvas.getHeight() / 10, p);

            int top, left, bot, right;
//            int sq = canvas.getWidth();
            left = 0;
            right = canvas.getWidth();
            top = canvas.getHeight() / 2 - canvas.getWidth() / 2;
            bot = canvas.getHeight() / 2 + canvas.getWidth() / 2;
            for (int i = 0; i <= game.getFieldY(); i++) {
                canvas.drawLine(left, top + i * sqr / game.getFieldY(), right, top + i * sqr / game.getFieldY(), p);
            }
            for (int i = 0; i <= game.getFieldX(); i++) {
                canvas.drawLine(left + i * sqr / game.getFieldX(), top, left + i * sqr / game.getFieldX(), bot, p);
            }
            p.setTextSize(canvas.getWidth() / game.getFieldY() / 2);
            for (int i = 0; i < game.getFieldY(); i++) {
                for (int j = 0; j < game.getFieldX(); j++) {
                    canvas.drawText(Integer.toString(game.getF(i,j)), left + j * sqr / game.getFieldX(), top + (i+1) * sqr / game.getFieldY(), p);
                }
            }

            p.setFilterBitmap(true);
//            canvas.drawBitmap(cr, 0, 0, p);
            for (int i = 0; i < game.getFieldY(); i++) {
                for (int j = 0; j < game.getFieldX(); j++) {
//                    canvas.drawBitmap(cr[i * game.getFieldX() + j], x(j), top + y(i), p);
                    if (game.getF(i, j) == 0) { continue; }
                    canvas.drawBitmap(cr[game.getF(i, j)], x(j), top + y(i), p);
                }
            }

        }

        int x(int i) {
            return i * sqr / game.getFieldX();
        }

        int y(int i) {
            return i * sqr / game.getFieldY();
        }
    }
}
