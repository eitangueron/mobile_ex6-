package com.example.ex6;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button getPriceBtn;
    EditText companyNameInput;
    static String companyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        companyNameInput = (EditText) findViewById(R.id.companyNameInput);
        getPriceBtn = findViewById(R.id.getPriceBtn);

        getPriceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                companyName = (String) companyNameInput.getText().toString();
                Toast toast = Toast.makeText(view.getContext(), "Fetching stock price " + companyName, Toast.LENGTH_LONG);
                toast.show();
                fetchStockPrice(view);
            }
        });
    }

    public void fetchStockPrice(final View view) {
        final StockPriceFetcher fetcher = new StockPriceFetcher(view.getContext());

        fetcher.dispatchRequest(new StockPriceFetcher.StockPriceResponseListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(StockPriceFetcher.PriceResponse response) {

                if (response.isError) {
                    Toast.makeText(view.getContext(), "Error while fetching stock data", Toast.LENGTH_LONG);
                    return;
                }
                if(response.found){
                    ((TextView)MainActivity.this.findViewById(R.id.priceText)).setText(response.symbol + ": $" + response.price);
                } else {
                    ((TextView)MainActivity.this.findViewById(R.id.priceText)).setText("Unfound");
                }

            }
        });
    }


}