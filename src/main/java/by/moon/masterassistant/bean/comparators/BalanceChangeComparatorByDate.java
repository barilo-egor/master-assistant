package by.moon.masterassistant.bean.comparators;

import by.moon.masterassistant.bean.BalanceChange;

import java.util.Comparator;

public class BalanceChangeComparatorByDate implements Comparator<BalanceChange> {
    @Override
    public int compare(BalanceChange balanceChange, BalanceChange t1) {
        return balanceChange.getDate().compareTo(t1.getDate());
    }
}
