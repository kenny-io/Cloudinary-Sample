package com.example.ekene.cloudinarsample;


import com.cloudinary.android.signed.Signature;
import com.cloudinary.android.signed.SignatureProvider;
import java.util.Map;

public class BackendServerSignatureProvider implements SignatureProvider {


    @Override
    public Signature provideSignature(Map options) {

        /*
           In this function you will get all the params you need to sign (options) on your server
           So you need to send it to your server, sign it.

           This function return a Signature object, so you need to fill the object with the current parameters form your server (signature, api_key and timestamp)
           Note the the timestamp is also from your server, this is the timestamp your actually used to sign.

         */


        return new Signature("the signature from the server", "your api_key", 59595959);
    }

    @Override
    public String getName() {
        return "SampleSignatureProvider";
    }
}

