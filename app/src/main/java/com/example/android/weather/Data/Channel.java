package com.example.android.weather.Data;

/**
 * Created by Bridget on 8/3/2016.
 */

import org.json.JSONObject;

public class Channel implements JSONPopulator {
    private Units units;
    private Item item;

    public Units getUnits() {
        return units;
    }

    public void setUnits(Units units) {
        this.units = units;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public void Populate(JSONObject data) {
        units=new Units();
        units.Populate(data.optJSONObject("units"));

        item=new Item();
        item.Populate(data.optJSONObject("item"));
    }
}
