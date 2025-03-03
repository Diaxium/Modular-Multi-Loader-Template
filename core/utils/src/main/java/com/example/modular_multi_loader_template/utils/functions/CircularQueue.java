package com.example.modular_multi_loader_template.utils.functions;

import java.util.ArrayList;
import java.util.List;

public class CircularQueue<T> {
    private final List<T> options;
    private int currentIndex;

    // Constructor initializes the list
    public CircularQueue() {
        this.options = new ArrayList<>();
        this.currentIndex = 0;
    }

    // Method for chaining options
    public CircularQueue<T> add(T option) {
        this.options.add(option);
        return this;
    }

    // Finalize the list
    public CircularQueue<T> build() {
        if (this.options.isEmpty()) {
            throw new IllegalStateException("No options added!");
        }

        return this;
    }

    // Get the current option
    public T get() {
        return this.options.get(this.currentIndex);
    }

    // Move to next option and return it
    public T next() {
        this.currentIndex = (this.currentIndex + 1) % this.options.size();
        return this.get();
    }

    // Move to previous option and return it
    public T previous() {
        this.currentIndex = (this.currentIndex - 1 + this.options.size()) % this.options.size();
        return this.get();
    }
}
