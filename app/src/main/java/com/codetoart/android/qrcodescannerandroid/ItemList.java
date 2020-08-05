package com.codetoart.android.qrcodescannerandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.codetoart.android.qrcodescannerandroid.Transaction;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

public class ItemList extends AppCompatActivity {

    ListView listView;
    String mTitle[] = {"Item1", "Item2", "Item3", "Item4","Item5"};
    String mDescription[] = {"Item1 Des", "Item2 Des", "Item3 Des", "Item4 Des", "Item5 Des"};
    int images[] = {R.drawable.groc, R.drawable.groc, R.drawable.groc, R.drawable.groc, R.drawable.groc};

    List<String> titles = new ArrayList<String>();
    List<String> desc = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        listView = findViewById(R.id.listView);


        for (Transaction t: Transaction.getTransactions()){
            titles.add(t.name);
            desc.add("Quantity:" + t.quantity);
        }

        int img[] = new int[Transaction.getTransactions().size()];

        for(int i = 0; i < img.length; i++) {
            img[i] = R.drawable.groc;
        }

        MyAdapter adapter = new MyAdapter(this, titles.toArray(new String[0]), desc.toArray(new String[0]), img);
        listView.setAdapter(adapter);

/*
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });
 */
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String rTitle[];
        String rDescription[];
        int rImgs[];

        MyAdapter(Context c, String title[], String description[], int imgs[]){
            super(c, R.layout.row, R.id.name, title);
            this.context = c;
            this.rTitle = title;
            this.rDescription = description;
            this.rImgs = imgs;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater)getApplicationContext().getSystemService((Context.LAYOUT_INFLATER_SERVICE));
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            ImageView images = row.findViewById(R.id.image);
            TextView myTitle = row.findViewById(R.id.name);
            TextView myDescription = row.findViewById(R.id.desc);

            images.setImageResource(rImgs[position]);
            myTitle.setText(rTitle[position]);
            myDescription.setText(rDescription[position]);

            Button deleteButton = (Button)row.findViewById(R.id.Delete);
            final int i = position;
            final Transaction t = Transaction.getTransactions().get(i);
            Picasso.get()
                    .load(t.imgURL)
                    .into(images);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    RequestQueue queue = Volley.newRequestQueue(MangoDB.getAppContext());

                    String url ="https://chiragshetty.web.illinois.edu/app_access/list.php?actionId=6&cuid=" + utilities.cuid  + "&txid=" + t.txId + "&prid=" + t.prId;


                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                            (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    try{
                                        if(response.getInt("success") != 1)
                                        {
                                            return;
                                        }
                                    }
                                    catch(Exception e){
                                        return;
                                    }
                                    Transaction.deleteTransaction(i);
                                    Toast.makeText(MangoDB.getAppContext(), "Item Deleted", Toast.LENGTH_LONG).show();
                                    finish();
                                    startActivity(getIntent());
                                }
                            }, new Response.ErrorListener() {

                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // TODO: Handle error
                                }
                            });

                    // Add the request to the RequestQueue.
                    queue.add(jsonObjectRequest);
                }
            });


            return row;
        }
    }
}