/*******************************************************************************
 * Copyright (C) 2013 Artem Yankovskiy (artemyankovskiy@gmail.com).
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package ru.neverdark.phototools.ui;

import ru.neverdark.phototools.log.Log;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.RelativeLayout;

/**
 * Implements RelativeLayout with illumination selection item
 */
public class CheckableRelativeLayout extends RelativeLayout implements Checkable {
    private boolean mChecked;

    public CheckableRelativeLayout(Context context) {
        super(context);
    }

    public CheckableRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /* (non-Javadoc)
     * @see android.widget.Checkable#setChecked(boolean)
     */
    @SuppressWarnings("deprecation")
    @SuppressLint("NewApi")
    public void setChecked(boolean checked) {
        Log.message("Enter");
        mChecked = checked;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
            Log.message("Old api");
            setBackgroundDrawable(checked ? new ColorDrawable(0xff84d3ef) : null);
        } else {
            Log.message("New api");
            setBackground(checked ? new ColorDrawable(0xff84d3ef) : null);
        }
    }

    /* (non-Javadoc)
     * @see android.widget.Checkable#isChecked()
     */
    public boolean isChecked() {
        return mChecked;
    }

    /* (non-Javadoc)
     * @see android.widget.Checkable#toggle()
     */
    public void toggle() {
        setChecked(!mChecked);
    }

}
