/*
 * Copyright (C) 2015 The Android Open Source Project
 * Copyright (C) 2015 crDroid Android
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.systemui.qs.tiles;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.view.IWindowManager;

import com.android.systemui.R;
import com.android.systemui.qs.QSTile;

public class PowerMenuTile extends QSTile<QSTile.BooleanState> {

private boolean mListening;

    public PowerMenuTile(Host host) {
        super(host);
    }

    @Override
    protected BooleanState newTileState() {
        return new BooleanState();
    }

    public void setListening(boolean listening) {
        if (mListening == listening) return;
        mListening = listening;
    }

    @Override
    public void handleClick() {
        mHost.collapsePanels();
        try {
            IWindowManager windowManagerService = IWindowManager.Stub.asInterface(
            ServiceManager.getService(Context.WINDOW_SERVICE));
            windowManagerService.toggleGlobalMenu();
        } catch (RemoteException e) {
        }
    }

    @Override
    public void handleLongClick() {
        mHost.collapsePanels();
        Intent intent = new Intent(Intent.ACTION_POWERMENU_REBOOT);
        mContext.sendBroadcast(intent);
    }

    @Override
    protected void handleUpdateState(BooleanState state, Object arg) {
        state.visible = true;
        state.label = mContext.getString(R.string.quick_settings_power_menu_label);
        state.icon = ResourceIcon.get(R.drawable.ic_qs_power);
    }
}
