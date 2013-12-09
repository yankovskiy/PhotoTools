package ru.neverdark.phototools.utils;

import android.content.Context;
import kankan.wheel.widget.adapters.AbstractWheelTextAdapter;

/**
 * Wheel's adapter with empty first item
 * @param <T> type of contained items
 */
public class WheelAdapter<T> extends AbstractWheelTextAdapter {
    
    // items
    private T items[];

    /**
     * Constructor
     * @param context the current context
     * @param items the items
     */
    public WheelAdapter(Context context, T items[]) {
        super(context);
        
        //setEmptyItemResource(TEXT_VIEW_ITEM_RESOURCE);
        this.items = items;
    }
    
    @Override
    public CharSequence getItemText(int index) {
        if (index > 0 && index <= items.length) {
            T item = items[index - 1];
            if (item instanceof CharSequence) {
                return (CharSequence) item;
            }
            return item.toString();
        }
        return null;
    }

    @Override
    public int getItemsCount() {
        return items.length + 1;
    }
    
    @Override
    public Object getItemByIndex(int index) {
        if (index > 0 && index <= items.length) {
            return items[index - 1];
        } 
        return null;
    }
}
