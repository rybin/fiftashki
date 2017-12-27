package com.example.f15.fiftashki;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "DEBUG";
//    private static final int IMAGEREQUESTCODE = 8242008;
    private static final int IMAGEREQUESTCODE = 123;
    public GameLogic game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        game = new GameLogic(4,4);
        final DrawView drawView = new DrawView(this);
        drawView.setOnTouchListener(new OnSwipeTouchListener(MainActivity.this) {
            public void onSwipeRight() {
//                Log.d(DEBUG_TAG, "RIGHT");
                game.move(game.RIGHT);
                drawView.invalidate();
            }

            public void onSwipeLeft() {
//                Log.d(DEBUG_TAG, "LEFT");
                game.move(game.LEFT);
                drawView.invalidate();
            }

            public void onSwipeTop() {
//                Log.d(DEBUG_TAG, "UP");
                game.move(game.UP);
                drawView.invalidate();
            }

            public void onSwipeBottom() {
//                Log.d(DEBUG_TAG, "DOWN");
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
                    if (i == game.getFieldY()-1 && j == game.getFieldX()-1) {
                        cr[0] = Bitmap.createBitmap(bm, sqr * j / game.getFieldX(), sqr * i / game.getFieldY(), sqr / game.getFieldX(), sqr / game.getFieldY());
                        break;
                    }
                    cr[i*game.getFieldX()+j+1] = Bitmap.createBitmap(bm, sqr * j / game.getFieldX(), sqr * i / game.getFieldY(), sqr / game.getFieldX(), sqr / game.getFieldY());
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

    private void pickImageFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, IMAGEREQUESTCODE);
    }

    @Override
    protected final void onActivityResult(final int requestCode, final int resultCode, final Intent i) {
        super.onActivityResult(requestCode, resultCode, i);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case IMAGEREQUESTCODE:
//                    manageImageFromUri(i.getData());
                    Toast.makeText(getBaseContext(), "Image_get", Toast.LENGTH_SHORT).show();
                    break;
            }
        } else {
            // manage result not ok !
        }

    }

    private void manageImageFromUri(Uri imageUri) {
        Bitmap bitmap = null;

        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
        } catch (Exception e) {
            // Manage exception ...
        }

        if (bitmap != null) {
            // Here you can use bitmap in your application ...
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings:
                Toast.makeText(getBaseContext(), "Action_Settings", Toast.LENGTH_SHORT).show();
                break;
            case R.id.getimage:
                pickImageFromGallery();
                break;
            case R.id.chmod:
                item.setChecked(!item.isChecked());
                game.changeMode();
                break;
            default: break;
        }

        return super.onOptionsItemSelected(item);
    }
}
