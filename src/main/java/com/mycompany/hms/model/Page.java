package com.mycompany.hms.model;

import java.util.List;

public record Page<T>(List<T> items, int page, int size, long total) {
    public int totalPages() { return size <= 0 ? 0 : (int) Math.ceil((double) total / size); }
    public boolean hasNext() { return (long) (page + 1) * size < total; }
    public boolean hasPrev() { return page > 0; }
}
