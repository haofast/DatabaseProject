package dbms.database.table.page;

import dbms.database.cell.AbstractCell;
import dbms.database.cell.ICell;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class Criteria {
    protected Map<String, String> criteria = new HashMap<String, String>();

    public boolean validateCell(ICell cell) {
        boolean validCell = false;
        String column = cell.getColumn().getName();
        String value;

        try {
            value = criteria.get(column);
        } catch (Exception e) {
            return false;
        }

        return Objects.equals(value, cell.getValue()) || notInCriteria(column);
    }

    public String getValue(String columnName){
        return criteria.get(columnName);
    }

    public boolean notInCriteria(String column){
        return !criteria.containsKey(column);
    }

    public Set<String> getColumnNames(){
        return criteria.keySet();
    }

    public void add(String columnName, String value){
        criteria.put(columnName, value);
    }

    public void remove(String columnName, String value){
        criteria.remove(columnName, value);
    }

    public void remove(String columnName){
        criteria.remove(columnName);
    }

    public void setCriteria(Map<String, String> criteria) {
        this.criteria = criteria;
    }

    public Map<String, String> getCriteria() {
        return criteria;
    }
}
