package com.maximchuk.ptc.ui.table;

import com.maximchuk.ptc.entity.CssPropertyEntity;
import com.maximchuk.ptc.entity.CssPropertyEnum;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Maxim L. Maximchuk
 */
public class CssPropertyTableModel implements TableModel {

    private String[] columnNames = {"Component name", "Css class value"};

    private List<CssPropertyEntity> dataList = new ArrayList<CssPropertyEntity>();
    private Set<CssPropertyEnum> addedCssPropertyTypeSet = new HashSet<CssPropertyEnum>();

    @Override
    public int getRowCount() {
        return dataList.size();
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != 0;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        String value = null;
        switch (columnIndex) {
            case 0: value = dataList.get(rowIndex).getType().name(); break;
            case 1: value = dataList.get(rowIndex).getValue(); break;
        }
        return value;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex == 1) {
            dataList.get(rowIndex).setValue((String)aValue);
        }
    }

    public void addRow(CssPropertyEntity cssProperty) {
        if (!isExistCssProperty(cssProperty.getType())) {
            addedCssPropertyTypeSet.add(cssProperty.getType());
            dataList.add(cssProperty);
        }
    }

    public void removeRowByInd(int ind) {
        CssPropertyEntity data = dataList.get(ind);
        addedCssPropertyTypeSet.remove(data.getType());
        dataList.remove(data);
    }

    public void clear() {
        dataList = new ArrayList<CssPropertyEntity>();
        addedCssPropertyTypeSet = new HashSet<CssPropertyEnum>();
    }

    public List<CssPropertyEntity> getDataList() {
        return dataList;
    }

    public boolean isExistCssProperty(CssPropertyEnum cssPropertyType) {
        return addedCssPropertyTypeSet.contains(cssPropertyType);
    }

    public boolean canAddedNewCssProperty() {
        return CssPropertyEnum.values().length != dataList.size();
    }

    @Override
    public void addTableModelListener(TableModelListener l) {
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
    }
}
