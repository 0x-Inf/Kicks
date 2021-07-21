package com.diablo.jayson.kicksv1.UI.AddActivity;

public class CostItem {

    private String costItemName;
    private int costItemAmount;

    public CostItem() {
    }

    public CostItem(String costItemName, int costItemAmount) {
        this.costItemName = costItemName;
        this.costItemAmount = costItemAmount;
    }

    public String getCostItemName() {
        return costItemName;
    }

    public void setCostItemName(String costItemName) {
        this.costItemName = costItemName;
    }

    public int getCostItemAmount() {
        return costItemAmount;
    }

    public void setCostItemAmount(int costItemAmount) {
        this.costItemAmount = costItemAmount;
    }
}
