package com.example.josh.memories;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static com.example.josh.memories.MemoryList.RESULT_ADD;

public class AddMemory extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    static final int CAM_REQUEST = 4;

    Button sqlAdd, picAdd;
    ImageView imageView, newImageView;
    private EditText sqlTitle, sqlDescription;
    private TextView sqlDate;
    private Intent intent;
    private final static String DEBUG_TAG = "AddMemory:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_memory);
        sqlAdd = findViewById(R.id.add_memory_button);
        picAdd = findViewById(R.id.add_picture);
        imageView = findViewById(R.id.image_view);


        intent = getIntent();

        sqlAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                sqlTitle = findViewById(R.id.add_title);
                sqlDate = findViewById(R.id.show_date);
                newImageView = findViewById(R.id.image_view);
                sqlDescription = findViewById(R.id.add_description);
                byte[] byteArray = imageViewToByte(newImageView);
                intent.putExtra("add_title", String.valueOf(sqlTitle.getText()));
                intent.putExtra("add_date", String.valueOf(sqlDate.getText()));
                intent.putExtra("add_description", String.valueOf(sqlDescription.getText()));
                intent.putExtra("add_picture", byteArray);
                Log.d(DEBUG_TAG, "value of title:" + String.valueOf(sqlTitle.getText()));
                Log.d(DEBUG_TAG, "value of date:" + String.valueOf(sqlDate.getText()));
                Log.d(DEBUG_TAG, "value of description:" + String.valueOf(sqlDescription.getText()));
                setResult(RESULT_ADD, intent);
                supportFinishAfterTransition();
            }
        });


        picAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = getFile();
                camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(camera_intent, CAM_REQUEST);
            }


        });
    }

    private File getFile(){
        File folder = new File("sdcard/camera_app");

        if(!folder.exists()){
            folder.mkdir();
        }
        File image_file = new File(folder, "cam_image.jpg");
        return image_file;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        String path = "sdcard/camera_app/cam_image.jpg";
        imageView.setImageDrawable(Drawable.createFromPath(path));
    }

    private byte[] imageViewToByte(ImageView image){
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public void datePicker (View view) {

        DatePickerFragment fragment = new DatePickerFragment();
        fragment.show(getSupportFragmentManager(), "date");
    }

    private void setDate(final Calendar calender) {
        final DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.MEDIUM);
        ((TextView) findViewById(R.id.show_date)).setText(dateFormat.format(calender.getTime()));
    }

    public void onDateSet(DatePicker view, int year, int month, int day){
        Calendar cal = new GregorianCalendar(year, month, day);
        setDate(cal);
    }

    public static class DatePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(),
                    (DatePickerDialog.OnDateSetListener) getActivity(), year, month, day);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
