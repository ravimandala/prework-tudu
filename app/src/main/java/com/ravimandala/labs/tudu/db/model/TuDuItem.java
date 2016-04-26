package com.ravimandala.labs.tudu.db.model;

import android.database.Cursor;

import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

@Table(name = "TuDuItem")
public class TuDuItem extends Model {
    @Column(name = "remote_id", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public long remoteId;

    @Column(name = "description")
    public String description;

    @Column(name = "priority")
    public String priority;

    public TuDuItem() {}

    public TuDuItem(String description, String priority) {
        this.description = description;
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "TuDu - Description = " + description + "; Priority = " + priority;
    }

    // Return cursor for result set for all todo items
    public static Cursor fetchResultCursor() {
        String tableName = Cache.getTableInfo(TuDuItem.class).getTableName();
        // Query all items without any conditions
        String queryAllRecords = new Select(tableName + ".*, " + tableName + ".Id as _id").
                from(TuDuItem.class).toSql();
        // Execute query on the underlying ActiveAndroid SQLite database
        Cursor resultCursor = Cache.openDatabase().rawQuery(queryAllRecords, null);
        return resultCursor;
    }
}
