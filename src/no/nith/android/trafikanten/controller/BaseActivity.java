package no.nith.android.trafikanten.controller;

import no.nith.android.trafikanten.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
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
     * Sets the layout for the activity, based on the layout ID.
     * @param layoutId The layout ID.
     */
    public void setLayout(int layoutId) {
        setContentView(layoutId);
    }
    
    /**
     * Sets the layout for the activity, based on the layout name.
     * @param layoutName The layout name.
     */
    public void setLayout(String layoutName) {
    	try {
    		int layoutId = getResourceReference(R.layout.class, layoutName);
			setLayout(layoutId);
		} catch (Exception e) {
			Log.e("REFLECTION", e.getStackTrace().toString());
		}
    }
    
    /**
     * Finds a view component in the layout, based on the view ID.
     * @param viewId The view ID.
     * @return The view component.
     */
    public View find(int viewId) {
    	return findViewById(viewId);
    }
    
    /**
     * Finds a view component in the layout, based on the view name.
     * @param viewName The view name.
     * @return The view component.
     */
    public View find(String viewName) {
    	return find(getResourceReference(R.id.class, viewName));
    }
    
    /**
     * Finds a button in the layout, based on the button ID.
     * @param buttonId The button ID.
     * @return The button component.
     */
    public Button findButton(int buttonId) {
    	return (Button) find(buttonId);
    }
    
    /**
     * Finds a button in the layout, based on the button name.
     * @param buttonName The button name.
     * @return The button component.
     */
    public Button findButton(String buttonName) {
    	return (Button) find(buttonName);
    }
    
    /**
     * Finds an EditText component in the layout, based on its ID.
     * @param editTextId The EditText component ID.
     * @return The EditText component.
     */
    public EditText findEditText(int editTextId) {
    	return (EditText) find(editTextId);
    }
    
    /**
     * Finds an EditText component in the layout, based on its name.
     * @param editTextName The name of the EditText component.
     * @return The EditText component.
     */
    public EditText findEditText(String editTextName) {
    	return (EditText) find(editTextName);
    }
    
    /**
     * Finds a TextView component in the layout, based on its ID.
     * @param textViewId The TextView component ID.
     * @return The TextView component.
     */
    public TextView findTextView(int textViewId) {
    	return (TextView) find(textViewId);
    }
    
    /**
     * Finds a TextView component in the layout, based on its name.
     * @param textViewName The name of the TextView component.
     * @return The TextView component.
     */
    public TextView findTextView(String textViewName) {
    	return (TextView) find(textViewName);
    }

    
    /**
     * Finds a ListView component in the layout, based on its ID.
     * @param listViewId The ListView component ID.
     * @return The ListView component.
     */
    public ListView findListView(int listViewId) {
    	return (ListView) find(listViewId);
    }
    
    /**
     * Finds a ListView component in the layout, based on its name.
     * @param listViewName The name of the ListView component.
     * @return The ListView component.
     */
    public ListView findListView(String listViewName) {
    	return (ListView) find(listViewName);
    }
    
    /**
     * Finds the resource ID as specified in the R class based on the provided
     * static class (i.e. R.layout.class) and the resource name (i.e. "main").
     * @param c The static R class attribute.
     * @param refName The resource name.
     * @return The resource ID.
     */
    private int getResourceReference(Class<?> c, String refName) {
    	try {
			return c.getField(refName).getInt(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return 0;
    }
}
