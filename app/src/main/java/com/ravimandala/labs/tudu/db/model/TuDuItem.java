package com.ravimandala.labs.tudu.db.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "TuDuItem")
public class TuDuItem extends Model {
    @Column(name = "Description")
    public String description;

    public TuDuItem() {}

    public TuDuItem(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "TuDu Description: " + description;
    }
}
