package com.android.messagebusexample.lib;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageData extends HashMap<String, Object> {
    public MessageData() {
        super();
    }

    public MessageData(int capacity) {
        super(capacity);
    }

    public <T> T getObject(String key) {
        Object o = get(key);
        if (o == null) {
            return null;
        }
        try {
            return (T) o;
        } catch (ClassCastException e) {
            return null;
        }
    }

    public void putArrayList(String key, ArrayList<? extends Object> data) {
        put(key, data);
    }

    public <T> ArrayList<T> getArrayList(String key) {
        return getArrayList(key, new ArrayList());
    }

    public <T> ArrayList<T> getArrayList(String key, ArrayList defaultValue) {
        Object o = get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return (ArrayList<T>) o;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }

    public void putBoolean(String key, boolean value) {
        put(key, value);
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        Object o = get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return (Boolean) o;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }

    public void putLong(String key, long val) {
        put(key, val);
    }

    public long getLong(String key) {
        return getLong(key, 0L);
    }

    public long getLong(String key, long defaultValue) {
        Object o = get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return (Long) o;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }

    public void putInt(String key, int value) {
        put(key, value);
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public int getInt(String key, int defaultValue) {
        Object o = get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return (Integer) o;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }


    public void putFloat(String key, float value) {
        put(key, value);
    }

    public float getFloat(String key) {
        return getFloat(key, 0.0f);
    }

    public float getFloat(String key, float defaultValue) {
        Object o = get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return (Float) o;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }

    public void putDouble(String key, double value) {
        put(key, value);
    }

    public double getDouble(String key) {
        return getDouble(key, 0.0);
    }

    public double getDouble(String key, double defaultValue) {
        Object o = get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return (Double) o;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }

    public void putString(String key, String value) {
        put(key, value);
    }

    public String getString(String key) {
        return getString(key, null);
    }

    public String getString(String key, String defaultValue) {
        Object o = get(key);
        if (o == null) {
            return defaultValue;
        }
        try {
            return (String) o;
        } catch (ClassCastException e) {
            return defaultValue;
        }
    }
}
