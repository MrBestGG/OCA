package com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;
import com.readboy.onlinecourseaides.utils.aicourse.bean.BaseUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class Ability implements Serializable {
    @SerializedName("id")
    private String id; // 编号(id)[Integer]
    @SerializedName("name")
    private String name; // 名称(name)[String]
    public JSONObject toJSON() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("id", BaseUtil.strToLong(id));
            jsonObject.put("name", BaseUtil.getString(name));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
