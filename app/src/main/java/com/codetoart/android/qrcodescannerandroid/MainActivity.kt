package com.codetoart.android.qrcodescannerandroid

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_display_item_info.*
import kotlinx.android.synthetic.main.activity_main.*

const val EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE"


class MainActivity : AppCompatActivity() {

    var txid = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("mangoDB")
        //setContentView(R.layout.test)

        //Picasso.get().load("https://i.imgur.com/DvpvklR.png").into(imageView);
        //imageView.setImageResource(R.drawable.tom);

        button_scan_qr_code.setOnClickListener {
            IntentIntegrator(this).initiateScan()
        }

        but_qrapi.setOnClickListener({
            dispatchTakePictureIntent()

        })

        wishlistButton.setOnClickListener({
            val myIntent = Intent(this@MainActivity,ItemList::class.java)
            this@MainActivity.startActivity(myIntent)

        })

        SearchButton.setOnClickListener {
            val intent = Intent(this, DisplayItemInfo::class.java).apply {
                putExtra(EXTRA_MESSAGE, SearchBar.text.toString())
                //Log.e("411", SearchBar.text.toString())
            }
            startActivity(intent)
        }


        val queue = Volley.newRequestQueue(MangoDB.getAppContext())
        val url2 = "https://chiragshetty.web.illinois.edu/app_access/list.php?actionId=4&cuid=1"
        val jsonObjectRequest2 = JsonObjectRequest(
            Request.Method.GET, url2, null,
            Response.Listener { response ->
                if (response.getString("success").toInt()==1) {
                    txid = response.getString("txid").toInt()

                }
            },
            Response.ErrorListener { error ->
            }
        )

        queue.add(jsonObjectRequest2)

        recommended.setOnClickListener {
            // Instantiate the RequestQueue.
            val url = "https://chiragshetty.web.illinois.edu/app_access/list.php?actionId=7&cuid=1"
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                Response.Listener { response ->
                    var suggestions = response.getJSONArray("suggestions")
                    for (i in 0 until suggestions.length()) {
                        var cur = suggestions.getJSONObject(i)
                        try {
                            val t = Transaction()
                            t.cuId = 1
                            t.prId = cur.getString("prID").toInt()
                            t.quantity = 1;
                            t.txId = txid;
                            t.addedFrom = "qr"
                            t.desc = "";
                            t.name = cur.getString("prName");
                            t.imgURL = cur.getString("prImage");


                            t.commit { success:Boolean ->
                            }
                        } catch (e: Exception) {}
                    }
                    Toast.makeText(this@MainActivity, "Added Recommendations", Toast.LENGTH_LONG).show()
                },
                Response.ErrorListener { // TODO: Handle error
                })

            // Add the request to the RequestQueue.

            // Add the request to the RequestQueue.
            queue.add(jsonObjectRequest)

        }




        buybutton.setOnClickListener {
            // Instantiate the RequestQueue.
            val queue = Volley.newRequestQueue(MangoDB.getAppContext())
            val url = "https://chiragshetty.web.illinois.edu/app_access/list.php?actionId=4&cuid=1"
            val jsonObjectRequest = JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                Response.Listener { response ->
                    try {
                        if (response.getInt("success") != 1) {
                            return@Listener
                        }
                    } catch (e: Exception) {
                        return@Listener
                    }

                    val url =
                        "https://chiragshetty.web.illinois.edu/app_access/list.php?actionId=3&cuid=1&txid=" + response.getString("txid");

                    val jsonObjectRequest = JsonObjectRequest(
                        Request.Method.GET,
                        url,
                        null,
                        Response.Listener { response ->
                            try {
                                if (response.getInt("success") != 1) {
                                    return@Listener
                                }
                            } catch (e: Exception) {
                                return@Listener
                            }
                            Toast.makeText(this@MainActivity, "Bought Items", Toast.LENGTH_LONG).show()
                            Transaction.clearTransactions()
                        },
                        Response.ErrorListener { // TODO: Handle error
                        })

                    // Add the request to the RequestQueue.

                    // Add the request to the RequestQueue.
                    queue.add(jsonObjectRequest)

                },
                Response.ErrorListener { // TODO: Handle error
                })

            // Add the request to the RequestQueue.

            // Add the request to the RequestQueue.
            queue.add(jsonObjectRequest)

        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                //textView.setText(result.contents);

                //-------------------------------------------------------------------------------
                 //val jsonParams: MutableMap<String, Int> = HashMap()
                 //jsonParams["prid"] = result.contents.toInt()
                 //val jsonObject = JSONObject(jsonParams)

                val intent = Intent(this, DisplayItemInfo::class.java).apply {
                    putExtra(EXTRA_MESSAGE, result.contents)
                }
                startActivity(intent)


/*
                val queue = Volley.newRequestQueue(this)
                val url = "https://chiragshetty.web.illinois.edu/get_product_details.php";

                val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url+"?prid="+result.contents, null,
                    Response.Listener { response ->
                        if (response.getString("success").toInt()==1) {
                            pr_name.text = " %s".format(response.getString("name"))
                            pr_price.text = "Price: $%s".format(response.getString("price"))
                            pr_aisle.text =
                                "Please go to aisle  %s".format(response.getString("aisle"))
                        }else
                        {
                            pr_name.text = "Not Found"
                            pr_price.text = " "
                            pr_aisle.text = " "
                        }
                    },
                    Response.ErrorListener { error ->
                        textView.text = "Error"
                    }
                )
                queue.add(jsonObjectRequest)

*/

 /*               //------------------------JSON Get Request Volley------------------------------------------
                val queue = Volley.newRequestQueue(this)
                val url = "https://chiragshetty.web.illinois.edu/get_product_details.php";

                val jsonObjectRequest = JsonObjectRequest(Request.Method.GET, url, null,
                    Response.Listener { response ->
                        textView.text = "Response: %s".format(response.getString("message"))
                    },
                    Response.ErrorListener { error ->
                        textView.text = "Error"
                    }
                )
                queue.add(jsonObjectRequest)
 */
                //---------------------------Simple Request Volley-----------------------------------
/*
                // Request a string response from the provided URL.
                val queue = Volley.newRequestQueue(this)
                val url = "https://chiragshetty.web.illinois.edu/get_product_details.php";
                val stringRequest = StringRequest(
                    Request.Method.GET, url,
                    Response.Listener<String> { response ->
                        // Display the first 500 characters of the response string.
                        textView.text = "Response is: ${response.substring(0,10)}"
                        //textView.text = "That work!"
                    },
                    Response.ErrorListener { textView.text = "That didn't work!" })

                // Add the request to the RequestQueue.
                queue.add(stringRequest)
*/
                //---------------------------------------------------------------------------------
            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            //imageView.setImageBitmap(imageBitmap)
        }
    }


    val REQUEST_IMAGE_CAPTURE = 1

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }


}
