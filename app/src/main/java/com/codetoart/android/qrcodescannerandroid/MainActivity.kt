package com.codetoart.android.qrcodescannerandroid

import android.R.attr.password
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
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_scan_qr_code.setOnClickListener {
            IntentIntegrator(this).initiateScan()
        }

        but_qrapi.setOnClickListener({
            dispatchTakePictureIntent()
        })
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
            imageView.setImageBitmap(imageBitmap)
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
