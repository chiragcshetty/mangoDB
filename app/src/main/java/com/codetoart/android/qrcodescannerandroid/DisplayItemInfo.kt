package com.codetoart.android.qrcodescannerandroid

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.codetoart.android.qrcodescannerandroid.utilities
import com.codetoart.android.qrcodescannerandroid.Transaction
import com.google.gson.Gson

import com.squareup.picasso.Callback

import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_display_item_info.*
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Integer.parseInt


var txid = 0;
class DisplayItemInfo : AppCompatActivity() {
    var prid = 0;
    var prname = "";
    var desc = "";
    var img = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_item_info)

        val image: ImageView
        val image_reco1: ImageView
        image = findViewById(R.id.item_img);
        image_reco1 = findViewById(R.id.img_reco1);

        val message = intent.getStringExtra(EXTRA_MESSAGE)

        val queue = Volley.newRequestQueue(this)
        val url = "https://chiragshetty.web.illinois.edu/get_product_details.php";
        val url2 = "https://chiragshetty.web.illinois.edu/app_access/list.php?actionId=4&cuid=1"

        prid = message.toInt();

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url+"?prid="+message, null,
            Response.Listener { response ->
                if (response.getString("success").toInt()==1) {
                    item_name.text = " %s".format(response.getString("name"))
                    prname = response.getString("name");

                    textView.text = "%s".format(response.getString("desc"))
                    item_price.text = "$%s".format(response.getString("price"))

                    desc = response.getString("desc");
                    //Picasso.get().load(response.getString("image")).into(item_img)

                    //pr_price.text = "Price: $%s".format(response.getString("price"))
                    //pr_aisle.text = "Please go to aisle  %s".format(response.getString("aisle"))
                    //image.setImageResource(R.drawable.tom);

                    //Picasso.get().load(response.getString("image")).into(image);

                    progressBar.setVisibility(View.VISIBLE);
                    img = response.getString("image");
                    Picasso.get()
                        .load(response.getString("image"))
                        .into(image, object: com.squareup.picasso.Callback {
                            override fun onSuccess() {
                                progressBar.setVisibility(View.GONE)

                            }
                            override fun onError(e: java.lang.Exception?) {
                                //do smth when there is picture loading error
                            }
                        })

                    progressBar_reco1.setVisibility(View.VISIBLE);
                    Picasso.get()
                        .load(response.getString("image"))
                        .into(image_reco1, object: com.squareup.picasso.Callback {
                            override fun onSuccess() {
                                progressBar_reco1.setVisibility(View.GONE)

                            }
                            override fun onError(e: java.lang.Exception?) {
                                //do smth when there is picture loading error
                            }
                        })

                }else
                {
                    item_name.text = "Not Found"
                    //pr_price.text = " "
                    //pr_aisle.text = " "
                }
            },
            Response.ErrorListener { error ->
                textView.text = "Error"
            }
        )


        val jsonObjectRequest2 = JsonObjectRequest(
            Request.Method.GET, url2, null,
            Response.Listener { response ->
                if (response.getString("success").toInt()==1) {
                    txid = response.getString("txid").toInt()

                }else
                {
                    textView.text = "Error"
                    //pr_price.text = " "
                    //pr_aisle.text = " "
                }
            },
            Response.ErrorListener { error ->
                textView.text = "Error"
            }
        )

        queue.add(jsonObjectRequest)
        queue.add(jsonObjectRequest2)


        add_cart.setOnClickListener{
            //utilities.save("test", "abc")
            val t = Transaction()
            t.cuId = 1
            t.prId = prid
            t.quantity = parseInt(quantity.text.toString());
            t.txId = txid
            t.addedFrom = "qr"
            t.desc = desc;
            t.name = prname;
            t.imgURL = img;


            t.commit { success:Boolean ->
                Toast.makeText(MangoDB.getAppContext(), "Added to cart", Toast.LENGTH_LONG).show()
            }

        }
    }
}