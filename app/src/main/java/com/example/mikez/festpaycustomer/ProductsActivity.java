package com.example.mikez.festpaycustomer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.mikez.festpaycustomer.adapters.HistoryAdapter;
import com.example.mikez.festpaycustomer.adapters.ProductsAdapter;

import java.util.ArrayList;
import java.util.List;

public class ProductsActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        imageBack = (ImageView) findViewById(R.id.products_button_back);
        imageBack.setOnClickListener(this);

        List<InfoProducts> data2 = new ArrayList<>();
        data2.add(new InfoProducts(1, "Coca-cola", "coca", "2 baneti"));
        data2.add(new InfoProducts(2, "Coca-cola", "coddca", "3 baneti"));
        data2.add(new InfoProducts(3, "ddd", "sdd", "sdsd"));
        data2.add(new InfoProducts(4, "ddd", "sdsd", "sdssd"));
        data2.add(new InfoProducts(5, "ddd", "sdd", "sdsd"));

        ProductsAdapter adapter = new ProductsAdapter(this, data2);
        RecyclerView recycleList = (RecyclerView) findViewById(R.id.products_recycler_view);
        recycleList.setLayoutManager(new LinearLayoutManager(this));
        recycleList.setAdapter(adapter);

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.products_button_back:
                finish();
                break;
        }
    }
}
