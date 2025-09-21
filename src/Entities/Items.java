package Entities;

import Entities.HelperClasses.ShippingMethod;

import java.util.UUID;

public class Items {
    protected UUID id;
    protected String name;
    protected String category;
    protected String color;
    protected int[] size;
    protected int count;
    protected double price;
    protected ShippingMethod shippingMethod;

    public Items(Builder builder) {
        this.id = builder.id;
        this.category = builder.category;
        this.name = builder.name;
        this.color = builder.color;
        this.size = builder.size;
        this.count = builder.count;
        this.price = builder.price;
        this.shippingMethod = builder.shippingMethod;
    }

    public static class Builder {
        private UUID id;
        private String name;
        private String category;
        private String color;
        private int[] size;
        private int count;
        private double price;
        private ShippingMethod shippingMethod;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder category(String category) {
            this.category = category;
            return this;
        }

        public Builder color(String color) {
            this.color = color;
            return this;
        }

        public Builder size(int[] size) {
            this.size = size;
            return this;
        }

        public Builder count(int count) {
            this.count = count;
            return this;
        }

        public Builder price(double price) {
            this.price = price;
            return this;
        }

        public Builder shippingMethod(ShippingMethod shippingMethod) {
            this.shippingMethod = shippingMethod;
            return this;
        }

        public Items build() {
            return new Items(this);
        }
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }
}
