package com.snobot.lib.autonomous;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.TableEntryListener;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;

public class ObservableSendableChooser<V> extends SendableChooser<V>
{
    private String mSendableName;
    private NetworkTable mTable;

    @Override
    public void initTable(NetworkTable table)
    {
        mTable = table;
        super.initTable(table);
    }

    public void addSelectionChangedListener(TableEntryListener listener)
    {
        mTable.addEntryListener("selected", listener, 0xFF);
    }
}
