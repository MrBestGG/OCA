package com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class Link {
   @SerializedName("id")
   private String id; // 编号(id)[Integer]
   @SerializedName("sourceid")
   private String sourceid; // 起点ID(sourceid)[Integer]
   @SerializedName("targetid")
   private String targetid; // 终点ID(targetid)[Integer]
   @SerializedName("color")
   private String color; // 颜色(color)[string] (单位:hex色值)
   @SerializedName("startPoint")
   private Coordinate startPoint; // 开始坐标(startPoint)[IntegerArray->Object]{ X坐标(x)[Interger],Y坐标(y)[Interger] }
   @SerializedName("endPoint")
   private Coordinate endPoint; // 结束坐标(endPoint)[IntegerArray->Object]{ X坐标(x)[Interger],Y坐标(y)[Interger] }

   @Override
   public String toString() {
      return "Link{" +
              "id='" + id + '\'' +
              ", sourceid='" + sourceid + '\'' +
              ", targetid='" + targetid + '\'' +
              ", color='" + color + '\'' +
              ", startPoint=" + startPoint +
              ", endPoint=" + endPoint +
              '}';
   }

   public String getId() {
      return id;
   }

   public void setId(String id) {
      this.id = id;
   }

   public String getSourceid() {
      return sourceid;
   }

   public void setSourceid(String sourceid) {
      this.sourceid = sourceid;
   }

   public String getTargetid() {
      return targetid;
   }

   public void setTargetid(String targetid) {
      this.targetid = targetid;
   }

   public String getColor() {
      return color;
   }

   public void setColor(String color) {
      this.color = color;
   }

   public Coordinate getStartPoint() {
      return startPoint;
   }

   public void setStartPoint(Coordinate startPoint) {
      this.startPoint = startPoint;
   }

   public Coordinate getEndPoint() {
      return endPoint;
   }

   public void setEndPoint(Coordinate endPoint) {
      this.endPoint = endPoint;
   }
}
