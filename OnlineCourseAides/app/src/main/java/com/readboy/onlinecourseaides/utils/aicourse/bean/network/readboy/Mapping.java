package com.readboy.onlinecourseaides.utils.aicourse.bean.network.readboy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@Data
public class Mapping {

    @SerializedName("id")
    private String id; // 编号(id)[Integer]
    @SerializedName("name")
    private String name; // 名称(name)[String]
    @SerializedName("status")
    private String status; // 状态(status)[Integer]
    @SerializedName("createTime")
    private String createTime; // 创建时间(createTime)[Timestamp]
    @SerializedName("updateTime")
    private String updateTime; // 更新时间(updateTime)[Timestamp]
    @SerializedName("nodes")
    private List<Node> nodes; // 节点(nodes)[ObjectArray]
    @SerializedName("links")
    private List<Link> links; // 线路(links)[ObjectArray]
    @SerializedName("nodeOrder")
    private List<NodeOrder> nodeOrder; // 节点顺序(nodeOrder)[ObjectArray]
    @SerializedName("section")
    private List<Section> section; // 所属章节(section)[ObjectArray]{ 章节编号(id)[Integer], 章节名称(name)[String], 章节来源(source)[Integer] }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @Data
    public static class Node {
        @SerializedName("id")
        private String id; // 编号(id)[Integer]
        @SerializedName("type")
        private Long type; // 类型(type)[Integer]
        @SerializedName("name")
        private String name; // 名称(name)[String]
        @SerializedName("x")
        private Double x; // X轴距离(x)[Integer]
        @SerializedName("y")
        private Double y; // Y轴距离(y)[Integer]
        @SerializedName("difficulty")
        private Long difficulty; // 难度(difficulty)[Integer]
        @SerializedName("shape")
        private Shape shape; // 形状(shape)[ObjectArray->Object]
        @SerializedName("font")
        private Font font; // 字体(font)[ObjectArray->Object]

        @Override
        public String toString() {
            return "Node{" +
                    "id='" + id + '\'' +
                    ", type=" + type +
                    ", name='" + name + '\'' +
                    ", x=" + x +
                    ", y=" + y +
                    ", difficulty=" + difficulty +
                    ", shape=" + shape +
                    ", font=" + font +
                    '}';
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Long getType() {
            return type;
        }

        public void setType(Long type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Double getX() {
            return x;
        }

        public void setX(Double x) {
            this.x = x;
        }

        public Double getY() {
            return y;
        }

        public void setY(Double y) {
            this.y = y;
        }

        public Long getDifficulty() {
            return difficulty;
        }

        public void setDifficulty(Long difficulty) {
            this.difficulty = difficulty;
        }

        public Shape getShape() {
            return shape;
        }

        public void setShape(Shape shape) {
            this.shape = shape;
        }

        public Font getFont() {
            return font;
        }

        public void setFont(Font font) {
            this.font = font;
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        @NoArgsConstructor
        @Data
        public static class Shape {
            @SerializedName("type")
            private String type; // 类型(type)[String]
            @SerializedName("radius")
            private Long radius; // 半径(radius)[Integer] 单位:px
            @SerializedName("height")
            private Long height; // 高(height)[Integer] 单位:px
            @SerializedName("width")
            private Long width; // 宽(width)[Integer] 单位:px
            @SerializedName("color")
            private String color; // 颜色(color)[string] 单位:hex色值

            @Override
            public String toString() {
                return "Shape{" +
                        "type='" + type + '\'' +
                        ", radius=" + radius +
                        ", height=" + height +
                        ", width=" + width +
                        ", color='" + color + '\'' +
                        '}';
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public Long getRadius() {
                return radius;
            }

            public void setRadius(Long radius) {
                this.radius = radius;
            }

            public Long getHeight() {
                return height;
            }

            public void setHeight(Long height) {
                this.height = height;
            }

            public Long getWidth() {
                return width;
            }

            public void setWidth(Long width) {
                this.width = width;
            }

            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }
        }

        @JsonIgnoreProperties(ignoreUnknown = true)
        @NoArgsConstructor
        @Data
        public static class Font {
            @SerializedName("size")
            private Double size; // 大小(size)[Integer] 单位:px
            @SerializedName("color")
            private String color; // 颜色(color)[string]
            @SerializedName("fontPosition")
            private String fontPosition; // 相对位置(fontPosition)[string]
            @SerializedName("x")
            private Double x; // 相对位置的X坐标(x)[Integer]
            @SerializedName("y")
            private Double y; // 相对位置的Y坐标(y)[Integer]
            @SerializedName("offset")
            private Long offset; // 距离中心点(offset)[Integer]

            @Override
            public String toString() {
                return "Font{" +
                        "size=" + size +
                        ", color='" + color + '\'' +
                        ", fontPosition='" + fontPosition + '\'' +
                        ", x=" + x +
                        ", y=" + y +
                        ", offset=" + offset +
                        '}';
            }

            public Double getSize() {
                return size;
            }

            public void setSize(Double size) {
                this.size = size;
            }

            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }

            public String getFontPosition() {
                return fontPosition;
            }

            public void setFontPosition(String fontPosition) {
                this.fontPosition = fontPosition;
            }

            public Double getX() {
                return x;
            }

            public void setX(Double x) {
                this.x = x;
            }

            public Double getY() {
                return y;
            }

            public void setY(Double y) {
                this.y = y;
            }

            public Long getOffset() {
                return offset;
            }

            public void setOffset(Long offset) {
                this.offset = offset;
            }
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @NoArgsConstructor
    @Data
    public static class NodeOrder {
        @SerializedName("id")
        private String id; // 编号(id)[Integer]
        @SerializedName("order")
        private List<String> order; // 节点顺序(order)[IntegerArray]
        @SerializedName("type")
        private Long type; // 类型(type)[Integer]
        @SerializedName("difficulty")
        private Long difficulty; // 难度(difficulty)[Integer]
        @SerializedName("createTime")
        private Long createTime; // 创建时间(createTime)[Timestamp]
        @SerializedName("updateTime")
        private Long updateTime; // 更新时间(updateTime)[Timestamp]

        @Override
        public String toString() {
            return "NodeOrder{" +
                    "id='" + id + '\'' +
                    ", order=" + order +
                    ", type=" + type +
                    ", difficulty=" + difficulty +
                    ", createTime=" + createTime +
                    ", updateTime=" + updateTime +
                    '}';
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public List<String> getOrder() {
            return order;
        }

        public void setOrder(List<String> order) {
            this.order = order;
        }

        public Long getType() {
            return type;
        }

        public void setType(Long type) {
            this.type = type;
        }

        public Long getDifficulty() {
            return difficulty;
        }

        public void setDifficulty(Long difficulty) {
            this.difficulty = difficulty;
        }

        public Long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(Long createTime) {
            this.createTime = createTime;
        }

        public Long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(Long updateTime) {
            this.updateTime = updateTime;
        }
    }

    @Override
    public String toString() {
        return "Mapping{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", createTime='" + createTime + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", nodes=" + nodes +
                ", links=" + links +
                ", nodeOrder=" + nodeOrder +
                ", section=" + section +
                '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public List<NodeOrder> getNodeOrder() {
        return nodeOrder;
    }

    public void setNodeOrder(List<NodeOrder> nodeOrder) {
        this.nodeOrder = nodeOrder;
    }

    public List<Section> getSection() {
        return section;
    }

    public void setSection(List<Section> section) {
        this.section = section;
    }
}
