package com.codetoart.android.qrcodescannerandroid

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.squareup.picasso.Callback

import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_display_item_info.*

class DisplayItemInfo : AppCompatActivity() {
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

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url+"?prid="+message, null,
            Response.Listener { response ->
                if (response.getString("success").toInt()==1) {
                    item_name.text = " %s".format(response.getString("name"))

                    textView.text = "%s".format(response.getString("desc"))
                    item_price.text = "$%s".format(response.getString("price"))
                    //Picasso.get().load(response.getString("image")).into(item_img)

                    //pr_price.text = "Price: $%s".format(response.getString("price"))
                    //pr_aisle.text = "Please go to aisle  %s".format(response.getString("aisle"))
                    //image.setImageResource(R.drawable.tom);

                    //Picasso.get().load(response.getString("image")).into(image);

                    progressBar.setVisibility(View.VISIBLE);
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
        queue.add(jsonObjectRequest)

    }
}