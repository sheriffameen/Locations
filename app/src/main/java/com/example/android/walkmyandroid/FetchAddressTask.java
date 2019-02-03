package com.example.android.walkmyandroid;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

//CREATE AN ASYNCTASK SUBCLASS
//Recall that an AsyncTask object is used to perform work off the main thread. An AsyncTask object
// contains one required override method, doInBackground() which is where the work is performed.
// For this use case, you need another override method, onPostExecute(), which is called on the main
// thread after doInBackground() finishes. In this step, you set up the boilerplate code for your AsyncTask
//11.Create a new class called FetchAddressTask that is a subclass of AsyncTask. Parameterize the
// AsyncTask using the three types described above
public class FetchAddressTask extends AsyncTask <Location, Void, String>{
    private final String TAG = FetchAddressTask.class.getSimpleName();
    private Context mContext;
    private OnTaskCompleted mListener;

    //13.Create a constructor for the AsyncTask that takes a Context as a parameter and assigns it to a member variable
    FetchAddressTask(Context applicationContext) {
        mContext = applicationContext;
    }
    //23.Add a parameter for the OnTaskCompleted interface to the FetchAddressTask constructor, and assign it to a member variable
    FetchAddressTask(Context applicationContext, OnTaskCompleted listener) {
        mContext = applicationContext;
        mListener = listener;
    }

    @Override
    protected String doInBackground(Location... locations) {
        //CONVERT THE LOCATION INTO AN ADDRESS

        //14.Create a Geocoder object. This class handles both geocoding (converting from an address into coordinates) and reverse geocoding:
        Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

        //15.Obtain a Location object. The passed-in parameter is a Java varargs argument that can
        // contain any number of objects. In this case we only pass in one Location object, so the
        // desired object is the first item in the varargs array
        Location location = locations[0];
        //16.Create an empty List of Address objects, which will be filled with the address obtained
        //from the Geocoder. Create an empty String to hold the final result, which will be either the address or an error
        List<Address> addresses = null;
        String resultMessage ="";

        //17.You are now ready to start the geocoding process. Open up a try block and use the
        // following code to attempt to obtain a list of addresses from the Location object.
        // The third parameter specifies the maximum number of addresses that you want to read.
        // In this case you only want a single address
        try {
            addresses = geocoder.getFromLocation(
                    location.getLatitude(),
                    location.getLongitude(),
                    1);
            //20.Check the address list and the resultMessage string. If the address
            // list is empty or null and the resultMessage string is empty, then set the resultMessage
            // to "No address found" and log the error
            //21.Create a new string resource with two replacement variables
            if (addresses == null || addresses.size() == 0){
                if (resultMessage.isEmpty()){
                    resultMessage = mContext.getString(R.string.no_address_found);
                    Log.e(TAG,resultMessage);
                }else {
                    //If an address is found, read it into resultMessage
                    Address address = addresses.get(0);
                    ArrayList<String> addressParts = new ArrayList<>();
                    for (int i = 0; i <= address.getMaxAddressLineIndex() ; i++) {
                        addressParts.add(address.getAddressLine(i));
                    }
                    resultMessage = TextUtils.join("\n", addressParts);
                }
            }


            //18.Open a catch block to catch IOException exceptions that are thrown if there is a
            // network error or a problem with the Geocoder service. In this catch block, set the
            // resultMessage to an error message that says "Service not available." Log the error and result message
        }catch (IOException ioException){
            //Catch network or other I/O problems
            resultMessage = mContext.getString(R.string.service_not_available);
            Log.e(TAG, resultMessage, ioException);

        }
        //19.Open another catch block to catch IllegalArgumentException exceptions. Set the
        // resultMessage to a string that says "Invalid coordinates were supplied to the Geocoder,"
        // and log the error and result message:
        catch (IllegalArgumentException illegalArgumentException){
            //Catch invalid latitude or longitude values
            resultMessage = mContext.getString(R.string.invalid_lat_long_used);
            Log.e(TAG,resultMessage + ". " + "Latitude = " + location.getLatitude() +
                    ", Longitude = " + location.getLongitude(), illegalArgumentException);
        }

        return resultMessage;
    }
//12.Override the onPostExecute() method. Again notice that the passed-in parameter is automatically typed as a String,
// because this what you put in the FetchAddressTask class declaration.
    @Override
    protected void onPostExecute(String address) {
        //23.In the onPostExecute() method, call onTaskCompleted() on the mListener interface,
        // passing in the result string(Next Task In Main Activity)
        mListener.onTaskCompleted(address);

        super.onPostExecute(address);


    }
    //22.Create an interface in FetchAddressTask called OnTaskCompleted that has one method, called
    // onTaskCompleted(). This method should take a string as an argument
    interface OnTaskCompleted {
        void onTaskCompleted(String result);
    }
}
