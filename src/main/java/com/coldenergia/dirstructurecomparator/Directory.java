package com.coldenergia.dirstructurecomparator;

/**
 * Represents a file system directory.
 * User: coldenergia
 * Date: 10/11/14
 * Time: 3:01 PM
 */
public class Directory {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Directory{");
        sb.append("name='").append(name).append('\'');
        sb.append('}');
        return sb.toString();
    }

}
