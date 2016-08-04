package com.example.android.weather.Data;

import org.json.JSONObject;

/**
 * Created by Bridget on 8/3/2016.
 */
public class Item implements JSONPopulator {
    private Condition condition;

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    @Override
    public void Populate(JSONObject data) {
        condition=new Condition();
        condition.Populate(data.optJSONObject("condition"));

    }
}
