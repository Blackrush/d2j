package org.d2j.common;

import java.util.HashMap;
import java.util.Map;

/**
 * User: Blackrush
 * Date: 01/11/11
 * Time: 10:32
 * IDE : IntelliJ IDEA
 */
public class StringTree<T> {
    private final StringTree<T> parent;
    private final Character key;
    private T value;
    private Map<Character, StringTree<T>> children = new HashMap<Character, StringTree<T>>();

    public StringTree() {
        this(null, null, null);
    }

    public StringTree(StringTree<T> parent) {
        this(parent, null, null);
    }

    public StringTree(StringTree<T> parent, Character key, T value) {
        this.parent = parent;
        this.key = key;
        this.value = value;
    }

    public Character getKey() {
        return key;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public StringTree<T> getRoot(){
        StringTree<T> p = this.parent;
        while(p.parent != null) p = p.parent;
        return p;
    }

    public StringTree<T> get(Character key){
        return children.get(key);
    }

    public StringTree<T> get(CharSequence keys){
        StringTree<T> last = this;
        for (int i = 0; i < keys.length(); ++i){
            Character key = keys.charAt(i);
            last = last.children.get(key);
        }
        return last;
    }

    public StringTree<T> add(Character key, T value) {
        StringTree<T> child = new StringTree(this, key, value);
        children.put(key, child);
        return child;
    }

    public StringTree<T> add(CharSequence keys, T value){
        StringTree<T> last = this;
        for (int i = 0; i < keys.length(); ++i){
            Character key = keys.charAt(i);

            StringTree<T> child = last.children.get(key);
            if (child == null)
                child = add(key, null);

            last = child;
            if (i == keys.length() - 1){
                last.setValue(value);
            }
        }
        return last;
    }
}
