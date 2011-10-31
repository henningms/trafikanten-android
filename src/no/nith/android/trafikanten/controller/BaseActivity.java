package no.nith.android.trafikanten.controller;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public abstract class BaseActivity extends Activity {

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        init();
        controller();
        view();
    }
    
    public abstract void init();
    
    public abstract void controller();
    
    public abstract void view();
    
    /**
     * Sets the layout for the activity.
     * @param layoutId
     */
    public void setLayout(int layoutId) {
        setContentView(layoutId);
    }
    
    public View find(int viewId) {
    	return findViewById(viewId);
    }
    
    public Button findButton(int viewId) {
    	return (Button) find(viewId);
    }
    
    public EditText findText(int viewId) {
    	return (EditText) find(viewId);
    }
    
    public TextView findLabl(int viewId) {
    	return (TextView) find(viewId);
    }
    
    public ListView findList(int viewId) {
    	return (ListView) find(viewId);
    }
}
