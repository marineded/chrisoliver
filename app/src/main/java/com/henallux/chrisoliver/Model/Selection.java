package com.henallux.chrisoliver.Model;


public class Selection {
    private int id;

    public Selection (){}
    public Selection(int id) {
        this.id = id;
    }
    public Selection (Selection selection)
    {
        this.id = selection.getId();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
