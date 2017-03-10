package de.sitewaerts.cordova.documentviewer;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.MenuItem;

import android.app.Activity;
import android.app.ActionBar;
import android.os.Bundle;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.shockwave.pdfium.PdfDocument;
import java.io.File;


public class PDFViewerActivity extends Activity implements OnPageChangeListener, OnLoadCompleteListener {
    private static final String TAG = PDFViewerActivity.class.getSimpleName();

    PDFView pdfView;
    Uri uri;
    Integer pageNumber = 0;
    String pdfFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getResources().getIdentifier("pdfviewer_activity", "layout", getPackageName()));

        pdfView = (PDFView) findViewById(getResources().getIdentifier("pdfView", "id", getPackageName()));

        Intent intent = getIntent();
        String filename = intent.getStringExtra(DocumentViewerPlugin.SET_FILE);

        Bundle options = intent.getBundleExtra(DocumentViewerPlugin.class.getName());
        setTitle(options.getString(DocumentViewerPlugin.TITLE_OPTIONS));


        pdfFileName = filename;

        ActionBar ab = getActionBar();
        if (ab != null) {
            Log.v(TAG, "There is an actionbar");
            ab.setDisplayHomeAsUpEnabled(true);
        }

        uri = Uri.parse(filename);

        if (uri.toString().startsWith("file:///android_asset/")) {
            String path = uri.toString().replace("file:///android_asset/", "");

            pdfView.fromAsset(path)
                .defaultPage(0)
                .swipeHorizontal(true)
                .onPageChange(this)
                .enableAnnotationRendering(false)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
        }
        else {
            pdfView.fromUri(uri)
                .defaultPage(0)
                .swipeHorizontal(true)
                .onPageChange(this)
                .enableAnnotationRendering(false)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(this))
                .load();
        }
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        // setTitle(String.format("%s / %s", page + 1, pageCount));
    }

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        // Respond to the action bar's Up/Home button
        case android.R.id.home:
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
