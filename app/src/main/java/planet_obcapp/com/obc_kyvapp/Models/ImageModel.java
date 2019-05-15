package planet_obcapp.com.obc_kyvapp.Models;

import android.net.Uri;

public class ImageModel {
    public Uri getmUri() {
        return mUri;
    }

    public void setmUri(Uri mUri) {
        this.mUri = mUri;
    }

    public int getmOrientation() {
        return mOrientation;
    }

    public void setmOrientation(int mOrientation) {
        this.mOrientation = mOrientation;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Uri mUri;
    public int mOrientation;
    public String id;
    public ImageModel(Uri mUri, int mOrientation, String id) {
        this.mUri = mUri;
        this.mOrientation = mOrientation;
        this.id = id;
    }




}
