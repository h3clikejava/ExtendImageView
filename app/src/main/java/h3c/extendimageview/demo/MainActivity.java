package h3c.extendimageview.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView iv1 = (ImageView) findViewById(R.id.p1);
        iv1.setOnClickListener(this);

        ImageView iv2 = (ImageView) findViewById(R.id.p2);
        iv2.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        PhotoActivity.clickView = view;
        Intent intent = new Intent(this, PhotoActivity.class);
        if(view.getId() == R.id.p1) {
            intent.putExtra("photoRes", R.mipmap.bmw1);
        } else {
            intent.putExtra("photoRes", R.mipmap.bmw2);
        }
        startActivity(intent);
    }
}
