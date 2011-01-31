package com.phonegap.plugins;

import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;
import com.phonegap.util.Logger;

import java.util.Enumeration;
import java.util.Hashtable;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.ui.UiApplication;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class Store extends Plugin {
    
    protected static final String ACTION_GET    = "get";
    protected static final String ACTION_ALL    = "all";
    protected static final String ACTION_PUT    = "put";
    protected static final String ACTION_REMOVE = "remove";
    protected static final String ACTION_NUKE   = "nuke";
    
    static PersistentObject store;
    static {
        //
        // The key to a PersistentStore must be of type 'long'.
        //
        // One unique identifier of a BlackBerry application is the class name.
        // However, hashCode() is not guaranteed to be unique for all Strings.
        //
        // @TODO Use a unique key for the PersistentStore.
        //       An alternative might be 'UiApplication.getUiApplication().hashCode()'
        //       but BlackBerry provides no documentation on thise implementation.
        //
        long key = UiApplication.getUiApplication().getClass().getName().hashCode();
        store    = PersistentStore.getPersistentObject(key);
    }
    
    /**
     * Executes the requested action and returns a PluginResult.
     *
     * @param action     The action to execute.
     * @param callbackId The callback ID to be invoked upon action completion.
     * @param args       JSONArry of arguments for the action.
     * @return           A PluginResult object with a status and message.
     */
    public PluginResult execute(String action, JSONArray args, String callbackId) {
        PluginResult result = null;
        
        /* Normalize the action */
        action = (action == null) ? "" : action.toLowerCase();
        
        if (action.equals(ACTION_GET)) {
            result = get(args);
        }
        else if (action.equals(ACTION_ALL)) {
            result = all(args);
        } 
        else if (action.equals(ACTION_PUT)) {
            result = put(args);
        }
        else if (action.equals(ACTION_REMOVE)) {
            result = remove(args);
        }
        else if (action.equals(ACTION_NUKE)) {
            result = nuke(args);
        }
        else {
            result = new PluginResult(PluginResult.Status.INVALIDACTION, "Store: invalid action " + action);
        }
        
        return result;
    }
    
    /**
     * Get a record.
     *
     * @param args JSONArray with [0]key.
     * @return     PluginResult that requires a String as the value.
     */
    protected PluginResult get(JSONArray args) {
        PluginResult result = null;
        
        try {
            try {
                Hashtable hash  = getHashtable();
                String    key   = args.getString(0);
                String    value = (hash.containsKey(key)) ? (String)hash.get(key) : "null";

                result = new PluginResult(PluginResult.Status.OK, value);
            }
            catch(JSONException e) {
                return new PluginResult(PluginResult.Status.JSONEXCEPTION, e.getMessage());
            }
        }
        catch (Exception e) {
            result = new PluginResult(PluginResult.Status.ERROR, e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Find all of the records.
     *
     * @param args JSONArray that should contain nothing.
     * @return     PluginResult that does not require a message.
     */
    protected PluginResult all(JSONArray args) {
        PluginResult result = null;
        
        try {
            JSONObject  json = new JSONObject();
            Hashtable   hash = getHashtable();
            Enumeration keys = hash.keys();
            
            String key   = "";
            String value = "";
            
            while (keys.hasMoreElements()) {
                key   = (String)keys.nextElement();
                value = (String)hash.get(key);
                                    
                try {
                    json.put(key, value);
                }
                catch (JSONException e) {
                    // Ignore it
                }
             }
             
             result = new PluginResult(PluginResult.Status.OK, json);
        }
        catch (Exception e) {
            result = new PluginResult(PluginResult.Status.ERROR, e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Save a record.
     *
     * @param args JSONArray with [0]key and [1]value.
     * @return     PluginResult that does not require a message.
     */
    protected PluginResult put(JSONArray args) {
        PluginResult result = null;
        
        try {
            try {
                String key   = args.getString(0);
                String value = args.getString(1);
                
                Hashtable hash = getHashtable();
                hash.put(key, value);
                setHashtable(hash);
                
                result = new PluginResult(PluginResult.Status.OK);
            }
            catch (JSONException e) {
                result = new PluginResult(PluginResult.Status.JSONEXCEPTION, e.getMessage());
            }
        }
        catch (Exception e) {
            result = new PluginResult(PluginResult.Status.ERROR, e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Delete a record.
     *
     * @param args JSONArray with [0]key.
     * @return     PluginResult that does not require a message.
     */
    protected PluginResult remove(JSONArray args) {
        PluginResult result = null;
        
        try {
            try {
                String    key  = args.getString(0);
                Hashtable hash = getHashtable();
                
                if (hash.remove(key) != null) {
                    setHashtable(hash);
                }

                result = new PluginResult(PluginResult.Status.OK);
            }
            catch(JSONException e) {
                return new PluginResult(PluginResult.Status.JSONEXCEPTION, e.getMessage());
            }            
        }
        catch (Exception e) {
            result = new PluginResult(PluginResult.Status.ERROR, e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Delete all records.
     *
     * @param args JSONArray that should contain nothing.
     * @return     PluginResult that does not require a message.
     */
    protected PluginResult nuke(JSONArray args) {
        PluginResult result = null;
        
        try {
            setHashtable(new Hashtable());
            result = new PluginResult(PluginResult.Status.OK);
        }
        catch(Exception e) {
            result = new PluginResult(PluginResult.Status.ERROR, e.getMessage());
        }
        
        return result;
    }
    
    /**
     * Helper: Get the existing hashtable or create a new one.
     *
     * @TODO Could a performance be improved by retaining the Hashtable
     *       as a static object?
     */
    private Hashtable getHashtable() {
        Object storeObj = null;
        Hashtable hash  = new Hashtable();
        
        synchronized(store) {
            storeObj = store.getContents();
        }
        
        if (storeObj != null) {
            hash = (Hashtable)storeObj;
        }
        
        return hash;
    }
    
    /**
     * Helper: Commit hashtable changes to the PersistentStore.
     *
     * @param hash is the new Hashtable to store.
     */
    private void setHashtable(Hashtable hash) {
        synchronized(store) {
            store.setContents(hash);
            store.commit();
        }
    }
}