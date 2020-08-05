package com.codetoart.android.qrcodescannerandroid;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

interface Callback {
    void call(boolean success);
}

public class Transaction {
    public int prId;
    public int txId;
    public int cuId = utilities.cuid;
    public int quantity;
    public String desc;
    public String addedFrom;
    public String name;
    public String imgURL;

    public static void clearTransactions() {
        utilities.save("transactions", (new Gson()).toJson(new ArrayList<Transaction>()));
    }

    public static List<Transaction> getTransactions()
    {
        Gson gson = new Gson();
        List<Transaction> transactions = new ArrayList<Transaction>();
        if(utilities.contains("transactions")) {
            transactions = gson.fromJson(utilities.load("transactions"), new TypeToken<List<Transaction>>() {
            }.getType());
        }
        return transactions;
    }

    public static void deleteTransaction(int position)
    {
        try {
            List<Transaction> transactions = getTransactions();

            transactions.remove(position);
            utilities.save("transactions", (new Gson()).toJson(transactions));
        }
        catch(Exception e)
        {

        }
    }

    public void commit(final Callback cb){
        cuId = utilities.cuid;
        // Instantiate the RequestQueue.
        final Transaction curr = this;
        RequestQueue queue = Volley.newRequestQueue(MangoDB.getAppContext());
        String url ="https://chiragshetty.web.illinois.edu/app_access/list.php?actionId=2&prid=" + prId + "&txid=" + txId +
                "&cuid=" + cuId + "&quantity=" + quantity + "&addedFrom=" + addedFrom;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            if(response.getInt("success") != 1)
                            {
                                cb.call(false);
                                return;
                            }
                        }
                        catch(Exception e){
                            cb.call(false);
                            return;
                        }

                        Gson gson = new Gson();
                        List<Transaction> transactions = getTransactions();
                        transactions.add(curr);
                        utilities.save("transactions", gson.toJson(transactions));
                        cb.call(true);
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                        cb.call(false);
                    }
                });

        // Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }
}