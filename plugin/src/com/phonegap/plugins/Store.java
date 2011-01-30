package com.phonegap.plugins;

import com.phonegap.api.Plugin;
import com.phonegap.api.PluginResult;
import com.phonegap.util.Logger;

import java.util.Enumeration;
import java.util.Hashtable;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

public class Store extends Plugin {
    
    protected static final String ACTION_ALL    = "all";
    protected static final String ACTION_SAVE   = "save";
    protected static final String ACTION_LOAD   = "load";
    protected static final String ACTION_REMOVE = "remove";
    protected static final String ACTION_NUKE   = "nuke";
    
    private static long KEY = 0x4a9ab8d0f0147f4cL;
    
    static PersistentObject store;
    static {
        store = PersistentStore.getPersistentObject(KEY);
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

        /*
         * Determine action to execute.
         */
        if (action.equals(ACTION_ALL)) {
            result = all(args);
        } 
        else if (action.equals(ACTION_SAVE)) {
            result = save(args);
        }
        else if (action.equals(ACTION_LOAD)) {
            result = load(args);
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
    
    protected PluginResult all(JSONArray args) {
        JSONObject json = new JSONObject();
        Object storeObj = null;
        Hashtable hash  = new Hashtable();
        
        try {
            synchronized(store) {
                storeObj = store.getContents();
            }
            
            if (storeObj != null) {
                hash = (Hashtable)storeObj;
            }
            
            Enumeration e = hash.keys();
            String key = "";
            String value = "";
            
            while (e.hasMoreElements()) {
                key   = (String)e.nextElement();
                value = (String)hash.get(key);
                //value = escapeString(value);
                                    
                try {
                    json.put(key, value);
                }
                catch (JSONException je) {
                    // Ignore it
                }
             }
        }
        catch (Exception e) {
            // Do something
        }
        
        return new PluginResult(PluginResult.Status.OK, json);
    }
    
    protected PluginResult save(JSONArray args) {
        Object storeObj = null;
        Hashtable hash  = new Hashtable();
        
        try {
            synchronized(store) {
                storeObj = store.getContents();
            }
            
            if (storeObj != null) {
                hash = (Hashtable)storeObj;
            }
            
            String key = "";
            String value = "";
            
            try {
                key   = args.getString(0);
                value = args.getString(1);
                Logger.log("Key: " + key + "\nValue: " + value);
            }
            catch (JSONException e) {
                // Do somethign
                Logger.log("Exception in json");
            }
            
            hash.put(key, value);
            Logger.log("Hash Put");
            
            Logger.log("setContents");
            
            synchronized(store) {
                store.setContents(hash);
                store.commit();
            }
        }
        catch (Exception e) {
            Logger.log("Exception in save: " + e.getMessage());
            // Do something
        }
        
        return new PluginResult(PluginResult.Status.OK);
    }
    
    protected PluginResult load(JSONArray args) {
        PluginResult result = null;
        Object storeObj = null;
        Hashtable hash = new Hashtable();
        String key = null;
        String value = null;
        
        try {
            key = args.getString(0);
        }
        catch(JSONException e) {
            return new PluginResult(PluginResult.Status.JSONEXCEPTION, e.getMessage());
        }
        
        try {
            synchronized(store) {
                storeObj = store.getContents();
            }
            
            if (storeObj != null) {
                hash = (Hashtable)storeObj;
            }
            
            if (hash.containsKey(key)) {
                value = (String)hash.get(key);
            }
            else {
                value = "null";
            }
            
            result = new PluginResult(PluginResult.Status.OK, value);
        }
        catch (Exception e) {
            Logger.log("Exception in save: " + e.getMessage());
            result = new PluginResult(PluginResult.Status.ERROR, e.getMessage());
        }
        
        return result;
    }
    
    protected PluginResult remove(JSONArray args) {
        PluginResult result = null;
        Object storeObj = null;
        Hashtable hash = new Hashtable();
        String key = null;
        
        try {
            key = args.getString(0);
        }
        catch(JSONException e) {
            return new PluginResult(PluginResult.Status.JSONEXCEPTION, e.getMessage());
        }
        
        try {
            synchronized(store) {
                storeObj = store.getContents();
            }
            
            if (storeObj != null) {
                hash = (Hashtable)storeObj;
            }
            
            if (hash.remove(key) != null) {
                synchronized(store) {
                    store.setContents(hash);
                    store.commit();
                }
            }
            
            result = new PluginResult(PluginResult.Status.OK);
        }
        catch (Exception e) {
            Logger.log("Exception in save: " + e.getMessage());
            result = new PluginResult(PluginResult.Status.ERROR, e.getMessage());
        }
        
        return result;
    }
    
    protected PluginResult nuke(JSONArray args) {
        PluginResult result = null;
        Hashtable hash = new Hashtable();
        
        try {
            synchronized(store) {
                store.setContents(hash);
                store.commit();
            }
            
            result = new PluginResult(PluginResult.Status.OK);
        }
        catch(Exception e) {
            result = new PluginResult(PluginResult.Status.ERROR, e.getMessage());
        }
        
        return result;
    }
    
    // private static final int SAVE_COMMAND = 0;
    // private static final int LOADALL_COMMAND = 1;
    // private static final int LOAD_COMMAND = 2;
    // private static final int REMOVE_COMMAND = 3;
    // private static final int NUKE_COMMAND = 4;
    // private static final String CODE = "PhoneGap=store";
    // private static final String STORE_SAVE_SUCCESS = ";if (navigator.store.save_success != null) { navigator.store.save_success(); };";
    // private static final String STORE_REMOVE_SUCCESS = ";if (navigator.store.remove_success != null) { navigator.store.remove_success(); };";
    // private static final String STORE_NUKE_SUCCESS = ";if (navigator.store.nuke_success != null) { navigator.store.nuke_success(); };";
    // private static final String JAVASCRIPT_NULL = "null";
    // private static long KEY = 0x4a9ab8d0f0147f4cL;
    // static PersistentObject store;
    // static {
    //  store = PersistentStore.getPersistentObject( KEY );
    // }
    // 
    // public StoreCommand() {
    //  // Generate a Persistent Storage key based on the application UID, which is generated at build-time by less-than-ideal string replacement inside PhoneGap.java :/
    //  try {
    //      StoreCommand.KEY = Long.parseLong(PhoneGap.APPLICATION_UID);
    //  } catch(NumberFormatException e) {
    //      // Just keep the stock one for now if something fucked up.
    //  }
    // }
    // 
    // /**
    //  * Determines whether the specified instruction is accepted by the command. 
    //  * @param instruction The string instruction passed from JavaScript via cookie.
    //  * @return true if the Command accepts the instruction, false otherwise.
    //  */
    // public boolean accept(String instruction) {
    //  return instruction != null && instruction.startsWith(CODE);
    // }
    // 
    // private int getCommand(String instruction) {
    //  String command = instruction.substring(CODE.length()+1);
    //  if (command.startsWith("save")) return SAVE_COMMAND;
    //  if (command.startsWith("loadAll")) return LOADALL_COMMAND;
    //  if (command.startsWith("load")) return LOAD_COMMAND;
    //  if (command.startsWith("remove")) return REMOVE_COMMAND;
    //  if (command.startsWith("nuke")) return NUKE_COMMAND;
    //  return -1;
    // }
    // 
    // public String execute(String instruction) {
    //  Hashtable hash = new Hashtable(); // The existing hash that we have in the system.
    //  StringBuffer retVal = new StringBuffer();
    //  Object storeObj = null;
    //  String key = "";
    //  switch (getCommand(instruction)) {
    //      case SAVE_COMMAND: // Saves data associated to a key to the store hash.
    //          try {
    //              String serialized = instruction.substring(CODE.length() + 6);
    //              int slashPos = serialized.indexOf("/");
    //              key = serialized.substring(0, slashPos);
    //              String value = serialized.substring(slashPos+1);
    //              // Retrieve the stored hash.
    //              synchronized(store) {
    //                  Object tempO = store.getContents();
    //                  if (tempO != null) {
    //                      hash = (Hashtable)tempO;
    //                  }
    //                  tempO = null;
    //              }
    //              // Add the new key/value pair to the hash.
    //              hash.put(key, value);
    //              synchronized(store) {
    //                  store.setContents(hash);
    //                  store.commit();
    //              }
    //              serialized = null;
    //              hash = null;
    //              value = null;
    //              return STORE_SAVE_SUCCESS;
    //          } catch(Exception e) {
    //              return ";if (navigator.store.save_error != null) { navigator.store.save_error('Exception: " + e.getMessage().replace('\'', '`') + "'); };";
    //          }
    //      case LOADALL_COMMAND: // Retrieves the entire hash, composes the JS object for it and returns it to the browser.
    //          try {
    //              synchronized(store) {
    //                  storeObj = store.getContents();
    //              }
    //              if (storeObj != null) {
    //                  hash = (Hashtable)storeObj;
    //                  Enumeration e = hash.keys();
    //                  retVal.append("{");
    //                  while (e.hasMoreElements()) {
    //                      key = (String)e.nextElement();
    //                      String value = (String)hash.get(key);
    //                      value = escapeString(value);
    //                      retVal.append("'");
    //                      retVal.append(key);
    //                      retVal.append("':'");
    //                      retVal.append(value);
    //                      retVal.append("',");
    //                      value = null;
    //                  }
    //                  if (retVal.length() > 1) retVal.deleteCharAt(retVal.length() - 1);
    //                  retVal.append("}");
    //              } else {
    //                  retVal.append("{}");
    //              }
    //              storeObj = null;
    //              return ";if (navigator.store.loadAll_success != null) { navigator.store.loadAll_success(" + retVal.toString() + "); };";
    //          } catch (Exception e) {
    //              return ";if (navigator.store.loadAll_error != null) { navigator.store.loadAll_error('Exception: " + e.getMessage().replace('\'', '`') + "'); };";
    //          }
    //      case LOAD_COMMAND: // Retrieves a particular value associated to a key in the hash.
    //          try {
    //              key = instruction.substring(CODE.length() + 6);
    //              synchronized(store) {
    //                  storeObj = store.getContents();
    //              }
    //              if (storeObj != null) {
    //                  hash = (Hashtable)storeObj;
    //                  if (hash.containsKey(key)) {
    //                      String value = (String)hash.get(key);
    //                      value = escapeString(value);
    //                      retVal.append("'");
    //                      retVal.append(value);
    //                      retVal.append("'");
    //                      value = null;
    //                  } else {
    //                      retVal.append(JAVASCRIPT_NULL);
    //                  }
    //              } else {
    //                  retVal.append(JAVASCRIPT_NULL);
    //              }
    //              storeObj = null;
    //              return ";if (navigator.store.load_success != null) { navigator.store.load_success(" + retVal.toString() + "); };";
    //          } catch (Exception e) {
    //              return ";if (navigator.store.load_error != null) { navigator.store.load_error('Exception: " + e.getMessage().replace('\'', '`') + "'); };";
    //          }
    //      case REMOVE_COMMAND: // Removes a particular key/value pair associated to a key in the hash.
    //          try {
    //              key = instruction.substring(CODE.length() + 8);
    //              synchronized(store) {
    //                  storeObj = store.getContents();
    //              }
    //              if (storeObj != null) {
    //                  hash = (Hashtable)storeObj;
    //                  if (hash.remove(key) != null) {
    //                      synchronized(store) {
    //                          store.setContents(hash);
    //                          store.commit();
    //                      }
    //                  }
    //              }
    //              storeObj = null;
    //              return STORE_REMOVE_SUCCESS;
    //          } catch (Exception e) {
    //              return ";if (navigator.store.remove_error != null) { navigator.store.remove_error('Exception: " + e.getMessage().replace('\'', '`') + "'); };";
    //          }
    //      case NUKE_COMMAND: // Kills the persistent store.
    //          try {
    //              synchronized(store) {
    //                  store.setContents(hash);
    //                  store.commit();
    //              }
    //              return STORE_NUKE_SUCCESS;
    //          } catch (Exception e) {
    //              return ";if (navigator.store.nuke_error != null) { navigator.store.nuke_error('Exception: " + e.getMessage().replace('\'', '`') + "'); };";
    //          }
    //  }
    //  return null;
    // }
    // 
    // private String escapeString(String value) {
    //  // Replace the following:
    //  //   => \ with \\
    //  //   => " with \"
    //  //   => ' with \'
    //  value = PhoneGap.replace(value, "\\", "\\\\");
    //  value = PhoneGap.replace(value, "\"", "\\\"");
    //  value = PhoneGap.replace(value, "'", "\\'");
    //  
    //  return value;
    // }
}